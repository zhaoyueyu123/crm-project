package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.DicValueService;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.service.CustomerService;
import com.bjpowernode.crm.workbench.service.TranService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@Controller
public class TranController {
    @Resource
    private DicValueService dicValueService;
    @Resource
    private UserService userService;
    @Resource
    private TranService tranService;
    @Resource
    private CustomerService customerService;
    @RequestMapping("/workbench/transaction/index.do")
    public String index(HttpServletRequest request){
        //调用service方法,查询下拉列表信息
        List<DicValue> transactionTypeList=dicValueService.queryDicValueByTypeCode("transactionType");
        List<DicValue> sourceList=dicValueService.queryDicValueByTypeCode("source");
        List<DicValue> stageList=dicValueService.queryDicValueByTypeCode("stage");
        //把数据保存到作用域
        request.setAttribute("transactionTypeList",transactionTypeList);
        request.setAttribute("sourceList",sourceList);
        request.setAttribute("stageList",stageList);
        //请求转发
        return "workbench/transaction/index";
    }

    @RequestMapping("/workbench/transaction/toSave.do")
    public  String toSave(HttpServletRequest request){
        //调用service层方法,查询下拉列表信息
        List<User> userList = userService.queryAllUsers();
        List<DicValue> transactionTypeList=dicValueService.queryDicValueByTypeCode("transactionType");
        List<DicValue> sourceList=dicValueService.queryDicValueByTypeCode("source");
        List<DicValue> stageList=dicValueService.queryDicValueByTypeCode("stage");
        //把数据保存到作用域
        request.setAttribute("userList",userList);
        request.setAttribute("transactionTypeList",transactionTypeList);
        request.setAttribute("sourceList",sourceList);
        request.setAttribute("stageList",stageList);
        //请求转发
        return "workbench/transaction/save";
    }

    @RequestMapping("/workbench/transaction/queryTranAll.do")
    @ResponseBody
    public Object queryTranAll(){
        List<Tran> tranList=tranService.queryTranAll();
        Map<String,Object> map=new HashMap<>();
        map.put("tranList",tranList);
        return map;
    }

    @RequestMapping("/workbench/transaction/getPossibilityByStage.do")
    @ResponseBody
    public Object getPossibilityByStage(String stageValue){
        //解析properties文件，根据阶段获取可能性
        ResourceBundle bundle=ResourceBundle.getBundle("possibility");
        String possibility=bundle.getString(stageValue);
        return possibility;
    }

    @RequestMapping("/workbench/transaction/queryAllCustomerNameByName.do")
    @ResponseBody
    public Object queryAllCustomerNameByName(String customerName){
        //调用service层方法,查询所有客户名称
        List<String> list=customerService.queryAllCustomerNameByName(customerName);
        return list;
    }

    @RequestMapping("/workbench/transaction/saveCreateTran.do")
    @ResponseBody
    public Object saveCreateTran(@RequestParam Map<String,Object> map, HttpSession session) {
        //封装参数
        map.put(Contants.SESSION_USER, session.getAttribute(Contants.SESSION_USER));
        ReturnObject returnObject=new ReturnObject();
        //调用service方法
        try {
            tranService.saveCreateTran(map);
            returnObject.setCode(Contants.RETUEN_OBJECT_CODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETUEN_OBJECT_CODE_FAIL);
            returnObject.setMessage("交易创建出错");
        }
        return returnObject;
    }
}
