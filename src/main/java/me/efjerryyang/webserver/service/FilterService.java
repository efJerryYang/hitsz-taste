package me.efjerryyang.webserver.service;

import me.efjerryyang.webserver.model.Dish;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FilterService {
    private List<Long> getMerchantIdListByDishes(List<Dish> dishes) {
        List<Long> merchantIdList = new ArrayList<>();
        for (Dish dish : dishes) {
            merchantIdList.add(dish.getMerchantId());
        }
        return merchantIdList;
    }

//    private List<Long> get

}
