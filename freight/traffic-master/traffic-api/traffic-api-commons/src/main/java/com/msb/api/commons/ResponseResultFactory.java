package com.msb.api.commons;

import com.msb.api.code.SystemCode;

public class ResponseResultFactory<T> {

    /**
     * 构建一个通用的成功的返回结果
     * @return
     */
    public static ResponseResult buildResponseResult(){
        return new ResponseResult(SystemCode.TRAFFIC_SYSTEM_SUCCESS);
    }

    public static ResponseResult buildResponseResult(String resultCode){
        return new ResponseResult(resultCode);
    }

    public static ResponseResult buildResponseResult(String resultCode,String resultMsg){
        return new ResponseResult(resultCode,resultMsg);
    }

    public ResponseResult buildResponseResult(String resultCode, String resultMsg, T obj){
        return new ResponseResult(resultCode,resultMsg,obj);
    }
}
