package com.example.merchstore.components.superClasses;

import com.example.merchstore.components.enums.Language;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 23.09.2024
 */

@Data
public abstract class Translatable {

    protected HashMap<String, Function<Translatable, String>> textFieldsGetter = new HashMap<>();

    protected HashMap<String, BiConsumer<Translatable, String>> textFieldsSetter = new HashMap<>();

    protected String className;

    public String getFieldValue(String fieldName) {
        Function<Translatable, String> getter = textFieldsGetter.get(fieldName);
        if (getter != null) {
            return getter.apply(this);
        }
        return null;
    }

    @JsonIgnore
    public abstract Long getTranslatableId();

    public abstract void setTranslatableId(Long id);

    @JsonIgnore
    public abstract Language getTranslatableLanguage();

    public abstract void setTranslatableLanguage(Language language);


}
