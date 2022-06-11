package com.msb.system.web;

import com.msb.api.code.SystemCode;
import com.msb.api.commons.ResponseResult;
import com.msb.api.commons.ResponseResultFactory;
import com.msb.api.commons.SystemUtils;
import com.msb.system.entity.RoleEntity;
import com.msb.system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;
    @RequestMapping("/addRole")
    @ResponseBody
    public ResponseResult addRole(RoleEntity roleEntity){
        ResponseResult responseResult = null;
        boolean bl = roleService.addRole(roleEntity);
        if(bl){
            responseResult = ResponseResultFactory.buildResponseResult(SystemCode.TRAFFIC_SYSTEM_SUCCESS,roleEntity);
        }else {
            responseResult = ResponseResultFactory.buildResponseResult(SystemCode.TRAFFIC_SYSTEM_ERROR);
        }
        return responseResult;
    }

    @RequestMapping("/queryAllRoles")
    @ResponseBody
    public ResponseResult queryAllRoles(){
        ResponseResult responseResult = null;
        List<RoleEntity> roleEntityList = roleService.queryAllRoles();
        return ResponseResultFactory.buildResponseResult(SystemCode.TRAFFIC_SYSTEM_SUCCESS,roleEntityList);
    }
}
