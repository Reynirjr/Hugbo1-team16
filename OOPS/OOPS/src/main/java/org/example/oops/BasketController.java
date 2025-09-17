package org.example.oops;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/baskets")
public class BasketController {

    private final BasketService service;
    public BasketController(BasketService service) { this.service = service; }

    @PostMapping
    public Map<String, Object> createBasket(@RequestParam(value = "basketId", required = false) String id) {
        UUID uuid = id == null || id.isBlank() ? UUID.randomUUID() : UUID.fromString(id);
        Basket b = service.getOrCreate(uuid);
        return Map.of("basketId", b.getId().toString(), "createdAt", b.getCreatedAt(), "items", b.getItems());
    }

    @GetMapping("/{basketId}")
    public Basket get(@PathVariable String basketId) {
        return service.getOrCreate(UUID.fromString(basketId));
    }

    @PostMapping("/{basketId}/items/{itemId}")
    public Basket addItem(@PathVariable String basketId,
                          @PathVariable Integer itemId,
                          @RequestParam(name = "qty", defaultValue = "1") int qty) {
        return service.addItem(UUID.fromString(basketId), itemId, qty);
    }

    @PatchMapping("/{basketId}/items/{basketItemId}")
    public Basket setQty(@PathVariable String basketId,
                         @PathVariable Integer basketItemId,
                         @RequestParam(name = "qty") int qty) {
        return service.setQuantity(UUID.fromString(basketId), basketItemId, qty);
    }

    @DeleteMapping("/{basketId}")
    public ResponseEntity<Void> clear(@PathVariable String basketId) {
        service.clear(UUID.fromString(basketId));
        return ResponseEntity.noContent().build();
    }
}
