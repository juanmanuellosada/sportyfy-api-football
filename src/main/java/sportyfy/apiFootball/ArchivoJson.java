package sportyfy.apiFootball;

import org.json.JSONArray;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ArchivoJson {

    public static void guardarEquiposEnArchivo(JSONArray equipos, String carpeta) throws IOException {
        File directorio = new File(carpeta);
        if (!directorio.exists() && !directorio.mkdirs()) {
            throw new IOException("No se pudo crear la carpeta: " + carpeta);
        }

        File archivoEquipos = new File(directorio, "equipos.json");

        try (FileWriter fileWriter = new FileWriter(archivoEquipos)) {
            fileWriter.write(equipos.toString(4));
        }
    }

    public static void guardarPartidosEnArchivo(JSONArray partidos, String carpeta, int equipoId) throws IOException {
        File directorio = new File(carpeta);
        if (!directorio.exists() && !directorio.mkdirs()) {
            throw new IOException("No se pudo crear la carpeta: " + carpeta);
        }

        String archivoJSON = carpeta + File.separator + "ultimos_resultados_" + equipoId + ".json";

        try (FileWriter fileWriter = new FileWriter(archivoJSON)) {
            fileWriter.write(partidos.toString(4));
        }
    }

}
