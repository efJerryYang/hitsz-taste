package me.efjerryyang.webserver.controller;

import jakarta.servlet.http.HttpSession;
import me.efjerryyang.webserver.model.Dish;
import me.efjerryyang.webserver.model.Order;
import me.efjerryyang.webserver.model.OrderItem;
import me.efjerryyang.webserver.model.User;
import me.efjerryyang.webserver.service.DishService;
import me.efjerryyang.webserver.service.OrderService;
import me.efjerryyang.webserver.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class HomeController {
    public static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    private Order order = null;
    private User user = null;
    private List<OrderItem> orderItemList;
    // maintain a dish map
    private Map<Long, Dish> dishMap;
    @Autowired
    private HttpSession session;
    @Autowired
    private OrderService orderService;
    @Autowired
    private DishService dishService;
    @Autowired
    private UserService userService;

    @GetMapping("/home")
    public String home(Model model) {
        logger.info("HomeController.home() called");
        if (session.getAttribute("username") != null) {
            session.setAttribute("isLoggedIn", true);
            session.setAttribute("lastActivity", System.currentTimeMillis());
            model.addAttribute("username", session.getAttribute("username"));
        }
        if (session.getAttribute("editingOrder") == null) {
            order = new Order();
            order.setOrderId(orderService.getNextId());
            order.setTotalPrice(0.0f);
            orderItemList = new ArrayList<>();
            dishMap = new HashMap<>();
            session.setAttribute("editingOrder", order);
            session.setAttribute("orderItemList", orderItemList);
            session.setAttribute("dishMap", dishMap);
        } else {
            order = (Order) session.getAttribute("editingOrder");
        }
        model.addAttribute("filterResult", session.getAttribute("filterResult"));
        model.addAttribute("order", order);
        model.addAttribute("orderItemList", orderItemList);
        model.addAttribute("dishMap", dishMap);
        return "home";
    }

    @PostMapping("/home/addDishToOrder")
    public String addDishToOrder(@RequestParam Long dishId, Model model) {
        logger.info("HomeController.addDishToOrder() called");
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }
        try {
            order = (Order) session.getAttribute("editingOrder");
        } catch (NullPointerException nullPointerException) {
            logger.error("Error retrieve 'editingOrder' attribute from session with null pointer: {}", nullPointerException.getMessage());
        }
        // add dish to map
        logger.info("Dish id selected: " + dishId);
        if (dishMap.containsKey(dishId)) {
            logger.info("Dish already in map");
        } else {
            logger.info("Dish not in map, adding to map");
            dishMap.put(dishId, dishService.getById(dishId));
        }
        // create order item and add to list
        OrderItem orderItem = new OrderItem();
        orderItem.setDishId(dishId);
        orderItem.setOrderId(order.getOrderId());
        orderItem.setQuantity(1L);
        boolean isDishExisted = false;
        float total = 0;
        for (int i = 0; i < orderItemList.size(); i++) {
            if (Objects.equals(orderItemList.get(i).getDishId(), dishId)) {
                orderItemList.get(i).setQuantity(orderItemList.get(i).getQuantity() + 1);
                isDishExisted = true;
            }
            total += dishService.getPrice(orderItemList.get(i).getDishId()) * orderItemList.get(i).getQuantity();
        }
        if (!isDishExisted) {
            orderItemList.add(orderItem);
            total += dishService.getPrice(dishId);
        }
        order.setTotalPrice(total);
        String username = null;
        try {
            username = (String) session.getAttribute("username");
        } catch (NullPointerException nullPointerException) {
            logger.error("Error retrieve 'username' attribute from session: {}", nullPointerException.getMessage());
        }
        // initialize username, address, contact with default value
        if (username != null && !username.isEmpty()) {
            user = userService.getByUsername(username);
            order.setUserId(user.getUserId());
            if (user.getAddress() != null && !user.getAddress().isEmpty()) {
                order.setAddress(user.getAddress());
            }
            order.setContact(user.getPhone());
            order.setStatus("pending");
        }
        session.setAttribute("editingOrder", order);
        session.setAttribute("orderItemList", orderItemList);
        session.setAttribute("dishMap", dishMap);
        model.addAttribute("orderItemList", orderItemList);
        model.addAttribute("dishMap", dishMap);
        return "redirect:/home";
    }

    @PostMapping("/home/updateOrder")
    public String updateOrder() {
        logger.info("HomeController.order() called");

        return "redirect:/home";
    }


    @PostMapping("/home/removeDishFromOrder")
    public String removeDishFromOrder(@RequestParam Long dishId, Model model) {
        logger.info("HomeController.removeDishFromOrder() called");
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }
        try {
            order = (Order) session.getAttribute("editingOrder");
        } catch (NullPointerException nullPointerException) {
            logger.error("Error retrieve 'editingOrder' attribute from session with null pointer: {}", nullPointerException.getMessage());
        }
//        // remove dish from map
//        logger.info("Dish id selected: " + dishId);
//        if (dishMap.containsKey(dishId)) {
//            logger.info("Dish already in map, removing from map");
//            dishMap.remove(dishId);
//        } else {
//            logger.info("Dish not in map");
//        }

        // remove order item from list
        float total = 0;
        Long[] tempArray = new Long[orderItemList.size()];
        // set temp array to zeros
        Arrays.fill(tempArray, 0L);
        int tempIndex = 0;
        for (OrderItem orderItem : orderItemList) {
            if (Objects.equals(orderItem.getDishId(), dishId)) {
                tempArray[tempIndex++] = orderItem.getDishId();
            } else {
                total += dishService.getPrice(orderItem.getDishId()) * orderItem.getQuantity();
            }
        }
        order.setTotalPrice(total);
        for (Long orderId : tempArray) {
            if (orderId != 0) {
                orderItemList.removeIf(orderItem -> Objects.equals(orderItem.getDishId(), dishId));
                logger.info("Item removed: {}", dishMap.get(dishId));
            }
        }

        session.setAttribute("editingOrder", order);
        session.setAttribute("orderItemList", orderItemList);
        session.setAttribute("dishMap", dishMap);
        model.addAttribute("orderItemList", orderItemList);
        model.addAttribute("dishMap", dishMap);
        return "redirect:/home";
    }

    @PostMapping("/home/checkout")
    public String checkout() {
        logger.info("HomeController.checkout() called");
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }
        return "redirect:/home";
    }
}
