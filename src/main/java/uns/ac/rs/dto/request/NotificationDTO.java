package uns.ac.rs.dto.request;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

@Data
@RegisterForReflection
public class NotificationDTO {
    public String from;

    public String to;

    public String msg;

}
