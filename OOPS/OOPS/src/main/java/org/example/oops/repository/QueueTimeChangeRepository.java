// repository/QueueTimeChangeRepository.java
package org.example.oops.repository;

import org.example.oops.QueueTimeChange;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QueueTimeChangeRepository extends JpaRepository<QueueTimeChange, Long> {}
