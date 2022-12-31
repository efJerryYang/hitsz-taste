package me.efjerryyang.webserver.service;

import me.efjerryyang.webserver.dao.CafeteriaDAO;
import me.efjerryyang.webserver.model.Cafeteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CafeteriaService {
    private static final Logger logger = LoggerFactory.getLogger(CafeteriaService.class);

    private final CafeteriaDAO cafeteriaDAO;


    @Autowired
    public CafeteriaService(CafeteriaDAO cafeteriaDAO) {
        this.cafeteriaDAO = cafeteriaDAO;
        logger.info("CafeteriaService initialized");
    }

    public List<Cafeteria> getAll() {
        return cafeteriaDAO.getAll();
    }

    public List<Cafeteria> getAllMatching(String query) {
        return cafeteriaDAO.getAllMatching(query);
    }

    public List<Cafeteria> getAllByIds(List<Long> ids) {
        return cafeteriaDAO.getAllByIds(ids);
    }

    public Cafeteria getById(Long id) {
        return cafeteriaDAO.getById(id);
    }
}
