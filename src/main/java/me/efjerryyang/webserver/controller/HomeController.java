package me.efjerryyang.webserver.controller;

import jakarta.servlet.http.HttpSession;
import me.efjerryyang.webserver.model.Dish;
import me.efjerryyang.webserver.model.Order;
import me.efjerryyang.webserver.model.OrderItem;
import me.efjerryyang.webserver.model.User;
import me.efjerryyang.webserver.service.DishService;
import me.efjerryyang.webserver.service.OrderService;
import me.efjerryyang.webserver.service.UserService;
import me.efjerryyang.webserver.service.ValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
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
    @Autowired
    private ValidationService validationService;

    @GetMapping("/home")
    public String home(Model model) {
        logger.info("HomeController.home() called");
        if (session.getAttribute("username") != null) {
            session.setAttribute("isLoggedIn", true);
            session.setAttribute("lastActivity", System.currentTimeMillis());
            model.addAttribute("username", session.getAttribute("username"));
            User user = userService.getByUsername((String) session.getAttribute("username"));
            if (user.getFirstname() != null && user.getLastname() != null) {
                if (validationService.isChineseFirstnameOrLastname(user.getFirstname()) && validationService.isChineseFirstnameOrLastname(user.getLastname())) {
                    model.addAttribute("name", user.getLastname() + user.getFirstname());
                } else {
                    model.addAttribute("name", user.getFirstname() + " " + user.getLastname());
                }
            }
            if (orderItemList == null) {
                orderItemList = new ArrayList<>();
                logger.info("Created new orderItemList");
            }
            if (dishMap == null) {
                dishMap = new HashMap<>();
                logger.info("Created new dishMap");
            }
            session.setAttribute("orderItemList", orderItemList);
            session.setAttribute("dishMap", dishMap);
        }
        if (session.getAttribute("editingOrder") == null) {
            order = new Order();
            order.setOrderId(orderService.getNextId());
            order.setTotalPrice(0.0f);
            session.setAttribute("editingOrder", order);
            logger.info("Created new order");
        } else {
            if (dishMap != null && dishMap.isEmpty() && session.getAttribute("username") != null) {
                order.setUserId(userService.getByUsername((String) session.getAttribute("username")).getUserId());
                logger.info("The first time to load the order with a valid username");
                initializeOrderWithCheckoutForm();
            }
            order = (Order) session.getAttribute("editingOrder");
            System.out.println(order);
        }

        model.addAttribute("filterResult", session.getAttribute("filterResult"));
        model.addAttribute("order", order);
        model.addAttribute("orderItemList", orderItemList);
        model.addAttribute("dishMap", dishMap);
        if (session.getAttribute("errorMessage") != null) {
            model.addAttribute("errorCheckout", session.getAttribute("errorMessage"));
            session.removeAttribute("errorMessage");
        }
        return "home";
    }

    private void initializeOrderWithCheckoutForm() {
        logger.info("HomeController.initializeOrderWithCheckoutForm() called");
        String username = null;
        try {
            username = (String) session.getAttribute("username");
            logger.info("username: {}", username);
        } catch (NullPointerException nullPointerException) {
            logger.error("Error retrieve 'username' attribute from session: {}", nullPointerException.getMessage());
        }
        // initialize username, address, contact with default value
        if (username != null && !username.isEmpty()) {
            user = userService.getByUsername(username);
            order.setUserId(user.getUserId());
            logger.info("order.getUserId(): {}", order.getUserId());
            if (user.getAddress() != null && !user.getAddress().isEmpty()) {
                order.setAddress(user.getAddress());
                logger.info("order.getAddress(): {}", order.getAddress());
            }
            order.setContact(user.getPhone());
            logger.info("order.getContact(): {}", order.getContact());
            order.setStatus("pending");
            logger.info("order.getStatus(): {}", order.getStatus());
        }
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
        session.setAttribute("editingOrder", order);
        session.setAttribute("orderItemList", orderItemList);
        session.setAttribute("dishMap", dishMap);
        model.addAttribute("orderItemList", orderItemList);
        model.addAttribute("dishMap", dishMap);
        return "redirect:/home";
    }

    @PostMapping("/home/updateOrder")
    public String updateOrder(@RequestParam Long dishId, @RequestParam Long quantity, Model model) {
        logger.info("HomeController.order() called");
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }
        try {
            order = (Order) session.getAttribute("editingOrder");
        } catch (NullPointerException nullPointerException) {
            logger.error("Error retrieve 'editingOrder' attribute from session with null pointer: {}", nullPointerException.getMessage());
        }
        logger.info("Dish id selected: " + dishId + ", quantity: " + quantity);
        if (dishMap.containsKey(dishId)) {
            logger.info("Dish already in map");
        } else {
            logger.info("Dish not in map, adding to map");
            dishMap.put(dishId, dishService.getById(dishId));
        }
        // create order item and update selected one
        OrderItem orderItem = new OrderItem();
        orderItem.setDishId(dishId);
        orderItem.setOrderId(order.getOrderId());
        orderItem.setQuantity(quantity);
        boolean isDishExisted = false;
        float total = 0;
        for (int i = 0; i < orderItemList.size(); i++) {
            if (Objects.equals(orderItemList.get(i).getDishId(), dishId)) {
                orderItemList.get(i).setQuantity(quantity);
                isDishExisted = true;
            }
            total += dishService.getPrice(orderItemList.get(i).getDishId()) * orderItemList.get(i).getQuantity();
        }
        if (!isDishExisted) {
            orderItemList.add(orderItem);
            total += dishService.getPrice(dishId) * quantity;
        }
        order.setTotalPrice(total);
        session.setAttribute("editingOrder", order);
        session.setAttribute("orderItemList", orderItemList);
        session.setAttribute("dishMap", dishMap);
        model.addAttribute("orderItemList", orderItemList);
        model.addAttribute("dishMap", dishMap);
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
    public String checkout(@RequestParam String name, @RequestParam String contact, @RequestParam String address, Model model) {
        logger.info("HomeController.checkout() called");
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }
        try {
            order = (Order) session.getAttribute("editingOrder");
        } catch (NullPointerException nullPointerException) {
            logger.error("Error retrieve 'editingOrder' attribute from session with null pointer: {}", nullPointerException.getMessage());
        }
        if (order.getTotalPrice() < 0.0 + 1e2) {
            logger.error("No dish selected");
            session.setAttribute("errorMessage", "No dish selected");
            return "redirect:/home";
        }
        // 理论上这里应该给 order 加一个 name 属性，但是我不想重写数据库了
        // 所以我直接摆烂了，不处理姓名异常的情况
        // TODO: add an attribute 'name' to order
