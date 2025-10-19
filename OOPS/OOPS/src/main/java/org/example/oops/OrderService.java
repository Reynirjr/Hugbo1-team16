package org.example.oops;

import jakarta.transaction.Transactional;
import org.example.oops.repository.BasketRepository;
import org.example.oops.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class OrderService {
    private final BasketRepository baskets;
    private final OrderRepository orders;
    private final SettingsService settings;

    public OrderService(BasketRepository baskets, OrderRepository orders, SettingsService settings) {
        this.baskets = baskets; this.orders = orders; this.settings = settings;
    }

    public Instant estimatePickupTime(Instant createdAt) {
        int minutes = settings.getQueueMinutes();
        return createdAt.plusSeconds(minutes * 60L);
    }

    @Transactional
    public Order createFromBasket(UUID basketId, String phone) {
        Basket basket = baskets.findById(basketId).orElseThrow(() -> new RuntimeException("Basket not found"));
        if (basket.getItems().isEmpty()) throw new RuntimeException("Basket is empty");

        Order o = new Order();
        o.setBasketId(basketId);
        if (phone != null && !phone.isBlank()) o.setCustomerPhone(phone);

        int total = 0;
        for (BasketItem bi : basket.getItems()) {
            Item it = bi.getItem();
            total += it.getPriceIsk() * bi.getQuantity();
            o.getItems().add(new OrderItem(o, it.getId(), it.getName(), it.getPriceIsk(), bi.getQuantity()));
        }
        o.setTotalIsk(total);

        o.setEstimatedReadyAt(estimatePickupTime(o.getCreatedAt()));

        Order saved = orders.save(o);
        baskets.delete(basket);
        return saved;
    }

    public Order get(Integer id) {
        return orders.findById(id).orElseThrow();
    }

    @Transactional
    public Order changeStatus(Integer id, String newStatus) {
        Order o = orders.findById(id).orElseThrow();
        String cur = o.getStatus();
        if (cur.equals("RECEIVED") && (newStatus.equals("PREPARING") || newStatus.equals("READY"))) {
            o.setStatus(newStatus);
        } else if (cur.equals("PREPARING") && (newStatus.equals("READY"))) {
            o.setStatus(newStatus);
        } else if (cur.equals("READY") && newStatus.equals("PICKED_UP")) {
            o.setStatus(newStatus);
        } else if (cur.equals(newStatus)) {
        } else {
            throw new IllegalArgumentException("Illegal status transition: " + cur + " -> " + newStatus);
        }
        return orders.save(o);
    }
}
