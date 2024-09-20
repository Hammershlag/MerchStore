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
@Table(name = "attributes")
public class Attribute implements DataDisplay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attribute_id")
    private Long attributeId;

    @Column(name = "value")
    private String value;

    @ManyToOne
    @JoinColumn(name = "attribute_type_id", nullable = false)
    private AttributeType attributeType;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    public Attribute(Attribute other) {
        this.attributeId = other.attributeId;
        this.value = other.value;
        this.attributeType = other.attributeType;
        this.item = other.item;
    }

    @Override
    public DataDisplay displayData() {
        return new Attribute(this);
    }

    @Override
    public DataDisplay limitedDisplayData() {
        return null;
    }
}
