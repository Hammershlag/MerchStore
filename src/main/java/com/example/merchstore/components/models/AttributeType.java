package com.example.merchstore.components.models;

import com.example.merchstore.components.interfaces.DataDisplay;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 18.09.2024
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "attribute_types")
public class AttributeType implements DataDisplay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attribute_type_id")
    private Long attributeTypeId;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    public AttributeType(AttributeType other) {
        this.attributeTypeId = other.attributeTypeId;
        this.name = other.name;
    }

    @Override
    public DataDisplay displayData() {
        return new AttributeType(this);
    }

    @Override
    public DataDisplay limitedDisplayData() {
        return null;
    }
}
