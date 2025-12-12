import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CIWorkflowTest {

    private static final String CI_WORKFLOW_PATH = "../.github/workflows/ci.yml";
    private static final String[] SERVICES = {"eureka-server", "api-gateway", "auth-service", "company-service"};

    @Test
    public void shouldSkipBuildAndPushWhenNoChangesDetected() throws IOException {
        Yaml yaml = new Yaml();
        Map<String, Object> ciConfig;
        
        try (FileInputStream fis = new FileInputStream(Paths.get(CI_WORKFLOW_PATH).toFile())) {
            ciConfig = yaml.load(fis);
        }

        // Get the docker-build job
        Map<String, Object> jobs = (Map<String, Object>) ciConfig.get("jobs");
        assertNotNull(jobs, "Jobs should be defined in CI workflow");

        Map<String, Object> dockerBuildJob = (Map<String, Object>) jobs.get("docker-build");
        assertNotNull(dockerBuildJob, "docker-build job should exist");

        // Verify strategy matrix contains all services
        Map<String, Object> strategy = (Map<String, Object>) dockerBuildJob.get("strategy");
        assertNotNull(strategy, "Strategy should be defined");

        Map<String, Object> matrix = (Map<String, Object>) strategy.get("matrix");
        assertNotNull(matrix, "Matrix should be defined");

        List<String> services = (List<String>) matrix.get("service");
        assertNotNull(services, "Services should be defined in matrix");
        for (String service : SERVICES) {
            assertTrue(services.contains(service), "Service " + service + " should be in matrix");
        }

        // Verify the workflow has changed-files detection
        List<Map<String, Object>> steps = (List<Map<String, Object>>) dockerBuildJob.get("steps");
        assertNotNull(steps, "Steps should be defined");

        // Find the "Check changed files" step
        Map<String, Object> changedFilesStep = steps.stream()
                .filter(step -> "changed_files".equals(step.get("id")))
                .findFirst()
                .orElse(null);
        assertNotNull(changedFilesStep, "Check changed files step should exist");

        // Verify it uses tj-actions/changed-files
        assertEquals("tj-actions/changed-files@v44", changedFilesStep.get("uses"),
                "Should use tj-actions/changed-files@v44");

        // Verify files filter includes service directory
        Map<String, Object> with = (Map<String, Object>) changedFilesStep.get("with");
        assertNotNull(with, "With parameters should be defined");
        assertNotNull(with.get("files"), "Files filter should be defined");

        // Find the "Skip if not changed" step
        Map<String, Object> skipStep = steps.stream()
                .filter(step -> step.containsKey("name") && 
                        step.get("name").toString().contains("Skip if not changed"))
                .findFirst()
                .orElse(null);
        assertNotNull(skipStep, "Skip if not changed step should exist");

        // Verify the skip condition
        String skipCondition = (String) skipStep.get("if");
        assertNotNull(skipCondition, "Skip condition should be defined");
        assertTrue(skipCondition.contains("steps.changed_files.outputs.any_changed != 'true'"),
                "Skip condition should check if no changes detected");

        // Verify run command exists and logs the skip
        String runCommand = (String) skipStep.get("run");
        assertNotNull(runCommand, "Run command should be defined");
        assertTrue(runCommand.contains("echo"), "Should log skip message");
        assertTrue(runCommand.contains("Skipping build and push"), "Should indicate skipping build and push");

        // Verify subsequent steps only run when changes are detected
        List<String> conditionalSteps = List.of("Login to DockerHub", "set IMAGE_NAME", 
                "Build Docker image", "Push Docker images");
        
        for (String stepName : conditionalSteps) {
            Map<String, Object> step = steps.stream()
                    .filter(s -> stepName.equals(s.get("name")))
                    .findFirst()
                    .orElse(null);
            assertNotNull(step, stepName + " step should exist");
            
            String condition = (String) step.get("if");
            assertNotNull(condition, stepName + " should have a condition");
            assertTrue(condition.contains("steps.changed_files.outputs.any_changed == 'true'"),
                    stepName + " should only run when changes are detected");
        }
    }

    @Test
    public void shouldCheckChangesInServiceDirectory() throws IOException {
        Yaml yaml = new Yaml();
        Map<String, Object> ciConfig;
        
        try (FileInputStream fis = new FileInputStream(Paths.get(CI_WORKFLOW_PATH).toFile())) {
            ciConfig = yaml.load(fis);
        }

        Map<String, Object> jobs = (Map<String, Object>) ciConfig.get("jobs");
        Map<String, Object> dockerBuildJob = (Map<String, Object>) jobs.get("docker-build");
        List<Map<String, Object>> steps = (List<Map<String, Object>>) dockerBuildJob.get("steps");

        // Find the changed files step
        Map<String, Object> changedFilesStep = steps.stream()
                .filter(step -> "changed_files".equals(step.get("id")))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Changed files step not found"));

        Map<String, Object> with = (Map<String, Object>) changedFilesStep.get("with");
        String filesFilter = (String) with.get("files");
        
        assertNotNull(filesFilter, "Files filter should be defined");
        assertTrue(filesFilter.contains("${{ matrix.service }}/**"),
                "Files filter should check for changes in service directory");
    }
}
