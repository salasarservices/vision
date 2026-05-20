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


def get_collection() -> Any:
    from pymongo import MongoClient

    mongo_uri = get_setting("MONGODB_URI", "mongodb://localhost:27017")
    db_name = get_setting("MONGODB_DB", "ocr_insurance")
    collection_name = get_setting("MONGODB_COLLECTION", "policies")
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
    model = genai.GenerativeModel("gemini-1.5-flash")

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

    response = model.generate_content(payload)
    parsed = parse_json_response(response.text)

    result = empty_record()
    for key in result.keys():
        value = parsed.get(key, "")
        result[key] = "" if value is None else str(value).strip()
    return result


def save_record(collection: Collection, record: Dict[str, Any], filenames: List[str]) -> str:
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
        st.write("Set these in Streamlit Cloud secrets:")
        st.code("GEMINI_API_KEY\nMONGODB_URI\nMONGODB_DB\nMONGODB_COLLECTION")
        if not get_setting("GEMINI_API_KEY"):
            st.warning("GEMINI_API_KEY is missing. Upload/review will load, but OCR will not run until it is set.")

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
