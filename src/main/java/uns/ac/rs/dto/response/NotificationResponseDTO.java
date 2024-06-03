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
    private Long receiverId;
    private NotificationType notificationType;
    public String senderEmail;
    public String message;
    public String timestamp;
    public boolean read;

    public NotificationResponseDTO(Notification notification) {
        this.id = notification.getId();
        this.receiverId = notification.getReceiverId();
        this.notificationType = notification.getNotificationType();
        this.senderEmail = notification.getSenderEmail();
        this.message = notification.getMessage();
        this.timestamp = notification.getTimestamp();
        this.read = notification.isRead();
    }
}
