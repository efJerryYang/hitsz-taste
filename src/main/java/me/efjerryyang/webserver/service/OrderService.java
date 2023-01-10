package me.efjerryyang.webserver.service;

import me.efjerryyang.webserver.dao.OrderDAO;
import me.efjerryyang.webserver.model.Dish;
import me.efjerryyang.webserver.model.Order;
import me.efjerryyang.webserver.model.OrderItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final OrderDAO orderDAO;
    private final OrderItemService orderItemService;
    private final DishService dishService;

    @Autowired
    public OrderService(OrderDAO orderDAO, OrderItemService orderItemService, DishService dishService) {
        this.orderDAO = orderDAO;
        this.orderItemService = orderItemService;
        this.dishService = dishService;
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

    public List<Order> getAllByUserId(Long userId) {
        return orderDAO.getAllByUserId(userId);
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

    // TODO: to be revised later
    public List<Order> getAllByMerchantId(Long merchantId) {
        List<Order> orderList = orderDAO.getAll();
        List<OrderItem> orderItemList = orderItemService.getAll();
        List<Dish> dishList = dishService.getAllByMerchantId(merchantId);
        // according to the dish list of specific merchant, select the order items associated with the merchant
        List<OrderItem> selectedOrderItemList = orderItemList.stream()
                .filter(orderItem -> dishList.stream()
                        .anyMatch(dish -> dish.getDishId().equals(orderItem.getDishId())))
                .toList();
        // according to the order items, select the orders associated with the merchant
        return orderList.stream()
                .filter(order -> selectedOrderItemList.stream()
                        .anyMatch(orderItem -> orderItem.getOrderId().equals(order.getOrderId())))
                .toList();
    }

    public void updateStatusById(Long orderId, String processing) {
        logger.info("Order status updated: " + orderId + " " + processing);
        orderDAO.updateStatusById(orderId, processing);
    }
}
