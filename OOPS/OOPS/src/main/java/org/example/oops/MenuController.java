package org.example.oops;

import org.example.oops.repository.MenuRepository;
import org.example.oops.repository.ItemRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
public class MenuController {
    private final MenuRepository repo;
    private final ItemRepository items; // ✅ add this field

    public MenuController(MenuRepository repo, ItemRepository items) {
        this.repo = repo;
        this.items = items; // ✅ constructor injection
    }

    @GetMapping
    public List<Menu> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Menu getById(@PathVariable Integer id) {
        return repo.findById(id).orElse(null);
    }

    @GetMapping("/{id}/items")
    public List<Item> getItemsForMenu(@PathVariable Integer id) {
        return items.findByMenuIdOrdered(id);
    }
}
