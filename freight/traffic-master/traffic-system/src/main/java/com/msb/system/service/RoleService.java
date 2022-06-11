package com.msb.system.service;

import com.msb.system.entity.RoleEntity;

import java.util.List;

public interface RoleService {

    public boolean addRole(RoleEntity roleEntity);

    public List<RoleEntity> queryAllRoles();
}
