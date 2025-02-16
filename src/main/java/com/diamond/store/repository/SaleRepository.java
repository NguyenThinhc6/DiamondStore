package com.diamond.store.repository;

import com.diamond.store.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface SaleRepository extends JpaRepository<Sale, String> {
    Optional<Sale> findByTimeStartLessThanEqualAndTimeEndGreaterThanEqual(Date timeStartIsLessThan, Date timeEndIsGreaterThan);
}
