// StoreSetting.java
package org.example.oops;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "store_settings")
public class StoreSetting {
    @Id
    private Short id = 1;

    @Column(name = "queue_minutes", nullable = false)
    private int queueMinutes = 20;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    @Column(name = "updated_by")
    private String updatedBy;

    public Short getId() { return id; }
    public void setId(Short id) { this.id = id; }

    public int getQueueMinutes() { return queueMinutes; }
    public void setQueueMinutes(int queueMinutes) { this.queueMinutes = queueMinutes; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
}
