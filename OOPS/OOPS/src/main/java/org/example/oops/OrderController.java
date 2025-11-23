package org.example.oops;

import org.example.oops.repository.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService service;
    private final OrderRepository orders;
    private final SettingsService settings;

    public OrderController(OrderService service, OrderRepository orders, SettingsService settings) {
        this.service = service;
        this.orders = orders;
        this.settings = settings;
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
        Order o =service.get(id);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isStaff = auth != null && auth.isAuthenticated() &&
                auth.getAuthorities().stream().anyMatch(a ->
                        a.getAuthority().equals("ROLE_STAFF") || a.getAuthority().equals("ROLE_SUPERUSER"));
        if(isStaff){
            return orderPayload(o);
        } else {
            return publicOrderPayload(o);
        }

    }

    @PreAuthorize("hasAnyRole('SUPERUSER','STAFF')")
    @GetMapping("/by-phone")
    public List<Map<String, Object>> getByPhone(@RequestParam String phone) {
        return orders.findByCustomerPhoneOrderByIdDesc(phone)
                .stream().map(this::orderPayload).toList();
    }

    @PreAuthorize("hasAnyRole('SUPERUSER','STAFF')")
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

    private Map<String, Object> publicOrderPayload(Order o) {
        int queueMinutes = settings.getQueueMinutes();
        Instant now = Instant.now();
        long elapsedMinutes = Duration.between(o.getCreatedAt(),now).toMinutes();
        long remaining = queueMinutes - elapsedMinutes;
        boolean overdue = remaining < 0;
        long remainingClamped = Math.max(0,remaining);

        String message;
        if (overdue && elapsedMinutes < queueMinutes + 10) {
            message = "Pöntunin þín ætti að vera að klárast núna.";
        } else if (overdue) {
            message = "Við erum aðeina á eftir áætlun - vinsamlegast hafðu við starfsfólk.";
        }else if (remainingClamped <= 5) {
            message = "pöntunin þín er næstum ready.";
        }else {
            message = "Áætlaður biðtími er um " + remainingClamped + " mínútur.";
        }
        Instant estimatedReady = o.getEstimatedReadyAt();
        if(estimatedReady == null){
            estimatedReady = o.getCreatedAt().plus(Duration.ofMinutes(queueMinutes));
        }
        return Map.of(
                "orderId", o.getId(),
                "status", o.getStatus(),
                "createdAt", o.getCreatedAt(),
                "estimatedReadyAt", estimatedReady,
                "queueMinutesAtOrderTime", queueMinutes,
                "elapsedMinutes", elapsedMinutes,
                "remainingMinutes", remainingClamped,
                "overdue", overdue,
                "message", message
        );
    }
}
