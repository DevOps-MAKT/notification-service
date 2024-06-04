package uns.ac.rs.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import uns.ac.rs.model.Notification;
import uns.ac.rs.model.NotificationType;

import java.util.List;

@ApplicationScoped
public class NotificationRepository implements PanacheMongoRepository<Notification> {

    public List<Notification> findByReceiverEmailAndTypes(String receiverEmail, List<NotificationType> types) {
        return find("receiverEmail = ?1 and notificationType in ?2", receiverEmail, types).list();
    }
}
