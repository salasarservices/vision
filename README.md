Insurance OCR Scanner (Streamlit + Gemini + MongoDB)
This app lets you upload scanned policy documents (PDF/images), run OCR using Gemini 1.5 Flash, map extracted values into grouped fields, review/edit, and save to MongoDB.

Features
Manual scan upload only (no prefilled forms).
OCR extraction through Gemini API.
Field segregation by policy sections.
Editable output before saving.
MongoDB persistence with file references + timestamp.
Setup
python -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
Environment Variables
export GEMINI_API_KEY="your_key"
export MONGODB_URI="mongodb://localhost:27017"
export MONGODB_DB="ocr_insurance"
export MONGODB_COLLECTION="policies"
Run
streamlit run app.py
Notes
Keep OCR strict by leaving unknown fields blank.
If your scans are mixed or low quality, upload multiple pages together.
