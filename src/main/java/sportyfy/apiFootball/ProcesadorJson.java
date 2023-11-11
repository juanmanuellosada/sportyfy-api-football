package sportyfy.apiFootball;

import org.json.JSONArray;
import org.json.JSONObject;

/*
 * Clase para procesar los JSONs de partidos
 * 
 */
public class ProcesadorJson {

    /*
     * Método para procesar los JSONs de equipos
     * 
     * @param respuesta Respuesta de la API
     * 
     * @return JSON con los equipos procesados a mi manera
     * 
     */
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

    /*
     * Método para procesar los JSONs de partidos
     * 
     * @param respuesta Respuesta de la API
     * 
     * @return JSON con los partidos procesados a mi manera
     * 
     */
    public static JSONArray procesarPartidos(String respuesta) {
        JSONObject jsonObject = new JSONObject(respuesta);
        JSONArray partidosFull = jsonObject.getJSONArray("response");
        JSONArray partidos = new JSONArray();

        for (int i = 0; i < partidosFull.length(); i++) {
            JSONObject enfrentamientoFull = partidosFull.getJSONObject(i);

            JSONObject equipos = enfrentamientoFull.getJSONObject("teams");
            JSONObject goles = enfrentamientoFull.getJSONObject("goals");

            if (goles.isNull("home") || goles.isNull("away")) {
                continue;
            }

            int golesLocal = goles.getInt("home");
            int golesVisitante = goles.getInt("away");

            JSONObject equipoLocalOriginal = equipos.getJSONObject("home");
            JSONObject equipoVisitanteOriginal = equipos.getJSONObject("away");

            JSONObject partidoJSON = new JSONObject();
            JSONObject partidoResultado = new JSONObject();
            JSONObject partidoLocal = new JSONObject();
            JSONObject partidoVisitante = new JSONObject();

            partidoLocal.put("nombre", equipoLocalOriginal.getString("name"));
            partidoVisitante.put("nombre", equipoVisitanteOriginal.getString("name"));

            partidoResultado.put("marcadorPorEquipo", new JSONObject()
                    .put(partidoLocal.getString("nombre"), golesLocal)
                    .put(partidoVisitante.getString("nombre"), golesVisitante));

            partidoJSON.put("resultado", partidoResultado);

            partidoJSON.put("partido", new JSONObject()
                    .put("visitante", partidoVisitante)
                    .put("local", partidoLocal));

            partidos.put(new JSONObject().put("resultadoPartido", partidoJSON));
        }

        return partidos;
    }

}
