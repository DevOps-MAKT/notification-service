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
import uns.ac.rs.dto.request.NotificationDTO;
import uns.ac.rs.dto.response.NotificationResponseDTO;
import uns.ac.rs.model.Notification;
import uns.ac.rs.service.NotificationService;

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
    public Response updateReadStatus(@PathParam("notification_id") String notificationId) {
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

}
