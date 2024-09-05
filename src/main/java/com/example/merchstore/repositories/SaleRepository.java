package com.example.merchstore.repositories;

import com.example.merchstore.components.models.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The SaleRepository interface is a Spring Data JPA repository for the Sale entity.
 * It provides CRUD operations.
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 01.09.2024
 */

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
}
