package org.example.oops;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
public class DebugController {
    private final JdbcTemplate jdbc;

    public DebugController(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @GetMapping("/debug/items")
    public List<Map<String, Object>> items() {
        return jdbc.queryForList("SELECT id, name, price_isk, available, tags FROM items ORDER BY id");
    }

    @GetMapping("/debug/sections")
    public List<Map<String, Object>> sections() {
        String sql = """
            SELECT s.id as section_id, s.name as section, s.display_order,
                   i.id as item_id, i.name as item, i.price_isk, i.available
            FROM menu_sections s
            JOIN items i ON i.section_id = s.id
            ORDER BY s.display_order, i.id
        """;
        return jdbc.queryForList(sql);
    }
}
