package uns.ac.rs.model;

import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.Data;
import org.bson.types.ObjectId;
import uns.ac.rs.dto.request.NotificationDTO;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Data
@MongoEntity(collection="notification")
public class Notification {

    public ObjectId id;
    public String receiverEmail;
    public NotificationType notificationType;
    public String senderEmail;
    public String message;
    public String timestamp;
    public int rating;
    public Long accommodationId;
    public boolean requestAccepted;
    public boolean read;

    public Notification(NotificationDTO notificationDTO) {
        this.receiverEmail = notificationDTO.getReceiverEmail();
        this.notificationType = notificationDTO.getNotificationType();
        this.senderEmail = notificationDTO.getSenderEmail();
        this.message = createMessage(notificationDTO);
        this.timestamp = new SimpleDateFormat("dd.MM.yyyy-HH:mm").format(Calendar.getInstance().getTime());
        this.read = false;
        this.rating = notificationDTO.getRating();
        this.accommodationId = notificationDTO.getAccommodationId();
        this.requestAccepted = notificationDTO.isRequestAccepted();
    }

    private String createMessage(NotificationDTO notificationDTO) {
        switch (notificationDTO.getNotificationType()) {
            case ACCOMMODATION_RATED -> {
                return "User " + notificationDTO.getSenderEmail() +
                        " has rated your accommodation with id "  + notificationDTO.getAccommodationId() +
                        " with grade " + notificationDTO.getRating();
            }
            case HOST_RATED -> {
                return "User " + notificationDTO.getSenderEmail() +
                        " has rated you with grade " + notificationDTO.getRating();
            }
            case RESERVATION_CANCELLED ->  {
                return "User " + notificationDTO.getSenderEmail() +
                        " has cancelled reservation for accommodation with id " + notificationDTO.getAccommodationId();
            }
            case RESERVATION_REQUESTED -> {
                return "User " + notificationDTO.getSenderEmail() +
                        " has requested reservation for accommodation with id " + notificationDTO.getAccommodationId();
            }
            case RESERVATION_REQUEST_ANSWERED -> {
                String decision = "declined";
                if (notificationDTO.isRequestAccepted()) {
                    decision = "accepted";
                }
                return "Host " + notificationDTO.getSenderEmail() +
                        " has " + decision + " your reservation request";
            }
        }
        return null;
    }

    public Notification() {

    }


}
