package com.example.merchstore.controllers.admin;

import com.example.merchstore.LocaleConfig;
import com.example.merchstore.components.enums.Language;
import com.example.merchstore.components.models.*;
import com.example.merchstore.repositories.*;
import com.example.merchstore.services.GlobalAttributeService;
import com.example.merchstore.services.LatestExchangeRateService;
import com.example.merchstore.services.TranslationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.merchstore.components.utilities.ImageProcessor.*;

/**
 * The ItemController_a class handles the web requests related to items in the admin section of the application.
 *
 * It has seven methods:
 * <ul>
 *     <li>addItem(Model model): Prepares the model for adding a new item and returns the view name.</li>
 *     <li>addItem(Item item, MultipartFile image): Handles the POST request for adding a new item. It processes the image data, sets the item's properties, saves the item, and redirects to the admin dashboard.</li>
 *     <li>viewItems(HttpServletRequest request, Long categoryId, int page, int size, String search, Model model): Handles the GET request for viewing items. It retrieves the items based on the search parameter, category, and pagination, adds them to the model, then returns the view name.</li>
 *     <li>viewItem(HttpServletRequest request, Long id, Model model): Handles the GET request for viewing a single item. It retrieves the item and adds it to the model, then returns the view name.</li>
 *     <li>updateItemForm(Long id, Model model): Prepares the model for updating an item and returns the view name.</li>
 *     <li>updateItem(Item item, MultipartFile image, Long categoryId): Handles the POST request for updating an item. It validates the item data, updates the item's properties, saves the item, and redirects to the items view.</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 03.06.2024
 */


@Controller
@RequestMapping("/api/admin")
public class ItemController_a {

    /**
     * The CategoryRepository that this controller uses to perform CRUD operations on categories.
     * @see CategoryRepository
     */
    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * The ItemRepository that this controller uses to perform CRUD operations on items.
     * @see ItemRepository
     */
    @Autowired
    private ItemRepository itemRepository;

    /**
     * The CurrencyRepository that this controller uses to perform CRUD operations on currencies.
     * @see CurrencyRepository
     */
    @Autowired
    private CurrencyRepository currencyRepository;

    /**
     * The LatestExchangeRateService that this controller uses to get the latest exchange rate for a currency.
     * @see LatestExchangeRateService
     */
    @Autowired
    private LatestExchangeRateService latestExchangeRateService;

    @Autowired
    private AttributeTypeRepository attributeTypeRepository;

    @Autowired
    private AttributesRepository attributesRepository;

    @Autowired
    private TranslationService translationService;

    @Autowired
    private LocaleConfig localeConfig;

    /**
     * Prepares the model for adding a new item and returns the view name.
     *
     * @param model The model to be prepared.
     * @return The view name.
     */
    @GetMapping("/add/item")
    public String addItem(Model model) {
        model.addAttribute("item", new Item());
        List<Category> categories = categoryRepository.findAll();
        List<AttributeType> attributeTypes = attributeTypeRepository.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("attributeTypes", attributeTypes);
        return "admin/add/addItem";
    }

    /**
     * Handles the POST request for adding a new item. It processes the image data, sets the item's properties, saves the item, and redirects to the admin dashboard.
     *
     * @param item The item to be added.
     * @param image The image data for the item.
     * @return The redirect URL.
     */
    @SneakyThrows
    @PostMapping("/add/item")
    public String addItem(Item item,
                          @RequestParam("imageData") MultipartFile image,
                          @RequestParam("attributeType[]") String[] attributeTypes,
                          @RequestParam("attributeValue[]") String[] attributeValues,
                          @RequestParam("language_iso") String languageIso) {
        if (!image.isEmpty()) {
            BufferedImage bufferedImage = ImageIO.read(image.getInputStream());

            int angle = bufferedImage.getHeight() > bufferedImage.getWidth() ? 90 : 0;

            bufferedImage = rotateImage(bufferedImage, angle);
            bufferedImage = resizeAndCropImage(bufferedImage, 200, 200);
            byte[] imageBytes = imageToByteArray(bufferedImage, image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf(".") + 1));
            item.setImage(imageBytes);
        } else {
            item.setImage(new byte[0]);
        }

        item.setCreatedAt(LocalDateTime.now());
        item.setCurrency(currencyRepository.findById(1L).orElse(null));
        item.setUpdatedAt(LocalDateTime.now());
        item.setLanguage(Language.fromCode(languageIso));

