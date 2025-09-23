// repository/StoreSettingRepository.java
package org.example.oops.repository;

import org.example.oops.StoreSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreSettingRepository extends JpaRepository<StoreSetting, Short> {}
