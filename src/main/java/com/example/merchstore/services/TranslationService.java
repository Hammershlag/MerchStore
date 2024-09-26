package com.example.merchstore.services;

import com.example.merchstore.components.enums.Language;
import com.example.merchstore.components.utilities.Translator;
import com.example.merchstore.components.models.LanguageText;
import com.example.merchstore.components.superClasses.Translatable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 23.09.2024
 */
@Service
public class TranslationService {

    @Autowired
    private Translator translator;

    public Translatable translate(Translatable translatable, Language outputLanguage) {
        try {
            if (outputLanguage == translatable.getTranslatableLanguage()) {
                return translatable; // Return the original object if the output language is the same
            }

            // Get the class of the object to be translated
            Class<?> clazz = translatable.getClass();

            // Use reflection to create a new instance of the same class
            Constructor<?> constructor = clazz.getConstructor();
            Translatable translatedObject = (Translatable) constructor.newInstance();

            // Copy non-translatable fields
            copyNonTranslatableFields(translatable, translatedObject);

            // Iterate over all fields using textFieldsGetter
            for (Map.Entry<String, Function<Translatable, String>> entry : translatable.getTextFieldsGetter().entrySet()) {
                String fieldName = entry.getKey();
                Function<Translatable, String> getter = entry.getValue();

                // Get the original text from the getter
                String originalText = getter.apply(translatable);

                // Translate the text
                LanguageText languageText = new LanguageText(translatable.getTranslatableLanguage(), originalText);
                LanguageText translatedText = translator.translateText(languageText, outputLanguage, clazz.getSimpleName(), fieldName, translatable.getTranslatableId());

                // Use the setter to set the translated text in the new object
                BiConsumer<Translatable, String> setter = translatable.getTextFieldsSetter().get(fieldName);
                if (setter != null) {
                    setter.accept(translatedObject, translatedText.getText());
                }
            }

            // Set the output language in the new object
            translatedObject.setTranslatableLanguage(outputLanguage);

            return translatedObject;

        } catch (Exception e) {
            e.printStackTrace();
            return null; // Return null or handle error as needed
        }
    }

    public String translateDocument(String text, Language outputLanguage, String documentName, Long documentId, Language inputLanguage) {
        if (text.length() > 255)
        {
            StringBuilder sb = new StringBuilder();
            String[] words = text.split("(?<=[.!?])\\s+");
            for (int i = 0; i < words.length; i++) {
                String word = words[i];
                if (word.length() > 255) {
                    String[] subWords = word.split("\\s+");
                    for (String subWord : subWords) {
                        sb.append(translator.translateText(new LanguageText(inputLanguage, subWord), outputLanguage, "Document", documentName + "_" + i, documentId).getText());
                    }
                    continue;
                }
                sb.append(translator.translateText(new LanguageText(inputLanguage, word), outputLanguage, "Document", documentName + "_" + i, documentId).getText());
            }
            return sb.toString();
        }
        return translator.translateText(new LanguageText(inputLanguage, text), outputLanguage, "Document", documentName, documentId).getText();
    }

    private void copyNonTranslatableFields(Translatable source, Translatable target) {
        try {
            Field[] fields = source.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true); // Bypass private access
                Object value = field.get(source);
                field.set(target, value);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
