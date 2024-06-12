package uns.ac.rs.controller.health;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Readiness
@ApplicationScoped
public class ReadinessCheck implements HealthCheck {
    @Inject
    MongoClient mongoClient;

    @Override
    public HealthCheckResponse call() {
        try {
            MongoDatabase database = mongoClient.getDatabase("devops");
            database.runCommand(new org.bson.Document("ping", 1));
            return HealthCheckResponse.up("MongoDB connection health check");
        } catch (Exception e) {
            return HealthCheckResponse.down("MongoDB connection health check");
        }
    }
}