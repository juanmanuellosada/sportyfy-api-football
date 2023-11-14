package sportyfy.apiFootball;

public class Main {
    public static void main(String[] args) {
        System.out.println("Conectando a la API...");
        GeneradorJsons.testConexion();
        GeneradorJsons.generarJsons("src/main/resources/datos/partidos", "src/main/resources/datos/equipos/");
    }
}
