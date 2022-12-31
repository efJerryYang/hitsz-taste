package me.efjerryyang.webserver.service;

import me.efjerryyang.webserver.dao.ContractDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractService {
    private static final Logger logger = LoggerFactory.getLogger(ContractService.class);

    private final ContractDAO contractDAO;

    @Autowired
    public ContractService(ContractDAO contractDAO) {
        this.contractDAO = contractDAO;
        logger.info("ContractService initialized");
    }

    public List<Long> getMerchantsByCafeteriaId(Long cafeteriaId) {
        return contractDAO.getMerchantIdsByCafeteriaId(cafeteriaId);
    }

    public List<Long> getCafeteriaIdsByMerchantId(Long merchantId) {
        return contractDAO.getCafeteriaIdsByMerchantId(merchantId);
    }
}
