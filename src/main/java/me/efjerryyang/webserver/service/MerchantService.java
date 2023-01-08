package me.efjerryyang.webserver.service;

import me.efjerryyang.webserver.dao.MerchantDAO;
import me.efjerryyang.webserver.model.Merchant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MerchantService {
    private static final Logger logger = LoggerFactory.getLogger(MerchantService.class);

    private MerchantDAO merchantDAO;

    @Autowired
    public MerchantService(MerchantDAO merchantDAO) {
        this.merchantDAO = merchantDAO;
        logger.info("MerchantService initialized");
    }

    public void create(Merchant merchant) {
        Merchant getByMerchantId = merchantDAO.getById(merchant.getMerchantId());
        if (getByMerchantId != null) {
            throw new RuntimeException("Merchant already exists");
        }
        merchantDAO.create(merchant);
        logger.info("Merchant created: " + merchant);
    }

    public List<Merchant> getAll() {
        return merchantDAO.getAll();
    }

    public List<Merchant> getAllMatching(String query) {
        return merchantDAO.getAllMatching(query);
    }

    public List<Merchant> getAllByNames(List<String> names) {
        return merchantDAO.getAllByNames(names);
    }

    public List<Merchant> getAllByDishIds(List<Long> dishIds) {
        return merchantDAO.getAllByDishIds(dishIds);
    }

    public Long getNextId() {
        return merchantDAO.getNextId();
    }
}
