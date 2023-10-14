package sportyfy.apiFootball;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfiguracionPropiedades {
    private final Properties prop;

    public ConfiguracionPropiedades(String filePath) throws IOException {
        prop = new Properties();
        try (FileInputStream input = new FileInputStream(filePath)) {
            prop.load(input);
        }
    }

    public String obtenerValor(String clave) {
        return prop.getProperty(clave);
    }
}
