package com.example.merchstore.controllers.functionality;

import com.example.merchstore.LocaleConfig;
import com.example.merchstore.components.enums.Language;
import com.example.merchstore.services.GlobalAttributeService;
import com.example.merchstore.services.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * The DocumentController_f class handles the web requests related to documents in the application.
 *
 * It has one method:
 * <ul>
 *     <li>displayDocument(String documentName, Model model): Handles the GET request for displaying a document. It retrieves the document based on the provided name, reads its content, adds the document name and content to the model, then returns the view name.</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 05.06.2024
 */

@Controller
@RequestMapping("/documents")
public class DocumentController_f {

    @Autowired
    private TranslationService translationService;

    @Autowired
    private LocaleConfig localeConfig;

    /**
     * Handles the GET request for displaying a document. It retrieves the document based on the provided name, reads its content, adds the document name and content to the model, then returns the view name.
     *
     * @param documentName The name of the document.
     * @param model The model to be prepared.
     * @return The view name.
     */
    @GetMapping
    public String displayDocument(@RequestParam("name") String documentName, Model model,
                                  @RequestParam(value = "lang", required = false) String lang) {

        Language language = localeConfig.getCurrentLanguage();


        String documentPath = null;
        String name = documentName;
        switch (documentName) {
            case "termsandconditions":
                documentPath = "src/main/resources/static/documents/termsandconditions.txt";
                name = "Terms and Conditions";
                break;
            default:
                return "error/404";
        }

        //TODO Long texts do not work
        try {
            String content = new String(Files.readAllBytes(Paths.get(documentPath)));
            model.addAttribute("documentName", translationService.translateDocument(name, language, name, 1L, Language.ENGLISH));
            model.addAttribute("documentContent", translationService.translateDocument(content, language, name, 2L, Language.ENGLISH));
        } catch (IOException e) {
            e.printStackTrace();
            return "error/404";
        }

        return "general/documents";
    }

}
