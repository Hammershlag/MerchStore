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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 28.05.2024
 */

@Controller
@RequestMapping("/api")
public class LoginController_s {

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    CustomUserRepository customUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private HttpSession httpSession;
    private final AuthenticationManager authenticationManager;

    @Autowired
    private CustomAuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private GlobalAttributeService globalAttributeService;


    public LoginController_s(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/login/form")
    public String showLoginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(HttpServletRequest req, HttpServletResponse res, @ModelAttribute LoginForm loginForm) {
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

    @GetMapping("/logout")
    public RedirectView logout(HttpSession session) {
        globalAttributeService.addAttribute("isLoggedIn", false);
        session.setAttribute("isLoggedIn", false);
        session.invalidate();

        return new RedirectView("/home");
    }

    @Data
    public static class LoginForm {
        private String username;
        private String password;

    }
}
