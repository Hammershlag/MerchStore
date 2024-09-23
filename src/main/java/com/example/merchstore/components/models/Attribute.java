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
@Table(name = "attributes")
public class Attribute extends Translatable implements DataDisplay {

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

    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private Language language;

    @JsonIgnore
    @Transient
    final String className = "attribute";

    @JsonIgnore
    @Transient
    final HashMap<String, Function<Translatable, String>> textFieldsGetter = new HashMap<>() {{
        put("value", (Translatable t) -> ((Attribute) t).getValue());
    }};

    @JsonIgnore
    @Transient
    final HashMap<String, BiConsumer<Translatable, String>> textFieldsSetter = new HashMap<>() {{
        put("value", (Translatable t, String value) -> ((Attribute) t).setValue(value));
    }};

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

    @JsonIgnore
    @Override
    public Long getTranslatableId() {
        return attributeId;
    }

    @Override
    public void setTranslatableId(Long id) {
        this.attributeId = id;
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
