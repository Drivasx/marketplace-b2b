import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.notNullValue;

public class AuthIntegrationTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:4001";
    }

    @Test
    public void shouldReturnOkWithValidToken() {
        String payload = """
                    {
                        "email": "admin@system.com",
                          "password": "password"
                    }
                
                """;

        Response response = RestAssured.given()
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .extract()
                .response();

        System.out.println("Generated token: "+response.jsonPath().getString("token"));
    }


    @Test
    public void shouldReturnUnauthorizedWithInvalidToken() {
        String payload = """
                    {
                        "email": "invalid@user.com",
                          "password": "invalid_password"
                    }
                
                """;

        RestAssured.given()
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(401);
    }
}
