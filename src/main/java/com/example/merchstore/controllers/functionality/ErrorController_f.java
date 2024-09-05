package com.example.merchstore.controllers.functionality;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * The ErrorController_f class handles the web requests related to errors in the application.
 *
 * It has one method:
 * <ul>
 *     <li>handleError(HttpServletRequest request, Model model): Handles the GET request for displaying an error page. It retrieves the error status code from the request, sets the status code, error message, and a custom message based on the status code in the model, then returns the view name for the error page.</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 05.06.2024
 */

@Controller
public class ErrorController_f implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            model.addAttribute("statusCode", statusCode);
            model.addAttribute("errorMessage", HttpStatus.valueOf(statusCode).getReasonPhrase());

            switch (HttpStatus.valueOf(statusCode)) {
                case NOT_FOUND:
                    model.addAttribute("customMessage", "The page you are looking for might have been removed or is temporarily unavailable.");
                    break;
                case INTERNAL_SERVER_ERROR:
                    model.addAttribute("customMessage", "We're experiencing internal issues. Please try again later.");
                    break;
                default:
                    model.addAttribute("customMessage", "An unexpected error occurred. Please try again.");
                    break;
            }
        } else {
            model.addAttribute("customMessage", "An error occurred. Please try again.");
        }
        return "error/error";
    }

}
