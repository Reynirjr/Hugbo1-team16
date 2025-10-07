package org.example.oops;

import org.example.oops.repository.MenuRepository;
import org.example.oops.repository.ItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.access.prepost.PreAuthorize;


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

    @PreAuthorize("hasRole('SUPERUSER')")
    @PostMapping("/{menuId}/items")
    public Item createItem(@PathVariable Integer menuId, @RequestBody CreateItemRequest request) {
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

    @PreAuthorize("hasRole('SUPERUSER')")
    @PutMapping("/{menuId}/items/{itemId}")
    public Item updateItem(
            @PathVariable Integer menuId,
            @PathVariable Integer itemId,
            @RequestBody CreateItemRequest request) {
        Item existing = items.findByMenuIdAndItemId(menuId, itemId);
        if (existing == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found in this menu");
        }
        existing.setName(request.getName());
        existing.setDescription(request.getDescription());
        existing.setPriceIsk(request.getPriceIsk());
        existing.setAvailable(request.isAvailable());
        existing.setTags(request.getTags());

        if (request.getSectionId() != null && !request.getSectionId().equals(existing.getSection().getId())) {
            Menu menu = repo.findById(menuId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu not found"));

            MenuSection newSection = menu.getSections().stream()
                    .filter(s -> s.getId().equals(request.getSectionId()))
                    .findFirst()
                    .orElseThrow(
                            () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid section for this menu"));

            existing.setSection(newSection);
        }

        return items.save(existing);
    }

    @PreAuthorize("hasRole('SUPERUSER')")
    @DeleteMapping("/{menuId}/items/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@PathVariable Integer menuId,
            @PathVariable Integer itemId) {
        int deleted = items.deleteByMenuIdAndItemId(menuId, itemId);
        if (deleted == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found in this menu");
        }
    }

    @PreAuthorize("hasAnyRole('SUPERUSER','STAFF')")
    @PutMapping("/{menuId}/items/{itemId}/availability")
    public Item updateAvailability(
            @PathVariable Integer menuId,
            @PathVariable Integer itemId,
            @RequestParam boolean available) {
        Item item = items.findByMenuIdAndItemId(menuId, itemId);
        if (item == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found in this menu");
        }
        item.setAvailable(available);
        return items.save(item);
    }
}
