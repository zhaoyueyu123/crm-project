package com.msb.system.service.impl;

import com.msb.api.commons.SystemUtils;
import com.msb.system.entity.UserEntity;
import com.msb.system.info.UserInfo;
import com.msb.system.repository.UserRepository;
import com.msb.system.service.UserService;
import com.sun.crypto.provider.HmacPKCS12PBESHA1;
import jdk.nashorn.internal.runtime.Specialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

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

    /**
     * 修改用户
     * @param userInfo
     * @return
     */
    @Override
    public boolean updUser(UserInfo userInfo) {
        logger.info("system user service updUser start");
        UserInfo user = userRepository.findById(userInfo.getUid()).get();
        if(!SystemUtils.isNullOrEmpty(userInfo.getUmail())){
            user.setUmail(userInfo.getUmail());
        }
        if(!SystemUtils.isNullOrEmpty(userInfo.getUname())){
            user.setUname(userInfo.getUname());
        }
        if(!SystemUtils.isNullOrEmpty(userInfo.getUphone())){
            user.setUphone(userInfo.getUphone());
        }
        UserInfo save = userRepository.save(user);
        logger.info("system user service updUser end");
        return true;
    }

    /**
     * 查询所有用户
     * @return
     */
    @Override
    public List<UserInfo> findAllUser() {
        List<UserInfo> userInfoList = userRepository.findAll();
        return userInfoList;
    }

    /**
     * 根据名字模糊查询用户
     * @param userInfo
     * @return
     */
    @Override
    public List<UserInfo> findUsersByName(UserInfo userInfo) {

        //创建匹配规则
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("uname",m->m.contains())
                .withIgnorePaths("uid")
                .withIgnorePaths("ustatus");
        //根据实体类去构建查询条件
        Example<UserInfo> example = Example.of(userInfo,matcher);
        //根据条件查询数据
        List<UserInfo> all = userRepository.findAll(example);
        return all;
    }

    /**
     * 分页查询
     * @param userInfo
     * @return
     */
    @Override
    public Map<String, Object> queryUsers(UserInfo userInfo,int page,int size) {
        logger.info("system user service queryUsers start");
        //分页对象
        Sort sort = Sort.by(Sort.Order.desc("uid"));
        Pageable of = PageRequest.of(page,size, sort);
        //条件查询
        Specification<UserInfo> spec = new Specification<UserInfo>() {
            @Override
            public Predicate toPredicate(Root<UserInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                //自定义条件查询对象
                Predicate predicate =criteriaBuilder.conjunction();
                //根据邮箱查询
                if(!SystemUtils.isNullOrEmpty(userInfo.getUmail())){
                    predicate.getExpressions().add(criteriaBuilder.equal(root.get("umail"),userInfo.getUmail()));
                }
                //根据手机是否模糊匹配
                if(!SystemUtils.isNullOrEmpty(userInfo.getUphone())){
                    predicate.getExpressions().add(criteriaBuilder.like(root.get("uphone").as(String.class),"%"+userInfo.getUphone()+"%"));
                }
                //起始时间
                //predicate.getExpressions().add(criteriaBuilder.greaterThanOrEqualTo(root.get("utime").as(String.class),userInfo.getStartTime()));
                //终止时间
                //predicate.getExpressions().add(criteriaBuilder.lessThanOrEqualTo(root.get("utime").as(String.class),userInfo.getEndTime()));
                return predicate;
            }
        };
        Page<UserInfo> page1 = userRepository.findAll(spec,of);
        Map<String,Object> map = new HashMap<>();
        map.put("users",page1.getContent());//查询到的列表信息
        map.put("totalPage",page1.getTotalPages());//总页数
        map.put("currentPage",page);
        map.put("pageSize",size);
        return map;
    }
}
