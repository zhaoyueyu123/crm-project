package com.msb.system.service;

import com.msb.system.entity.UserEntity;
import com.msb.system.info.UserInfo;

public interface UserService {

    /**
     * 添加用户
     * @param userInfo
     * @return
     */
    public boolean addUser(UserInfo userInfo);

    /**
     * 删除用户
     */
    public boolean delUser(String uid);
}
