package uns.ac.rs.dto.response;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;
import org.bson.types.ObjectId;
import uns.ac.rs.model.Notification;

@Data
@RegisterForReflection
public class NotificationResponseDTO {
    private ObjectId id;
    private String from;
    private String to;
    private String msg;
    public NotificationResponseDTO(Notification notification) {
        this.id = notification.getId();
        this.from = notification.getFrom();
        this.to = notification.getTo();
        this.msg = notification.getMsg();
    }
}
