package com.msb.system.web;

import com.msb.api.code.SystemCode;
import com.msb.api.commons.ResponseResult;
import com.msb.api.commons.ResponseResultFactory;
import com.msb.api.commons.SystemUtils;
import com.msb.system.entity.UserEntity;
import com.msb.system.info.UserInfo;
import com.msb.system.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

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
    public ResponseResult addUser(UserEntity userEntity){

        logger.error("system user addUser start");

        //参数非空判断
        if(SystemUtils.isNull(userEntity)){
            logger.error("system user addUser UserEntity is null");
            ResponseResult responseResult = ResponseResultFactory.buildResponseResult(SystemCode.SYSTEM_USER_ERROR_ADD_FAIL_PARAM_NULL);
            logger.info("system user addUser return msg :"+responseResult);
            return responseResult;

        };
        //用户名非空判断
        if(SystemUtils.isNullOrEmpty(userEntity.getUname())){
            logger.error("system user addUser uname is null");
            logger.info("param:"+userEntity);
            ResponseResult responseResult = ResponseResultFactory.buildResponseResult(SystemCode.SYSTEM_USER_ERROR_ADD_FAIL_NAME_NULL);
            logger.info("system user addUser return msg :"+responseResult);
            return responseResult;

        };
        //账号非空判断
        if(SystemUtils.isNullOrEmpty(userEntity.getUaccount())){
            logger.error("system user addUser uaccount is null");
            logger.info("param:"+userEntity);
            ResponseResult responseResult = ResponseResultFactory.buildResponseResult(SystemCode.SYSTEM_USER_ERROR_ADD_FAIL_ACCOUNT_NULL);
            logger.info("system user addUser return msg :"+responseResult);
            return responseResult;

        };
        //密码非空判断
        if(SystemUtils.isNullOrEmpty(userEntity.getUpass())){
            logger.error("system user addUser upass is null");
            logger.info("param:"+userEntity);
            ResponseResult responseResult = ResponseResultFactory.buildResponseResult(SystemCode.SYSTEM_USER_ERROR_ADD_FAIL_PASS_NULL);
            logger.info("system user addUser return msg :"+responseResult);
            return responseResult;

        };
        //手机号非空判断
        if(SystemUtils.isNullOrEmpty(userEntity.getUphone())){
            logger.error("system user addUser uphone is null");
            logger.info("param:"+userEntity);
            ResponseResult responseResult = ResponseResultFactory.buildResponseResult(SystemCode.SYSTEM_USER_ERROR_ADD_FAIL_PHONE_NULL);
            logger.info("system user addUser return msg :"+responseResult);
            return responseResult;

        };
        //用户名小于3个长度
        if(userEntity.getUname().trim().length() < NAME_LENGTH){
            logger.error("system user addUser uname length< 3");
            logger.info("param:"+userEntity);
            ResponseResult responseResult = ResponseResultFactory.buildResponseResult(SystemCode.SYSTEM_USER_ERROR_ADD_FAIL_NAME_SIZE);
            logger.info("system user addUser return msg :"+responseResult);
            return responseResult;

        };
        //账号长度
        if(userEntity.getUaccount().trim().length() < ACCOUNT_LENGTH){
            logger.error("system user addUser uaccount length < 6");
            logger.info("param:"+userEntity);
            ResponseResult responseResult = ResponseResultFactory.buildResponseResult(SystemCode.SYSTEM_USER_ERROR_ADD_FAIL_ACCOUNT_SIZE);
            logger.info("system user addUser return msg :"+responseResult);
            return responseResult;

        };
        //密码长度
        if(userEntity.getUpass().trim().length() < PASS_LENGTH){
            logger.error("system user addUser upass length < 6");
            logger.info("param:"+userEntity);
            ResponseResult responseResult = ResponseResultFactory.buildResponseResult(SystemCode.SYSTEM_USER_ERROR_ADD_FAIL_PASS_SIZE);
            logger.info("system user addUser return msg :"+responseResult);
            return responseResult;

        };

        //密码的加密操作
        logger.info("system user addUser pass digest");
        userEntity.setUpass(DigestUtils.md5DigestAsHex(userEntity.getUpass().getBytes()));

        //添加用户
        logger.info("system user addUser userService addUser start");
        boolean result = userService.addUser(userEntity);
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
}
