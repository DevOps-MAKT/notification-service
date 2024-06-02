package uns.ac.rs.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import uns.ac.rs.model.Notification;

@ApplicationScoped
public class NotificationRepository implements PanacheMongoRepository<Notification> {
}
