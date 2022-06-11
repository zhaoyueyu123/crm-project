package com.msb.system.repository;

import com.msb.system.info.RoleInfo;
import com.msb.system.info.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Collection;

/**
 * 负责数据查询的接口
 */
public interface RoleRepository extends JpaRepository<RoleInfo,Long>, JpaSpecificationExecutor<RoleInfo> {

}
