package sportyfy.apiFootball;

import org.json.JSONArray;
import org.json.JSONObject;

public class ProcesadorJson {
    public static JSONArray procesarEquipos(String respuesta) {
        JSONObject jsonObject = new JSONObject(respuesta);
        JSONArray equiposFull = jsonObject.getJSONArray("response");
        JSONArray equipos = new JSONArray();

        for (int i = 0; i < equiposFull.length(); i++) {
            JSONObject equipoCompleto = equiposFull.getJSONObject(i);
            JSONObject equipoReducido = equipoCompleto.getJSONObject("team");

            JSONObject equipoReducido2 = new JSONObject()
                    .put("nombre", equipoReducido.getString("name"))
                    .put("id", equipoReducido.getInt("id"));

            equipos.put(equipoReducido2);
        }
        return equipos;
    }

    public static JSONArray procesarPartidos(String respuesta) {
        JSONObject jsonObject = new JSONObject(respuesta);
        JSONArray partidosFull = jsonObject.getJSONArray("response");
        JSONArray partidos = new JSONArray();

        for (int i = 0; i < partidosFull.length(); i++) {
            JSONObject enfrentamientoFull = partidosFull.getJSONObject(i);

            JSONObject equipos = enfrentamientoFull.getJSONObject("teams");
            JSONObject goles = enfrentamientoFull.getJSONObject("goals");

            if(goles.isNull("home") || goles.isNull("away")) {
                continue;
            }

            int golesLocal = goles.getInt("home");
            int golesVisitante = goles.getInt("away");

            JSONObject equipoLocalOriginal = equipos.getJSONObject("home");
            JSONObject equipoVisitanteOriginal = equipos.getJSONObject("away");

            JSONObject equipoLocal = new JSONObject();
            JSONObject equipoVisitante = new JSONObject();

            equipoLocal.put("id", equipoLocalOriginal.getInt("id"));

            equipoVisitante.put("id", equipoVisitanteOriginal.getInt("id"));

            JSONObject partidoJSON = new JSONObject();
            partidoJSON.put("equipoLocal", equipoLocal);
            partidoJSON.put("equipoVisitante", equipoVisitante);
            partidoJSON.put("golesLocal", golesLocal);
            partidoJSON.put("golesVisitante", golesVisitante);

            partidos.put(partidoJSON);
        }

        return partidos;
    }

}
