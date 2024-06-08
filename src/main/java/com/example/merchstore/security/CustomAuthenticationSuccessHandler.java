package com.example.merchstore.security;

import com.example.merchstore.components.enums.Role;
import com.example.merchstore.components.models.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 08.06.2024
 */
@Component
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private HttpSession httpSession;

    private final HttpSessionRequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        String targetUrl = determineTargetUrl(request);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    // Determine the target URL after successful authentication
    public String determineTargetUrl(HttpServletRequest request) {
        SavedRequest savedRequest = requestCache.getRequest(request, null);

        if (savedRequest != null && !savedRequest.getRedirectUrl().contains("register")) {
            // Redirect to the URL the user originally tried to access
            return savedRequest.getRedirectUrl();
        } else {
            User user = (User) httpSession.getAttribute("user");
            if (user.getRole() == Role.ADMIN || user.getRole() == Role.OWNER) {
                return "/api/admin/dashboard";
            }
            return "/home";
        }
    }
}








