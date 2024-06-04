package uns.ac.rs.integration;

import io.quarkus.test.InjectMock;
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
import uns.ac.rs.GeneralResponse;
import uns.ac.rs.MicroserviceCommunicator;
import uns.ac.rs.config.IntegrationConfig;
import uns.ac.rs.controller.NotificationController;
import uns.ac.rs.dto.NotificationStatusesDTO;
import uns.ac.rs.model.NotificationType;
import static org.hamcrest.Matchers.*;

import java.net.URL;

import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.doReturn;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NotificationControllerTests {

    @Autowired
    private IntegrationConfig config;

    public static String createdNotificationId;

    @TestHTTPEndpoint(NotificationController.class)
    @TestHTTPResource("create")
    URL createNotificationEndpoint;

    @TestHTTPEndpoint(NotificationController.class)
    @TestHTTPResource("users-notifications")
    URL getUsersNotificationsEndpoint;

    @InjectMock
    private MicroserviceCommunicator microserviceCommunicator;

    @Test
    @Order(1)
    public void whenCreateNotification_thenReturnCreatedInfo() {
        String requestBody = """
                    {
                        "receiverEmail": "pera@gmail.com",
                        "notificationType": "RESERVATION_REQUESTED",
                        "senderEmail": "sender@gmail.com",
                        "accommodationId": 1
                    }""";

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
        .when()
                .post(createNotificationEndpoint)
        .then()
                .statusCode(201)
                .body("data.receiverEmail", equalTo("pera@gmail.com"))
                .body("data.notificationType", equalTo(NotificationType.RESERVATION_REQUESTED.toString()))
                .body("data.senderEmail", equalTo("sender@gmail.com"))
                .body("data.read", equalTo(false))
                .extract().response();

        createdNotificationId = response.path("data.id");
    }

    @Test
    @Order(2)
    public void whenGetNotificationsWithoutActiveTypes_thenReturnZeroNotifications() {
        doReturn(new GeneralResponse("pera@gmail.com", "200"))
                .when(microserviceCommunicator)
                .processResponse(config.userServiceAPI() + "/auth/authorize/everyone",
                        "GET",
                        "Bearer good-jwt");

        NotificationStatusesDTO notificationStatusesDTO = new NotificationStatusesDTO();
        notificationStatusesDTO.setReservationRequestedNotificationsActive(false);
        notificationStatusesDTO.setAccommodationRatedNotificationsActive(false);
        notificationStatusesDTO.setHostRatedNotificationsActive(false);
        notificationStatusesDTO.setReservationCancelledNotificationsActive(false);
        notificationStatusesDTO.setReservationRequestAnsweredActive(false);

        doReturn(new GeneralResponse(notificationStatusesDTO, "200"))
                .when(microserviceCommunicator)
                .processResponse(config.userServiceAPI() + "/user/retrieve-active-notification-types",
                        "GET",
                        "");

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer good-jwt")
        .when()
                .get(getUsersNotificationsEndpoint)
        .then()
                .statusCode(200)
                .body("data.size()", equalTo(0))
                .body("message", equalTo("Successfully retrieved notifications"));
    }

    @Test
    @Order(3)
    public void whenGetNotificationsWithActiveTypes_thenReturnNotifications() {
        doReturn(new GeneralResponse("pera@gmail.com", "200"))
                .when(microserviceCommunicator)
                .processResponse(config.userServiceAPI() + "/auth/authorize/everyone",
                        "GET",
                        "Bearer good-jwt");

        NotificationStatusesDTO notificationStatusesDTO = new NotificationStatusesDTO();
        notificationStatusesDTO.setReservationRequestedNotificationsActive(true);
        notificationStatusesDTO.setAccommodationRatedNotificationsActive(false);
        notificationStatusesDTO.setHostRatedNotificationsActive(false);
        notificationStatusesDTO.setReservationCancelledNotificationsActive(false);
        notificationStatusesDTO.setReservationRequestAnsweredActive(false);

        doReturn(new GeneralResponse(notificationStatusesDTO, "200"))
                .when(microserviceCommunicator)
                .processResponse(config.userServiceAPI() + "/user/retrieve-active-notification-types",
                        "GET",
                        "");

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer good-jwt")
        .when()
                .get(getUsersNotificationsEndpoint)
        .then()
                .statusCode(200)
                .body("data.size()", greaterThan(0))
                .body("message", equalTo("Successfully retrieved notifications"));
    }
}
