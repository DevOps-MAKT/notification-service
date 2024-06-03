package uns.ac.rs.dto.request;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;
import uns.ac.rs.model.NotificationType;

@Data
@RegisterForReflection
public class NotificationDTO {
    private Long receiverId;
    private NotificationType notificationType;
    private String senderEmail;
    public int rating;
    public Long accommodationId;
    public boolean requestAccepted;

    public NotificationDTO() {

    }

}
