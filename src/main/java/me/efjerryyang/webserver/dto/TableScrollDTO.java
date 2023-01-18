package me.efjerryyang.webserver.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class TableScrollDTO {
    private String tableId;
    private int tableScrollPos;

    @JsonCreator
    public TableScrollDTO(@JsonProperty("tableId") String tableId,
                          @JsonProperty("tableScrollPos") int tableScrollPos) {
        this.tableId = tableId;
        this.tableScrollPos = tableScrollPos;
    }
}
