package com.mhc.springtest.web.shiro;

import com.mhc.Constants;
import com.mhc.springtest.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-2-15
 * <p>Version: 1.0
 */
public class SysUserFilter extends PathMatchingFilter {

    @Autowired
    private UserService userService;

    @Override

    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {

        /**
         * onPreHandle：如果url模式与请求url匹配，那么会执行onPreHandle，并把该拦截器配置的参数传入。默认什么不处理直接返回true。
         */
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        request.setAttribute(Constants.CURRENT_USER, userService.findByUsername(username));
        return true;
    }
}
