from flask import Flask, request
from flask_cors import CORS
from supabase import create_client
from dotenv import load_dotenv
from datetime import datetime, timezone
from dateutil import parser
import os

load_dotenv()

supabase_url = os.getenv("SUPABASE_URL")
supabase_key = os.getenv("SUPABASE_KEY")

supabase = create_client(supabase_url, supabase_key)

app = Flask(__name__)
CORS(app)

# Endpoints para taquillas


@app.route("/taquillas", methods=["GET"])
def obtener_taquillas():
    res = supabase.table("taquillas").select("*").execute()
    return {"ok": True, "rows": res.data}


@app.route("/taquillas/<id_taquilla>", methods=["GET"])
def obtener_taquilla(id_taquilla):
    res = (
        supabase.table("taquillas").select("*").eq("id", id_taquilla).single().execute()
    )
    if res.data:
        return {"ok": True, "row": res.data}

    return {"ok": False, "error": "Taquilla not found"}, 404


@app.route("/taquillas/<id_taquilla>/estado", methods=["GET"])
def obtener_estado_taquilla(id_taquilla):
    res = (
        supabase.table("taquillas")
        .select("reservado")
        .eq("id", id_taquilla)
        .single()
        .execute()
    )
    if res.data:
        return {"ok": True, "estado": res.data["reservado"]}

    return {"ok": False, "error": "Taquilla not found"}, 404


# Endpoints para reservas
@app.route("/reservas", methods=["GET"])
def obtener_reservas():
    res = supabase.table("reservas").select("*").execute()
    return {"ok": True, "rows": res.data}


@app.route("/reservas/<reserva_id>", methods=["GET"])
def obtener_reserva(reserva_id):
    res = supabase.table("reservas").select("*").eq("id", reserva_id).single().execute()
    return {"ok": True, "row": res.data}


@app.route("/reservas/taquilla/<id_taquilla>", methods=["GET"])
def obtener_reservas_por_taquilla(id_taquilla):
    res = (
        supabase.table("reservas").select("*").eq("id_taquilla", id_taquilla).execute()
    )
    return {"ok": True, "rows": res.data}


@app.route("/reservas/<id_taquilla>", methods=["POST"])
def crear_reserva(id_taquilla):
    data = request.json

    # 1. Verificar que llega la fecha del usuario
    if "fecha_fin" not in data:
        return {"ok": False, "error": "fecha_fin is required"}, 400

    # 2. Parsear CUALQUIER formato recibido
    try:
        fecha_fin = parser.parse(data["fecha_fin"])
    except Exception:
        return {"ok": False, "error": "Invalid fecha_fin format"}, 400

    # 3. Convertir a TIMESTAMPTZ en UTC (obligatorio)
    fecha_fin_utc = fecha_fin.astimezone(timezone.utc).isoformat()

    # 4. Añadir al diccionario final
    data["fecha_fin"] = fecha_fin_utc
    # 5. Crear fecha_inicio = ahora
    fecha_inicio_utc = datetime.now(timezone.utc).isoformat()
    data["fecha_inicio"] = fecha_inicio_utc
    data["id_taquilla"] = id_taquilla

    # ---- Lógica de reserva ----

    res_taquilla = (
        supabase.table("taquillas").select("*").eq("id", id_taquilla).execute()
    )

    if not res_taquilla.data:
        return {"ok": False, "error": "Taquilla not found"}, 404

    taquilla = res_taquilla.data[0]

    if taquilla["reservado"]:
        return {"ok": False, "error": "Taquilla already reserved"}, 400

    try:
        res = (
            supabase.table("reservas")
            .insert(data, returning="representation")
            .execute()
        )

        supabase.table("taquillas").update({"reservado": True}).eq(
            "id", id_taquilla
        ).execute()

    except Exception as e:
        return {"ok": False, "error": str(e)}, 500

    return {"ok": True, "reserva": res.data[0], "message": "Reserva creada"}, 201


if __name__ == "__main__":
    app.run(debug=True)
