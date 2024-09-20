package com.example.merchstore.repositories;

import com.example.merchstore.components.models.Attribute;
import com.example.merchstore.components.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 18.09.2024
 */
@Repository
public interface AttributesRepository extends JpaRepository<Attribute, Long> {

    List<Attribute> findAllByItem(Item item);
}
