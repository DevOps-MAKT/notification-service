package uns.ac.rs.integration;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import uns.ac.rs.controller.NotificationController;
import uns.ac.rs.model.NotificationType;
import static org.hamcrest.Matchers.*;

import java.net.URL;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NotificationControllerTests {

    @TestHTTPEndpoint(NotificationController.class)
    @TestHTTPResource("create")
    URL createNotificationEndpoint;

    @Test
    @Order(1)
    public void whenCreateNotification_thenReturnCreatedInfo() {
        String requestBody = """
                    {
                        "receiverId": 1,
                        "notificationType": "RESERVATION_REQUESTED",
                        "senderEmail": "user@gmail.com",
                        "accommodationId": 1
                    }""";

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
        .when()
                .post(createNotificationEndpoint)
        .then()
                .statusCode(201)
                .body("data.receiverId", equalTo(1))
                .body("data.notificationType", equalTo(NotificationType.RESERVATION_REQUESTED.toString()))
                .body("data.senderEmail", equalTo("user@gmail.com"))
                .body("data.message", notNullValue())
                .body("data.read", equalTo(false));
    }
}
