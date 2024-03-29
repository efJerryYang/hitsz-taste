package me.efjerryyang.webserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Order implements IModel {
    private Long orderId;
    private Long userId;
    private Long reviewId;
    private Float totalPrice;
    private String address;
    private String contact;
    private String status; // available status: pending, processing, delivering, completed, cancelled
    private Timestamp createAt;
}
