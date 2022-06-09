package com.msb.system.repository;

import com.msb.system.entity.UserEntity;
import com.msb.system.info.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 负责数据查询的接口
 */
public interface UserRepository extends JpaRepository<UserInfo,Long> {
}
