package me.efjerryyang.webserver.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Contract {
    private Long cafeteriaId;
    private Long merchantId;
    private Date startDate;
    private Date endDate;
}
