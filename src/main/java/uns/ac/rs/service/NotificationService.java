package uns.ac.rs.service;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uns.ac.rs.dto.request.NotificationDTO;
import uns.ac.rs.model.Notification;
import uns.ac.rs.repository.NotificationRepository;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public Notification createNotification(NotificationDTO notificationDTO) {
        Notification notification = new Notification(notificationDTO);
        notificationRepository.persist(notification);
        return notification;
    }

    public Notification updateReadStatus(String notificationId) {
        Notification notification = notificationRepository.findById(new ObjectId(notificationId));
        notification.setRead(true);
        notificationRepository.update(notification);
        return notification;
    }
}
