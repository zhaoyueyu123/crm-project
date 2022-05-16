package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
@Controller
public class ActivityRemarkController {

    @Autowired
    private ActivityRemarkService activityRemarkService;
    @RequestMapping("/workbench/activity/saveCreateActivityRemark.do")
    @ResponseBody
    public Object saveCreateActivityRemark(ActivityRemark activityRemark, HttpSession session){
        User user=(User)session.getAttribute(Contants.SESSION_USER);
        ReturnObject returnObject = new ReturnObject();
        if(user == null){
            returnObject.setCode(Contants.RETUEN_OBJECT_CODE_FAIL);
            returnObject.setMessage("身份信息认证失败，请重新登录。");
            return returnObject;
        }
        //封装参数
        activityRemark.setId(UUIDUtils.getUUID());
        activityRemark.setCreateTime(DateUtils.formateDateTime(new Date()));
        activityRemark.setCreateBy(user.getId());
        activityRemark.setEditFlag(Contants.REMARK_EDIT_FLAG_NO_EDITED);
        //调用service层方法,插入数据

        try {
            int result = activityRemarkService.saveCreateActivityRemark(activityRemark);
            if(result > 0){
                returnObject.setCode(Contants.RETUEN_OBJECT_CODE_SUCCESS);
                returnObject.setRetData(activityRemark);
            }else{
                returnObject.setCode(Contants.RETUEN_OBJECT_CODE_FAIL);
                returnObject.setMessage("保存失败。。。。");
            }
        }catch(Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETUEN_OBJECT_CODE_FAIL);
            returnObject.setMessage("保存失败。。。。");
        }

        return returnObject;
    }

    @RequestMapping("/workbench/activity/deleteActivityRemarkById.do")
    @ResponseBody
    public Object deleteActivityRemarkById(String id){
        ReturnObject returnObject = new ReturnObject();
        //调用service层方法，删除备注
        try {
            int ret = activityRemarkService.deleteActivityRemarkById(id);
            if(ret > 0){
                returnObject.setCode(Contants.RETUEN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contants.RETUEN_OBJECT_CODE_FAIL);
                returnObject.setMessage("删除失败。。。。");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETUEN_OBJECT_CODE_FAIL);
            returnObject.setMessage("删除失败。。。。");
        }
        return returnObject;
    }
}
