package org.example.oops.repository;

import org.example.oops.OpeningHour;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpeningHourRepository extends JpaRepository<OpeningHour, Integer> {
}
