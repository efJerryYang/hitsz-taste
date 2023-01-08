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
public class Review implements IModel {
    private Long reviewId;
    private Long userId;
    private Integer rating;
    private String comment;
    private Timestamp createAt;
}
