package com.example.merchstore.principals;

import com.example.merchstore.components.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * The CustomUserPrincipal class is an implementation of the UserDetails interface from Spring Security.
 * It provides core user information to the framework.
 *
 * It has three methods:
 * <ul>
 *     <li>getAuthorities(): This method returns a collection of authorities granted to the user.</li>
 *     <li>getPassword(): This method returns the password used to authenticate the user.</li>
 *     <li>getUsername(): This method returns the username used to authenticate the user.</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 28.05.2024
 */

public class CustomUserPrincipal implements UserDetails {

    /**
     * The User object associated with the principal.
     * @see User
     */
    private User user;

    /**
     * Constructs a CustomUserPrincipal object with the specified User object.
     *
     * @param user The User object associated with the principal.
     */
    public CustomUserPrincipal(User user) {
        this.user = user;
    }

    /**
     * Returns a collection of authorities granted to the user.
     *
     * @return A collection of authorities granted to the user.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().toString()));
        return authorities;
    }

    /**
     * Returns the password used to authenticate the user.
     *
     * @return The password used to authenticate the user.
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Returns the username used to authenticate the user.
     *
     * @return The username used to authenticate the user.
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }
}
