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

    @PostMapping("/{menuId}/items")
    public Item createItem(@PathVariable Integer menuId, @RequestBody CreateItemRequest request) {
        // make sure section exists and belongs to the menu
        MenuSection section = repo.findById(menuId)
                .flatMap(menu -> menu.getSections().stream()
                        .filter(s -> s.getId().equals(request.getSectionId()))
                        .findFirst())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid section for this menu"));

        Item item = new Item();
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setPriceIsk(request.getPriceIsk());
        item.setAvailable(request.isAvailable());
        item.setTags(request.getTags());
        item.setSection(section);

        return items.save(item);
    }
}