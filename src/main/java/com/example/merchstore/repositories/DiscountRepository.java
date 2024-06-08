package com.example.merchstore.repositories;

import com.example.merchstore.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 02.06.2024
 */
public interface DiscountRepository extends JpaRepository<Discount, Long> {

    public Discount findByCode(String code);
    List<Discount> findByCodeStartingWithIgnoreCase(String code);

    @Query("SELECT d FROM Discount d WHERE d.validUntil >= :today AND d.validFrom <= :today")
    List<Discount> findAllValidDiscounts(@Param("today") LocalDate today);
}
