package com.example.merchstore.repositories;

import com.example.merchstore.components.models.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 01.09.2024
 */
public interface SaleRepository extends JpaRepository<Sale, Long> {
}
