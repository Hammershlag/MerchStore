package com.example.merchstore.components.models;

import com.example.merchstore.components.enums.Language;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LanguageText {

    Language language;
    String text;

}
