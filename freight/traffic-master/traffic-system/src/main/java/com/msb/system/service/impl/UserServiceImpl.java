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

import java.util.HashSet;
import java.util.Set;

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

    /**
     * 删除用户
     * @param uid
     * @return
     */
    @Override
    public boolean delUser(String uid) {
        //删除一条删除多条的业务逻辑
        String[] ids = uid.split(",");
        if(SystemUtils.isNull(ids) || ids.length == 0){
            return false;
        }
        //删除一条
        if(ids.length == 1){
            //先查询后更新

            try {
                Long id = Long.parseLong(ids[0]);
                UserInfo userInfo = userRepository.findById(id).get();
                userInfo.setUstatus(1);
                userRepository.save(userInfo);
                return true;
            }catch (Exception e){
                return false;
            }
        }else {//删除多条
            try {
                Set<Long> sets = new HashSet<>();
                for (String id : ids) {
                    sets.add(Long.parseLong(id));
                }
                userRepository.updates(sets);
                return true;
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }
    }
}
