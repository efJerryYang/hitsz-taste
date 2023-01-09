package me.efjerryyang.webserver.controller;

import jakarta.servlet.http.HttpSession;
import me.efjerryyang.webserver.model.Order;
import me.efjerryyang.webserver.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private HttpSession session;

    @Autowired
    private OrderService orderService;

    private List<Order> histOrderList;

    @PostMapping("/orders")
    public String showHistory(@RequestParam(value = "sort", defaultValue = "createAt") String sort,
                              @RequestParam(value = "dir", defaultValue = "asc") String dir,
                              Model model) {
        histOrderList = orderService.getAll();
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
