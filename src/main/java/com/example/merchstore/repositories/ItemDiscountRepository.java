package com.example.merchstore.repositories;

import com.example.merchstore.components.models.Discount;
import com.example.merchstore.components.models.ItemDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 09.06.2024
 */

@Repository
public interface ItemDiscountRepository extends JpaRepository<ItemDiscount, Long> {
    List<ItemDiscount> findAll();
    ItemDiscount findItemDiscountByDiscount(Discount discount);
}
