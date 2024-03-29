package me.efjerryyang.webserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Dish implements IModel {
    private Long dishId;
    private Long merchantId;
    private Long categoryId;
    private String name;
    private Float price;
    private String ingredients;
    private String description;
}
