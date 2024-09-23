package com.example.merchstore.components.models;

import com.example.merchstore.components.enums.Language;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 23.09.2024
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pre_translated_texts")
public class PreTranslatedTexts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "class_name")
    private String className;

    @Column(name = "field_name")
    private String fieldName;

    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private Language language;

    @Column(name = "text")
    private String text;

    @Column(name = "entity_id")
    private Long entityId;

    @Column(name = "last_update")
    private LocalDateTime lastUpdate;
}
