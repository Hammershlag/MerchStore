package com.example.merchstore.controllers.functionality;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 05.06.2024
 */
@Controller
@RequestMapping("/documents")
public class DocumentController_f {

    @GetMapping
    public String displayDocument(@RequestParam("name") String documentName, Model model) {
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

        try {
            String content = new String(Files.readAllBytes(Paths.get(documentPath)));
            model.addAttribute("documentName", name);
            model.addAttribute("documentContent", content);
        } catch (IOException e) {
            e.printStackTrace();
            return "error/404";
        }

        return "general/documents";
    }

}
