package org.example.oops.repository;

import org.example.oops.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

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

       @Query("""
                     SELECT i FROM Item i
                     JOIN i.section s
                     JOIN s.menu m
                     WHERE m.id = :menuId AND i.id = :itemId
                     """)
       Item findByMenuIdAndItemId(Integer menuId, Integer itemId);

       @Modifying
       @Transactional
       @Query("""
                     DELETE FROM Item i
                     WHERE i.id = :itemId
                     AND i.section.menu.id = :menuId
                     """)
       int deleteByMenuIdAndItemId(Integer menuId, Integer itemId);

}
