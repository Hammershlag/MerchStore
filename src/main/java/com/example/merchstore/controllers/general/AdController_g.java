package com.example.merchstore.controllers.general;

import com.example.merchstore.components.enums.Language;
import com.example.merchstore.components.models.Ad;
import com.example.merchstore.repositories.AdRepository;
import com.example.merchstore.services.GlobalAttributeService;
import com.example.merchstore.services.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The AdController_g class handles the web requests related to advertisements in the application.
 *
 * It has one method:
 * <ul>
 *     <li>updateAds(Model model, int maxAds): Handles the GET request for updating advertisements. It retrieves a list of random advertisements based on the provided maximum number, adds the list to the model, and returns the view name for the advertisement fragment.</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 27.08.2024
 */

@Controller
public class AdController_g {

    /**
     * The GlobalAttributeService that this controller uses to retrieve random advertisements.
     * @see GlobalAttributeService
     */
    @Autowired
    private GlobalAttributeService globalAttributeService;

    @Autowired
    private TranslationService translationService;

    /**
     * Handles the GET request for updating advertisements. It retrieves a list of random advertisements based on the provided maximum number, adds the list to the model, and returns the view name for the advertisement fragment.
     *
     * @param model The model to be prepared.
     * @param maxAds The maximum number of advertisements to display.
     * @return The view name for the advertisement fragment.
     */
    @GetMapping("/updateAds")
    public String updateAds(Model model, @RequestParam("maxAds") int maxAds, @RequestParam(value = "lang", required = false) String lang) {
        List<Ad> randomAds = globalAttributeService.getRandomAds(maxAds);// Choose how many ads to display
        Language language;
        if (lang != null) {
            language = Language.fromCode(lang);
            if(lang.equals("pl")) {
                language = (Language) globalAttributeService.getGlobalAttributes().get("language");
            } else if (!language.getCode().equals(((Language) globalAttributeService.getGlobalAttributes().get("language")).getCode())) {
                globalAttributeService.getGlobalAttributes().put("language", language);
            }
        } else {
            language = (Language) globalAttributeService.getGlobalAttributes().get("language");
        }
        List<Ad> translatedAds = new ArrayList<>();
        for (Ad ad : randomAds) {
            Ad translatedAd = (Ad) translationService.translate(ad, language);
            translatedAds.add(translatedAd);
        }
        model.addAttribute("ads", translatedAds);
        return "fragments/ad :: ad";
    }

}
