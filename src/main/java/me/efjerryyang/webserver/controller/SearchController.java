package me.efjerryyang.webserver.controller;

import me.efjerryyang.webserver.model.Dish;
import me.efjerryyang.webserver.service.DishService;
import me.efjerryyang.webserver.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchController {
    private DishService dishService;
    private ValidationService validationService;

    @Autowired
    public SearchController(DishService dishService, ValidationService validationService) {
        this.dishService = dishService;
        this.validationService = validationService;
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
}

