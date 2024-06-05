package com.example.merchstore.controllers.auth;

import com.example.merchstore.model.User;
import com.example.merchstore.repositories.CustomUserRepository;
import com.example.merchstore.services.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public LoginController_s(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/login/form")
    public String showLoginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(HttpServletRequest req, @ModelAttribute LoginForm loginForm) {
        if (customUserDetailsService.authenticateUser(loginForm, passwordEncoder)) {

            User user = customUserRepository.findByUsername(loginForm.getUsername());
            if (user == null) {
                user = customUserRepository.findByEmail(loginForm.getUsername());
            }

            httpSession.setAttribute("user", user);

            UsernamePasswordAuthenticationToken authReq
                    = new UsernamePasswordAuthenticationToken(user.getUsername(), loginForm.getPassword());
            Authentication auth = authenticationManager.authenticate(authReq);

            SecurityContext sc = SecurityContextHolder.getContext();
            sc.setAuthentication(auth);
            HttpSession session = req.getSession(true);
            session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);
            session.setAttribute("isLoggedIn", true);

            if (user.getRole().equals("ADMIN")) {
                return "redirect:/api/admin/dashboard";
            }

            return "redirect:/home";
        } else {
            return "redirect:/api/login/form?error=true";
        }
    }

    @GetMapping("/logout")
    public RedirectView logout(HttpSession session) {
        session.invalidate();
        return new RedirectView("/home");
    }

    @Data
    public static class LoginForm {
        private String username;
        private String password;

    }
}
