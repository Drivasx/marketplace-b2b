import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SpringProfileConfigTest {

    private static final String DOCKER_COMPOSE_PATH = "../docker-compose.yml";

    @Test
    public void authServiceShouldActivateProdProfile() throws IOException {
        Map<String, Object> dockerCompose = loadDockerCompose();
        Map<String, Object> services = (Map<String, Object>) dockerCompose.get("services");
        
        Map<String, Object> authService = (Map<String, Object>) services.get("auth-service");
        assertNotNull(authService, "auth-service should be defined");

        List<String> environment = (List<String>) authService.get("environment");
        assertNotNull(environment, "auth-service should have environment variables");

        boolean hasProdProfile = environment.stream()
                .anyMatch(env -> env.equals("SPRING_PROFILES_ACTIVE=prod"));
        assertTrue(hasProdProfile, "auth-service should activate 'prod' Spring profile");
    }

    @Test
    public void apiGatewayShouldActivateProdProfile() throws IOException {
        Map<String, Object> dockerCompose = loadDockerCompose();
        Map<String, Object> services = (Map<String, Object>) dockerCompose.get("services");
        
        Map<String, Object> apiGateway = (Map<String, Object>) services.get("api-gateway");
        assertNotNull(apiGateway, "api-gateway should be defined");

        List<String> environment = (List<String>) apiGateway.get("environment");
        assertNotNull(environment, "api-gateway should have environment variables");

        boolean hasProdProfile = environment.stream()
                .anyMatch(env -> env.equals("SPRING_PROFILES_ACTIVE=prod"));
        assertTrue(hasProdProfile, "api-gateway should activate 'prod' Spring profile");
    }

    @Test
    public void companyServiceShouldActivateProdProfile() throws IOException {
        Map<String, Object> dockerCompose = loadDockerCompose();
        Map<String, Object> services = (Map<String, Object>) dockerCompose.get("services");
        
        Map<String, Object> companyService = (Map<String, Object>) services.get("company-service");
        assertNotNull(companyService, "company-service should be defined");

        List<String> environment = (List<String>) companyService.get("environment");
        assertNotNull(environment, "company-service should have environment variables");

        boolean hasProdProfile = environment.stream()
                .anyMatch(env -> env.equals("SPRING_PROFILES_ACTIVE=prod"));
        assertTrue(hasProdProfile, "company-service should activate 'prod' Spring profile");
    }

    @Test
    public void allRequiredServicesShouldActivateProdProfile() throws IOException {
        Map<String, Object> dockerCompose = loadDockerCompose();
        Map<String, Object> services = (Map<String, Object>) dockerCompose.get("services");
        
        String[] requiredServices = {"auth-service", "api-gateway", "company-service"};
        
        for (String serviceName : requiredServices) {
            Map<String, Object> service = (Map<String, Object>) services.get(serviceName);
            assertNotNull(service, serviceName + " should be defined");
            
            List<String> environment = (List<String>) service.get("environment");
            assertNotNull(environment, serviceName + " should have environment variables");
            
            boolean hasProdProfile = environment.stream()
                    .anyMatch(env -> env.equals("SPRING_PROFILES_ACTIVE=prod"));
            assertTrue(hasProdProfile, serviceName + " should activate 'prod' Spring profile");
        }
    }

    @Test
    public void eurekaServerShouldNotHaveSpringProfilesActive() throws IOException {
        Map<String, Object> dockerCompose = loadDockerCompose();
        Map<String, Object> services = (Map<String, Object>) dockerCompose.get("services");
        
        Map<String, Object> eurekaServer = (Map<String, Object>) services.get("eureka-server");
        assertNotNull(eurekaServer, "eureka-server should be defined");

        List<String> environment = (List<String>) eurekaServer.get("environment");
        
        // eureka-server may not have environment variables or may not have SPRING_PROFILES_ACTIVE
        if (environment != null) {
            boolean hasSpringProfilesActive = environment.stream()
                    .anyMatch(env -> env.startsWith("SPRING_PROFILES_ACTIVE="));
            assertFalse(hasSpringProfilesActive, 
                    "eureka-server should not have SPRING_PROFILES_ACTIVE in docker-compose");
        }
    }

    private Map<String, Object> loadDockerCompose() throws IOException {
        Yaml yaml = new Yaml();
        try (FileInputStream fis = new FileInputStream(Paths.get(DOCKER_COMPOSE_PATH).toFile())) {
            return yaml.load(fis);
        }
    }
}
