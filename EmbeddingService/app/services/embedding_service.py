from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import requests

app = FastAPI()

class EmbeddingRequest(BaseModel):
    text: str

class EmbeddingResponse(BaseModel):
    embedding: list

@app.post("/generate_embedding", response_model=EmbeddingResponse)
async def generate_embedding(request: EmbeddingRequest):
    try:
        url = "http://localhost:11434/api/v1/generate"
        payload = {
            "model": "llama-3b",
            "input": request.text,
            "options": {"embedding": True}
        }

        response = requests.post(url, json=payload)
        if response.status_code != 200:
            raise HTTPException(status_code=response.status_code, detail=response.text)

        result = response.json()
        embedding = result.get("embedding")
        return EmbeddingResponse(embedding=embedding)
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))



# Health check endpoint
@app.get("/")
def health_check():
    return {"message": "Embedding service is running"}
