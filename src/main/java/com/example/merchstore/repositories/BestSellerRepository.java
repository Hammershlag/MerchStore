package com.example.merchstore.repositories;

import com.example.merchstore.components.models.Bestseller;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The BestSellerRepository interface is a Spring Data JPA repository for the Bestseller entity.
 * It provides CRUD operations.
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 01.09.2024
 */
public interface BestSellerRepository extends JpaRepository<Bestseller, Long> {

}
