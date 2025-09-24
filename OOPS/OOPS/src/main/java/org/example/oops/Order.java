package org.example.oops;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "basket_id")
    private java.util.UUID basketId;

    @Column(name = "customer_phone")
    private String customerPhone;

    @Column(name = "status", nullable = false)
    private String status = "RECEIVED";

    @Column(name = "total_isk", nullable = false)
    private Integer totalIsk = 0;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<OrderItem> items = new ArrayList<>();

    public Integer getId() { return id; }
    public Instant getCreatedAt() { return createdAt; }
    public java.util.UUID getBasketId() { return basketId; }
    public void setBasketId(java.util.UUID basketId) {this.basketId = basketId; }
    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getTotalIsk() { return totalIsk; }
    public void setTotalIsk(Integer totalIsk) { this.totalIsk = totalIsk; }
    public List<OrderItem> getItems() {return items;}

}
