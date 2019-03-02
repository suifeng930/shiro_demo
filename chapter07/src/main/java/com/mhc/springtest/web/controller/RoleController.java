package com.mhc.springtest.web.controller;

import com.mhc.springtest.entity.Role;
import com.mhc.springtest.service.ResourceService;
import com.mhc.springtest.service.RoleService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author xiaoma
 * @create 2019-03-01 20:43
 * 角色controller
 */
@Controller
@RequestMapping("/role")
public class RoleController {


    //注入service
    @Autowired
    private RoleService roleService;
    @Autowired
    private ResourceService resourceService;

    //查看所有角色

    @RequiresPermissions("role:view")
    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model){

        model.addAttribute("roleList",roleService.findAll());
        return "role/list";
    }

    //跳转新增页面
    @RequiresPermissions("role:create")
    @RequestMapping(value = "/edit",method = RequestMethod.GET)
    public String showCreateRoleForm(Model model){

        setCommonData(model);
        model.addAttribute("role",new Role());
        model.addAttribute("op","新增");
        return "role/edit";
    }
    //新增操作
    public String create(Role role, RedirectAttributes redirectAttributes){


        roleService.createRole(role);
        redirectAttributes.addFlashAttribute("msg","新增成功");

        return "redirect:/role";
    }


    //跳转到修改页面
    @RequiresPermissions("role:update")
    @RequestMapping(value = "/{id}/update",method = RequestMethod.GET)
    public String showUpdateForm(@PathVariable ("id") Long id, Model model){

        setCommonData(model);
        model.addAttribute("role",roleService.findOne(id));
        model.addAttribute("op","修改");

        return "role/edit";
    }


    //修改操作
    @RequiresPermissions("role:update")
    @RequestMapping(value = "/{id}/update",method =RequestMethod.POST)
    public String update(Role role,RedirectAttributes redirectAttributes){

        roleService.updateRole(role);
        redirectAttributes.addFlashAttribute("msg","修改成功");

        return "redirect:/role";
    }

    //跳转到删除页面
    @RequiresPermissions("role:delete")
    @RequestMapping(value = "/{id}/delete",method = RequestMethod.GET)
    public String  showDeleteForm(@PathVariable("id") Long id,Model model){

        setCommonData(model);
        model.addAttribute("role",roleService.findOne(id));
        model.addAttribute("op","删除");

        return "role/edit";

    }

    //删除操作
    @RequiresPermissions("role:delete")
    @RequestMapping(value = "/{id}/delete",method = RequestMethod.POST)
    public String delete(@PathVariable("id") Long id,RedirectAttributes redirectAttributes){

        roleService.deleteRole(id);
        redirectAttributes.addFlashAttribute("msg","删除成功");

        return "redirect:/role";
    }


    private void setCommonData(Model model) {
        model.addAttribute("resourceList", resourceService.findAll());
    }




}
