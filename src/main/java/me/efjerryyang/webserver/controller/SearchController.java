package me.efjerryyang.webserver.controller;

import jakarta.servlet.http.HttpSession;
import me.efjerryyang.webserver.model.Dish;
import me.efjerryyang.webserver.service.*;
import me.efjerryyang.webserver.view.BaseView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchController {
    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);
    private final CafeteriaService cafeteriaService;
    private final MerchantService merchantService;
    private final DishService dishService;
    private final ValidationService validationService;
    private final ContractService contractService;
    private final FilterService filterService;
    @Autowired
    private HttpSession session;
    private List<Dish> dishList = null;

    @Autowired
    public SearchController(CafeteriaService cafeteriaService, DishService dishService, ValidationService validationService, MerchantService merchantService, ContractService contractService, FilterService filterService) {
        this.cafeteriaService = cafeteriaService;
        this.dishService = dishService;
        this.validationService = validationService;
        this.merchantService = merchantService;
        this.contractService = contractService;
        this.filterService = filterService;
    }

    @GetMapping("/home/search")
    public String search(@RequestParam("query") String query, Model model) {
        String updatedQuery;
        logger.info("query= {}", query);
        if (!validationService.isJavascriptEnabled()) {
            try {
                updatedQuery = validationService.sanitizeSearchQuery(query);
            } catch (IllegalArgumentException e) {
                model.addAttribute("error", e.getMessage());
                return "home";
            }
            if (!updatedQuery.equals(query)) {
                model.addAttribute("error", "Invalid special characters in search query");
                model.addAttribute("query", updatedQuery);
                return "home";
            }
        }
        dishList = dishService.searchDishes(query);
        model.addAttribute("query", query);
        // TODO: return how many rows of query result
        System.out.println(dishList.toString());
        List<BaseView> filterResult;
        filterResult = filterService.createViewList(dishList, merchantService.getAllByDishIds(filterService.getDishIds(dishList)), contractService.getAll());
        session.setAttribute("filterResult", filterResult);
        model.addAttribute("filterResult", filterResult);
        updateModelWithSession(model);
        return "home";
    }

    @GetMapping("/home/filter")
    public String filter(@RequestParam(value = "cafeteria", required = false) Long cafeteriaId, @RequestParam(value = "merchant", required = false) Long merchantId, Model model) {
        if (dishList == null) {
            model.addAttribute("error", "No search results to filter");
            return "home";
        }
        List<BaseView> filterResult;
        filterResult = filterService.createViewList(dishList, merchantService.getAllByDishIds(filterService.getDishIds(dishList)), contractService.getAll());
        session.setAttribute("filterResult", filterResult);
        model.addAttribute("filterResult", filterResult);
        updateModelWithSession(model);
        return "home";
    }

    private void updateModelWithSession(Model model) {
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("order", session.getAttribute("editingOrder"));
    }
    // TODO: sort result by price/merchant name/cafeteria name/dish name
}

