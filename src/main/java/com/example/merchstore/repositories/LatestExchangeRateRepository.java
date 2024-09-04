package com.example.merchstore.repositories;

import com.example.merchstore.components.models.LatestExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 04.09.2024
 */

@Repository
public interface LatestExchangeRateRepository extends JpaRepository<LatestExchangeRate, Long> {

    LatestExchangeRate findFirstByCurrencyIdOrderByLastUpdatedDesc(Long currencyId);

}
