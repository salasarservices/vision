import json
import os
from datetime import datetime
from typing import Any, Dict, List
from urllib.parse import urlparse

import streamlit as st

st.set_page_config(page_title="Insurance OCR Extractor", layout="wide")

MONGODB_CLUSTER_NAME = "ocr-insurance"
DEFAULT_MONGODB_DB = "salvision"
DEFAULT_MONGODB_COLLECTION = "insurance_policies"
DEFAULT_GEMINI_MODEL = "gemini-2.0-flash"
GEMINI_MODEL_FALLBACKS = [
    DEFAULT_GEMINI_MODEL,
    "gemini-2.5-flash",
    "gemini-2.0-flash-lite",
]

FIELD_GROUPS: Dict[str, Dict[str, str]] = {
    "Policy Details": {
        "policy_no": "Policy No",
        "department": "Department",
        "policy_type": "Policy Type",
        "insured_proposer_name": "Insured/Proposer Name",
        "policy_issuance_date": "Policy Issuance Date",
        "policy_start_date": "Policy Start Date",
        "policy_expiry_date": "Policy Expiry Date",
        "coverage_type": "Coverage Type",
        "coverage_tp": "Coverage/TP",
        "receive_date": "Receive Date",
    },
    "Vehicle Information": {
        "vehicle_no": "Vehicle No",
        "variant": "Variant",
        "year_of_manufacture": "Year of Manufacture",
        "vehicle_registration_date": "Vehicle Registration Date",
        "make": "Make",
        "model": "Model",
        "rto_code": "RTO Code",
        "type_of_vehicle": "Type of Vehicle",
        "passenger_gvw": "Passenger/GVW",
        "ai_extracted_vehicle_details": "AI Extracted Vehicle Details",
    },
    "Customer & Insurer Information": {
        "insurer_name": "Insurer Name",
        "customer_name": "Customer Name",
        "mobile_no": "Mobile No",
        "email": "Email",
        "address": "Address",
        "nominee_name": "Nominee Name",
    },
    "Business Information": {
        "business_brokerage_date": "Business/Brokerage Date",
        "branch": "Branch",
        "agent_code": "Agent Code",
        "source_channel": "Source Channel",
    },
    "Financial Information": {
        "od_premium": "OD Premium",
        "tp_premium": "TP Premium",
        "net_premium": "Net Premium",
        "gst_amount": "GST Amount",
        "total_premium": "Total Premium",
        "idv": "IDV",
    },
    "Internal Information": {
        "saiba_sync_status": "SAIBA Sync Status",
        "remarks": "Remarks",
        "document_source": "Document Source",
    },
}


def get_setting(name: str, default: str = "") -> str:
    value = os.getenv(name)
    if value:
        return value

    try:
        secret_value = st.secrets.get(name)
    except Exception:
        secret_value = None

    return str(secret_value) if secret_value else default


def get_database_name_from_uri(mongo_uri: str) -> str:
    path = urlparse(mongo_uri).path.strip("/")
    return path if path else ""


def get_collection() -> Any:
    from pymongo import MongoClient

    mongo_uri = get_setting("MONGODB_URI", "mongodb://localhost:27017")
    db_name = get_setting("MONGODB_DB", get_database_name_from_uri(mongo_uri) or DEFAULT_MONGODB_DB)
    collection_name = get_setting("MONGODB_COLLECTION", DEFAULT_MONGODB_COLLECTION)
    client = MongoClient(mongo_uri, serverSelectionTimeoutMS=5000)
    return client[db_name][collection_name]


def flatten_fields() -> Dict[str, str]:
    merged: Dict[str, str] = {}
    for grp in FIELD_GROUPS.values():
        merged.update(grp)
    return merged


def empty_record() -> Dict[str, Any]:
    return {k: "" for k in flatten_fields().keys()}


def parse_json_response(raw_text: str) -> Dict[str, Any]:
    text = raw_text.strip()
    if text.startswith("```"):
        text = text.split("\n", 1)[1]
        text = text.rsplit("```", 1)[0]
    return json.loads(text)


