package com.example.merchstore.controllers.admin;

import com.example.merchstore.model.Category;
import com.example.merchstore.model.Item;
import com.example.merchstore.repositories.CategoryRepository;
import com.example.merchstore.repositories.ItemRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.merchstore.model.ImageProcessor.*;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 03.06.2024
 */


@Controller
@RequestMapping("/api/admin")
public class ItemController_a {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ItemRepository itemRepository;

    @GetMapping("/add/item")
    public String addItem(Model model) {
        model.addAttribute("item", new Item());
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        return "admin/add/addItem";
    }

    @SneakyThrows
    @PostMapping("/add/item")
    public String addItem(Item item, @RequestParam("imageData") MultipartFile image) {
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
        for (int i = 0; i < 100; i++) {
            itemRepository.save(new Item(null, item.getName() + String.valueOf(i), item.getDescription() + String.valueOf(i), item.getPrice(), item.getStockQuantity() + i, item.getCategory(), item.getCreatedAt(), item.getUpdatedAt(), item.getImage()));
        }
        return "redirect:/api/admin/dashboard";
    }

    @GetMapping("/view/items")
    public String viewItems(@RequestParam(value = "category", required = false) Long categoryId,
                            @RequestParam(value = "page", defaultValue = "0") int page,
                            @RequestParam(value = "size", defaultValue = "10") int size,
                            Model model) {
        Page<Item> items;
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId).orElse(null);
            items = category != null ? itemRepository.findByCategory(category, PageRequest.of(page, size)) : Page.empty();
        } else {
            items = itemRepository.findAll(PageRequest.of(page, size));
        }
        model.addAttribute("items", items.getContent());
        model.addAttribute("totalPages", items.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("selectedCategoryId", categoryId);
        return "admin/view/viewItems";
    }

    @GetMapping("/view/item")
    public String viewItem(@RequestParam Long id, Model model) {
        Item item = itemRepository.findById(id).orElse(null);
        if (item == null) {
            return "redirect:/api/admin/view/items";
        }
        model.addAttribute("item", item);
        return "admin/view/viewItem";
    }
}
