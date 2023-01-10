package me.efjerryyang.webserver.controller;

import jakarta.servlet.http.HttpSession;
import me.efjerryyang.webserver.model.Dish;
import me.efjerryyang.webserver.model.Merchant;
import me.efjerryyang.webserver.model.Order;
import me.efjerryyang.webserver.model.User;
import me.efjerryyang.webserver.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MerchantController {
    private static final Logger logger = LoggerFactory.getLogger(MerchantController.class);
    private final MerchantService merchantService;
    private final UserService userService;
    private final UserRoleService userRoleService;
    private final OrderService orderService;
    private final DishService dishService;
    private final MerchantUserService merchantUserService;
    @Autowired
    private HttpSession session;

    private List<Dish> dishList;
    private List<Order> orderList;

    @Autowired
    public MerchantController(MerchantService merchantService, UserService userService, UserRoleService userRoleService, OrderService orderService, DishService dishService, MerchantUserService merchantUserService) {
        this.merchantService = merchantService;
        this.userService = userService;
        this.userRoleService = userRoleService;
        this.orderService = orderService;
        this.dishService = dishService;
        this.merchantUserService = merchantUserService;
    }

    @GetMapping("/dashboard/merchant")
    public String merchant(Model model) {
        if (session.getAttribute("username") != null) {
            String username = session.getAttribute("username").toString();
            model.addAttribute("username", username);
            User user = userService.getByUsername(username);
            Merchant merchant = null;

            try {
                merchant = merchantService.getById(merchantUserService.getMerchantIdByUserId(user.getUserId()));
                if (merchant == null) {
                    logger.error("Not a merchant role of user: {}", username);
                    return "redirect:/dashboard";
                }
            } catch (Exception e) {
                logger.error("Error retrieve merchant: {}", e.getMessage());
                return "redirect:/dashboard";
            }
            if (merchantUserService.getMerchantIdByUserId(user.getUserId()) != null) {
//                model.addAttribute("merchant", merchant);
                model.addAttribute("merchantName", merchant.getName());
                // TODO: to be fixed, current order structure is not correct, merchants received should be suborders
                //  Cart (customer) -> order (cafeteria) -> suborder (merchant)
                //  So now restrict the order to only contains one merchant
                dishList = dishService.getAllByMerchantId(merchant.getMerchantId());
                orderList = orderService.getAllByMerchantId(merchant.getMerchantId());
                model.addAttribute("dishList", dishList);
                model.addAttribute("pendingOrderList", orderList.stream().filter(order -> order.getStatus().equals("pending")).toList());
                model.addAttribute("processingOrderList", orderList.stream().filter(order -> order.getStatus().equals("processing")).toList());
                model.addAttribute("completedOrderList", orderList.stream().filter(order -> order.getStatus().equals("completed")).toList());
                return "dashboard_merchant";
            } else {
                return "redirect:/dashboard";
            }
        }
        return "dashboard_merchant";
    }
}
