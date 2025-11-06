package org.example.oops.repository;

import org.example.oops.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Integer> {
    List<Order> findByCustomerPhoneOrderByIdDesc(String customerPhone);

}
