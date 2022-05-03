package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.sun.deploy.net.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
//@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;
    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin(){
        return "settings/qx/user/login";
    }

    @RequestMapping("/settings/qx/user/login.do")
    @ResponseBody
    public Object login(String loginAct, String loginPwd, String isRemPwd, HttpServletRequest request, HttpServletResponse response, HttpSession session){
        //封装参数
        Map<String,Object> map = new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
        //调用service层方法，查询用户
        User user = userService.queryUserByLoginActAndPwd(map);
        //根据查询结果，生成响应信息
        ReturnObject returnObject = new ReturnObject();

        if(user == null){
            //登录失败，用户名或者密码错误
            returnObject.setCode(Contants.RETUEN_OBJECT_CODE_FAIL);
            returnObject.setMessage("用户名或者密码错误");
        }else{
            //判断账号合法性
            //设置时间格式
            String nowStr = DateUtils.formateDateTime(new Date());
            if(nowStr.compareTo(user.getExpireTime())>0){
                //登录失败，账号已过期
                returnObject.setCode(Contants.RETUEN_OBJECT_CODE_FAIL);
                returnObject.setMessage("账号已过期");
            }else if("0".equals(user.getLockState())){
                //登录失败，状态被锁定
                returnObject.setCode(Contants.RETUEN_OBJECT_CODE_FAIL);
                returnObject.setMessage("状态被锁定");
            }else if(user.getAllowIps().contains(request.getRemoteAddr())){
                //登录失败，ip受限
                returnObject.setCode(Contants.RETUEN_OBJECT_CODE_FAIL);
                returnObject.setMessage("ip受限");
            }else{
                //登录成功
                returnObject.setCode(Contants.RETUEN_OBJECT_CODE_SUCCESS);
                //把user保存到session中
                session.setAttribute(Contants.SESSION_USER,user);
                //如果需要记住密码，则往外写cookie
                if("true".equals(isRemPwd)){
                    //向cookie中保存账号和密码
                    Cookie cookieAct = new Cookie("loginAct",user.getLoginAct());
                    Cookie cookiePwd = new Cookie("loginPwd",user.getLoginPwd());
                    //设置保存的时间
                    cookieAct.setMaxAge(10*24*60*60);
                    cookiePwd.setMaxAge(10*24*60*60);
                    //把cookie返回前台
                    response.addCookie(cookieAct);
                    response.addCookie(cookiePwd);
                }else{
                    //删除cookie
                    Cookie cookieAct = new Cookie("loginAct","1");
                    Cookie cookiePwd = new Cookie("loginPwd","1");
                    //设置保存的时间
                    cookieAct.setMaxAge(0);
                    cookiePwd.setMaxAge(0);
                    //把cookie返回前台
                    response.addCookie(cookieAct);
                    response.addCookie(cookiePwd);
                }
            }

        }
        return returnObject;
    }

    @RequestMapping("/settings/qx/user/logout.do")
    public String logout(HttpServletResponse response,HttpSession session){
        Cookie cookieAct = new Cookie("loginAct","1");
        Cookie cookiePwd = new Cookie("loginPwd","1");

        cookieAct.setMaxAge(0);
        cookiePwd.setMaxAge(0);

        response.addCookie(cookieAct);
        response.addCookie(cookiePwd);
        //session数据销毁
        session.invalidate();
        return "redirect:/";
    }

    @RequestMapping("/settings/qx/user/login1.do")
    @ResponseBody
    public Object login1(User user1){
        //封装参数
        List<String> list = new ArrayList<>();
        ReturnObject returnObject = new ReturnObject();
        String loginAct = user1.getLoginAct();
        String loginPwd = user1.getLoginPwd();
        if("zs".equals(user1.getLoginAct()) || (3 <= loginAct.length() && loginAct.length()<= 5)) {
            if("yf123".equals(user1.getLoginPwd()) || (6 <= loginPwd.length() && loginPwd.length()<= 12)) {
                Map<String, Object> map = new HashMap<>();
                map.put("loginAct", loginAct);
                map.put("loginPwd", loginPwd);
                //调用service层方法，查询用户

                User user = userService.queryUserByLoginActAndPwd(map);

                if(user == null){
                    //登录失败，用户名或者密码错误
                    returnObject.setCode(Contants.RETUEN_OBJECT_CODE_FAIL);
                    returnObject.setMessage("用户不存在");
                }else{
                    returnObject.setCode(Contants.RETUEN_OBJECT_CODE_SUCCESS);
                }
            }else {
                returnObject.setCode(Contants.RETURN_ZHANGHAO);
                returnObject.setMessage("密码格式错误");
            }
        }else {
            returnObject.setCode(Contants.RETURN_ZHANGHAO);
            returnObject.setMessage("账号格式错误");
        }
        //System.out.println(user1.getLoginAct());
        return returnObject;
    }
}
