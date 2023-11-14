package sportyfy.apiFootball;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.stream.Collectors;

/**
 * Clase para abrir y obtener la respuesta de una conexión HTTP
 *
 */
public class ConexionHttp {

    /**
     * Método para abrir una conexión HTTP
     */
    public static HttpURLConnection abrirConexion(String urlString, String hostNombre, String hostValor,
            String keyNombre, String keyValor) throws IOException, URISyntaxException {
        URI uri = new URI(urlString);
        HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty(hostNombre, hostValor);
        connection.setRequestProperty(keyNombre, keyValor);
        return connection;
    }

    /**
     * Método para obtener la respuesta de una conexión HTTP
     */
    public static String obtenerRespuesta(HttpURLConnection connection) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
}
