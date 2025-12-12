import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class DockerComposeConfigTest {

    private static final String DOCKER_COMPOSE_PATH = "../docker-compose.yml";

    @Test
    public void authServiceShouldDependOnEurekaServer() throws IOException {
        Map<String, Object> dockerCompose = loadDockerCompose();
        
        Map<String, Object> services = (Map<String, Object>) dockerCompose.get("services");
        assertNotNull(services, "Services should be defined");

        Map<String, Object> authService = (Map<String, Object>) services.get("auth-service");
        assertNotNull(authService, "auth-service should be defined");

        List<String> dependsOn = (List<String>) authService.get("depends_on");
        assertNotNull(dependsOn, "auth-service should have depends_on");
        assertTrue(dependsOn.contains("eureka-server"), 
                "auth-service should depend on eureka-server");
    }

    @Test
    public void apiGatewayShouldDependOnEurekaServer() throws IOException {
        Map<String, Object> dockerCompose = loadDockerCompose();
        
        Map<String, Object> services = (Map<String, Object>) dockerCompose.get("services");
        assertNotNull(services, "Services should be defined");

        Map<String, Object> apiGateway = (Map<String, Object>) services.get("api-gateway");
        assertNotNull(apiGateway, "api-gateway should be defined");

        List<String> dependsOn = (List<String>) apiGateway.get("depends_on");
        assertNotNull(dependsOn, "api-gateway should have depends_on");
        assertTrue(dependsOn.contains("eureka-server"), 
                "api-gateway should depend on eureka-server");
    }

    @Test
    public void companyServiceShouldDependOnEurekaServer() throws IOException {
        Map<String, Object> dockerCompose = loadDockerCompose();
        
        Map<String, Object> services = (Map<String, Object>) dockerCompose.get("services");
        assertNotNull(services, "Services should be defined");

        Map<String, Object> companyService = (Map<String, Object>) services.get("company-service");
        assertNotNull(companyService, "company-service should be defined");

        List<String> dependsOn = (List<String>) companyService.get("depends_on");
        assertNotNull(dependsOn, "company-service should have depends_on");
        assertTrue(dependsOn.contains("eureka-server"), 
                "company-service should depend on eureka-server");
    }

    @Test
    public void allServicesShouldDependOnEurekaServer() throws IOException {
        Map<String, Object> dockerCompose = loadDockerCompose();
        Map<String, Object> services = (Map<String, Object>) dockerCompose.get("services");
        
        String[] requiredServices = {"auth-service", "api-gateway", "company-service"};
        
        for (String serviceName : requiredServices) {
            Map<String, Object> service = (Map<String, Object>) services.get(serviceName);
            assertNotNull(service, serviceName + " should be defined");
            
            List<String> dependsOn = (List<String>) service.get("depends_on");
            assertNotNull(dependsOn, serviceName + " should have depends_on");
            assertTrue(dependsOn.contains("eureka-server"), 
                    serviceName + " should depend on eureka-server");
        }
    }

    @Test
    public void eurekaServerShouldExist() throws IOException {
        Map<String, Object> dockerCompose = loadDockerCompose();
        Map<String, Object> services = (Map<String, Object>) dockerCompose.get("services");
        
        Map<String, Object> eurekaServer = (Map<String, Object>) services.get("eureka-server");
        assertNotNull(eurekaServer, "eureka-server should be defined");
        
        String image = (String) eurekaServer.get("image");
        assertNotNull(image, "eureka-server should have an image");
        assertTrue(image.contains("eureka-server"), "Image should be eureka-server");
    }

    private Map<String, Object> loadDockerCompose() throws IOException {
        Yaml yaml = new Yaml();
        try (FileInputStream fis = new FileInputStream(Paths.get(DOCKER_COMPOSE_PATH).toFile())) {
            return yaml.load(fis);
        }
    }
}
