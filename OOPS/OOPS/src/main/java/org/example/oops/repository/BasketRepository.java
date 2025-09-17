package org.example.oops.repository;


import org.example.oops.Basket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BasketRepository extends JpaRepository<Basket, UUID> {
}
