package sportyfy.apiFootball;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConexionHttp {

    public static HttpURLConnection abrirConexion(URL url, String hostNombre, String hostValor, String keyNombre, String keyValor) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty(hostNombre, hostValor);
        connection.setRequestProperty(keyNombre, keyValor);
        return connection;
    }

    public static String obtenerRespuesta(HttpURLConnection connection) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }
}
