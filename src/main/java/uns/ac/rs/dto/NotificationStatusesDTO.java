package uns.ac.rs.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedHashMap;

@Data
@RegisterForReflection
public class NotificationStatusesDTO {
    private boolean reservationRequestedNotificationsActive;
    private boolean reservationCancelledNotificationsActive;
    private boolean hostRatedNotificationsActive;
    private boolean accommodationRatedNotificationsActive;
    private boolean reservationRequestAnsweredActive;

    public NotificationStatusesDTO() {

    }

    public NotificationStatusesDTO(boolean reservationRequestedNotificationsActive,
                                   boolean reservationCancelledNotificationsActive,
                                   boolean hostRatedNotificationsActive,
                                   boolean accommodationRatedNotificationsActive,
                                   boolean reservationRequestAnsweredActive) {
        this.reservationRequestedNotificationsActive = reservationRequestedNotificationsActive;
        this.reservationCancelledNotificationsActive = reservationCancelledNotificationsActive;
        this.hostRatedNotificationsActive = hostRatedNotificationsActive;
        this.accommodationRatedNotificationsActive = accommodationRatedNotificationsActive;
        this.reservationRequestAnsweredActive = reservationRequestAnsweredActive;
    }
    public NotificationStatusesDTO(LinkedHashMap data) {
        this.reservationRequestedNotificationsActive = (boolean) data.get("reservationRequestedNotificationsActive");
        this.reservationCancelledNotificationsActive = (boolean) data.get("reservationCancelledNotificationsActive");
        this.hostRatedNotificationsActive = (boolean) data.get("hostRatedNotificationsActive");
        this.accommodationRatedNotificationsActive = (boolean) data.get("accommodationRatedNotificationsActive");
        this.reservationRequestAnsweredActive = (boolean) data.get("reservationRequestAnsweredActive");
    }
}
