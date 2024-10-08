package com.example.merchstore.security;

import com.example.merchstore.LocaleConfig;
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
 * The CustomAuthenticationSuccessHandler class is a custom authentication success handler for a Spring Security application.
 * It extends SavedRequestAwareAuthenticationSuccessHandler, which is a built-in Spring Security class that handles redirections after successful authentication.
 *
 * It has two main methods:
 * <ul>
 *     <li>onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication): This method is called when a user is successfully authenticated. It determines the target URL and then redirects the user to that URL.</li>
 *     <li>determineTargetUrl(HttpServletRequest request): This method determines the URL to redirect the user to after successful authentication. If there is a saved request that does not contain "register", it redirects the user to the URL they originally tried to access. Otherwise, it checks the role of the authenticated user and redirects them to the appropriate dashboard ("/api/admin/dashboard" for admins and owners, "/home" for others).</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 08.06.2024
 */

@Component
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    /**
     * The HttpSession object that stores the user session.
     */
    @Autowired
    private HttpSession httpSession;

    @Autowired
    private LocaleConfig localeConfig;

    /**
     * The HttpSessionRequestCache object that stores the saved requests.
     */
    private final HttpSessionRequestCache requestCache = new HttpSessionRequestCache();

    /**
     * This method is called when a user is successfully authenticated. It determines the target URL and then redirects the user to that URL.
     *
     * @param request The HttpServletRequest object.
     * @param response The HttpServletResponse object.
     * @param authentication The Authentication object.
     * @throws IOException If an input or output exception occurs.
     * @throws ServletException If a servlet exception occurs.
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        String targetUrl = determineTargetUrl(request);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }


    /**
     * This method determines the URL to redirect the user to after successful authentication. If there is a saved request that does not contain "register", it redirects the user to the URL they originally tried to access. Otherwise, it checks the role of the authenticated user and redirects them to the appropriate dashboard ("/api/admin/dashboard" for admins and owners, "/home" for others).
     *
     * @param request The HttpServletRequest object.
     * @return The URL to redirect the user to after successful authentication.
     */
    public String determineTargetUrl(HttpServletRequest request) {
        SavedRequest savedRequest = requestCache.getRequest(request, null);

        if (savedRequest != null && !savedRequest.getRedirectUrl().contains("register")) {
            // Redirect to the URL the user originally tried to access
            return savedRequest.getRedirectUrl();
        } else {
            User user = (User) httpSession.getAttribute("user");
            if (user.getRole() == Role.ADMIN || user.getRole() == Role.OWNER) {
                return "/api/admin/dashboard?lang=" + localeConfig.getCurrentLanguage().code;
            }
            return "/home?lang=" + localeConfig.getCurrentLanguage().code;
        }
    }
}








