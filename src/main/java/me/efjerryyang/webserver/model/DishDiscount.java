package me.efjerryyang.webserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class DishDiscount implements IModel {
    private Long dishId;
    private Long discountId;
    private Date startDate;
    private Date endDate;
}
