from flask import Flask, request, jsonify
from supabase import create_client
from dotenv import load_dotenv
from datetime import datetime, timedelta, timezone
import os

load_dotenv()

supabase_url = os.getenv("SUPABASE_URL")
supabase_key = os.getenv("SUPABASE_KEY")

supabase = create_client(supabase_url, supabase_key)

app = Flask(__name__)

# Endpoints para taquillas


@app.route("/taquillas", methods=["GET"])
def obtener_taquillas():
    res = supabase.table("taquillas").select("*").execute()
    return {"ok": True, "rows": res.data}


@app.route("/taquillas/<taquilla_id>", methods=["GET"])
def obtener_taquilla(taquilla_id):
    res = (
        supabase.table("taquillas").select("*").eq("id", taquilla_id).single().execute()
    )
    if res.data:
        return {"ok": True, "row": res.data}

    return {"ok": False, "error": "Taquilla not found"}, 404


@app.route("/taquillas/<taquilla_id>/estado", methods=["GET"])
def obtener_estado_taquilla(taquilla_id):
    res = (
        supabase.table("taquillas")
        .select("reservado")
        .eq("id", taquilla_id)
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


@app.route("/reservas/taquilla/<taquilla_id>", methods=["GET"])
def obtener_reservas_por_taquilla(taquilla_id):
    res = (
        supabase.table("reservas").select("*").eq("taquilla_id", taquilla_id).execute()
    )
    return {"ok": True, "rows": res.data}


@app.route("/reservas/<taquilla_id>", methods=["POST"])
def crear_reserva(taquilla_id):
    res_taquilla = (
        supabase.table("taquillas").select("*").eq("id", taquilla_id).execute()
    )

    if not res_taquilla.data:
        return {"ok": False, "error": "Taquilla not found"}, 404

    taquilla = res_taquilla.data[0]

    if taquilla["reservado"]:
        return {"ok": False, "error": "Taquilla already reserved"}, 400

    data = request.json
    data["id_taquilla"] = taquilla_id

    now = datetime.now(timezone.utc)
    data["fecha_inicio"] = now.isoformat()
    data["fecha_fin"] = (now + timedelta(hours=1)).isoformat()

    try:
        res = (
            supabase.table("reservas")
            .insert(data, returning="representation")
            .execute()
        )
        supabase.table("taquillas").update({"reservado": True}).eq(
            "id", taquilla_id
        ).execute()
    except Exception as e:
        return {"ok": False, "error": str(e)}, 500

    return {"ok": True, "reserva": res.data[0], "message": "Reserva creada"}, 201


if __name__ == "__main__":
    app.run(debug=True)
