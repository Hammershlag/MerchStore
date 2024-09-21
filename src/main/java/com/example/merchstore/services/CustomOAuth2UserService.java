package com.example.merchstore.services;

import com.example.merchstore.components.enums.Gender;
import com.example.merchstore.components.enums.Role;
import com.example.merchstore.components.models.User;
import com.example.merchstore.repositories.CustomUserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static com.example.merchstore.components.utilities.ImageProcessor.imageToByteArray;
import static com.example.merchstore.components.utilities.ImageProcessor.resizeAndCropImage;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 21.09.2024
 */
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private GlobalAttributeService globalAttributeService;

    private final CustomUserRepository userRepository;

    public CustomOAuth2UserService(CustomUserRepository userRepository, GlobalAttributeService globalAttributeService) {
        this.userRepository = userRepository;
        this.globalAttributeService = globalAttributeService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // Extract user information from the OAuth2 response
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String firstName = (String) attributes.get("given_name");
        String lastName = (String) attributes.get("family_name");
        String profilePictureUrl = (String) attributes.get("picture"); // Extract profile picture URL



        // Check if the user already exists in the database
        User userOptional = userRepository.findByEmail(email);

        // Extract OAuth provider (e.g., "google")
        String provider = userRequest.getClientRegistration().getRegistrationId();

        // Extract the unique OAuth user ID
        String oauthUserId = (String) attributes.get("sub");

        User user;
        if (userOptional == null) {
            // If the user does not exist, create a new one and save it to the database
            user = new User();
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setUsername(email); // Or generate a unique username
            user.setRole(Role.USER); // Default role
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            user.setGender(Gender.DEFAULT);
            user.setOauthProvider(provider);
            user.setOauthUserId(oauthUserId);
            Random random = new Random();
            user.setPhoneNumber(String.format("%03d-%03d-%04d",
                    random.nextInt(900) + 100,  // First three digits (area code)
                    random.nextInt(900) + 100,  // Next three digits
                    random.nextInt(9000) + 1000)); // Last four digits);
            // Optionally set other fields like gender, phone number, etc.
            user.setPassword("default");
            // Default password
            if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
                user.setOauthUserPictureUrl(profilePictureUrl);
            }
            user.setAddress("Default address");
            user.setBirthDate(LocalDate.now());

            userRepository.save(user);
        } else {
            // If the user already exists, you may update their information if necessary
            user = userOptional;
        }

        // Manually retrieve the HttpSession from the HttpServletRequest
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        HttpSession session = request.getSession();

        HttpServletResponse res = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();

        session.setAttribute("user", user);
        session.setAttribute("isLoggedIn", true);
        globalAttributeService.addAttribute("user", user);
        globalAttributeService.addAttribute("isLoggedIn", true);

        Cookie loginCookie = new Cookie("login", "new");
        loginCookie.setMaxAge(24 * 60 * 60);
        loginCookie.setPath("/");
        res.addCookie(loginCookie);

        // Return the OAuth2User object with updated attributes
        return new DefaultOAuth2User(
                Collections.singleton(new OAuth2UserAuthority(attributes)),
                attributes,
                "name" // principal attribute (can be "sub", "email", etc.)
        );
    }
}