        item = itemRepository.save(item);

        for (int i = 0; i < attributeTypes.length; i++) {
            String attributeTypeName = attributeTypes[i];
            String attributeValue = attributeValues[i];

            AttributeType attributeType = attributeTypeRepository.findByName(attributeTypeName);
            if (attributeType == null) {
                attributeType = new AttributeType();
                attributeType.setName(attributeTypeName);
                attributeType = attributeTypeRepository.save(attributeType);
            }

            Attribute attribute = new Attribute();
            attribute.setAttributeType(attributeType);
            attribute.setValue(attributeValue);
            attribute.setItem(item);
            attributesRepository.save(attribute);
        }

        return "redirect:/api/admin/dashboard";
    }

    /**
     * Handles the GET request for viewing items. It retrieves the items based on the search parameter, category, and pagination, adds them to the model, then returns the view name.
     *
     * @param request The HttpServletRequest object.
     * @param categoryId The ID of the category to filter the items by.
     * @param page The page number.
     * @param size The number of items per page.
     * @param search The search parameter.
     * @param model The model to be prepared.
     * @return The view name.
     */
    @GetMapping("/view/items")
    public String viewItems(HttpServletRequest request, @RequestParam(value = "category", required = false) Long categoryId,
                            @RequestParam(value = "page", defaultValue = "0") int page,
                            @RequestParam(value = "size", defaultValue = "10") int size,
                            @RequestParam(value = "searchItem", required = false) String search,
                            @RequestParam(required = false) String lang,Model model) {

        Language language = localeConfig.getCurrentLanguage();


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

        Currency currency = currencyRepository.findById(1L).orElse(null);
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("currency")) {
                    currency = currencyRepository.findByShortName(cookie.getValue());
                }
            }
        }

        ExchangeRate exchangeRate = latestExchangeRateService.getLatestExchangeRateForCurrency(currency.getId());

        model.addAttribute("currency", currency);
        model.addAttribute("exchangeRate", exchangeRate);
        HashMap<Long, Language> originalLanguages = new HashMap<>();
        List<Item> translatedItems = new ArrayList<>();
        for (Item item : items) {
            originalLanguages.put(item.getItemId(), item.getLanguage());
            Item translatedItem = (Item) translationService.translate(item, language);
            translatedItems.add(translatedItem);
        }

        model.addAttribute("language", language);
        model.addAttribute("original_languages", originalLanguages);
        model.addAttribute("items", translatedItems);
        model.addAttribute("totalPages", items.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("searchItem", search);
        return "admin/view/viewItems";
    }

    /**
     * Handles the GET request for viewing a single item. It retrieves the item and adds it to the model, then returns the view name.
     *
     * @param request The HttpServletRequest object.
     * @param id The ID of the item to view.
     * @param model The model to be prepared.
     * @return The view name.
     */
    @GetMapping("/view/item")
    public String viewItem(HttpServletRequest request, @RequestParam Long id,
                           @RequestParam(required = false) String lang, Model model) {


        Item item = itemRepository.findById(id).orElse(null);
        if (item == null) {
            return "redirect:/api/admin/view/items";
        }
        Currency currency = currencyRepository.findById(1L).orElse(null);
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("currency")) {
                    currency = currencyRepository.findByShortName(cookie.getValue());
                }
            }
        }

        ExchangeRate exchangeRate = latestExchangeRateService.getLatestExchangeRateForCurrency(currency.getId());

        model.addAttribute("currency", currency);
        model.addAttribute("exchangeRate", exchangeRate);

        List<Attribute> attributes = attributesRepository.findAllByItem(item);
        model.addAttribute("attributes", attributes);

        Language language = localeConfig.getCurrentLanguage();

        Language originalLanguage = item.getLanguage();
        Item translatedItem = (Item) translationService.translate(item, language);
        model.addAttribute("original_language", originalLanguage);
        model.addAttribute("language", language);
        model.addAttribute("item", translatedItem);
        return "admin/view/viewItem";
    }

    /**
     * Prepares the model for updating an item and returns the view name.
     *
     * @param id The ID of the item to update.
     * @param model The model to be prepared.
     * @return The view name.
     */
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

    /**
     * Handles the POST request for updating an item. It validates the item data, updates the item's properties, saves the item, and redirects to the items view.
     *
     * @param item The item to be updated.
     * @param image The image data for the item.
     * @param categoryId The ID of the category to update the item with.
     * @return The redirect URL.
     */
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
