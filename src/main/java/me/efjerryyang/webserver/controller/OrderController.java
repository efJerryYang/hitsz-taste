package me.efjerryyang.webserver.controller;

import jakarta.servlet.http.HttpSession;
import me.efjerryyang.webserver.model.Order;
import me.efjerryyang.webserver.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;

@Controller
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    @Autowired
    private HttpSession session;

    @Autowired
    private OrderService orderService;

    private List<Order> histOrderList;

    @GetMapping("/orders")
    public String loadOrder(){
        logger.info("OrderController.loadOrder() called");
        if (histOrderList == null) {
            histOrderList = orderService.getAll();
        }
        return "orders";
    }
    @PostMapping("/orders")
    public String showHistory(@RequestParam(value = "sort", defaultValue = "createAt") String sort,
                              @RequestParam(value = "dir", defaultValue = "asc") String dir,
                              Model model) {
        if (sort.equals("createAt")) {
            if (dir.equals("asc")) {
                histOrderList.sort(Comparator.comparing(Order::getCreateAt));
            } else {
                histOrderList.sort(Comparator.comparing(Order::getCreateAt).reversed());
            }
        } else if (sort.equals("total")) {
            if (dir.equals("asc")) {
                histOrderList.sort(Comparator.comparing(Order::getTotalPrice));
            } else {
                histOrderList.sort(Comparator.comparing(Order::getTotalPrice).reversed());
            }
        }
        session.setAttribute("histOrderList", histOrderList);
        model.addAttribute("histOrderList", histOrderList);
        return "orders";
    }

}
