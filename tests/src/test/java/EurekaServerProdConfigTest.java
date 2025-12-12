import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class EurekaServerProdConfigTest {

    private static final String EUREKA_SERVER_PROD_CONFIG_PATH = 
            "../eureka-server/src/main/resources/application-prod.yml";

    @Test
    public void eurekaServerShouldNotRegisterWithEurekaInProdProfile() throws IOException {
        Map<String, Object> config = loadYamlConfig(EUREKA_SERVER_PROD_CONFIG_PATH);
        
        Map<String, Object> eureka = (Map<String, Object>) config.get("eureka");
        assertNotNull(eureka, "eureka configuration should be defined in prod profile");

        Map<String, Object> client = (Map<String, Object>) eureka.get("client");
        assertNotNull(client, "eureka.client configuration should be defined");

        Object registerWithEureka = client.get("register-with-eureka");
        assertNotNull(registerWithEureka, "register-with-eureka should be configured");
        assertEquals(false, registerWithEureka, 
                "eureka-server should not register with eureka in prod profile");
    }

    @Test
    public void eurekaServerShouldNotFetchRegistryInProdProfile() throws IOException {
        Map<String, Object> config = loadYamlConfig(EUREKA_SERVER_PROD_CONFIG_PATH);
        
        Map<String, Object> eureka = (Map<String, Object>) config.get("eureka");
        assertNotNull(eureka, "eureka configuration should be defined in prod profile");

        Map<String, Object> client = (Map<String, Object>) eureka.get("client");
        assertNotNull(client, "eureka.client configuration should be defined");

        Object fetchRegistry = client.get("fetch-registry");
        assertNotNull(fetchRegistry, "fetch-registry should be configured");
        assertEquals(false, fetchRegistry, 
                "eureka-server should not fetch registry in prod profile");
    }

    @Test
    public void eurekaServerShouldHaveBothRegistryPropertiesDisabled() throws IOException {
        Map<String, Object> config = loadYamlConfig(EUREKA_SERVER_PROD_CONFIG_PATH);
        
        Map<String, Object> eureka = (Map<String, Object>) config.get("eureka");
        assertNotNull(eureka, "eureka configuration should be defined");

        Map<String, Object> client = (Map<String, Object>) eureka.get("client");
        assertNotNull(client, "eureka.client configuration should be defined");

        // Verify both properties are false
        Object registerWithEureka = client.get("register-with-eureka");
        Object fetchRegistry = client.get("fetch-registry");
        
        assertNotNull(registerWithEureka, "register-with-eureka should be configured");
        assertNotNull(fetchRegistry, "fetch-registry should be configured");
        
        assertEquals(false, registerWithEureka, "register-with-eureka should be false");
        assertEquals(false, fetchRegistry, "fetch-registry should be false");
    }

    @Test
    public void eurekaServerProdConfigShouldDefineServerPort() throws IOException {
        Map<String, Object> config = loadYamlConfig(EUREKA_SERVER_PROD_CONFIG_PATH);
        
        Map<String, Object> server = (Map<String, Object>) config.get("server");
        assertNotNull(server, "server configuration should be defined");

        Object port = server.get("port");
        assertNotNull(port, "server.port should be configured");
        assertEquals(8761, port, "eureka-server should run on port 8761");
    }

    @Test
    public void eurekaServerProdConfigShouldHaveInstanceConfiguration() throws IOException {
        Map<String, Object> config = loadYamlConfig(EUREKA_SERVER_PROD_CONFIG_PATH);
        
        Map<String, Object> eureka = (Map<String, Object>) config.get("eureka");
        assertNotNull(eureka, "eureka configuration should be defined");

        Map<String, Object> instance = (Map<String, Object>) eureka.get("instance");
        assertNotNull(instance, "eureka.instance configuration should be defined");

        String hostname = (String) instance.get("hostname");
        assertEquals("eureka-server", hostname, "hostname should be 'eureka-server'");

        Object preferIpAddress = instance.get("prefer-ip-address");
        assertEquals(true, preferIpAddress, "prefer-ip-address should be true");
    }

    private Map<String, Object> loadYamlConfig(String path) throws IOException {
        Yaml yaml = new Yaml();
        try (FileInputStream fis = new FileInputStream(Paths.get(path).toFile())) {
            return yaml.load(fis);
        }
    }
}
