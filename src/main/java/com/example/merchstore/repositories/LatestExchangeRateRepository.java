package com.example.merchstore.repositories;

import com.example.merchstore.components.models.LatestExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The LatestExchangeRateRepository interface is a Spring Data JPA repository for the LatestExchangeRate entity.
 * It provides CRUD operations and a method to find the most recent LatestExchangeRate entity by currency id.
 *
 * It has one method:
 * <ul>
 *     <li>findFirstByCurrencyIdOrderByLastUpdatedDesc(Long currencyId): This method retrieves the most recent LatestExchangeRate entity associated with the provided currency id, ordered by the lastUpdated field in descending order.</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 04.09.2024
 */

@Repository
public interface LatestExchangeRateRepository extends JpaRepository<LatestExchangeRate, Long> {

    /**
     * This method retrieves the most recent LatestExchangeRate entity associated with the provided currency id, ordered by the lastUpdated field in descending order.
     *
     * @param currencyId The currency id to retrieve the most recent LatestExchangeRate entity for.
     * @return The most recent LatestExchangeRate entity associated with the provided currency id.
     */
    LatestExchangeRate findFirstByCurrencyIdOrderByLastUpdatedDesc(Long currencyId);

}
