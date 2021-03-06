package com.mhc.springtest.realm;

import com.mhc.springtest.entity.User;
import com.mhc.springtest.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author xiaoma
 * @create 2019-03-01 15:27
 */
public class UserRealm extends AuthorizingRealm {

    //注入service
    @Autowired
    private UserService userService;


    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        /**
         * 1.获取 传递过来的username
         * 2.将 数据库中查询到的 角色 权限   设置到AuthenticationInfo对象中 并返回
         */
        String username = (String)principals.getPrimaryPrincipal();

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(userService.findRoles(username));  //将查询到的角色信息设置到 AuthenticationInfo对象中
        authorizationInfo.setStringPermissions(userService.findPermissions(username)); //同理
        return authorizationInfo;
    }

    //身份认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        /**
         * 1.获取传递过来的username
         * 2.去数据库中查找 对应的username
         * 3.做判断 没有匹配到账号， 账号被锁定
         * 4.认证通过  返回 AuthenticationInfo对象
         */

            String username = (String)token.getPrincipal();

            User user = userService.findByUsername(username); //查询用户信息

            if(user == null) {
                throw new UnknownAccountException();//没找到帐号
            }

            if(Boolean.TRUE.equals(user.getLocked())) {
                throw new LockedAccountException(); //帐号锁定
            }

            //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以自定义实现
            SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                    user.getUsername(), //用户名
                    user.getPassword(), //密码
                    ByteSource.Util.bytes(user.getCredentialsSalt()),//salt=username+salt
                    getName()  //realm name
            );
            return authenticationInfo;
        }

        @Override
        public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
            super.clearCachedAuthorizationInfo(principals);
        }

        @Override
        public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
            super.clearCachedAuthenticationInfo(principals);
        }

        @Override
        public void clearCache(PrincipalCollection principals) {
            super.clearCache(principals);
        }

        public void clearAllCachedAuthorizationInfo() {
            getAuthorizationCache().clear();
        }

        public void clearAllCachedAuthenticationInfo() {
            getAuthenticationCache().clear();
        }

        public void clearAllCache() {
            clearAllCachedAuthenticationInfo();
            clearAllCachedAuthorizationInfo();
        }
}
