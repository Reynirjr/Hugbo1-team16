package org.example.oops;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "order_items")

public class OrderItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id",nullable = false)
    @JsonIgnore
    private Order order;

    @Column(name = "item_id", nullable = false)
    private Integer itemId;

    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Column(name = "price_isk", nullable = false)
    private Integer priceIsk;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    public OrderItem() {}
    public OrderItem(Order order, Integer itemId, String itemName, Integer priceIsk, Integer quantity) {
        this.order = order;
        this.itemId = itemId;
        this.itemName = itemName;
        this.priceIsk = priceIsk;
        this.quantity = quantity;
    }

    public Integer getId() {
        return id;
    }
    public Order getOrder() {
        return order;
    }
    public void setOrder(Order order) {
        this.order = order;
    }
    public Integer getItemId() {
        return itemId;
    }
    public String getItemName() {
        return itemName;
    }
    public Integer getPriceIsk() {
        return priceIsk;
    }
    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }


}
