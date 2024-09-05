package com.example.merchstore.controllers.admin;

import com.example.merchstore.components.models.Category;
import com.example.merchstore.repositories.CategoryRepository;
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
import java.util.List;

import static com.example.merchstore.components.utilities.ImageProcessor.*;

/**
 * The CategoryController_a class handles the web requests related to categories in the admin section of the application.
 *
 * It has three methods:
 * <ul>
 *     <li>addCategory(Model model): Prepares the model for adding a new category and returns the view name.</li>
 *     <li>addCategory(Category category, MultipartFile image): Handles the POST request for adding a new category. It processes the image data, sets the category's properties, saves the category, and redirects to the admin dashboard.</li>
 *     <li>getAllCategories(String search, Model model): Handles the GET request for viewing categories. It retrieves the categories based on the search parameter and adds them to the model, then returns the view name.</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 03.06.2024
 */

@Controller
@RequestMapping("/api/admin")
public class CategoryController_a {


    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/add/category")
    public String addCategory(Model model) {
        model.addAttribute("category", new Category());
        return "admin/add/addCategory";
    }

    @SneakyThrows
    @PostMapping("/add/category")
    public String addCategory(Category category, @RequestParam("imageData") MultipartFile image) {
        if (categoryRepository.findByName(category.getName()) != null) {
            return "redirect:/api/admin/add/category?error=name";
        }

        if (!image.isEmpty()) {
            BufferedImage bufferedImage = ImageIO.read(image.getInputStream());

            int angle = bufferedImage.getHeight() > bufferedImage.getWidth() ? 90 : 0;

            bufferedImage = rotateImage(bufferedImage, angle);
            bufferedImage = resizeAndCropImage(bufferedImage, 200, 200);
            byte[] imageBytes = imageToByteArray(bufferedImage, image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf(".") + 1));
            category.setImage(imageBytes);

        } else {
            category.setImage(new byte[0]);
        }

        categoryRepository.save(category);
        return "redirect:/api/admin/dashboard";
    }

    @GetMapping("/view/categories")
    public String getAllCategories(@RequestParam(value = "searchCat", required = false) String search, Model model) {
        List<Category> categories;
        if (search != null && !search.isEmpty()) {
            categories = categoryRepository.findByNameStartingWithIgnoreCase(search);
        } else {
            categories = categoryRepository.findAll();
        }
        model.addAttribute("categories", categories);
        model.addAttribute("searchCat", search);
        return "admin/view/viewCategories";
    }


}
