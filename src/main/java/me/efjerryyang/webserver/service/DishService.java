package me.efjerryyang.webserver.service;

import me.efjerryyang.webserver.dao.DishDAO;
import me.efjerryyang.webserver.model.Dish;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishService {
    private static final Logger logger = LoggerFactory.getLogger(DishService.class);
    @Autowired
    private DishDAO dishDAO;

    public List<Dish> searchDishes(String query) {
        logger.info("DishService.searchDishes() called");
        if (query == null || query.isEmpty()) {
            logger.info("DishService.searchDishes() query is empty");
            return dishDAO.getAll();
        } else {
            logger.info("DishService.searchDishes() query is {}", query);
            return dishDAO.getAllMatching(query);
        }
    }

    public Float getPrice(Long dishId) {
        logger.info("DishService.getPrice() called");
        return dishDAO.getPrice(dishId);
    }

    public Dish getById(Long dishId) {
        logger.info("DishService.getById() called");
        return dishDAO.getById(dishId);
    }
}
