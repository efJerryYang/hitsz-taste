package me.efjerryyang.webserver.service;

import me.efjerryyang.webserver.dao.MerchantUserDAO;
import me.efjerryyang.webserver.model.MerchantUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MerchantUserService {
    private final MerchantUserDAO merchantUserDAO;

    @Autowired
    public MerchantUserService(MerchantUserDAO merchantUserDAO) {
        this.merchantUserDAO = merchantUserDAO;
    }

    public void create(MerchantUser merchantUser) {
        MerchantUser getByUserIdAndMerchantId = merchantUserDAO.getByUserIdAndMerchantId(merchantUser.getUserId(), merchantUser.getMerchantId());
        if (getByUserIdAndMerchantId != null) {
            throw new RuntimeException("Merchant user already exists");
        }
        merchantUserDAO.create(merchantUser);
    }

    public MerchantUser bindUserAndMerchant(Long userId, Long merchantId) {
        MerchantUser merchantUser = new MerchantUser();
        merchantUser.setUserId(userId);
        merchantUser.setMerchantId(merchantId);
        merchantUser.setUpdateTime(new java.sql.Timestamp(System.currentTimeMillis()));
        return merchantUser;
    }


}
