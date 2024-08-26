package com.example.merchstore.components.models;

import com.example.merchstore.components.enums.AdStatus;
import com.example.merchstore.components.enums.Gender;
import com.example.merchstore.components.enums.Role;
import com.example.merchstore.components.interfaces.DataDisplay;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Base64Util;
import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.util.Base64Utils;

import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 25.08.2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ads")
public class Ad implements DataDisplay { //TODO !!!

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
    }

    @Override
    public DataDisplay displayData() {
        return new Ad(this);
    }

    @Override
    public DataDisplay limitedDisplayData() {
        return null;
    }

    //TODO Get rid of ImageController and move to interface
    public String base64Image() {
        return Base64.getEncoder().encodeToString(image);
    }
}
