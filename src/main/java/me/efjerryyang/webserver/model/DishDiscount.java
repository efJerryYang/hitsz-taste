package me.efjerryyang.webserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class DishDiscount implements IModel {
    private Long dishId;
    private Long discountId;
    private Timestamp startTimestamp;
    private Timestamp endTimestamp;
}
