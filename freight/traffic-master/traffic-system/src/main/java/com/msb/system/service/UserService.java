package com.msb.system.service;

import com.msb.system.entity.UserEntity;
import com.msb.system.info.UserInfo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

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

    /**
     * 修改用户信息
     */
    public boolean updUser(UserInfo userInfo);

    /**
     * 查询所有用户
     * @return
     */
    public List<UserInfo> findAllUser();

    /**
     * 根据名字模糊查询用户
     * @return
     */
    public List<UserInfo> findUsersByName(UserInfo userInfo);

    /**
     * 分页查询
     * @param userInfo
     * @return
     */
    public Map<String,Object> queryUsers(UserInfo userInfo,int page,int size);
}
