package com.mhc.springtest.web.controller;

import com.mhc.springtest.entity.Resource;
import com.mhc.springtest.service.ResourceService;
import javafx.geometry.Pos;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author xiaoma
 * @create 2019-03-01 21:29
 */

@Controller
@RequestMapping("/resource")
public class ResourceController {


    @Autowired
    private ResourceService resourceService;

    /**
     *
     */
    @ModelAttribute("types")
    public Resource.ResourceType[] resourceTypes() {
        return Resource.ResourceType.values();
    }


    //跳转到资源页面
    @RequiresPermissions("resource:view")
    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model){

        model.addAttribute("resourceList",resourceService.findAll());
        return "resource/list";
    }

    //跳转到 新增资源页面

    @RequiresPermissions("resource:create")
    @RequestMapping(value = "/{parentId}/appendChild",method = RequestMethod.GET)
    public String showAppendChildForm(@PathVariable("parentId") Long parentId, Model model){

        Resource parent = resourceService.findOne(parentId);//查找出 parent
        model.addAttribute("parent",parent);
        //构建新节点
        Resource child = new Resource();
        child.setParentId(parentId);
        child.setParentIds(parent.makeSelfAsParentIds());
        model.addAttribute("child",child);
        model.addAttribute("op","新增子节点");

        return"resource/edit";
    }

    //新增操作
    @RequiresPermissions("resource:create")
    @RequestMapping(value = "/{parentId}/appendChild",method = RequestMethod.POST)
    public String create(Resource resource, RedirectAttributes redirectAttributes){

        resourceService.createResource(resource);
        redirectAttributes.addFlashAttribute("msg","新增子节点成功");

        return "redirect:/resource";
    }

    //跳转到修改页面
    @RequiresPermissions("resource:update")
    @RequestMapping(value = "/{id}/update",method = RequestMethod.GET)
    public String showUpdateForm(@PathVariable("id") Long id,Model model){


        model.addAttribute("resource",resourceService.findOne(id));
        model.addAttribute("op","修改");
         return "resource/edit";
    }

    @RequiresPermissions("resource:update")
    @RequestMapping(value = "/{id}/update",method = RequestMethod.POST)
    public String update(Resource resource,RedirectAttributes redirectAttributes){

        resourceService.updateResource(resource);
        redirectAttributes.addFlashAttribute("msg","修改成功");

        return "redirect:resource";
    }


    @RequiresPermissions("resource:delete")
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        resourceService.deleteResource(id);
        redirectAttributes.addFlashAttribute("msg", "删除成功");
        return "redirect:/resource";
    }







}
