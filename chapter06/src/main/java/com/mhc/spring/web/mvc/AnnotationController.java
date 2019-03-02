package com.mhc.spring.web.mvc;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author xiaoma
 * @create 2019-03-01 13:49
 */
@Controller
public class AnnotationController {

    @RequestMapping("/hello1")
    public String hello1() {
        SecurityUtils.getSubject().checkRole("admin");
        return "success";
    }


    @RequiresRoles("admin") //当前方法访问权限必须具有 admin角色
    @RequestMapping ("/hello2")
    public String hello2(){

        return "success";
    }


}
