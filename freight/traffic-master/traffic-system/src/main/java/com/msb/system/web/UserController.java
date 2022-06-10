package com.msb.system.web;

import com.msb.api.code.SystemCode;
import com.msb.api.commons.ResponseResult;
import com.msb.api.commons.ResponseResultFactory;
import com.msb.api.commons.SystemUtils;
import com.msb.system.entity.UserEntity;
import com.msb.system.info.UserInfo;
import com.msb.system.service.UserService;
import org.springframework.data.domain.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {


    final Logger logger = LoggerFactory.getLogger(UserController.class);
    final int NAME_LENGTH=3;
    final int ACCOUNT_LENGTH=6;
    final int PASS_LENGTH=6;
    @Autowired
    private UserService userService;
    @RequestMapping(value="/addUser",method = RequestMethod.POST)
    public ResponseResult addUser(UserInfo userInfo){

        logger.error("system user addUser start");

        //参数非空判断
        if(SystemUtils.isNull(userInfo)){
            logger.error("system user addUser UserEntity is null");
            ResponseResult responseResult = ResponseResultFactory.buildResponseResult(SystemCode.SYSTEM_USER_ERROR_ADD_FAIL_PARAM_NULL);
            logger.info("system user addUser return msg :"+responseResult);
            return responseResult;

        };
        //用户名非空判断
        if(SystemUtils.isNullOrEmpty(userInfo.getUname())){
            logger.error("system user addUser uname is null");
            logger.info("param:"+userInfo);
            ResponseResult responseResult = ResponseResultFactory.buildResponseResult(SystemCode.SYSTEM_USER_ERROR_ADD_FAIL_NAME_NULL);
            logger.info("system user addUser return msg :"+responseResult);
            return responseResult;

        };
        //账号非空判断
        if(SystemUtils.isNullOrEmpty(userInfo.getUaccount())){
            logger.error("system user addUser uaccount is null");
            logger.info("param:"+userInfo);
            ResponseResult responseResult = ResponseResultFactory.buildResponseResult(SystemCode.SYSTEM_USER_ERROR_ADD_FAIL_ACCOUNT_NULL);
            logger.info("system user addUser return msg :"+responseResult);
            return responseResult;

        };
        //密码非空判断
        if(SystemUtils.isNullOrEmpty(userInfo.getUpass())){
            logger.error("system user addUser upass is null");
            logger.info("param:"+userInfo);
            ResponseResult responseResult = ResponseResultFactory.buildResponseResult(SystemCode.SYSTEM_USER_ERROR_ADD_FAIL_PASS_NULL);
            logger.info("system user addUser return msg :"+responseResult);
            return responseResult;

        };
        //手机号非空判断
        if(SystemUtils.isNullOrEmpty(userInfo.getUphone())){
            logger.error("system user addUser uphone is null");
            logger.info("param:"+userInfo);
            ResponseResult responseResult = ResponseResultFactory.buildResponseResult(SystemCode.SYSTEM_USER_ERROR_ADD_FAIL_PHONE_NULL);
            logger.info("system user addUser return msg :"+responseResult);
            return responseResult;

        };
        //用户名小于3个长度
        if(userInfo.getUname().trim().length() < NAME_LENGTH){
            logger.error("system user addUser uname length< 3");
            logger.info("param:"+userInfo);
            ResponseResult responseResult = ResponseResultFactory.buildResponseResult(SystemCode.SYSTEM_USER_ERROR_ADD_FAIL_NAME_SIZE);
            logger.info("system user addUser return msg :"+responseResult);
            return responseResult;

        };
        //账号长度
        if(userInfo.getUaccount().trim().length() < ACCOUNT_LENGTH){
            logger.error("system user addUser uaccount length < 6");
            logger.info("param:"+userInfo);
            ResponseResult responseResult = ResponseResultFactory.buildResponseResult(SystemCode.SYSTEM_USER_ERROR_ADD_FAIL_ACCOUNT_SIZE);
            logger.info("system user addUser return msg :"+responseResult);
            return responseResult;

        };
        //密码长度
        if(userInfo.getUpass().trim().length() < PASS_LENGTH){
            logger.error("system user addUser upass length < 6");
            logger.info("param:"+userInfo);
            ResponseResult responseResult = ResponseResultFactory.buildResponseResult(SystemCode.SYSTEM_USER_ERROR_ADD_FAIL_PASS_SIZE);
            logger.info("system user addUser return msg :"+responseResult);
            return responseResult;

        };

        //密码的加密操作
        logger.info("system user addUser pass digest");
        userInfo.setUpass(DigestUtils.md5DigestAsHex(userInfo.getUpass().getBytes()));

        //添加用户
        logger.info("system user addUser userService addUser start");
        boolean result = userService.addUser(userInfo);
        logger.info("system user addUser userService addUser end :"+result);
        ResponseResult responseResult = null;
        if(result){
            responseResult = ResponseResultFactory.buildResponseResult();
        }else{
            responseResult = ResponseResultFactory.buildResponseResult(SystemCode.SYSTEM_USER_ERROR_ADD_FAIL);
        }
        logger.info("system user addUser return :"+responseResult);
        return responseResult;
    }

    /**
     * 删除用户
     * @param uid
     * @return
     */
    @RequestMapping(value = "/delUser",method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult delUser(String uid){
        //判断传过来的参数是否为空
        if(SystemUtils.isNullOrEmpty(uid)){
            logger.error("system user delUser uid is null");
            ResponseResult responseResult = ResponseResultFactory.buildResponseResult(SystemCode.SYSTEM_USER_ERROR_DEL_FAIL_UID_NULL);
            logger.info("system user delUser return msg :"+responseResult);
            return responseResult;
        }
        //简单的逻辑判断；
        logger.info("system user delUser UserService start");
        boolean bl = userService.delUser(uid);
        logger.error("system user delUser UserService end");
        ResponseResult responseResult;
        if(bl){
            logger.error("system user delUser success");
            responseResult = new ResponseResult(SystemCode.TRAFFIC_SYSTEM_SUCCESS);
        }else {
            logger.error("system user delUser fail");
            responseResult = new ResponseResult(SystemCode.TRAFFIC_SYSTEM_ERROR);
        }
        logger.info("system user delUser end");
        return responseResult;
    }

    /**
     * 更新用户信息
     * @param userInfo
     * @return
     */
    @RequestMapping(value = "/updUser",method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult updUser(UserInfo userInfo){
        ResponseResult responseResult = null;
        //判断传过来的参数是否为空
        if(SystemUtils.isNull(userInfo)){
            logger.error("system user updUser userInfo is null");
            responseResult = ResponseResultFactory.buildResponseResult(SystemCode.SYSTEM_USER_ERROR_UPD_FAIL_UID_NULL);
            logger.info("system user updUser return msg :"+responseResult);
            return responseResult;
        }

        if(userInfo.getUid() == 0){
            logger.error("system user updUser uid is null");
            responseResult = ResponseResultFactory.buildResponseResult(SystemCode.SYSTEM_USER_ERROR_UPD_FAIL_UID_NULL);
            logger.info("system user updUser return msg :"+responseResult);
            return responseResult;
        }
        logger.info("system user updUser UserService start");
        boolean bl = userService.updUser(userInfo);
        logger.info("system user updUser UserService end");
        if(bl){
            logger.info("system user updUser success");
            responseResult = new ResponseResult(SystemCode.TRAFFIC_SYSTEM_SUCCESS);
        }else {
            logger.error("system user updUser fail");
            responseResult = new ResponseResult(SystemCode.TRAFFIC_SYSTEM_ERROR);
        }
        logger.info("system user updUser end");
        return responseResult;
    }

    /**
     * 查询所有的用户信息
     * @return
     */
    @RequestMapping(value = "/findAllUser")
    public ResponseResult<List<UserInfo>> findAllUser(){
        logger.info("system user findAllUser start");
        List<UserInfo> allUser = userService.findAllUser();
        ResponseResult<List<UserInfo>> responseResult =  ResponseResultFactory.buildResponseResult(SystemCode.TRAFFIC_SYSTEM_SUCCESS,allUser);
        logger.info("system user findAllUser end");
        return responseResult;
    }

    @RequestMapping(value = "/findUsersByName")
    public ResponseResult<List<UserInfo>> findUsersByName(UserInfo userInfo){
        logger.info("system user findUsersByName start");
        ResponseResult<List<UserInfo>> responseResult = null;
        List<UserInfo> userInfoList = userService.findUsersByName(userInfo);
        responseResult = ResponseResultFactory.buildResponseResult(SystemCode.TRAFFIC_SYSTEM_SUCCESS,userInfoList);
        logger.info("system user findUsersByName end");
        return responseResult;
    }

    /**
     * 通用分页查询
     */
    @RequestMapping("/queryUsers")
    public ResponseResult queryUsers(UserInfo userInfo){
        logger.info("system user queryUsers start");
        ResponseResult responseResult = null;
        Map<String,Object> map = userService.queryUsers(userInfo,0,3);
        responseResult = ResponseResultFactory.buildResponseResult(SystemCode.TRAFFIC_SYSTEM_SUCCESS,map);
        return responseResult;
    }
}
