package sportyfy.apiFootball;

import org.json.JSONArray;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

/*
 * Clase para generar los JSONs de equipos y partidos
 * 
 */
public class GeneradorJsons {
    private static final Logger logger = Logger.getLogger(GeneradorJsons.class.getName());
    private static final String ARCHIVO_PROPIEDADES = "src/main/resources/rutas.properties";

    private static final ConfiguracionPropiedades propiedades;

    static {
        try {
            propiedades = new ConfiguracionPropiedades(ARCHIVO_PROPIEDADES);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static final String apiUrl = propiedades.obtenerValor("apiUrl");
    private static final String ligaId = propiedades.obtenerValor("leagueId");
    private static final String temporadaId = propiedades.obtenerValor("temporadaId");
    private static final String hostNombre = propiedades.obtenerValor("hostNombre");
    private static final String hostValor = propiedades.obtenerValor("hostValor");
    private static final String keyNombre = propiedades.obtenerValor("keyNombre");
    private static final String keyValor = propiedades.obtenerValor("keyValor");

    /*
     * Método para probar la conexión a la API
     * 
     */
    public static void testConexion() {
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conexion = ConexionHttp.abrirConexion(url, hostNombre, hostValor, keyNombre, keyValor);
            int codigoRespuesta = conexion.getResponseCode();
            logger.info("Código de respuesta: " + codigoRespuesta);
        } catch (IOException e) {
            logger.severe("Error en la conexión: " + e.getMessage());
        }
    }

    /*
     * Método para generar los JSONs de equipos y partidos
     * 
     * @param carpetaPartidos Carpeta donde se guardarán los JSONs de partidos
     * 
     * @param carpetaEquipos Carpeta donde se guardará el JSON de equipos
     * 
     */
    public static void generarJsons(String carpetaPartidos, String carpetaEquipos) {
        generarJsonEquipos(carpetaEquipos);
        generarJsonsPartidos(carpetaPartidos, carpetaEquipos);
    }

    /*
     * Método para generar el JSON de equipos
     * 
     * @param carpeta Carpeta donde se guardará el JSON de equipos
     * 
     */
    public static void generarJsonEquipos(String carpeta) {
        try {

            String equiposUrl = propiedades.obtenerValor("equiposUrl");

            URL url = new URL(apiUrl + equiposUrl + "?league=" + ligaId + "&season=" + temporadaId);
            HttpURLConnection conexion = ConexionHttp.abrirConexion(url, hostNombre, hostValor, keyNombre, keyValor);
            String respuesta = ConexionHttp.obtenerRespuesta(conexion);
            JSONArray equipos = ProcesadorJson.procesarEquipos(respuesta);
            ArchivoJson.guardarEquiposEnArchivo(equipos, carpeta);
        } catch (IOException e) {
            logger.severe("Error en la generación de JSON: " + e.getMessage());
        }
    }

    /*
     * Método para generar los JSONs de partidos
     * 
     * @param carpeta Carpeta donde se guardarán los JSONs de partidos
     * 
     * @param carpetaEquipos Carpeta donde se encuentra el JSON de equipos
     * 
     */
    public static void generarJsonsPartidos(String carpeta, String carpetaEquipos) {
        try {
            JSONTokener tokener = new JSONTokener(new FileInputStream(carpetaEquipos + "equipos.json"));
            JSONArray equiposTotales = new JSONArray(tokener);

            String partidosUrl = propiedades.obtenerValor("partidosUrl");

            File directorio = new File(carpeta);
            if (!directorio.exists() && !directorio.mkdirs()) {
                throw new IOException("No se pudo crear la carpeta: " + carpeta);
            }

            for (int i = 0; i < equiposTotales.length(); i++) {
                int equipoId = equiposTotales.getJSONObject(i).getInt("id");
                String nombreEquipo = equiposTotales.getJSONObject(i).getString("nombre");
                URL url = new URL(apiUrl + partidosUrl + "?season=" + temporadaId + "&team=" + equipoId);
                HttpURLConnection conexion = ConexionHttp.abrirConexion(url, hostNombre, hostValor, keyNombre,
                        keyValor);
                String respuesta = ConexionHttp.obtenerRespuesta(conexion);
                JSONArray partidos = ProcesadorJson.procesarPartidos(respuesta);
                ArchivoJson.guardarPartidosEnArchivo(partidos, carpeta, equipoId, nombreEquipo.replace(" ", "_"));

                // Si no le metía un timeout entre peticiones armaba jsons vacios
                Thread.sleep(2700);

            }

        } catch (IOException e) {
            logger.severe("Error en la generación de JSON de partidos: " + e.getMessage());
        } catch (InterruptedException e) {
            logger.severe("Error en el retraso entre solicitudes: " + e.getMessage());
        }
    }
}
