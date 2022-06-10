package com.msb.system.repository;

import com.msb.system.info.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

/**
 * 负责数据查询的接口
 */
public interface UserRepository extends JpaRepository<UserInfo,Long>, JpaSpecificationExecutor<UserInfo> {

//    @Modifying
//    @Transactional
//    @Query("update UserInfo set ustatus = ?2 where uid = ?1")
//    public void deleteUid(int status,int id);

    //批量更新
    @Modifying
    @Transactional
    @Query("update UserInfo set ustatus = 1 where uid in(?1)")
    public int updates(Collection<Long> ids);


}
