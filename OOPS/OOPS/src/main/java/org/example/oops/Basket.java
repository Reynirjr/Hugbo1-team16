package org.example.oops;


import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "baskets")
public class Basket {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(name = "created_at", nullable=false)
    private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "basket", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<BasketItem> items = new ArrayList<>();

    public Basket() {this.id = UUID.randomUUID(); }
    public Basket(UUID id) { this.id = id == null ? UUID.randomUUID() : id; }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public List<BasketItem> getItems() { return items; }

    public void setItems(List<BasketItem> items) {
        this.items = items;
    }


}
