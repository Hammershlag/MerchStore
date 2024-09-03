package com.example.merchstore.repositories;

import com.example.merchstore.components.models.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 02.06.2024
 */

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {

    Discount findByCode(String code);
    Discount findByDiscountId(Long id);
    List<Discount> findByCodeStartingWithIgnoreCase(String code);

    @Query("SELECT d FROM Discount d WHERE d.validUntil >= :today AND d.validFrom <= :today")
    List<Discount> findAllValidDiscounts(@Param("today") LocalDate today);
}
