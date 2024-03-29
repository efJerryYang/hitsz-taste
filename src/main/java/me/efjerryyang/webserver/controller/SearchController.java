package me.efjerryyang.webserver.controller;

import jakarta.servlet.http.HttpSession;
import me.efjerryyang.webserver.model.Dish;
import me.efjerryyang.webserver.model.User;
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
    private final UserService userService;
    private final ValidationService validationService;
    private final ContractService contractService;
    private final FilterService filterService;
    private List<BaseView> filterResult;
    @Autowired
    private HttpSession session;
    private List<Dish> dishList = null;

    @Autowired
    public SearchController(CafeteriaService cafeteriaService, DishService dishService, ValidationService validationService, MerchantService merchantService, UserService userService, ContractService contractService, FilterService filterService) {
        this.cafeteriaService = cafeteriaService;
        this.dishService = dishService;
        this.validationService = validationService;
        this.merchantService = merchantService;
        this.userService = userService;
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
        filterResult = filterService.createViewList(dishList, merchantService.getAllByDishIds(filterService.getDishIds(dishList)), contractService.getAll());
        session.setAttribute("filterResult", filterResult);
        updateModelWithSession(model);
        return "home";
    }

    private void updateModelWithSession(Model model) {
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("order", session.getAttribute("editingOrder"));
        model.addAttribute("filterResult", session.getAttribute("filterResult"));
        if (session.getAttribute("username") != null) {
            User user = userService.getByUsername((String) session.getAttribute("username"));
            if (user.getFirstname() != null && user.getLastname() != null) {
                if (validationService.isChineseFirstnameOrLastname(user.getFirstname()) && validationService.isChineseFirstnameOrLastname(user.getLastname())) {
                    model.addAttribute("name", user.getLastname() + user.getFirstname());
                } else {
                    model.addAttribute("name", user.getFirstname() + " " + user.getLastname());
                }
            }
            model.addAttribute("orderItemList", session.getAttribute("orderItemList"));
            model.addAttribute("dishMap", session.getAttribute("dishMap"));
            System.out.println("OrderItemList: " + session.getAttribute("orderItemList"));
            System.out.println("DishMap: " + session.getAttribute("dishMap"));
        }
    }

    @GetMapping("/home/filter")
    public String filter(@RequestParam(value = "cafeteria", required = false) Long cafeteriaId, @RequestParam(value = "merchant", required = false) Long merchantId, Model model) {
        if (dishList == null) {
            model.addAttribute("error", "No search results to filter");
            return "home";
        }
        filterResult = filterService.createViewList(dishList, merchantService.getAllByDishIds(filterService.getDishIds(dishList)), contractService.getAll());
        session.setAttribute("filterResult", filterResult);
        updateModelWithSession(model);
        return "home";
    }
    // TODO: sort result by price/merchant name/cafeteria name/dish name
}

