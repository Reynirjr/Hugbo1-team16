package org.example.oops;

import org.example.oops.repository.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService service;
    private final OrderRepository orders;

    public OrderController(OrderService service, OrderRepository orders) {
        this.service = service;
        this.orders = orders;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> create(@RequestParam UUID basketId,
                                      @RequestParam(required = false) String phone) {
        Order o = service.createFromBasket(basketId, phone);
        return orderPayload(o, service.estimatePickupTime(o.getCreatedAt()));
    }

    @GetMapping("/{id}")
    public Map<String, Object> getOne(@PathVariable Integer id) {
        Order o = service.get(id);
        return orderPayload(o, service.estimatePickupTime(o.getCreatedAt()));
    }

    @GetMapping("/by-phone")
    public List<Map<String, Object>> getByPhone(@RequestParam String phone) {
        return orders.findByCustomerPhoneOrderByIdDesc(phone).stream()
                .map(o -> orderPayload(o, service.estimatePickupTime(o.getCreatedAt())))
                .collect(Collectors.toList());
    }

    private Map<String, Object> orderPayload(Order o, Instant eta) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("orderId", o.getId());
        m.put("createdAt", o.getCreatedAt());
        m.put("status", o.getStatus());
        m.put("totalIsk", o.getTotalIsk());
        m.put("estimatedPickupAt", eta);
        m.put("customerPhone", o.getCustomerPhone());
        m.put("basketId", o.getBasketId());
        m.put("items", o.getItems().stream().map(oi -> {
            Map<String, Object> im = new LinkedHashMap<>();
            im.put("id", oi.getId());
            im.put("itemId", oi.getItemId());
            im.put("itemName", oi.getItemName());
            im.put("priceIsk", oi.getPriceIsk());
            im.put("quantity", oi.getQuantity());
            return im;
        }).collect(Collectors.toList()));
        return m;
    }
}
