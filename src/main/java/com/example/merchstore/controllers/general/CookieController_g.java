package com.example.merchstore.controllers.general;

import com.example.merchstore.components.models.User;
import com.example.merchstore.repositories.UserItemHistoryRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The CookieController_g class handles the web requests related to cookies in the application.
 *
 * It has several methods:
 * <ul>
 *     <li>setCookie(HttpServletResponse response, String key, String value): Handles the GET request for setting a cookie. It creates a new cookie with the provided key and value, sets its max age and path, adds it to the response, and returns a success message.</li>
 *     <li>historyKey(): Generates a unique key for history cookies based on the current date and time.</li>
 *     <li>getCookie(HttpServletRequest request, String key, String value): Handles the GET request for retrieving a cookie. It retrieves the cookie with the provided key from the request, checks if the value matches (if provided), and returns the cookie value or a default value.</li>
 *     <li>getAllCookies(HttpServletRequest request): Handles the GET request for retrieving all cookies. It retrieves all cookies from the request and returns their names and values.</li>
 *     <li>getHistoryCookies(HttpServletRequest request): Handles the GET request for retrieving all history cookies. It retrieves all cookies from the request, filters the history cookies, and returns their names and values.</li>
 *     <li>deleteAllCookies(HttpSession session, HttpServletRequest request, HttpServletResponse response, String key): Handles the GET request for deleting cookies. It retrieves all cookies from the request, deletes the cookie with the provided key or all cookies if no key is provided, and returns a success message or a count of history cookies and the index of the first history cookie.</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 02.09.2024
 */

@RestController
@RequestMapping("/cookie")
public class CookieController_g {

    /**
     * The UserItemHistoryRepository that this controller uses to perform CRUD operations on user item history.
     * @see UserItemHistoryRepository
     */
    @Autowired
    private UserItemHistoryRepository userItemHistoryRepository;

    /**
     * Handles the GET request for setting a cookie. It creates a new cookie with the provided key and value, sets its max age and path, adds it to the response, and returns a success message.
     *
     * @param response The HttpServletResponse to add the cookie to.
     * @param key The key of the cookie.
     * @param value The value of the cookie.
     * @return A success message.
     */
    @GetMapping("/add")
    public String setCookie(HttpServletResponse response, @RequestParam(name = "key") String key, @RequestParam(name = "value") String value) {
        if (key.equals("history")) {
            key = historyKey();
        }
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 60 * 24 * 7); // 7 days
        cookie.setPath("/");

        response.addCookie(cookie);

        return "Cookies set successfully";
    }

    /**
     * Generates a unique key for history cookies based on the current date and time.
     *
     * @return A unique key for history cookies.
     */
    public String historyKey() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss");
        LocalDateTime now = LocalDateTime.now();
        return "history_" + formatter.format(now);
    }

    /**
     * Handles the GET request for retrieving a cookie. It retrieves the cookie with the provided key from the request, checks if the value matches (if provided), and returns the cookie value or a default value.
     *
     * @param request The HttpServletRequest to retrieve the cookie from.
     * @param key The key of the cookie.
     * @param value The value to match (if provided).
     * @return The cookie value or a default value.
     */
    @GetMapping("/get")
    public String getCookie(HttpServletRequest request, @RequestParam(required = true) String key, @RequestParam(required = false, name = "value") String value) {
        Cookie[] cookies = request.getCookies();
        String cookieValue = "false";
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (value != null) {
                    if (key.equals("history") && cookie.getName().contains(key) && cookie.getValue().equals(value)) {
                        cookieValue = "true";
                        break;
                    }
                }
                if (cookie.getName().equals(key)) {
                    cookieValue = cookie.getValue();
                    break;
                }
            }
        }
        return cookieValue;
    }

    /**
     * Handles the GET request for retrieving all cookies. It retrieves all cookies from the request and returns their names and values.
     *
     * @param request The HttpServletRequest to retrieve the cookies from.
     * @return The names and values of all cookies.
     */
    @GetMapping("/get/all")
    public String getAllCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        StringBuilder cookieValue = new StringBuilder();
        for (Cookie cookie : cookies) {
            cookieValue.append(cookie.getName()).append(": ").append(cookie.getValue()).append("; ");
        }
        return cookieValue.toString();
    }

    /**
     * Handles the GET request for retrieving all history cookies. It retrieves all cookies from the request, filters the history cookies, and returns their names and values.
     *
     * @param request The HttpServletRequest to retrieve the cookies from.
     * @return The names and values of all history cookies.
     */
    @GetMapping("/get/history")
    public String getHistoryCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        StringBuilder cookieValue = new StringBuilder();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().startsWith("history")) {
                    cookieValue.append(cookie.getName()).append(": ").append(cookie.getValue()).append("; ");
                }
            }
        }
        return cookieValue.toString();
    }

    /**
     * Handles the GET request for deleting cookies. It retrieves all cookies from the request, deletes the cookie with the provided key or all cookies if no key is provided, and returns a success message or a count of history cookies and the index of the first history cookie.
     *
     * @param session The HttpSession to retrieve the user from.
     * @param request The HttpServletRequest to retrieve the cookies from.
     * @param response The HttpServletResponse to delete the cookie from.
     * @param key The key of the cookie to delete (if provided).
     * @return A success message or a count of history cookies and the index of the first history cookie.
     */
    @GetMapping("/delete")
    public String deleteAllCookies(HttpSession session, HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false) String key) {
        Cookie[] cookies = request.getCookies();
        int historyCounter = 0;
        int firstHistoryIndex = -1;
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (key != null) {
                    if (cookie.getName().equals(key)) {
                        cookie.setMaxAge(0);
                        cookie.setPath("/");
                        response.addCookie(cookie);
                        return "Cookie deleted successfully";
                    } else if (cookie.getName().startsWith("history") && key.contains("history")) {
                        historyCounter++;
                        if (historyCounter == 1) {
                            firstHistoryIndex = i;
                        }
                    } else if (cookie.getName().startsWith("history") && key.equals("allHistory")) {
                        cookie.setMaxAge(0);
                        cookie.setPath("/");
                        response.addCookie(cookie);
                        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
                        if (isLoggedIn == null) {
                            isLoggedIn = false;
                        }
                        if (isLoggedIn) {
                            userItemHistoryRepository.deleteAllByUser(((User) session.getAttribute("user")));
                        }
                    }
                }
                else {
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);

                }
            }
            if (historyCounter > 10) {
                Cookie cookie = new Cookie(cookies[firstHistoryIndex].getName(), cookies[firstHistoryIndex].getValue());
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        }
        return "History cookies: " + historyCounter + ", firstHistoryIndex: " + firstHistoryIndex;
    }

}
