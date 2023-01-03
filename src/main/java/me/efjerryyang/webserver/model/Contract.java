package me.efjerryyang.webserver.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Contract implements IModel {
    private Long cafeteriaId;
    private Long merchantId;
    private Timestamp startTimestamp;
    private Timestamp endTimestamp;
}
