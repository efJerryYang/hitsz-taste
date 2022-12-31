package me.efjerryyang.webserver.controller;

import me.efjerryyang.webserver.model.Dish;
import me.efjerryyang.webserver.model.Merchant;
import me.efjerryyang.webserver.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SearchController {
    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);
    private final CafeteriaService cafeteriaService;
    private final MerchantService merchantService;
    private final DishService dishService;
    private final ValidationService validationService;
    private final ContractService contractService;

    @Autowired
    public SearchController(CafeteriaService cafeteriaService, DishService dishService, ValidationService validationService, MerchantService merchantService, ContractService contractService) {
        this.cafeteriaService = cafeteriaService;
        this.dishService = dishService;
        this.validationService = validationService;
        this.merchantService = merchantService;
        this.contractService = contractService;
    }

    @GetMapping("/search")
    public String search(@RequestParam("query") String query, Model model) {
        String updatedQuery;
        if (!validationService.isJavascriptEnabled()) {
            try {
                updatedQuery = validationService.sanitizeSearchQuery(query);
            } catch (IllegalArgumentException e) {
                model.addAttribute("error", e.getMessage());
                return "home";
            }
            if (!updatedQuery.equals(query)) {
                model.addAttribute("error", "Invalid special characters in search query");
                return "home";
            }
        }
        List<Dish> searchResults = dishService.searchDishes(query);
        model.addAttribute("query", query);
        model.addAttribute("searchResults", searchResults);
        System.out.printf(searchResults.toString());
        return "home";
    }

    @GetMapping("/filter")
    public String filter(@RequestParam(value = "cafeteria", required = false) Long cafeteriaId, @RequestParam(value = "merchant", required = false) Long merchantId, Model model) {
        // Retrieve the list of merchants from the database
        List<Merchant> merchants = merchantService.getAll();

        // If a cafeteria and merchant are selected, filter the list of merchants by both cafeteria and merchant
        if (cafeteriaId != null && merchantId != null) {
            merchants = merchants.stream()
                    .filter(merchant -> cafeteriaService.getAllByIds(contractService.getCafeteriaIdsByMerchantId(merchant.getMerchantId())).contains(cafeteriaService.getById(cafeteriaId))
                            && merchant.getMerchantId().equals(merchantId))
                    .collect(Collectors.toList());
        }
        // If only a cafeteria is selected, filter the list of merchants by cafeteria
        else if (cafeteriaId != null) {
            merchants = merchants.stream()
                    .filter(merchant -> cafeteriaService.getAllByIds(contractService.getCafeteriaIdsByMerchantId(merchant.getMerchantId())).contains(cafeteriaService.getById(cafeteriaId)))
                    .collect(Collectors.toList());
        }
        // If only a merchant is selected, filter the list of merchants by merchant
        else if (merchantId != null) {
            merchants = merchants.stream().filter(merchant -> merchant.getMerchantId().equals(merchantId)).collect(Collectors.toList());
        }

        // Add the filtered list of merchants to the model
        model.addAttribute("merchants", merchants);
        // Render the template with the filtered list of merchants
        return "home";
    }

}

