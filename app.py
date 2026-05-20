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
