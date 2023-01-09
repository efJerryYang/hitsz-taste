package me.efjerryyang.webserver.service;

import me.efjerryyang.webserver.dao.OrderDAO;
import me.efjerryyang.webserver.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private OrderDAO orderDAO;

    @Autowired
    public OrderService(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
        logger.info("OrderService initialized");
    }

    public Order createOrder() {
        Order order = new Order();
        order.setOrderId(getNextId());
        return order;
    }

    public void updateOrder() {
    }

    public void removeOrder() {
    }

    public void checkout() {
    }

    public Long getNextId() {
        return orderDAO.getNextId();
    }
}
