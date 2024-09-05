package com.example.merchstore.repositories;

import com.example.merchstore.components.models.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The ExchangeRateRepository interface is a Spring Data JPA repository for the ExchangeRate entity.
 * It provides CRUD operations.
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 04.09.2024
 */

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

}
