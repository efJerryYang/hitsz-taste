package me.efjerryyang.webserver.view;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseView {
    protected Long dishId;
    protected String dishName;
    protected Float price;
    protected Long merchantId;
    protected String merchantName;
    protected Long cafeteriaId;
    protected String cafeteriaName;
}
