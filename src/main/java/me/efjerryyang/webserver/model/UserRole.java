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
public class UserRole implements IModel {
    private Long userId;
    private Integer roleId;
    private Timestamp grantDate;
}
