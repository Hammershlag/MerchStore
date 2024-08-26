package com.example.merchstore.controllers.admin;

import com.example.merchstore.components.models.Ad;
import com.example.merchstore.components.models.User;
import com.example.merchstore.repositories.AdRepository;
import com.example.merchstore.repositories.ItemRepository;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDateTime;

import static com.example.merchstore.components.utilities.ImageProcessor.*;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 26.08.2024
 */

@Controller
@RequestMapping("/api/admin")
public class AdController_a {

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private HttpSession httpSession;


    @GetMapping("/add/ad")
    public String addAd(Model model) {

        model.addAttribute("ad", new Ad());
        model.addAttribute("items", itemRepository.findAll());

        return "admin/add/addAd";
    }

    @SneakyThrows
    @PostMapping("/add/ad")
    public String addAd(Ad ad, @RequestParam("imageData") MultipartFile imageData) {

        User currentUser = (User) httpSession.getAttribute("user");

        ad.setUser(currentUser);

        if (!imageData.isEmpty()) {
            BufferedImage bufferedImage = ImageIO.read(imageData.getInputStream());

            int angle = bufferedImage.getHeight() > bufferedImage.getWidth() ? 90 : 0;

            bufferedImage = rotateImage(bufferedImage, angle);
            bufferedImage = resizeAndCropImage(bufferedImage, 200, 200);
            byte[] imageBytes = imageToByteArray(bufferedImage, imageData.getOriginalFilename().substring(imageData.getOriginalFilename().lastIndexOf(".") + 1));
            ad.setImage(imageBytes);

        } else {
            ad.setImage(new byte[0]);
        }


        ad.setCreatedAt(LocalDateTime.now());
        ad.setUpdatedAt(LocalDateTime.now());
        adRepository.save(ad);

        return "redirect:/api/admin/dashboard"; // Redirect after successful ad creation
    }

    @GetMapping("/view/ads")
    public String viewAds(@RequestParam(value = "page", defaultValue = "0") int page,
                          @RequestParam(value = "size", defaultValue = "10") int size,
                          Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Ad> ads = adRepository.findAll(pageable);

        model.addAttribute("ads", ads.getContent());
        model.addAttribute("totalPages", ads.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        return "admin/view/viewAds";
    }

}
