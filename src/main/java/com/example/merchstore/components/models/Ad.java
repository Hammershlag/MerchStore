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
     * @see Item
     */
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    /**
     * The user who posted the ad.
     * @see User
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * The description of the ad.
     */
    @Column(name = "description", nullable = false)
    private String description;

    /**
     * The start date of the ad.
     */
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    /**
     * The end date of the ad.
     */
    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    /**
     * The status of the ad.
     * @see AdStatus
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private AdStatus status = AdStatus.ACTIVE; // Default status

    /**
     * The image of the ad in byte array format.
     */
    @JsonIgnore
    @JdbcTypeCode(Types.BINARY)
    @Column(name = "image", columnDefinition = "BYTEA", nullable = true)
    private byte[] image;

    /**
     * The creation date of the ad.
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * The last update date of the ad.
     */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * The copy constructor of the Ad class.
     *
     * @param other the Ad object to copy.
     */
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

    /**
     * The default constructor of the Ad class.
     */
    public Ad() {
        setDefaultImage();
    }

    /**
     * Get the status of the image.
     *
     * @return a String representing the status of the image.
     */
    @JsonProperty("imageStatus")
    public String getImageStatus() {
        return image != null ? "Uploaded" : "Not uploaded";
    }

    /**
     * Display the data of the ad.
     *
     * @see DataDisplay
     *
     * @return a DataDisplay object representing the ad data.
     */
    @Override
    public DataDisplay displayData() {
        return new Ad(this);
    }

    /**
     * Display a limited set of data of the ad.
     *
     * @see DataDisplay
     *
     * @return a DataDisplay object representing the limited ad data.
     */
    @Override
    public DataDisplay limitedDisplayData() {
        return null;
    }

    /**
     * Set the default image.
     *
     * @see ImageDisplay
     */
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
    public String resolveImage() {
        return "";
    }

    /**
     * Get the Base64 representation of the image.
     *
     * @see ImageDisplay
     *
     * @return a String representing the Base64 image.
     */
    @Override
    public String base64Image() {
        setDefaultImage();
        return Base64.getEncoder().encodeToString(image);
    }

    /**
     * Check if the ad should be displayed.
     *
     * @see AdStatus
     *
     * @return a boolean indicating if the ad should be displayed.
     */
    public boolean shouldBeDisplayed() {
        return status == AdStatus.ACTIVE && startDate.isBefore(LocalDateTime.now()) && endDate.isAfter(LocalDateTime.now());
    }
}
