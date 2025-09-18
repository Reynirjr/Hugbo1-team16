package org.example.oops;

import org.example.oops.repository.MenuRepository;
import org.example.oops.repository.ItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
public class MenuController {
    private final MenuRepository repo;
    private final ItemRepository items;

    public MenuController(MenuRepository repo, ItemRepository items) {
        this.repo = repo;
        this.items = items;
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

    @GetMapping("/{menuId}/items/{itemId}")
    public Item getItemFromMenu(
            @PathVariable Integer menuId,
            @PathVariable Integer itemId) {
        Item item = items.findByMenuIdAndItemId(menuId, itemId);
        if (item == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found in this menu");
        }
        return item;
    }

}