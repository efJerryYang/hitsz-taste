package me.efjerryyang.webserver.controller;

import jakarta.servlet.http.HttpSession;
import me.efjerryyang.webserver.dto.PageScrollDTO;
import me.efjerryyang.webserver.dto.TableScrollDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ScrollController {
    private static final Logger logger = LoggerFactory.getLogger(ScrollController.class);
    @Autowired
    HttpSession session;

    @PostMapping("/save-scroll-position")
    public void saveScrollPosition(@RequestBody PageScrollDTO pageScrollDTO, Model model) {
        logger.info("pageScrollDTO= {}", pageScrollDTO);
        session.setAttribute("pageScrollDTO", pageScrollDTO);
        Map<String, Integer> tableMap = new HashMap<>();
        for (TableScrollDTO tableScrollDTO : pageScrollDTO.getTableScrollDTOS()) {
            tableMap.put(tableScrollDTO.getTableId(), tableScrollDTO.getTableScrollPos());
        }
        session.setAttribute("tableScrollMap", tableMap);
    }


}