//        order.setCustomerName(name);
        order.setContact(contact);
        order.setAddress(address);
        session.setAttribute("editingOrder", order);
        logger.info("Post order: {}", order);
        if (!validationService.isName(name)) {
            logger.info("Invalid name");
            session.setAttribute("errorMessage", "Invalid name");
            return "redirect:/home";
        } else if (!(validationService.isPhone(contact) || validationService.isEmail(contact))) {
            logger.info("Invalid contact");
            session.setAttribute("errorMessage", "Invalid phone or email");
            return "redirect:/home";
        } else if (!validationService.isAddress(address)) {
            logger.info("Invalid address");
            session.setAttribute("errorMessage", "Invalid address");
            return "redirect:/home";
        }
        // save order
        // TODO: separate orders for different cafeterias
        //  Possible solution is to separate an order with suborders, but this will cause an update to the database
        //  And a Cart is necessary now, because order should be responsible for an merchant or cafeteria
        //  Cart (customer) -> order (cafeteria) -> suborder (merchant)
        //  A customer may order food in different cafeteria, so a cart should create multiple orders if contains dish in different cafeterias; then suborders are distributed to merchants
        //  In this way, a createAt may belong to multiple orders of the same customer
        try {
            order.setCreateAt(new Timestamp(System.currentTimeMillis()));
            orderService.create(order);
            logger.info("Order created: {}", order);
        } catch (Exception e) {
            logger.error("Error saving order: {}", e.getMessage());
            session.setAttribute("errorMessage", "Error saving order");
            return "redirect:/home";
        }

        // remove session attributes in home controller and search controller (all in home page)
        session.removeAttribute("orderItemList"); // HomeController
        session.removeAttribute("dishMap");
        session.removeAttribute("editingOrder");
        session.removeAttribute("filterResult"); // SearchController
        return "redirect:/orders";
    }
}
