package our.yurivongella.instagramclone.controller.actuator;

import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import our.yurivongella.instagramclone.service.support.actuator.ApplicationHealthIndicator;

@RequestMapping("/health")
@RestController
@RequiredArgsConstructor
public final class HealthIndicatorRestController {
    private final ApplicationHealthIndicator applicationHealthIndicator;

    @GetMapping("/check")
    public void getState(HttpServletResponse response) {
        Health health = applicationHealthIndicator.health();
        response.setStatus(health.getStatus() == Status.UP ? HttpServletResponse.SC_OK
                                                           : HttpServletResponse.SC_SERVICE_UNAVAILABLE);
    }

    @PutMapping("/true")
    public void setReady() {
        applicationHealthIndicator.setReady(true);
    }

    @PutMapping("/false")
    public void setTerminated() {
        applicationHealthIndicator.setReady(false);
    }
}
