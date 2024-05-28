package com.example.merchstore.controllers;

import com.example.merchstore.dto.User;
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

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    CustomUserRepository customUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private HttpSession httpSession;
    private final AuthenticationManager authenticationManager;

    public LoginController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/login/form")
    public String showLoginForm(Model model) {
        logger.info("Displaying login form");
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }

    @PostMapping("/login")
    public String login(HttpServletRequest req, @ModelAttribute LoginForm loginForm) {
        if (customUserDetailsService.authenticateUser(loginForm.getUsername(), loginForm.getPassword(), passwordEncoder)) {

            User user = customUserRepository.findByUsername(loginForm.getUsername());

            httpSession.setAttribute("user", user);

            UsernamePasswordAuthenticationToken authReq
                    = new UsernamePasswordAuthenticationToken(loginForm.getUsername(), loginForm.getPassword());
            Authentication auth = authenticationManager.authenticate(authReq);

            SecurityContext sc = SecurityContextHolder.getContext();
            sc.setAuthentication(auth);
            HttpSession session = req.getSession(true);
            session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);

            return "redirect:/home2";
        } else {
            return "redirect:/login/form?error=true";
        }
    }

    @Data
    public static class LoginForm {
        private String username;
        private String password;

    }
}
