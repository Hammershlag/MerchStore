package com.example.merchstore.controllers.general;

import com.example.merchstore.components.models.Ad;
import com.example.merchstore.repositories.AdRepository;
import com.example.merchstore.services.GlobalAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 27.08.2024
 */

@Controller
public class AdController_g {

    @Autowired
    private GlobalAttributeService globalAttributeService;

    @GetMapping("/updateAds")
    public String updateAds(Model model) {
        List<Ad> randomAds = globalAttributeService.getRandomAds(1); // Choose how many ads to display
        model.addAttribute("ads", randomAds);
        return "fragments/ad :: ad";
    }

}
