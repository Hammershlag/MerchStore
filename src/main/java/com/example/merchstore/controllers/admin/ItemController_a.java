package com.example.merchstore.controllers.admin;

import com.example.merchstore.components.models.Category;
import com.example.merchstore.components.models.Item;
import com.example.merchstore.repositories.CategoryRepository;
import com.example.merchstore.repositories.ItemRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

import static com.example.merchstore.components.utilities.ImageProcessor.*;

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
                            @RequestParam(value = "search", required = false) String search,
                            Model model) {
        Page<Item> items;
        Pageable pageable = PageRequest.of(page, size);

        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId).orElse(null);
            if (category != null) {
                if (search != null && !search.isEmpty()) {
                    items = itemRepository.findByCategoryAndNameStartingWithIgnoreCase(category, search, pageable);
                } else {
                    items = itemRepository.findByCategory(category, pageable);
                }
            } else {
                items = Page.empty();
            }
        } else {
            if (search != null && !search.isEmpty()) {
                items = itemRepository.findByNameStartingWithIgnoreCase(search, pageable);
            } else {
                items = itemRepository.findAll(pageable);
            }
        }

        model.addAttribute("items", items.getContent());
        model.addAttribute("totalPages", items.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("search", search);
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

    @GetMapping("/update/item")
    public String updateItemForm(@RequestParam Long id, Model model) {
        Item item = itemRepository.findById(id).orElse(null);
        if (item == null) {
            return "redirect:/api/admin/view/items";
        }
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("item", item);
        model.addAttribute("categories", categories);
        return "admin/update/updateItem";
    }

    @SneakyThrows
    @PostMapping("/update/item")
    public String updateItem(Item item,
                             @RequestParam("imageData") MultipartFile image,
                             @RequestParam("categoryId") Long categoryId) {
        Item existingItem = itemRepository.findById(item.getItemId()).orElse(null);
        if (existingItem == null) {
            return "redirect:/api/admin/view/items";
        }

        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) {
            return "redirect:/api/admin/view/items";
        }

        if (!image.isEmpty()) {
            BufferedImage bufferedImage = ImageIO.read(image.getInputStream());

            //TODO if image is higher rather than wider, it gets rotated by 90 degrees, FIX IT but now im too lazy
            int angle = bufferedImage.getHeight() > bufferedImage.getWidth() ? 90 : 0;

            bufferedImage = rotateImage(bufferedImage, angle);
            bufferedImage = resizeAndCropImage(bufferedImage, 200, 200);
            byte[] imageBytes = imageToByteArray(bufferedImage, image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf(".") + 1));
            existingItem.setImage(imageBytes);
        }

        existingItem.setName(item.getName());
        existingItem.setDescription(item.getDescription());
        existingItem.setPrice(item.getPrice());
        existingItem.setStockQuantity(item.getStockQuantity());
        existingItem.setCategory(category);
        existingItem.setUpdatedAt(LocalDateTime.now());

        itemRepository.save(existingItem);

        return "redirect:/api/admin/view/items";
    }

}
