package uns.ac.rs.model;

import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.Data;
import org.bson.types.ObjectId;
import uns.ac.rs.dto.request.NotificationDTO;

@Data
@MongoEntity(collection="notification")
public class Notification {

    public ObjectId id;
    private String from;
    private String to;
    private String msg;

    public Notification(NotificationDTO form) {
        this.from = form.from;
        this.to = form.to;
        this.msg = form.msg;
    }
}
