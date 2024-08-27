package com.example.merchstore.components.models;

import com.example.merchstore.components.enums.AdStatus;
import com.example.merchstore.components.enums.Gender;
import com.example.merchstore.components.enums.Role;
import com.example.merchstore.components.interfaces.DataDisplay;
import com.example.merchstore.components.interfaces.ImageDisplay;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Base64Util;
import org.hibernate.annotations.JdbcTypeCode;

import java.io.IOException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;

import static com.example.merchstore.components.utilities.ImageProcessor.getImageAsByteArray;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 25.08.2024
 */
@Data
@AllArgsConstructor
@Entity
@Table(name = "ads")
public class Ad implements DataDisplay, ImageDisplay { //TODO !!!

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ad_id")
    private Long adId;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private AdStatus status = AdStatus.ACTIVE; // Default status

    @JsonIgnore
    @JdbcTypeCode(Types.BINARY)
    @Column(name = "image", columnDefinition = "BYTEA", nullable = true)
    private byte[] image;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Ad(Ad other) {
        this.adId = other.adId;
        this.item = other.item;
        this.user = other.user;
        this.description = other.description;
        this.startDate = other.startDate;
        this.endDate = other.endDate;
        this.status = other.status;
        this.image = other.image;
        this.createdAt = other.createdAt;
        this.updatedAt = other.updatedAt;
        setDefaultImage();
    }

    public Ad() {
        setDefaultImage();
    }

    @Override
    public DataDisplay displayData() {
        return new Ad(this);
    }

    @Override
    public DataDisplay limitedDisplayData() {
        return null;
    }

    @Override
    public void setDefaultImage() {
        if (image == null || image.length == 0) {
            try {
                image = getImageAsByteArray("static/images/avatars/male_avatar_small.jpg");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public String base64Image() {
        setDefaultImage();
        return Base64.getEncoder().encodeToString(image);
    }

    public boolean shouldBeDisplayed() {
        return status == AdStatus.ACTIVE && startDate.isBefore(LocalDateTime.now()) && endDate.isAfter(LocalDateTime.now());
    }
}
