package uns.ac.rs.integration;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import uns.ac.rs.config.IntegrationConfig;
import uns.ac.rs.controller.NotificationController;
import uns.ac.rs.model.NotificationType;
import static org.hamcrest.Matchers.*;

import java.net.URL;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NotificationControllerTests {

    @Autowired
    private IntegrationConfig config;

    public static String createdNotificationId;

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

        Response response = given()
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
                .body("data.read", equalTo(false))
                .extract().response();

        createdNotificationId = response.path("data.id");
    }

    @Test
    @Order(2)
    public void whenUpdateNotification_thenReturnReadTrue() {
        given()
                .contentType(ContentType.JSON)
        .when()
                .patch(config.notificationServiceAPI() + "/notification/update-read-status/" + createdNotificationId)
        .then()
                .statusCode(200)
                .body("data.read", equalTo(true));
    }
}
