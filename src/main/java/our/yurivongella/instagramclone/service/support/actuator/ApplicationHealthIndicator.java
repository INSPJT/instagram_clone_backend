package our.yurivongella.instagramclone.service.support.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public final class ApplicationHealthIndicator implements HealthIndicator {

    private boolean ready;

    @Override
    public Health health() {
        return ready ? Health.up().build() : Health.down().build();
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
}