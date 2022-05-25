package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.DicValueService;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;
import com.bjpowernode.crm.workbench.domain.ClueRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ClueActivityRelationService;
import com.bjpowernode.crm.workbench.service.ClueRemarkService;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class ClueController {

    @Autowired
    private UserService userService;

    @Autowired
    private DicValueService dicValueService;

    @Autowired
    private ClueService clueService;

    @Autowired
    private ClueRemarkService clueRemarkService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ClueActivityRelationService clueActivityRelationService;

    @RequestMapping("/workbench/clue/index.do")
    public String index(HttpServletRequest request){
        //调用service层方法
        List<User> userList=userService.queryAllUsers();
        List<DicValue> appellationList=dicValueService.queryDicValueByTypeCode("appellation");
        List<DicValue> clueStateList=dicValueService.queryDicValueByTypeCode("clueState");
        List<DicValue> sourceList=dicValueService.queryDicValueByTypeCode("source");
        //数据发送到request上
        request.setAttribute("userList",userList);
        request.setAttribute("appellationList",appellationList);
        request.setAttribute("clueStateList",clueStateList);
        request.setAttribute("sourceList",sourceList);
        //请求转发
        return "workbench/clue/index";
    }

    @RequestMapping("/workbench/clue/saveCreateClue.do")
    @ResponseBody
    public Object saveCreateClue(Clue clue, HttpSession session){
        User user=(User)session.getAttribute(Contants.SESSION_USER);
        //封装参数
        clue.setId(UUIDUtils.getUUID());
        clue.setCreateTime(DateUtils.formateDateTime(new Date()));
        clue.setCreateBy(user.getId());
        ReturnObject returnObject=new ReturnObject();
        //调用service层方法
        try{
            int ret=clueService.saveCreateClue(clue);
            if(ret>0){
                returnObject.setCode(Contants.RETUEN_OBJECT_CODE_SUCCESS);
                returnObject.setRetData(clue);
            }else {
                returnObject.setCode(Contants.RETUEN_OBJECT_CODE_FAIL);
                returnObject.setMessage("线索插入失败。。。");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETUEN_OBJECT_CODE_FAIL);
            returnObject.setMessage("线索插入失败。。。");
        }
        return returnObject;
    }
    @RequestMapping("/workbench/clue/queryClueByConditionForPage.do")
    @ResponseBody
    public Object queryClueByConditionForPage(String fullname,String company,String mphone,String source,String owner,String phone,String state,int pageNo,int pageSize){
        //封装参数
        Map<String,Object> map = new HashMap<>();
        map.put("fullname",fullname);
        map.put("company",company);
        map.put("mphone",mphone);
        map.put("source",source);
        map.put("owner",owner);
        map.put("phone",phone);
        map.put("state",state);
        map.put("beginNo",(pageNo-1)*pageSize);
        map.put("pageSize",pageSize);
        //调用service方法，查询数据
        List<Clue> clueList = clueService.queryClueByConditionForPage(map);
        int totalRows = clueService.queryCountOfClueByCondition(map);
        //根据查询结果，生成响应信息
        Map<String,Object> retMap = new HashMap<>();
        retMap.put("clueList",clueList);
        retMap.put("totalRows",totalRows);
        return retMap;
    }
    @RequestMapping("/workbench/clue/detailClue.do")
    public String detailClue(String id,HttpServletRequest request){
        //调用service层方法,查询数据
        Clue clue = clueService.queryClueById(id);
        List<ClueRemark> remarkList = clueRemarkService.queryClueRemarkForDetailByClueId(id);
        List<Activity> activityList = activityService.queryActivityForDetailByClueId(id);

        request.setAttribute("clue",clue);
        request.setAttribute("remarkList",remarkList);
        request.setAttribute("activityList",activityList);
        return "workbench/clue/detail";
    }

    @RequestMapping("/workbench/clue/queryActivityForDetailByNameClueId.do")
    @ResponseBody
    public Object queryActivityForDetailByNameClueId(String activityName,String clueId){
        //封装参数
        Map<String,Object> map =new HashMap<>();
        map.put("activityName",activityName);
        map.put("clueId",clueId);
        //调用service层方法
        List<Activity> activityList = activityService.queryActivityForDetailByNameClueId(map);
        //根据查询结果返回响应信息
        return activityList;
    }

    @RequestMapping("/workbench/clue/saveBund.do")
    @ResponseBody
    public Object saveBund(String[] activityId,String clueId) {
        //封装参数
        ClueActivityRelation car = null;
        List<ClueActivityRelation> list = new ArrayList<>();
        ReturnObject returnObject=new ReturnObject();
        for (String ai : activityId) {
            car = new ClueActivityRelation();
            car.setClueId(clueId);
            car.setActivityId(ai);
            car.setId(UUIDUtils.getUUID());
            list.add(car);
        }
        //调用serivce方法，批量保存线索和市场活动关联关系
        try {
            int ret = clueActivityRelationService.saveCreateClueActivityRelationList(list);
            if(ret>0){
                returnObject.setCode(Contants.RETUEN_OBJECT_CODE_SUCCESS);
                List<Activity> activityList = activityService.queryActivityForDetailByIds(activityId);
                returnObject.setRetData(activityList);
            }else {
                returnObject.setCode(Contants.RETUEN_OBJECT_CODE_FAIL);
                returnObject.setMessage("关联失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETUEN_OBJECT_CODE_FAIL);
            returnObject.setMessage("关联失败");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/clue/removeClueActivityRelationById.do")
    @ResponseBody
    public Object removeClueActivityRelationById(String clueId,String activityId){
        ReturnObject returnObject= new ReturnObject();
        //封装参数
        ClueActivityRelation clueActivityRelation =new ClueActivityRelation();
        clueActivityRelation.setActivityId(activityId);
        clueActivityRelation.setClueId(clueId);
        //调用service层方法，删除线索和市场活动关联关系
        try{
            int ret = clueActivityRelationService.removeClueActivityRelationByClueIdActivityId(clueActivityRelation);
            if(ret>0){
                returnObject.setCode(Contants.RETUEN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contants.RETUEN_OBJECT_CODE_FAIL);
                returnObject.setMessage("删除失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETUEN_OBJECT_CODE_FAIL);
            returnObject.setMessage("删除失败");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/clue/toConvert.do")
    public String toConvert(String id,HttpServletRequest request){
        //调用service层方法，查询线索的明细信息
        Clue clue=clueService.queryClueById(id);
        List<DicValue> stageList=dicValueService.queryDicValueByTypeCode("stage");
        request.setAttribute("clue",clue);
        request.setAttribute("stageList",stageList);
        //请求转发
        return "workbench/clue/convert";

    }

    @RequestMapping("/workbench/clue/queryActivityForConvertByNameClueId.do")
    @ResponseBody
    public Object queryActivityForConvertByNameClueId(String activityName,String clueId){
        //封装参数
        Map<String,Object> map=new HashMap<>();
        map.put("activityName",activityName);
        map.put("clueId",clueId);
        //调用service层方法,查询市场活动
        List<Activity> activityList=activityService.queryActivityForConvertByNameClueId(map);
        //根据查询结果，返回响应信息
        return activityList;
    }
}
