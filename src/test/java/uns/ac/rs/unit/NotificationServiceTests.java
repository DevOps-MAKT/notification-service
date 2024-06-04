package uns.ac.rs.unit;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uns.ac.rs.dto.NotificationStatusesDTO;
import uns.ac.rs.dto.request.NotificationDTO;
import uns.ac.rs.model.Notification;
import uns.ac.rs.model.NotificationType;
import uns.ac.rs.repository.NotificationRepository;
import uns.ac.rs.service.NotificationService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTests {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    public void testCreateNotification() {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setReceiverEmail("receiver@gmail.com");
        notificationDTO.setNotificationType(NotificationType.RESERVATION_REQUESTED);
        notificationDTO.setSenderEmail("user@gmail.com");
        notificationDTO.setAccommodationId(1L);

        Notification notification = notificationService.createNotification(notificationDTO);

        verify(notificationRepository, times(1)).persist(notification);

        assertEquals(notification.getNotificationType(), notificationDTO.getNotificationType());
        assertEquals(notification.getReceiverEmail(), notificationDTO.getReceiverEmail());
        assertEquals(notification.getSenderEmail(), notificationDTO.getSenderEmail());
        assertEquals(notification.getAccommodationId(), notificationDTO.getAccommodationId());
    }

    @Test
    public void testUpdateNotification() {
        Notification notification = new Notification();
        notification.setRead(false);
        ObjectId objectId = new ObjectId();

        when(notificationRepository.findById(objectId)).thenReturn(notification);

        Notification updatedNotification = notificationService.updateReadStatus(objectId.toHexString());

        verify(notificationRepository, times(1)).update(updatedNotification);

        assertTrue(updatedNotification.isRead());
    }

    @Test
    public void testRetrieveNotifications() {
        String email = "example@example.com";
        NotificationStatusesDTO notificationStatusesDTO = new NotificationStatusesDTO(true, false, false, true, false);

        List<Notification> expectedNotifications = Arrays.asList(
                new Notification(),
                new Notification()
        );
        doReturn(expectedNotifications)
                .when(notificationRepository)
                .findByReceiverEmailAndTypes(email,
                        Arrays.asList(
                                NotificationType.ACCOMMODATION_RATED,
                                NotificationType.RESERVATION_REQUESTED
                        )
                );

        List<Notification> actualNotifications = notificationService.retrieveNotifications(email, notificationStatusesDTO);

        assertEquals(expectedNotifications.size(), actualNotifications.size());
        assertEquals(expectedNotifications, actualNotifications);
    }
}
