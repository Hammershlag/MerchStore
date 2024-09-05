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
import org.springframework.context.annotation.DependsOn;

import java.io.IOException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;

import static com.example.merchstore.components.utilities.Defaults.DEFAULT_AD_IMAGE;
import static com.example.merchstore.components.utilities.ImageProcessor.getImageAsByteArray;

/**
 * The Ad class represents an advertisement in the system.
 * It implements the DataDisplay and ImageDisplay interfaces to control the display of data and images.
 *
 * It has several fields, including:
 * <ul>
 *     <li>adId: The ID of the ad.</li>
 *     <li>item: The item being advertised.</li>
 *     <li>user: The user who posted the ad.</li>
 *     <li>description: The description of the ad.</li>
 *     <li>startDate: The start date of the ad.</li>
 *     <li>endDate: The end date of the ad.</li>
 *     <li>status: The status of the ad (active by default).</li>
 *     <li>image: The image of the ad in byte array format.</li>
 *     <li>createdAt: The creation date of the ad.</li>
 *     <li>updatedAt: The last update date of the ad.</li>
 * </ul>
 *
 * It also includes methods to get the status of the image, display data, set the default image, get the Base64 representation of the image, and check if the ad should be displayed.
 *
 * This class is also a JPA entity, meaning it is mapped to a corresponding table in the database.
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 25.08.2024
 */
@Data
@AllArgsConstructor
@Entity
@Table(name = "ads")
public class Ad implements DataDisplay, ImageDisplay {

    /**
     * The ID of the ad.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ad_id")
    private Long adId;

    /**
     * The item being advertised.
     */
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    /**
     * The user who posted the ad.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * The description of the ad.
     */
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

    @JsonProperty("imageStatus")
    public String getImageStatus() {
        return image != null ? "Uploaded" : "Not uploaded";
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
        if (DEFAULT_AD_IMAGE == null) {
            throw new IllegalStateException("Defaults.DEFAULT_AD_IMAGE is not initialized");
        }
        if (image == null || image.length == 0) {
            try {
                image = getImageAsByteArray(DEFAULT_AD_IMAGE); //TODO Works everywhere but here
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
