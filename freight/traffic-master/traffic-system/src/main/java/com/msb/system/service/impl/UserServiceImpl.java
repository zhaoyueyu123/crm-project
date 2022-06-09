package com.msb.system.service.impl;

import com.msb.api.commons.SystemUtils;
import com.msb.system.entity.UserEntity;
import com.msb.system.info.UserInfo;
import com.msb.system.repository.UserRepository;
import com.msb.system.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    final int ZERO = 0;

    @Autowired
    UserRepository userRepository;
    final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    /**
     * 添加用户
     * @param userInfo
     * @return
     */
    @Override
    public boolean addUser(UserInfo userInfo) {

        UserInfo user = null;
        logger.info("system user service addUser start :" +userInfo);
        logger.info("system user service addUser userRepository start");
        try {
            user = userRepository.save(userInfo);
        }catch (Exception e){
            logger.error("system user service addUser fail"+e);
            return false;
        }
        logger.info("system user service addUser userRepository end");
        logger.info("user:",user);
        if(!SystemUtils.isNull(user) && user.getUid() != ZERO){
            logger.info("system user service addUser success");
            return true;
        }
        logger.error("system user service addUser fail");
        return false;
    }

}
