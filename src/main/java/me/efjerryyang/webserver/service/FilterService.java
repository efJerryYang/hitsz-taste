package me.efjerryyang.webserver.service;

import me.efjerryyang.webserver.model.Contract;
import me.efjerryyang.webserver.model.Dish;
import me.efjerryyang.webserver.model.Merchant;
import me.efjerryyang.webserver.view.BaseView;
import me.efjerryyang.webserver.view.IView;
import me.efjerryyang.webserver.view.SearchResultView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FilterService {
    @Autowired
    private CafeteriaService cafeteriaService;

    private List<Long> getMerchantIdListByDishes(List<Dish> dishes) {
        List<Long> merchantIdList = new ArrayList<>();
        for (Dish dish : dishes) {
            merchantIdList.add(dish.getMerchantId());
        }
        return merchantIdList;
    }

    public List<BaseView> createViewList(List<Dish> dishList, List<Merchant> merchantList, List<Contract> contractList) {
        List<BaseView> searchResultViewList = new ArrayList<>();
        for (Dish dish : dishList) {
            for (Merchant merchant : merchantList) {
                if (dish.getMerchantId().equals(merchant.getMerchantId())) {
                    for (Contract contract : contractList) {
                        if (merchant.getMerchantId().equals(contract.getMerchantId())) {
                            SearchResultView searchResultView = new SearchResultView();
                            searchResultView.setDishId(dish.getDishId());
                            searchResultView.setDishName(dish.getName());
                            searchResultView.setPrice(dish.getPrice());
                            searchResultView.setMerchantId(merchant.getMerchantId());
                            searchResultView.setMerchantName(merchant.getName());
                            searchResultView.setCafeteriaId(contract.getCafeteriaId());
                            searchResultView.setCafeteriaName(cafeteriaService.getById(contract.getCafeteriaId()).getName());
                            searchResultViewList.add(searchResultView);
                        }
                    }
                }
            }
        }
        return searchResultViewList;
    }

    public List<Long> getDishIds(List<Dish> dishes) {
        List<Long> dishIds = new ArrayList<>();
        for (Dish dish : dishes) {
            dishIds.add(dish.getDishId());
        }
        return dishIds;
    }
}
