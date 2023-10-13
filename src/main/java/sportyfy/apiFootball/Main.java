package sportyfy.apiFootball;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println(leerProperties("apiUrl"));
        System.out.println("Hello world!");
    }

    public static String leerProperties(String propertie) throws IOException {
        Properties prop = new Properties();

        FileInputStream input = new FileInputStream("src/main/resources/rutas.properties");
        prop.load(input);

        return prop.getProperty(propertie);
    }
}