package com.example.merchstore.repositories;

import com.example.merchstore.components.models.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * The DiscountRepository interface is a Spring Data JPA repository for the Discount entity.
 * It provides CRUD operations and methods to find Discount entities by code, id, or by the starting characters of the code, ignoring case.
 * It also provides a method to find all valid discounts for a given date.
 *
 * It has four methods:
 * <ul>
 *     <li>findByCode(String code): This method retrieves a Discount entity with the provided code.</li>
 *     <li>findByDiscountId(Long id): This method retrieves a Discount entity with the provided id.</li>
 *     <li>findByCodeStartingWithIgnoreCase(String code): This method retrieves a list of Discount entities where the code starts with the provided string, ignoring case.</li>
 *     <li>findAllValidDiscounts(LocalDate today): This method retrieves a list of Discount entities that are valid on the provided date.</li>
 * </ul>
 *
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
