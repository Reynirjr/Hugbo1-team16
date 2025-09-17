package org.example.oops;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "basket_items")
public class BasketItem {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "basket_id")
    @JsonIgnore
    private Basket basket;

    @ManyToOne(optional = false)
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(nullable = false)
    private int quantity = 1;

    public BasketItem() {}

    public BasketItem(Basket basket, Item item, int quantity) {
        this.basket = basket;
        this.item = item;
        this.quantity = quantity;
    }

    public Integer getId() {
        return id;
    }

    public Basket getBasket() {
        return basket;
    }
    public void setBasket(Basket basket) {
        this.basket = basket;
    }
    public Item getItem() {
        return item;
    }
    public void setItem(Item item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
