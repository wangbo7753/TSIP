package com.future.tsip.common.shiro;

import com.future.tsip.common.Utils;
import com.future.tsip.model.system.User;
import com.future.tsip.service.system.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class ShiroDbRealm extends AuthorizingRealm {

    private static final Logger log = LoggerFactory.getLogger(ShiroDbRealm.class);

    private static final String authorizationCacheName = "shiro-authorizationCacheName";

    @Autowired
    private UserService userService;

    public ShiroDbRealm() {
        // 认证不缓存
        super.setAuthenticationCachingEnabled(false);
        // 授权
        super.setAuthorizationCacheName(authorizationCacheName);
    }

    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
     * 第一次需要授权的时候调用此方法将授权存入缓存，后续验证授权的时候不再访问本方法，只读取缓存中的权限即可
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        if (!SecurityUtils.getSubject().isAuthenticated()) {
            doClearCache(principals);
            SecurityUtils.getSubject().logout();
            return null;
        }
        // 获取登陆时输入的用户名
        String userId = (String) principals.getPrimaryPrincipal();
        if (StringUtils.isNotBlank(userId)) {
            // 权限信息对象info,用来存放查出来的用户的所有角色（role）及权限（permission）
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            // 用户的角色集合
            info.addRoles(userService.getUserRolesAsString(userId));
            // 用户的权限集合
            info.addStringPermissions(userService.getPermissionAsString(userId));
            return info;
        }
        return null;
    }

    /**
     * 认证回调函数, 登录时调用.
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authToken) throws AuthenticationException {
        // UsernamePasswordToken对象用来存放提交的登录信息
        UsernamePasswordToken token = (UsernamePasswordToken) authToken;
        User user = userService.getUserById(token.getUsername());
        if (user != null) {
            String password = Utils.dePassword(user.getUser_pass());
            if (password.equals(String.valueOf(token.getPassword()))) {
                // 若存在，将此用户存放到登录认证info中
                AuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user.getUser_id(), password, getName());
                // doGetAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());//因为在验证后不自动调用doGetAuthorizationInfo,所以采用此方法
                setSession("AUTH_USER", user);
                return authenticationInfo;
            } else {
                throw new IncorrectCredentialsException();
            }
        } else {
            return null;
        }
    }

    private void setSession(Object key, Object value) {
        Subject currentUser = SecurityUtils.getSubject();
        if (null != currentUser) {
            Session session = currentUser.getSession();
            if (null != session) {
                session.setAttribute(key, value);
            }
        }
    }
}
