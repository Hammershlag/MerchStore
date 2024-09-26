package com.example.merchstore.controllers.auth;

import com.example.merchstore.components.models.User;
import com.example.merchstore.repositories.CustomUserRepository;
import com.example.merchstore.security.CustomAuthenticationSuccessHandler;
import com.example.merchstore.services.CustomUserDetailsService;
import com.example.merchstore.services.GlobalAttributeService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

/**
 * The LoginController_s class handles the web requests related to user authentication in the application.
 *
 * It has three methods:
 * <ul>
 *     <li>showLoginForm(Model model): Prepares the model for the login form and returns the view name.</li>
 *     <li>login(HttpServletRequest req, HttpServletResponse res, LoginForm loginForm): Handles the POST request for user login. It authenticates the user, sets the user and login status in the session, creates a login cookie, and redirects to the appropriate URL based on the custom success handler.</li>
 *     <li>logout(HttpSession session): Handles the GET request for user logout. It invalidates the session and redirects to the home page.</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 28.05.2024
 */

@Controller
@RequestMapping("/api")
public class LoginController_s {

    /**
     * The CustomUserDetailsService that this controller uses to authenticate users.
     * @see CustomUserDetailsService
     */
    @Autowired
    CustomUserDetailsService customUserDetailsService;

    /**
     * The CustomUserRepository that this controller uses to perform CRUD operations on users.
     * @see CustomUserRepository
     */
    @Autowired
    CustomUserRepository customUserRepository;

    /**
     * The PasswordEncoder that this controller uses to encode passwords.
     * @see PasswordEncoder
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * The HttpSession object that this controller uses to store and retrieve session attributes.
     */
    @Autowired
    private HttpSession httpSession;

    /**
     * The AuthenticationManager that this controller uses to authenticate users.
     */
    private final AuthenticationManager authenticationManager;

    /**
     * The CustomAuthenticationSuccessHandler that this controller uses to determine the target URL after successful login.
     * @see CustomAuthenticationSuccessHandler
     */
    @Autowired
    private CustomAuthenticationSuccessHandler authenticationSuccessHandler;

    /**
     * The GlobalAttributeService that this controller uses to add global attributes.
     * @see GlobalAttributeService
     */
    @Autowired
    private GlobalAttributeService globalAttributeService;

    /**
     * Creates a new LoginController_s with the specified AuthenticationManager.
     *
     * @param authenticationManager The AuthenticationManager to be used.
     */
    public LoginController_s(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * Prepares the model for the login form and returns the view name.
     *
     * @param model The model to be prepared.
     * @return The view name.
     */
    @GetMapping("/login/form")
    public String showLoginForm(Model model, @RequestParam(required = false) String lang) {
        model.addAttribute("loginForm", new LoginForm());
        return "auth/login";
    }

    /**
     * Handles the POST request for user login. It authenticates the user, sets the user and login status in the session, creates a login cookie, and redirects to the appropriate URL based on the custom success handler.
     *
     * @param req The HttpServletRequest object.
     * @param res The HttpServletResponse object.
     * @param loginForm The login form data.
     * @return The redirect URL.
     */
    @PostMapping("/login")
    public String login(HttpServletRequest req, HttpServletResponse res, @ModelAttribute LoginForm loginForm,
                        @RequestParam(required = false) String lang) {
        if (customUserDetailsService.authenticateUser(loginForm, passwordEncoder)) {

            User user = customUserRepository.findByUsername(loginForm.getUsername());
            if (user == null) {
                user = customUserRepository.findByEmail(loginForm.getUsername());
            }

            httpSession.setAttribute("user", user);
            globalAttributeService.addAttribute("user", user);

            UsernamePasswordAuthenticationToken authReq
                    = new UsernamePasswordAuthenticationToken(user.getUsername(), loginForm.getPassword());
            Authentication auth = authenticationManager.authenticate(authReq);

            SecurityContext sc = SecurityContextHolder.getContext();
            sc.setAuthentication(auth);
            HttpSession session = req.getSession(true);
            session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);
            session.setAttribute("isLoggedIn", true);
            globalAttributeService.addAttribute("isLoggedIn", true);

            Cookie loginCookie = new Cookie("login", "new");
            loginCookie.setMaxAge(24 * 60 * 60);
            loginCookie.setPath("/");
            res.addCookie(loginCookie);

            return "redirect:" + authenticationSuccessHandler.determineTargetUrl(req); // Redirect using the custom success handler
        } else {
            return "redirect:/api/login/form?error=true";
        }
    }

    /**
     * Handles the GET request for user logout. It invalidates the session and redirects to the home page.
     *
     * @param session The HttpSession object.
     * @return The redirect URL.
     */
    @GetMapping("/logout")
    public RedirectView logout(HttpSession session, @RequestParam(required = false) String lang) {
        globalAttributeService.addAttribute("isLoggedIn", false);
        session.setAttribute("isLoggedIn", false);
        session.invalidate();

        return new RedirectView("/home");
    }

    /**
     * The LoginForm class represents the login form data.
     */
    @Data
    public static class LoginForm {
        private String username;
        private String password;

    }
}
