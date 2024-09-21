package com.example.merchstore.security;

import com.example.merchstore.repositories.CustomUserRepository;
import com.example.merchstore.services.CustomOAuth2UserService;
import com.example.merchstore.services.CustomUserDetailsService;
import com.example.merchstore.services.GlobalAttributeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.Collections;
import java.util.Map;

/**
 * The SecurityConfig class is a configuration class for Spring Security.
 * It provides several beans that are used to configure the security settings of the application.
 *
 * It has several methods:
 * <ul>
 *     <li>securityFilterChain(HttpSecurity http): This method configures the security filter chain. It sets up the security rules for different URL patterns and configures the login and logout behavior.</li>
 *     <li>passwordEncoder(): This method provides a BCryptPasswordEncoder bean for password encoding.</li>
 *     <li>authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder): This method provides an AuthenticationManager bean. It sets up a DaoAuthenticationProvider with the provided UserDetailsService and PasswordEncoder, and uses it to create a ProviderManager.</li>
 *     <li>userDetailsService(): This method provides a UserDetailsService bean. It returns the customUserDetailsService that is used to load user-specific data.</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 28.05.2024
 */

@Configuration
public class SecurityConfig {

    /**
     * The CustomUserDetailsService that is used to load user-specific data.
     */
    private final CustomUserDetailsService customUserDetailsService;

    /**
     * The CustomAuthenticationSuccessHandler that is used to handle successful authentication.
     */
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    private final CustomUserRepository customUserRepository;
    private final GlobalAttributeService globalAttributeService;


    /**
     * The constructor for the SecurityConfig class.
     *
     * @param customUserDetailsService The CustomUserDetailsService that is used to load user-specific data.
     * @param customAuthenticationSuccessHandler The CustomAuthenticationSuccessHandler that is used to handle successful authentication.
     */
    public SecurityConfig(CustomUserDetailsService customUserDetailsService,
                          CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler,
                          CustomUserRepository customUserRepository,
                          GlobalAttributeService globalAttributeService) {
        this.customUserDetailsService = customUserDetailsService;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
        this.customUserRepository = customUserRepository;
        this.globalAttributeService = globalAttributeService;
    }

    /**
     * This method configures the security filter chain. It sets up the security rules for different URL patterns and configures the login and logout behavior.
     *
     * @param http The HttpSecurity object.
     * @return A SecurityFilterChain object that represents the security filter chain configuration.
     * @throws Exception If an exception occurs during the configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/api/login", "/api/register", "/home", "/",
                                        "/api/register/form", "/api/login/form", "/item/**", "/api/image/**",
                                        "/error", "/help/**", "/info/**", "/join/**", "/updateAds").permitAll()
                                .requestMatchers("/documents/**","/css/**", "/javascript/**", "/svg/**", "/images/**", "/api/config", "/cookie/**", "/api/currencies/**").permitAll() //static recourses
                                .requestMatchers("/api/admin/**").hasAnyRole("ADMIN", "OWNER")
                                .anyRequest().authenticated()
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/api/login/form")
                                .defaultSuccessUrl("/home", true)
                                .failureUrl("/api/login?error=true")
                                .permitAll()
                                .successHandler(customAuthenticationSuccessHandler) // Use the custom success handler

                )
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .loginPage("/api/login/form")
                                .defaultSuccessUrl("/home", true)
                                .userInfoEndpoint(userInfoEndpoint ->
                                        userInfoEndpoint.userService(oAuth2UserService())
                                )
                                .successHandler(customAuthenticationSuccessHandler) // Use the custom success handler
                )
                .logout(LogoutConfigurer::permitAll);
        ;

        return http.build();
    }

    /**
     * This method provides a BCryptPasswordEncoder bean for password encoding.
     *
     * @return A BCryptPasswordEncoder bean for password encoding.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    /**
     * This method provides an AuthenticationManager bean. It sets up a DaoAuthenticationProvider with the provided UserDetailsService and PasswordEncoder, and uses it to create a ProviderManager.
     *
     * @param userDetailsService The UserDetailsService bean.
     * @param passwordEncoder The PasswordEncoder bean.
     * @return An AuthenticationManager bean.
     */
    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        ProviderManager providerManager = new ProviderManager(authenticationProvider);
        providerManager.setEraseCredentialsAfterAuthentication(false);

        return providerManager;
    }

    /**
     * This method provides a UserDetailsService bean. It returns the customUserDetailsService that is used to load user-specific data.
     *
     * @return A UserDetailsService bean.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return customUserDetailsService;
    }

    // Custom user service for processing the OAuth2 user info
    private OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
        return new CustomOAuth2UserService(customUserRepository, globalAttributeService);
    }

}
