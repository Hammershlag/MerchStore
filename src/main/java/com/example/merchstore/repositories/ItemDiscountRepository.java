package com.example.merchstore.repositories;

import com.example.merchstore.components.models.Discount;
import com.example.merchstore.components.models.ItemDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The ItemDiscountRepository interface is a Spring Data JPA repository for the ItemDiscount entity.
 * It provides CRUD operations and methods to find ItemDiscount entities by associated Discount.
 *
 * It has three methods:
 * <ul>
 *     <li>findAll(): This method retrieves all ItemDiscount entities.</li>
 *     <li>findItemDiscountByDiscount(Discount discount): This method retrieves an ItemDiscount entity associated with the provided Discount entity.</li>
 *     <li>findAllByDiscount(Discount discount): This method retrieves a list of ItemDiscount entities associated with the provided Discount entity.</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 09.06.2024
 */

@Repository
public interface ItemDiscountRepository extends JpaRepository<ItemDiscount, Long> {
    List<ItemDiscount> findAll();
    ItemDiscount findItemDiscountByDiscount(Discount discount);
    List<ItemDiscount> findAllByDiscount(Discount discount);
}
