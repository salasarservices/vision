import json
import os
from datetime import datetime
from io import BytesIO
from typing import Any, Dict, List

import streamlit as st
from PIL import Image
from pymongo import MongoClient
from pymongo.collection import Collection
import google.generativeai as genai

st.set_page_config(page_title="Insurance OCR Extractor", layout="wide")

FIELD_GROUPS = {
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
