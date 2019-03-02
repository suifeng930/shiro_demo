package com.mhc.springtest.web.controller;

import com.mhc.springtest.entity.User;
import com.mhc.springtest.service.OrganizationService;
import com.mhc.springtest.service.RoleService;
import com.mhc.springtest.service.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.Mergeable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * @author xiaoma
 * @create 2019-03-01 17:00
 */
@Controller
@RequestMapping("/user")
public class UserController {

    //注入 userService organizationService roleService
    @Autowired
    private UserService userService;

    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private RoleService roleService;




    // 用户查看权限
    @RequiresPermissions("user:view")
    @RequestMapping(method = RequestMethod.GET)
    public String list (Model model){

        List<User> userList = userService.findAll();
        model.addAttribute("userList",userList);
        return "user/list";
    }

    //增加用户

    @RequiresPermissions("user:create")
    @RequestMapping(value = "/create",method = RequestMethod.GET)
    public String showCreateForm(Model model){

        setCommonData(model);
        model.addAttribute("user",new User());
        model.addAttribute("op","新增");

        return "user/edit";

    }

    //
    @RequiresPermissions("user:create")
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public  String  create(User user, RedirectAttributes redirectAttributes){

        /**
         * RedirectAttributes是Spring mvc 3.1版本之后出来的一个功能，专门用于重定向之后还能带参数跳转的
         * attr.addFlashAttribute("param", value);
         * 这种方式也能达到重新向带参，而且能隐藏参数，其原理就是放到session中
         * ，session在跳到页面后马上移除对象。所以你刷新一下后这个值就会丢掉
         */
        //向数据库中插入数据
        userService.createUser(user);
        //
        redirectAttributes.addFlashAttribute("msg","新增成功");

        //重定向到user
        return "redirect:/user";

    }

    //到查看修改页面
    @RequiresPermissions("user:update")
    @RequestMapping(value = "/{id}/update",method = RequestMethod.GET)
    public String showUpdateForm(@PathVariable("id") Long id, Model model){

        setCommonData(model);
        model.addAttribute("user",userService.findOne(id));
        model.addAttribute("op","修改");

        return "user/edit";

    }

    //执行修改操作
    @RequiresPermissions("user:update")
    @RequestMapping(value = "/{id}/update",method = RequestMethod.POST)
    public String update(User user,RedirectAttributes attributes){

        userService.updateUser(user);
        attributes.addFlashAttribute("msg","修改成功");

        return "redirect:/user";
    }

    //到删除页面
    @RequiresPermissions("user:delete")
    @RequestMapping(value = "/{id}/delete",method = RequestMethod.GET)
    public String showDelteForm(@PathVariable("id") Long id,Model model){

        setCommonData(model);
        model.addAttribute("user",userService.findOne(id));
        model.addAttribute("op","删除");

        return "user/edit";
    }

    //执行删除操作
    @RequiresPermissions("user:delete")
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        userService.deleteUser(id);
        redirectAttributes.addFlashAttribute("msg", "删除成功");
        return "redirect:/user";
    }



    //到修改密码页面
    @RequiresPermissions("user:update")
    @RequestMapping(value = "/{id}/changePassword",method = RequestMethod.GET)
    public String showChangePasswordForm(@PathVariable("id") Long id,Model model){
        setCommonData(model);
        model.addAttribute("user",userService.findOne(id));
        model.addAttribute("op","修改密码");

        return "user/changePassword";
    }

    @RequiresPermissions("user:update")
    @RequestMapping(value = "/{id}/changePassword",method =RequestMethod.POST)
    public String changePassword(@PathVariable("id") Long id,String newPassword,RedirectAttributes attributes){

        userService.changePassword(id,newPassword);
        attributes.addFlashAttribute("msg","修改密码成功");
        return "redirect:/user";
    }

//    查询用户角色
    private void setCommonData(Model model) {
        model.addAttribute("organizationList", organizationService.findAll());
        model.addAttribute("roleList", roleService.findAll());
    }

}
