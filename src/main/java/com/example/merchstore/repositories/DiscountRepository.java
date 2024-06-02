package com.example.merchstore.repositories;

import com.example.merchstore.dto.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 02.06.2024
 */
public interface DiscountRepository extends JpaRepository<Discount, Long> {

    public Discount findByCode(String code);

}
