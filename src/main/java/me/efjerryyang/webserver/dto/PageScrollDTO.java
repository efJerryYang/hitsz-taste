package me.efjerryyang.webserver.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class PageScrollDTO {
    private String pageId;
    private int pageScrollPos;
    private List<TableScrollDTO> tableScrollDTOS;

    @JsonCreator
    public PageScrollDTO(@JsonProperty("pageId") String pageId,
                         @JsonProperty("pageScrollPos") int pageScrollPos,
                         @JsonProperty("tables") TableScrollDTO[] tableScrollDTOS) {

        this.pageId = pageId;
        this.pageScrollPos = pageScrollPos;
        this.tableScrollDTOS = Arrays.stream(tableScrollDTOS).toList();
    }
}

