package com.mhc.spring.realm;

import com.mhc.spring.entity.User;
import com.mhc.spring.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

/**
 * @author xiaoma
 * @create 2019-03-01 11:14
 * 自定义realm
 *
 */
public class UserRealm extends AuthorizingRealm {

    //注入service
//    @Autowired
    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        /**
         * 1.获取传递过来的用户名
         * 2.将用户角色 权限 设置到 simpleAuthorization 对象中
         */

        String username = (String)principals.getPrimaryPrincipal();

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(userService.findRoles(username));
        authorizationInfo.setStringPermissions(userService.findPermissions(username));

        return authorizationInfo;

    }

    //身份认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {


        /**
         * 1.获取 传递过来的用户名
         * 2.去数据库中查找 对应的用户名
         * 3.判断是否查询到用户名，没找到 抛异常
         * 4.判断用户名是否被锁住
         * 5.找到用户名  交给credentialMatcher 进行密码匹配
         * 6.返回simpleAuthenticationinfo对象
         *
         */
        String username = (String) token.getPrincipal();
        User user = userService.findByUsername(username);

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
