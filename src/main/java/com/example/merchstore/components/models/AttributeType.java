package com.example.merchstore.components.models;

import com.example.merchstore.components.enums.Language;
import com.example.merchstore.components.interfaces.DataDisplay;
import com.example.merchstore.components.superClasses.Translatable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;

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
public class AttributeType extends Translatable implements DataDisplay {

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

    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private Language language;

    @JsonIgnore
    @Transient
    final String className = "attributeType";

    @JsonIgnore
    @Transient
    final HashMap<String, Function<Translatable, String>> textFieldsGetter = new HashMap<>() {{
        put("name", (Translatable t) -> ((AttributeType) t).getName());
    }};

    @JsonIgnore
    @Transient
    final HashMap<String, BiConsumer<Translatable, String>> textFieldsSetter = new HashMap<>() {{
        put("name", (Translatable t, String value) -> ((AttributeType) t).setName(value));
    }};

    @Override
    public DataDisplay displayData() {
        return new AttributeType(this);
    }

    @Override
    public DataDisplay limitedDisplayData() {
        return null;
    }

    @JsonIgnore
    @Override
    public Long getTranslatableId() {
        return attributeTypeId;
    }

    @Override
    public void setTranslatableId(Long id) {
        this.attributeTypeId = id;
    }

    @JsonIgnore
    @Override
    public Language getTranslatableLanguage() {
        return language;
    }


    @Override
    public void setTranslatableLanguage(Language language) {
        this.language = language;
    }
}
