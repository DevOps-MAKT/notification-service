package uns.ac.rs.controller;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import uns.ac.rs.GeneralResponse;
import uns.ac.rs.MicroserviceCommunicator;
import uns.ac.rs.config.IntegrationConfig;
import uns.ac.rs.dto.NotificationStatusesDTO;
import uns.ac.rs.dto.request.NotificationDTO;
import uns.ac.rs.dto.response.NotificationResponseDTO;
import uns.ac.rs.model.Notification;
import uns.ac.rs.service.NotificationService;

import java.util.ArrayList;
import java.util.List;

@Path("/notification")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NotificationController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private MicroserviceCommunicator microserviceCommunicator;
    @Inject
    private IntegrationConfig config;

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createNotification(NotificationDTO notificationDTO) {
        try {
            Notification notification = notificationService.createNotification(notificationDTO);
            logger.info("Notification successfully created");
            return Response.status(Response.Status.CREATED)
                    .entity(new GeneralResponse<>(new NotificationResponseDTO(notification),
                            "Notification successfully created"))
                    .build();
        } catch (Exception e) {
            logger.error("Error creating notification: {}",  e.getLocalizedMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error creating notification").build();
        }
    }

    @PATCH
    @Path("/update-read-status/{notification_id}")
    public Response updateReadStatus(@HeaderParam("Authorization") String authorizationHeader,
                                     @PathParam("notification_id") String notificationId) {
        GeneralResponse response = microserviceCommunicator.processResponse(
                config.userServiceAPI() + "/auth/authorize/everyone",
                "GET",
                authorizationHeader);

        String userEmail = (String) response.getData();
        if (userEmail.equals("")) {
            logger.warn("Unauthorized access for retrieve users notifications");
            return Response.status(Response.Status.UNAUTHORIZED).entity(response).build();
        }
        try {
            Notification notification = notificationService.updateReadStatus(notificationId);
            logger.info("Notification read status successfully updated");
            return Response.status(Response.Status.OK)
                    .entity(new GeneralResponse<>(new NotificationResponseDTO(notification),
                            "Notification read status successfully updated"))
                    .build();
        } catch (Exception e) {
            logger.error("Error updating read status: {}", e.getLocalizedMessage());
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error updating read status").build();
        }
    }

    @GET
    @Path("/users-notifications")
    public Response retrieveUsersNotifications(@HeaderParam("Authorization") String authorizationHeader) {
        GeneralResponse response = microserviceCommunicator.processResponse(
                config.userServiceAPI() + "/auth/authorize/everyone",
                "GET",
                authorizationHeader);

        String userEmail = (String) response.getData();
        if (userEmail.equals("")) {
            logger.warn("Unauthorized access for retrieve users notifications");
            return Response.status(Response.Status.UNAUTHORIZED).entity(response).build();
        }
        try {
            logger.info("Retrieving active notifications for user with email " + userEmail);
            GeneralResponse activeNotifications = microserviceCommunicator.processResponse(
                    config.userServiceAPI() + "/user/retrieve-active-notification-types",
                    "GET",
                    "");

            NotificationStatusesDTO notificationStatusesDTO = (NotificationStatusesDTO) activeNotifications.getData();
            logger.info("Successfully retrieved active notifications for user with email " + userEmail);
            logger.info("Retrieving notifications");
            List<Notification> notifications = notificationService.retrieveNotifications(userEmail, notificationStatusesDTO);
            List<NotificationResponseDTO> notificationResponseDTOS = new ArrayList<>();
            for (Notification notification: notifications) {
                notificationResponseDTOS.add(new NotificationResponseDTO(notification));
            }
            logger.info("Successfully retrieved notifications");
            return Response
                    .ok()
                    .entity(new GeneralResponse<>(notificationResponseDTOS,
                            "Successfully retrieved notifications")
                    )
                    .build();
        } catch (Exception e) {
            logger.error("Error retrieving users notifications: {}", e.getLocalizedMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error retrieving ").build();
        }
    }

}
