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
        this.baskets = baskets;
        this.orders = orders;
        this.settings = settings;
    }

    public Instant estimatePickupTime(Instant reference) {
        int minutes = settings.getQueueMinutes();
        Instant base = (reference != null) ? reference : Instant.now();
        return base.plusSeconds(minutes * 60L);
    }

    public Instant estimatePickupTimeNow() {
        return estimatePickupTime(Instant.now());
    }

    @Transactional
    public Order createFromBasket(UUID basketId, String phone) {
        Basket basket = baskets.findById(basketId)
                .orElseThrow(() -> new RuntimeException("Basket not found"));
        if (basket.getItems().isEmpty()) {
            throw new RuntimeException("Basket is empty");
        }

        Order order = new Order();
        order.setBasketId(basketId);
        if (phone != null && !phone.isBlank()) order.setCustomerPhone(phone);

        int total = 0;
        for (BasketItem bi : basket.getItems()) {
            Item it = bi.getItem();
            int line = it.getPriceIsk() * bi.getQuantity();
            total += line;
            order.getItems().add(new OrderItem(
                    order, it.getId(), it.getName(), it.getPriceIsk(), bi.getQuantity()
            ));
        }
        order.setTotalIsk(total);
        Order saved = orders.save(order);

        baskets.delete(basket);
        return saved;
    }

    public Order get(Integer id){
        return orders.findById(id).orElseThrow();
    }
}
