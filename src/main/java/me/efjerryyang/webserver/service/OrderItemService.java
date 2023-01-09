package me.efjerryyang.webserver.service;

import me.efjerryyang.webserver.dao.OrderItemDAO;
import me.efjerryyang.webserver.model.OrderItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemService {
    private static final Logger logger = LoggerFactory.getLogger(OrderItemService.class);

    private OrderItemDAO orderItemDAO;

    @Autowired
    public OrderItemService(OrderItemDAO orderItemDAO) {
        this.orderItemDAO = orderItemDAO;
        logger.info("OrderItemService initialized");
    }

    public void create(OrderItem orderItem){
        orderItemDAO.create(orderItem);
        logger.info("OrderItem created: " + orderItem);
    }
    public void createAll(List<OrderItem> orderItemList)  {
        orderItemDAO.createAll(orderItemList);
        logger.info("OrderItem created: " + orderItemList);
    }
}
