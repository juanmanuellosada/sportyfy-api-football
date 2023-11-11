package sportyfy.apiFootball;

import org.json.JSONArray;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/*
 * Clase para guardar los JSONs de equipos y partidos en archivos
 * 
 */
public class ArchivoJson {

    public static void guardarEquiposEnArchivo(JSONArray equipos, String carpeta) throws IOException {
        crearDirectorio(carpeta);
        File archivoEquipos = new File(carpeta, "equipos.json");
        try (FileWriter fileWriter = new FileWriter(archivoEquipos)) {
            fileWriter.write(equipos.toString(4));
        }
    }

    public static void guardarPartidosEnArchivo(JSONArray partidos, String carpeta, int equipoId, String nombreEquipo)
            throws IOException {
        crearDirectorio(carpeta);
        String archivoJSON = carpeta + File.separator + "partidos_" + nombreEquipo + ".json";
        try (FileWriter fileWriter = new FileWriter(archivoJSON)) {
            fileWriter.write(partidos.toString(4));
        }
    }

    private static void crearDirectorio(String carpeta) throws IOException {
        File directorio = new File(carpeta);
        if (!directorio.exists() && !directorio.mkdirs()) {
            throw new IOException("No se pudo crear la carpeta: " + carpeta);
        }
    }

}
