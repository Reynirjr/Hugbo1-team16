package org.example.oops;

import org.example.oops.repository.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService service;
    private final OrderRepository orders;

    public OrderController(OrderService service, OrderRepository orders) {
        this.service = service; this.orders = orders;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> create(@RequestParam UUID basketId,
                                      @RequestParam(required = false) String phone) {
        Order o = service.createFromBasket(basketId, phone);
        return orderPayload(o);
    }

    @GetMapping("/{id}")
    public Map<String, Object> getOne(@PathVariable Integer id) {
        return orderPayload(service.get(id));
    }

    @GetMapping("/by-phone")
    public List<Map<String, Object>> getByPhone(@RequestParam String phone) {
        return orders.findByCustomerPhoneOrderByIdDesc(phone)
                .stream().map(this::orderPayload).toList();
    }

    @PutMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> updateStatus(@PathVariable Integer id,
                                            @RequestParam String value) {
        Order o = service.changeStatus(id, value);
        return orderPayload(o);
    }

    private Map<String, Object> orderPayload(Order o) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("orderId", o.getId());
        m.put("createdAt", o.getCreatedAt());
        m.put("status", o.getStatus());
        m.put("totalIsk", o.getTotalIsk());
        m.put("estimatedPickupAt", o.getEstimatedReadyAt());
        m.put("customerPhone", o.getCustomerPhone());
        m.put("basketId", o.getBasketId());
        m.put("items", o.getItems().stream().map(oi -> Map.of(
                "id", oi.getId(),
                "itemId", oi.getItemId(),
                "itemName", oi.getItemName(),
                "priceIsk", oi.getPriceIsk(),
                "quantity", oi.getQuantity()
        )).toList());
        return m;
    }
}
