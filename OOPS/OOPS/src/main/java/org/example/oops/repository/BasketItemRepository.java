package org.example.oops.repository;

import org.example.oops.BasketItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasketItemRepository extends JpaRepository<BasketItem, Integer> {
}
