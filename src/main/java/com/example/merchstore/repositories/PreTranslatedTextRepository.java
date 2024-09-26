package com.example.merchstore.repositories;

import com.example.merchstore.components.enums.Language;
import com.example.merchstore.components.models.PreTranslatedTexts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 23.09.2024
 */
public interface PreTranslatedTextRepository extends JpaRepository<PreTranslatedTexts, Long> {

    PreTranslatedTexts findByClassNameAndFieldNameAndLanguageAndEntityId(String className, String fieldName, Language language, Long entityId);

    List<PreTranslatedTexts> findTop10ByClassNameAndFieldNameAndLanguageAndTextStartingWithIgnoreCase(String className, String fieldName, Language language, String text);
    List<PreTranslatedTexts> findAllByClassNameAndFieldNameAndLanguageAndTextStartingWithIgnoreCase(String className, String fieldName, Language language, String text);
}
