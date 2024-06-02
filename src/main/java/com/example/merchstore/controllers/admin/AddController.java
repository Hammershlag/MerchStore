package com.example.merchstore.controllers.admin;

import com.example.merchstore.dto.Category;
import com.example.merchstore.dto.Discount;
import com.example.merchstore.dto.Item;
import com.example.merchstore.repositories.CategoryRepository;
import com.example.merchstore.repositories.DiscountRepository;
import com.example.merchstore.repositories.ItemRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.merchstore.dto.ImageProcessor.*;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 01.06.2024
 */

@Controller
@RequestMapping("/api/admin/add")
public class AddController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private ItemRepository itemRepository;


    @GetMapping("/category")
    public String addCategory(Model model) {
        model.addAttribute("category", new Category());
        return "addCategory";
    }

    @PostMapping("/category")
    public String addCategory(Category category) {
        if (categoryRepository.findByName(category.getName()) != null) {
            return "redirect:/api/admin/add/category?error=name";
        }
        categoryRepository.save(category);
        return "redirect:/api/admin/dashboard";
    }

    @GetMapping("/discount")
    public String addDiscount(Model model) {
        model.addAttribute("discount", new Discount());
        return "addDiscount";
    }

    @PostMapping("/discount")
    public String addDiscount(Discount discount) {
        if (discountRepository.findByCode(discount.getCode()) != null) {
            return "redirect:/api/admin/add/discount?error=code";
        }
        if (discount.getValidFrom().isBefore(LocalDate.now())) {
            return "redirect:/api/admin/add/discount?error=validFrom";
        }
        if (discount.getValidUntil().isBefore(discount.getValidFrom())) {
            return "redirect:/api/admin/add/discount?error=validUntil";
        }
        discountRepository.save(discount);
        return "redirect:/api/admin/dashboard";
    }

    @GetMapping("/item")
    public String addItem(Model model ) {
        model.addAttribute("item", new Item());
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        return "addItem";
    }

    @SneakyThrows
    @PostMapping("/item")
    public String addItem(Item item,  @RequestParam("imageData") MultipartFile image) {
        if (!image.isEmpty()) {
            BufferedImage bufferedImage = ImageIO.read(image.getInputStream());

            //TODO if image is higher rather than wider, it gets rotated by 90 degrees, FIX IT but now im too lazy
            int angle = bufferedImage.getHeight() > bufferedImage.getWidth() ? 90 : 0;

            //bufferedImage = simpleResizeImage(bufferedImage, 200, 200);
            bufferedImage = rotateImage(bufferedImage, 90);
            bufferedImage = resizeAndCropImage(bufferedImage, 200, 200);
            byte[] imageBytes = imageToByteArray(bufferedImage, image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf(".") + 1));
            item.setImage(imageBytes);

        } else {
            item.setImage(new byte[0]);
        }

        item.setCreatedAt(LocalDateTime.now());
        item.setUpdatedAt(LocalDateTime.now());

        itemRepository.save(item);
        return "redirect:/api/admin/dashboard";
    }

}
