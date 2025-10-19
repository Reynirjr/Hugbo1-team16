package org.example.oops;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.Map;

@RestController
@RequestMapping("/api/hours")
public class HoursController {
    private final HoursService service;

    public HoursController(HoursService service) {
        this.service = service;
    }

    @GetMapping
    public Map<String, Object> getAll() {
        return Map.of(
            "regular", service.getAllHours(),
            "exceptions", service.getAllExceptions()
        );
    }

    @PreAuthorize("hasRole('SUPERUSER')")
    @PutMapping("/{id}")
    public OpeningHour update(@PathVariable Integer id, @RequestBody OpeningHour newData) {
        OpeningHour hour = service.getAllHours().stream()
                .filter(h -> h.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        hour.setOpenTime(newData.getOpenTime());
        hour.setCloseTime(newData.getCloseTime());
        return service.saveHour(hour);
    }

    @PreAuthorize("hasRole('SUPERUSER')")
    @PostMapping("/exception")
    public OpeningException addException(@RequestBody OpeningException ex) {
        return service.saveException(ex);
    }

    @PreAuthorize("hasRole('SUPERUSER')")
    @DeleteMapping("/exception/{id}")
    public void deleteException(@PathVariable Integer id) {
        service.deleteException(id);
    }
}
