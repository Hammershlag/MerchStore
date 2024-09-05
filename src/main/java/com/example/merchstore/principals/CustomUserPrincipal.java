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

    private User user;

    public CustomUserPrincipal(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().toString()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }
}
