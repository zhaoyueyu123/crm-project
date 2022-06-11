package com.msb.system.service.impl;

import com.msb.system.entity.RoleEntity;
import com.msb.system.info.RoleInfo;
import com.msb.system.repository.RoleRepository;
import com.msb.system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("roleService")
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;
    @Override
    public boolean addRole(RoleEntity roleEntity) {
        //日志
        boolean bl = false;
        RoleInfo roleInfo = roleRepository.save(zhuanhuan(roleEntity));
        if(roleInfo !=null && roleInfo.getRid() != 0){
            bl=true;
        }
        return bl;
    }

    @Override
    public List<RoleEntity> queryAllRoles() {
        //日志
        List<RoleInfo> roleInfoList = roleRepository.findAll();
        List<RoleEntity> roleEntityList = new ArrayList<>();
        for(RoleInfo roleInfo:roleInfoList){
            roleEntityList.add(nizhuan(roleInfo));
        }
        return roleEntityList;
    }

    private RoleEntity nizhuan(RoleInfo roleInfo){
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setRid(roleInfo.getRid());
        roleEntity.setRname(roleInfo.getRname());
        roleEntity.setRtype(roleInfo.getRtype());
        roleEntity.setRdesc(roleInfo.getRdesc());
        return roleEntity;
    }
    private RoleInfo zhuanhuan(RoleEntity roleEntity){
        RoleInfo roleInfo = new RoleInfo();
        roleInfo.setRid(roleEntity.getRid());
        roleInfo.setRname(roleEntity.getRname());
        roleInfo.setRtype(roleEntity.getRtype());
        roleInfo.setRdesc(roleEntity.getRdesc());
        return roleInfo;
    }
}
