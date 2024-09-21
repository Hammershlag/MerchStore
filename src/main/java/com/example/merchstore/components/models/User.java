package com.example.merchstore.components.models;

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
import lombok.Value;
import org.hibernate.annotations.JdbcTypeCode;

import java.io.IOException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;

import static com.example.merchstore.components.utilities.Defaults.*;
import static com.example.merchstore.components.utilities.ImageProcessor.getImageAsByteArray;

/**
 * The User class represents a user in the system.
 * It implements the DataDisplay and ImageDisplay interfaces to control the display of data and images.
 *
 * It has fourteen fields:
 * <ul>
 *     <li>userId: The ID of the user.</li>
 *     <li>username: The username of the user.</li>
 *     <li>email: The email of the user.</li>
 *     <li>password: The password of the user.</li>
 *     <li>firstName: The first name of the user.</li>
 *     <li>lastName: The last name of the user.</li>
 *     <li>phoneNumber: The phone number of the user.</li>
 *     <li>address: The address of the user.</li>
 *     <li>role: The role of the user.</li>
 *     <li>createdAt: The date and time when the user was created.</li>
 *     <li>updatedAt: The date and time when the user was last updated.</li>
 *     <li>image: The image of the user.</li>
 *     <li>gender: The gender of the user.</li>
 *     <li>birthDate: The birth date of the user.</li>
 * </ul>
 *
 * It also includes methods to display data, limited display data, and base64 image.
 *
 * This class is also a JPA entity, meaning it is mapped to a corresponding table in the database.
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 28.05.2024
 */

@Data
@NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements DataDisplay, ImageDisplay {

    /**
     * The ID of the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true, nullable = false)
    private Long userId;

    /**
     * The username of the user.
     */
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    /**
     * The email of the user.
     */
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    /**
     * The password of the user.
     */
    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * The first name of the user.
     */
    @Column(name = "first_name", nullable = false)
    private String firstName;

    /**
     * The last name of the user.
     */
    @Column(name = "last_name", nullable = false)
    private String lastName;

    /**
     * The phone number of the user.
     */
    @Column(name = "phone_number")
    private String phoneNumber;

    /**
     * The address of the user.
     */
    @Column(name = "address")
    private String address;

    /**
     * The role of the user.
     * @see Role
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    /**
     * The date and time when the user was created.
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * The date and time when the user was last updated.
     */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * The image of the user in byte array format.
     */
    @JsonIgnore
    @JdbcTypeCode(Types.BINARY)
    @Column(name = "image", columnDefinition = "BYTEA", nullable = true)
    private byte[] image;

    /**
     * The gender of the user
     * @see Gender
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    /**
     * The birth date of the user.
     */
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "oauth_provider")
    private String oauthProvider;

    @Column(name = "oauth_user_id")
    private String oauthUserId;

    @Column(name = "oauth_user_picture_url")
    private String oauthUserPictureUrl;

    /**
     * The constructor of the User class.
     *
     * @param userId The ID of the user.
     * @param username The username of the user.
     * @param email The email of the user.
     * @param firstName The first name of the user.
     * @param lastName The last name of the user.
     * @param phoneNumber The phone number of the user.
     * @param address The address of the user.
     * @param createdAt The date and time when the user was created.
     * @param updatedAt The date and time when the user was last updated
     */
    public User(Long userId, String username, String email, String firstName, String lastName, String phoneNumber, String address, LocalDateTime createdAt, LocalDateTime updatedAt, Gender gender, LocalDate birthDate) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.gender = gender;
        this.image = null;
        oauthUserId = null;
        oauthProvider = null;
        oauthUserPictureUrl = null;
        setDefaultImage();
        this.birthDate = birthDate;
    }
    /**
     * The copy constructor of the User class.
     *
     * @param user the User object to copy.
     */
    public User(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.phoneNumber = user.getPhoneNumber();
        this.address = user.getAddress();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
        this.gender = user.getGender();
        this.image = user.getImage() != null ? user.getImage().clone() : null;
        setDefaultImage();
        this.role = user.getRole();
        this.birthDate = user.getBirthDate();
    }

    /**
     * The method to set the default image of the user.
     * @see ImageDisplay
     */
    @Override
    public void setDefaultImage() {
        if ((getImage() == null || getImage().length == 0) && oauthUserPictureUrl == null) {
            try {
                switch (getGender()) {
                    case MALE -> image = getImageAsByteArray(DEFAULT_USER_MALE_IMAGE);
                    case FEMALE -> image = getImageAsByteArray(DEFAULT_USER_FEMALE_IMAGE);
                    default -> image = getImageAsByteArray(DEFAULT_USER_IMAGE);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public String resolveImage() {
        return oauthUserPictureUrl == null ? "/api/image/user/" + userId : oauthUserPictureUrl;
    }

    /**
     * Get the status of the image. Uploaded if the image is not null, Not uploaded otherwise.
     * @return a String representing the status of the image.
     */
    @JsonProperty("imageStatus")
    public String getImageStatus() {
        return image != null ? "Uploaded" : "Not uploaded";
    }

    /**
     * Display the data of the user.
     *
     * @see DataDisplay
     *
     * @return a DataDisplay object representing the user data.
     */
    @Override
    public User displayData() {
        return new User(this);
    }

    /**
     * Display a limited set of data of the user.
     *
     * @see DataDisplay
     *
     * @return a DataDisplay object representing the limited user data.
     */
    @Override
    public DataDisplay limitedDisplayData() {
        return null;
    }

    /**
     * Convert the image to a base64 string.
     *
     * @return a String representing the image in base64 format.
     */
    @Override
    public String base64Image() {
        setDefaultImage();
        return Base64.getEncoder().encodeToString(image);
    }

}
