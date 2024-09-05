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

    /**
     * This method retrieves a Discount entity with the provided code.
     *
     * @param code The code of the Discount entity to retrieve.
     * @return A Discount entity with the provided code.
     */
    Discount findByCode(String code);

    /**
     * This method retrieves a Discount entity with the provided id.
     *
     * @param id The id of the Discount entity to retrieve.
     * @return A Discount entity with the provided id.
     */
    Discount findByDiscountId(Long id);

    /**
     * This method retrieves a list of Discount entities where the code starts with the provided string, ignoring case.
     *
     * @param code The string to search for at the beginning of the code.
     * @return A list of Discount entities where the code starts with the provided string, ignoring case.
     */
    List<Discount> findByCodeStartingWithIgnoreCase(String code);

    /**
     * This method retrieves a list of Discount entities that are valid on the provided date.
     *
     * @param today The date to check the validity of the discounts for.
     * @return A list of Discount entities that are valid on the provided date.
     */
    @Query("SELECT d FROM Discount d WHERE d.validUntil >= :today AND d.validFrom <= :today")
    List<Discount> findAllValidDiscounts(@Param("today") LocalDate today);
}
