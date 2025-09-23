// SettingsService.java
package org.example.oops;

import jakarta.transaction.Transactional;
import org.example.oops.repository.QueueTimeChangeRepository;
import org.example.oops.repository.StoreSettingRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class SettingsService {
    private static final int MIN_Q = 0;    // allow 0 = "ASAP"
    private static final int MAX_Q = 180;  // 3 hours hard cap

    private final StoreSettingRepository settingsRepo;
    private final QueueTimeChangeRepository changesRepo;

    public SettingsService(StoreSettingRepository s, QueueTimeChangeRepository c) {
        this.settingsRepo = s; this.changesRepo = c;
    }

    public int getQueueMinutes() {
        StoreSetting s = settingsRepo.findById((short)1).orElseGet(() -> settingsRepo.save(new StoreSetting()));
        return s.getQueueMinutes();
    }

    @Transactional
    public StoreSetting setQueueMinutes(int minutes, String actor) {
        if (minutes < MIN_Q || minutes > MAX_Q) {
            throw new IllegalArgumentException("Queue time must be between " + MIN_Q + " and " + MAX_Q + " minutes.");
        }
        StoreSetting s = settingsRepo.findById((short)1).orElseGet(() -> settingsRepo.save(new StoreSetting()));
        int old = s.getQueueMinutes();
        if (old != minutes) {
            s.setQueueMinutes(minutes);
            s.setUpdatedAt(Instant.now());
            s.setUpdatedBy(actor);
            changesRepo.save(new QueueTimeChange(old, minutes, actor));
        }
        return s;
    }
}
