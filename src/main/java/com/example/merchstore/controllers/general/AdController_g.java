package com.example.merchstore.controllers.general;

import com.example.merchstore.components.models.Ad;
import com.example.merchstore.repositories.AdRepository;
import com.example.merchstore.services.GlobalAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @Autowired
    private GlobalAttributeService globalAttributeService;

    @GetMapping("/updateAds")
    public String updateAds(Model model, @RequestParam("maxAds") int maxAds) {
        List<Ad> randomAds = globalAttributeService.getRandomAds(maxAds); // Choose how many ads to display
        model.addAttribute("ads", randomAds);
        return "fragments/ad :: ad";
    }

}
