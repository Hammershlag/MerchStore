package com.example.merchstore.repositories;

import com.example.merchstore.components.models.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 03.09.2024
 */

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    Currency findByShortName(String shortName);

}
