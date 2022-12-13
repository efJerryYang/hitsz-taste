package me.efjerryyang.webserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Order implements Model {
    private Long orderId;
    private Long userId;
    private Long reviewId;
    private Float totalPrice;
    private String address;
    private String contact;
    private String status;
    private Date timestamp;
}
