// QueueTimeChange.java
package org.example.oops;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "queue_time_changes")
public class QueueTimeChange {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "old_minutes", nullable = false)
    private int oldMinutes;

    @Column(name = "new_minutes", nullable = false)
    private int newMinutes;

    @Column(name = "changed_by")
    private String changedBy;

    @Column(name = "changed_at", nullable = false)
    private Instant changedAt = Instant.now();

    public QueueTimeChange() {}
    public QueueTimeChange(int oldM, int newM, String by) {
        this.oldMinutes = oldM; this.newMinutes = newM; this.changedBy = by;
    }

    // getters...
}
