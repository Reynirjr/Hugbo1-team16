package org.example.oops;


import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService service;
    public OrderController(OrderService service){
        this.service = service;
    }

    @PostMapping
    public Map<String, Object> create(@RequestParam String basketId,
                                      @RequestParam(required = false) String phone) {
        Order o = service.createFromBasket(UUID.fromString(basketId), phone);
        return Map.of(
                "orderId", o.getId(),
                "status", o.getStatus(),
                "totalIsk", o.getTotalIsk(),
                "createdAt", o.getCreatedAt(),
                "items", o.getItems()
        );
    }

    @GetMapping
    public Order get(@PathVariable Integer id){
        return service.get(id);
    }

    public static void main(String[] args) {

    }
}
