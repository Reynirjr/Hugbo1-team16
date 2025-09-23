// SettingsController.java
package org.example.oops;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/settings")
public class SettingsController {
    private final SettingsService settings;

    public SettingsController(SettingsService settings) { this.settings = settings; }

    @GetMapping("/queue-time")
    public Map<String, Object> getQueueTime() {
        int minutes = settings.getQueueMinutes();
        return Map.of("minutes", minutes);
    }

    @PutMapping("/queue-time")
    public ResponseEntity<Map<String, Object>> setQueueTime(
            @RequestParam int minutes,
            Principal principal) {
        String actor = principal != null ? principal.getName() : "system";
        StoreSetting s = settings.setQueueMinutes(minutes, actor);
        return ResponseEntity.ok(Map.of(
                "minutes", s.getQueueMinutes(),
                "updatedAt", s.getUpdatedAt(),
                "updatedBy", s.getUpdatedBy()
        ));
    }
}
