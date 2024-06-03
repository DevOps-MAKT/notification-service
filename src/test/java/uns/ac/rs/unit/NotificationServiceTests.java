package uns.ac.rs.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uns.ac.rs.dto.request.NotificationDTO;
import uns.ac.rs.model.Notification;
import uns.ac.rs.model.NotificationType;
import uns.ac.rs.repository.NotificationRepository;
import uns.ac.rs.service.NotificationService;

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
        notificationDTO.setReceiverId(1L);
        notificationDTO.setNotificationType(NotificationType.RESERVATION_REQUESTED);
        notificationDTO.setSenderEmail("user@gmail.com");
        notificationDTO.setAccommodationId(1L);

        Notification notification = notificationService.createNotification(notificationDTO);

        verify(notificationRepository, times(1)).persist(notification);

        assertEquals(notification.getNotificationType(), notificationDTO.getNotificationType());
        assertEquals(notification.getReceiverId(), notificationDTO.getReceiverId());
        assertEquals(notification.getSenderEmail(), notificationDTO.getSenderEmail());
        assertEquals(notification.getAccommodationId(), notificationDTO.getAccommodationId());
    }
}
