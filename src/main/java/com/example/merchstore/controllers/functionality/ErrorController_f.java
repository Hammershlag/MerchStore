package com.example.merchstore.controllers.functionality;

import com.example.merchstore.LocaleConfig;
import com.example.merchstore.components.enums.Language;
import com.example.merchstore.services.GlobalAttributeService;
import com.example.merchstore.services.TranslationService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @Autowired
    private TranslationService translationService;

    @Autowired
    private LocaleConfig localeConfig;

    /**
     * Handles the GET request for displaying an error page. It retrieves the error status code from the request, sets the status code, error message, and a custom message based on the status code in the model, then returns the view name for the error page.
     *
     * @param request The HTTP request.
     * @param model The model to be prepared.
     * @return The view name for the error page.
     */
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model,
                              @RequestParam(value = "lang", required = false) String lang) {

        Language language = localeConfig.getCurrentLanguage();

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            model.addAttribute("statusCode", statusCode);
            model.addAttribute("errorMessage", translationService.translateDocument(HttpStatus.valueOf(statusCode).getReasonPhrase(), language, "error", 1L, Language.ENGLISH));

            switch (HttpStatus.valueOf(statusCode)) {
                case NOT_FOUND:
                    model.addAttribute("customMessage",
                            translationService.translateDocument("The page you are looking for might have been removed or is temporarily unavailable.",language, "error", 2L, Language.ENGLISH));
                    break;
                case INTERNAL_SERVER_ERROR:
                    model.addAttribute("customMessage",
                            translationService.translateDocument("We're experiencing internal issues. Please try again later.", language, "error", 3L, Language.ENGLISH));
                    break;
                default:
                    model.addAttribute("customMessage",
                            translationService.translateDocument("An unexpected error occurred. Please try again.", language, "error", 4L, Language.ENGLISH));
                    break;
            }
        } else {
            model.addAttribute("customMessage",
                    translationService.translateDocument("An error occurred. Please try again.", language, "error", 5L, Language.ENGLISH));
        }
        return "error/error";
    }

}
