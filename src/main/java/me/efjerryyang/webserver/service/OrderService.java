package me.efjerryyang.webserver.service;

import me.efjerryyang.webserver.dao.OrderDAO;
import me.efjerryyang.webserver.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private OrderDAO orderDAO;

    @Autowired
    public OrderService(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
        logger.info("OrderService initialized");
    }

    public void create(Order order) {
        orderDAO.create(order);
        logger.info("Order created: " + order);
    }
    public void update(Order order) {
        orderDAO.update(order);
        logger.info("Order updated: " + order);
    }

    public Order getById(Long orderId) {
        return orderDAO.getById(orderId);
    }

    public List<Order> getAll() {
        return orderDAO.getAll();
    }
    public void cancel(Long orderId) {
        orderDAO.cancel(orderId);
        logger.info("Order cancelled: " + orderId);
    }
    public void cancel(Order order) {
        orderDAO.cancel(order);
        logger.info("Order cancelled: " + order);
    }

    public Long getNextId() {
        return orderDAO.getNextId();
    }

    public void delete(Long orderId) {
        orderDAO.delete(orderId);
        logger.info("Order deleted: " + orderId);
    }
    public void delete(Order order) {
        orderDAO.delete(order);
        logger.info("Order deleted: " + order);
    }
}
