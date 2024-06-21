package uns.ac.rs.service;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uns.ac.rs.dto.NotificationStatusesDTO;
import uns.ac.rs.dto.request.NotificationDTO;
import uns.ac.rs.model.Notification;
import uns.ac.rs.model.NotificationType;
import uns.ac.rs.repository.NotificationRepository;

import java.util.ArrayList;
import java.util.List;

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

    public List<Notification> retrieveNotifications(String email, NotificationStatusesDTO notificationStatusesDTO) {
        List<NotificationType> neededNotificationTypes = constructNeededNotificationTypes(notificationStatusesDTO);
        return notificationRepository.findByReceiverEmailAndTypes(email, neededNotificationTypes);
    }

    private List<NotificationType> constructNeededNotificationTypes(NotificationStatusesDTO notificationStatusesDTO) {
        List<NotificationType> notificationTypes = new ArrayList<>();
        if (notificationStatusesDTO.isAccommodationRatedNotificationsActive()) {
            notificationTypes.add(NotificationType.ACCOMMODATION_RATED);
        }
        if (notificationStatusesDTO.isHostRatedNotificationsActive()) {
            notificationTypes.add(NotificationType.HOST_RATED);
        }
        if (notificationStatusesDTO.isReservationCancelledNotificationsActive()) {
            notificationTypes.add(NotificationType.RESERVATION_CANCELLED);
        }
        if (notificationStatusesDTO.isReservationRequestedNotificationsActive()) {
            notificationTypes.add(NotificationType.RESERVATION_REQUESTED);
        }
        if (notificationStatusesDTO.isReservationRequestAnsweredActive()) {
            notificationTypes.add(NotificationType.RESERVATION_REQUEST_ANSWERED);
        }
        return notificationTypes;

    }
}
