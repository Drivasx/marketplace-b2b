# Configuration Unit Tests

This directory contains unit tests that verify the configuration of the CI/CD pipeline, Docker Compose setup, and Spring application profiles.

## Test Files

### 1. CIWorkflowTest.java
Tests the CI workflow configuration (`.github/workflows/ci.yml`) to ensure proper build optimization.

**Test Cases:**
- `shouldSkipBuildAndPushWhenNoChangesDetected()`: Verifies that the CI workflow correctly skips Docker build and push steps when no changes are detected in a service's directory.
- `shouldCheckChangesInServiceDirectory()`: Ensures the changed-files detection is configured to monitor each service's directory (`${{ matrix.service }}/**`).

**What it validates:**
- The workflow uses `tj-actions/changed-files@v44` to detect changes
- Build and push steps are conditional on `steps.changed_files.outputs.any_changed == 'true'`
- The skip step logs when build/push are skipped
- All services (eureka-server, api-gateway, auth-service, company-service) are in the build matrix

### 2. DockerComposeConfigTest.java
Tests the Docker Compose configuration (`docker-compose.yml`) for service dependencies.

**Test Cases:**
- `authServiceShouldDependOnEurekaServer()`: Verifies auth-service depends on eureka-server
- `apiGatewayShouldDependOnEurekaServer()`: Verifies api-gateway depends on eureka-server
- `companyServiceShouldDependOnEurekaServer()`: Verifies company-service depends on eureka-server
- `allServicesShouldDependOnEurekaServer()`: Validates all three services have the eureka-server dependency
- `eurekaServerShouldExist()`: Ensures eureka-server is defined in docker-compose

**What it validates:**
- All microservices that need service discovery properly depend on eureka-server
- This ensures eureka-server starts before dependent services
- Prevents race conditions during container startup

### 3. SpringProfileConfigTest.java
Tests Spring profile activation in Docker Compose environment variables.

**Test Cases:**
- `authServiceShouldActivateProdProfile()`: Verifies auth-service activates 'prod' profile
- `apiGatewayShouldActivateProdProfile()`: Verifies api-gateway activates 'prod' profile
- `companyServiceShouldActivateProdProfile()`: Verifies company-service activates 'prod' profile
- `allRequiredServicesShouldActivateProdProfile()`: Validates all three services activate 'prod' profile
- `eurekaServerShouldNotHaveSpringProfilesActive()`: Ensures eureka-server doesn't have SPRING_PROFILES_ACTIVE set

**What it validates:**
- Services load production-specific configuration when deployed
- Environment variable `SPRING_PROFILES_ACTIVE=prod` is set for each service
- This ensures services use production databases, ports, and eureka settings

### 4. EurekaServerProdConfigTest.java
Tests the eureka-server production profile configuration (`eureka-server/src/main/resources/application-prod.yml`).

**Test Cases:**
- `eurekaServerShouldNotRegisterWithEurekaInProdProfile()`: Verifies `register-with-eureka: false`
- `eurekaServerShouldNotFetchRegistryInProdProfile()`: Verifies `fetch-registry: false`
- `eurekaServerShouldHaveBothRegistryPropertiesDisabled()`: Validates both properties are false
- `eurekaServerProdConfigShouldDefineServerPort()`: Ensures server runs on port 8761
- `eurekaServerProdConfigShouldHaveInstanceConfiguration()`: Validates hostname and IP preference settings

**What it validates:**
- Eureka server doesn't try to register itself as a client
- Eureka server doesn't try to fetch registry from other eureka instances
- This is the correct configuration for a standalone eureka server
- Proper hostname and network configuration for Docker environment

## Running the Tests

### Prerequisites
- Java 21
- Maven 3.x

### Run all tests
```bash
cd tests
mvn clean test
```

### Run specific test class
```bash
mvn test -Dtest=CIWorkflowTest
mvn test -Dtest=DockerComposeConfigTest
mvn test -Dtest=SpringProfileConfigTest
mvn test -Dtest=EurekaServerProdConfigTest
```

## Dependencies
The tests use:
- **JUnit Jupiter 5.12.2**: Testing framework
- **SnakeYAML 2.3**: YAML parsing library for reading configuration files

## Configuration Validation
These tests serve as "infrastructure as code" validation, ensuring:
1. CI/CD pipeline optimizations are in place
2. Service orchestration is configured correctly
3. Production profiles are activated properly
4. Service discovery is configured appropriately

If any test fails, it indicates a misconfiguration that could cause deployment issues.
