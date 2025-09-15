package org.example.oops.repository;

import org.example.oops.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    @Query("""
           SELECT i FROM Item i
           JOIN i.section s
           JOIN s.menu m
           WHERE m.id = :menuId
           ORDER BY s.displayOrder, i.id
           """)
    List<Item> findByMenuIdOrdered(Integer menuId);

    @Query("""
           SELECT i FROM Item i
           JOIN i.section s
           ORDER BY s.displayOrder, i.id
           """)
    List<Item> findAllOrdered();
}
