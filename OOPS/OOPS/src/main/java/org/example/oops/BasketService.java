package org.example.oops;

import jakarta.transaction.Transactional;
import org.example.oops.repository.BasketItemRepository;
import org.example.oops.repository.BasketRepository;
import org.example.oops.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BasketService {
    private final BasketRepository baskets;
    private final BasketItemRepository basketItems;
    private final ItemRepository items;

    public BasketService(BasketRepository baskets, BasketItemRepository basketItems, ItemRepository items) {
        this.baskets = baskets;
        this.basketItems = basketItems;
        this.items = items;
    }

    public Basket getOrCreate(UUID basketId) {
        return baskets.findById(basketId).orElseGet(() -> baskets.save(new Basket(basketId)));
    }

    @Transactional
    public Basket addItem(UUID basketId, Integer itemId, int qty) {
        if (qty <= 0) qty = 1;
        Basket basket = getOrCreate(basketId);
        Item item = items.findById(itemId).orElseThrow(() -> new IllegalArgumentException("Item not found: " + itemId));
        for (BasketItem bi : basket.getItems()) {
            if (bi.getItem().getId().equals(itemId)) {
                bi.setQuantity(bi.getQuantity() + qty);
                return basket;
            }
        }
        basket.getItems().add(new BasketItem(basket, item, qty));
        return basket;
    }

    @Transactional
    public Basket setQuantity(UUID basketId, Integer basketItemId, int qty) {
        if ( qty < 0) qty = 0;
        Basket basket = baskets.findById(basketId).orElseThrow();
        BasketItem bi = basket.getItems().stream()
                .filter(x -> java.util.Objects.equals(x.getId(), basketItemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("BasketItem not found"));
        if (qty == 0) {
            basket.getItems().remove(bi);
            basketItems.delete(bi);
        } else {
            bi.setQuantity(qty);
        }
        return basket;
    }

    @Transactional
    public void clear(UUID basketId) {
        Basket basket = baskets.findById(basketId).orElse(null);
        if (basket != null) baskets.delete(basket);
    }

}
