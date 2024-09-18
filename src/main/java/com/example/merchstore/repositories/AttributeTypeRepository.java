package com.example.merchstore.repositories;

import com.example.merchstore.components.models.AttributeType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 18.09.2024
 */
public interface AttributeTypeRepository extends JpaRepository<AttributeType, Long> {
    AttributeType findByName(String name);
}
