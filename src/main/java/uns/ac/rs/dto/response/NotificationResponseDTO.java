package uns.ac.rs.dto.response;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;
import org.bson.types.ObjectId;
import uns.ac.rs.model.Notification;
import uns.ac.rs.model.NotificationType;

@Data
@RegisterForReflection
public class NotificationResponseDTO {
    private ObjectId id;
    private String receiverEmail;
    private NotificationType notificationType;
    public String senderEmail;
    public String message;
    public String timestamp;
    public boolean read;

    public NotificationResponseDTO(Notification notification) {
        this.id = notification.getId();
        this.receiverEmail = notification.getReceiverEmail();
        this.notificationType = notification.getNotificationType();
        this.senderEmail = notification.getSenderEmail();
        this.message = notification.getMessage();
        this.timestamp = notification.getTimestamp();
        this.read = notification.isRead();
    }
}
