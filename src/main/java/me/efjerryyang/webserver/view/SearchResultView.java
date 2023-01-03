package me.efjerryyang.webserver.view;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import me.efjerryyang.webserver.model.Contract;
import me.efjerryyang.webserver.model.Dish;
import me.efjerryyang.webserver.model.Merchant;
import me.efjerryyang.webserver.service.CafeteriaService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class SearchResultView extends BaseView implements IView {

    private CafeteriaService cafeteriaService;

    @Override
    public IView createView() {
        return new SearchResultView();
    }

    @Override
    public void show() {
        System.out.println(dishId + "\t" + dishName + "\t" + price + "\t" + merchantId + "\t" + merchantName + "\t" + cafeteriaId + "\t" + cafeteriaName);
    }

    @Override
    public void showHeader() {
        System.out.println("SearchResultView:");
        System.out.println("==========================================================================================");
        System.out.println("Dish ID\tDish Name\tPrice\tMerchant ID\tMerchant Name\tCategory ID\tCategory Name");
    }

    @Override
    public void showList(List<IView> iViewList) {
        iViewList.get(0).showHeader();
        for (IView iView : iViewList) {
            iView.show();
        }
        System.out.println("==========================================================================================");
    }
}
