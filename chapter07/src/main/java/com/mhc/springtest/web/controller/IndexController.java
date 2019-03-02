package com.mhc.springtest.web.controller;

import com.mhc.springtest.entity.Resource;
import com.mhc.springtest.entity.User;
import com.mhc.springtest.service.ResourceService;
import com.mhc.springtest.service.UserService;
import com.mhc.springtest.web.bind.annotation.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Set;

/**
 * @author xiaoma
 * @create 2019-03-01 16:12
 */
@Controller
public class IndexController {

    @Autowired
    private ResourceService  resourceService;

    @Autowired
    private UserService userService;


    @RequestMapping("/")
    public String  index(@CurrentUser User user, Model model){

        //查询出用户的权限
        Set<String> permissions = userService.findPermissions(user.getUsername());

        List<Resource> menus = resourceService.findMenus(permissions);

        model.addAttribute("menus",menus);

        return "index";
    }

    @RequestMapping("/welcome")
    public String welcome() {
        return "welcome";
    }


}
