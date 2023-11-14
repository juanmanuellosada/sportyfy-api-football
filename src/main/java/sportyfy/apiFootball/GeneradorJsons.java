package sportyfy.apiFootball;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Logger;

/**
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

    /**
     * Método para probar la conexión a la API
     */
    public static void testConexion() {
        try {
            HttpURLConnection conexion = (HttpURLConnection) new URL(apiUrl).openConnection();
            conexion.setRequestProperty(hostNombre, hostValor);
            conexion.setRequestProperty(keyNombre, keyValor);
            int codigoRespuesta = conexion.getResponseCode();
            logger.info("Código de respuesta: " + codigoRespuesta);
        } catch (IOException e) {
            logger.severe("Error en la conexión a la API: " + e.getMessage());
        }
    }

    /**
     * Método para generar los JSONs de equipos y partidos
     * 
     * @param carpetaPartidos Carpeta donde se guardarán los JSONs de partidos
     * 
     * @param carpetaEquipos  Carpeta donde se guardará el JSON de equipos
     * 
     */
    public static void generarJsons(String carpetaPartidos, String carpetaEquipos) {
        generarJsonEquipos(carpetaEquipos);
        generarJsonsPartidos(carpetaPartidos, carpetaEquipos);
    }

    /**
     * Método para generar el JSON de equipos
     * 
     * @param carpeta Carpeta donde se guardará el JSON de equipos
     * 
     */
    public static void generarJsonEquipos(String carpeta) {
        System.out.println("carpeta equipos: " + carpeta);
        try {

            String equiposUrl = propiedades.obtenerValor("equiposUrl");
            String url = apiUrl + equiposUrl + "?league=" + ligaId + "&season=" + temporadaId;
            HttpURLConnection conexion = ConexionHttp.abrirConexion(url, hostNombre, hostValor, keyNombre, keyValor);
            String respuesta = ConexionHttp.obtenerRespuesta(conexion);
            JSONArray equipos = ProcesadorJson.procesarEquipos(respuesta);
            ArchivoJson.guardarEquiposEnArchivo(equipos, carpeta);
        } catch (IOException e) {
            logger.severe("Error en la generación de JSON: " + e.getMessage());
        } catch (URISyntaxException e) {
            logger.severe("Error en la URI: " + e.getMessage());
        }
    }

    /**
     * Método para generar los JSONs de partidos
     * 
     * @param carpeta        Carpeta donde se guardarán los JSONs de partidos
     * 
     * @param carpetaEquipos Carpeta donde se encuentra el JSON de equipos
     * 
     */
    @SuppressWarnings("BusyWait")
    public static void generarJsonsPartidos(String carpeta, String carpetaEquipos) {
        try (FileInputStream fis = new FileInputStream(carpetaEquipos + "equipos.json")) {
            JSONArray equiposTotales = new JSONArray(new JSONTokener(fis));
            String partidosUrl = propiedades.obtenerValor("partidosUrl");

            File directorio = new File(carpeta);
            if (!directorio.exists() && !directorio.mkdirs()) {
                throw new IOException("No se pudo crear la carpeta: " + carpeta);
            }

            for (int i = 0; i < equiposTotales.length(); i++) {
                JSONObject equipo = equiposTotales.getJSONObject(i);
                String url = apiUrl + partidosUrl + "?season=" + temporadaId + "&team=" + equipo.getInt("id");
                HttpURLConnection conexion = ConexionHttp.abrirConexion(url, hostNombre, hostValor, keyNombre,
                        keyValor);
                JSONArray partidos = ProcesadorJson.procesarPartidos(ConexionHttp.obtenerRespuesta(conexion));
                ArchivoJson.guardarPartidosEnArchivo(partidos, carpeta, equipo.getInt("id"),
                        equipo.getString("nombre").replace(" ", "_"));

                // Retraso de 3 segundos entre solicitudes para no superar el límite de
                // peticiones por minuto
                Thread.sleep(6000);
            }
        } catch (IOException e) {
            logger.severe("Error en la generación de JSON de partidos: " + e.getMessage());
        } catch (InterruptedException e) {
            logger.severe("Error en el retraso entre solicitudes: " + e.getMessage());
        } catch (URISyntaxException e) {
            logger.severe("Error en la URI: " + e.getMessage());
        }
    }
}
