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
public class UserRole implements Model {
    private Long userId;
    private Long roleId;
    private Date grantDate;
}
