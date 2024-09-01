package com.example.merchstore.repositories;

import com.example.merchstore.components.models.BestSeller;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 01.09.2024
 */
public interface BestSellerRepository extends JpaRepository<BestSeller, Long> {

}
