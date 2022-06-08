package com.msb.api.code;

/**
 * 系统管理模块的错误码
 * 区间:10000-15555
 */
public interface SystemCode {

    //系统通用成功状态码
    String TRAFFIC_SYSTEM_SUCCESS = "000000";
    String TRAFFIC_SYSTEM_ERROR = "000001";
    /**
     * 错误 信息 警告
     */
    //用户管理      10000-10999
    //10000-10499   错误提示
    String SYSTEM_USER_ERROR_ADD_FAIL = "10000";
    String SYSTEM_USER_ERROR_ADD_FAIL_PARAM_NULL = "10001";
    String SYSTEM_USER_ERROR_ADD_FAIL_NAME_NULL = "10002";
    String SYSTEM_USER_ERROR_ADD_FAIL_ACCOUNT_NULL = "10003";
    String SYSTEM_USER_ERROR_ADD_FAIL_PASS_NULL = "10004";
    String SYSTEM_USER_ERROR_ADD_FAIL_PHONE_NULL = "10005";
    String SYSTEM_USER_ERROR_ADD_FAIL_NAME_SIZE = "10006";
    String SYSTEM_USER_ERROR_ADD_FAIL_ACCOUNT_SIZE = "10007";
    String SYSTEM_USER_ERROR_ADD_FAIL_PASS_SIZE = "10008";
    //10500-10999  成功
    String SYSTEM_USER_INFO_ADD = "10500";
    //角色管理      11000-11999
    String SYSTEM_ROLE_ERROR_ADD_FAIL = "11000";

    //权限管理      12000-12999

}
