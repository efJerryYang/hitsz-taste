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
public class MerchantUser implements IModel {
    private Long merchantId;
    private Long userId;
    private Timestamp updateTime;
    private String jobTitle;
    private String Company;
}