def run_ocr_with_gemini(files: List[Any]) -> Dict[str, Any]:
    import google.generativeai as genai

    api_key = get_setting("GEMINI_API_KEY")
    if not api_key:
        raise RuntimeError("GEMINI_API_KEY is not set.")

    genai.configure(api_key=api_key)

    all_fields = flatten_fields()
    schema = {k: f"string ({v})" for k, v in all_fields.items()}

    prompt = f"""
You are an OCR and information extraction engine for Indian motor insurance policy documents.

Extract ONLY the requested fields from the provided image(s)/PDF pages.
- Return strict JSON object only (no markdown).
- Use these keys exactly and keep every key present.
- If value is missing, set empty string.
- Do not hallucinate.

Expected JSON keys and meanings:
{json.dumps(schema, indent=2)}
"""

    payload: List[Any] = [prompt]
    for f in files:
        data = f.getvalue()
        mime = getattr(f, "type", "") or ("application/pdf" if f.name.lower().endswith(".pdf") else "image/png")
        payload.append({"mime_type": mime, "data": data})

    preferred_model = get_setting("GEMINI_MODEL", DEFAULT_GEMINI_MODEL)
    model_names = [preferred_model] + [name for name in GEMINI_MODEL_FALLBACKS if name != preferred_model]
    last_error: Exception | None = None

    for model_name in model_names:
        try:
            model = genai.GenerativeModel(model_name)
            response = model.generate_content(payload)
            break
        except Exception as exc:
            last_error = exc
    else:
        raise RuntimeError(f"Gemini OCR failed for all configured models: {last_error}") from last_error

    parsed = parse_json_response(response.text)

    result = empty_record()
    for key in result.keys():
        value = parsed.get(key, "")
        result[key] = "" if value is None else str(value).strip()
    return result


def save_record(collection: Any, record: Dict[str, Any], filenames: List[str]) -> str:
    doc = {
        "created_at": datetime.utcnow(),
        "source_files": filenames,
        "data": record,
    }
    inserted = collection.insert_one(doc)
    return str(inserted.inserted_id)


def render_form(data: Dict[str, Any]) -> Dict[str, Any]:
    edited = dict(data)
    for group_name, fields in FIELD_GROUPS.items():
        st.subheader(group_name)
        col1, col2 = st.columns(2)
        for idx, (key, label) in enumerate(fields.items()):
            target = col1 if idx % 2 == 0 else col2
            with target:
                edited[key] = st.text_input(label, value=edited.get(key, ""), key=f"field_{group_name}_{key}")
        st.divider()
    return edited


def main() -> None:
    st.title("Insurance OCR Scanner & Field Segregation")
    st.caption("Upload scanned policy documents. The app extracts and maps OCR values into exact field groups.")

    with st.sidebar:
        st.header("Configuration")
        st.caption(f"Atlas cluster: {MONGODB_CLUSTER_NAME}")
        st.write("Set these in Streamlit Cloud secrets:")
        st.code(
            f'GEMINI_API_KEY = "your_gemini_key"\n'
            f'GEMINI_MODEL = "{DEFAULT_GEMINI_MODEL}"\n'
            f'MONGODB_URI = "your_mongodb_atlas_connection_string/{DEFAULT_MONGODB_DB}"\n'
            f'MONGODB_DB = "{DEFAULT_MONGODB_DB}"\n'
            f'MONGODB_COLLECTION = "{DEFAULT_MONGODB_COLLECTION}"'
        )
        st.caption(
            "MongoDB creates this database and collection automatically on the first successful save "
            "if they do not already exist."
        )
        if not get_setting("GEMINI_API_KEY"):
            st.warning("GEMINI_API_KEY is missing. Upload/review will load, but OCR will not run until it is set.")
        if not get_setting("MONGODB_URI"):
            st.warning("MONGODB_URI is missing. OCR/review will load, but saving will use local MongoDB unless the Atlas URI is set.")

    if "record" not in st.session_state:
        st.session_state.record = empty_record()

    uploaded_files = st.file_uploader(
        "Upload scan(s): PDF/PNG/JPG",
        type=["pdf", "png", "jpg", "jpeg"],
        accept_multiple_files=True,
    )

    if st.button("Process Scans", type="primary"):
        if not uploaded_files:
            st.warning("Please upload at least one scanned file.")
        else:
            with st.spinner("Running OCR and field mapping with Gemini..."):
                try:
                    st.session_state.record = run_ocr_with_gemini(uploaded_files)
                    st.success("OCR complete. Review extracted fields below.")
                except Exception as exc:
                    st.error(f"Processing failed: {exc}")

    st.session_state.record = render_form(st.session_state.record)

    if st.button("Save to MongoDB"):
        try:
            collection = get_collection()
            object_id = save_record(collection, st.session_state.record, [f.name for f in uploaded_files or []])
            st.success(f"Saved successfully. MongoDB ObjectId: {object_id}")
        except Exception as exc:
            st.error(f"Save failed: {exc}")

    if uploaded_files:
        st.subheader("Uploaded Preview")
        for f in uploaded_files:
            if f.type.startswith("image"):
                from PIL import Image

                st.image(Image.open(f), caption=f.name, use_container_width=True)
            else:
                st.write(f"PDF uploaded: {f.name}")


if __name__ == "__main__":
    try:
        main()
    except Exception as exc:
        st.error("The app failed while starting.")
        st.exception(exc)
