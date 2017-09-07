package com.future.tsip.common.shiro;

import com.future.tsip.common.Utils;
import com.future.tsip.model.system.User;
import com.future.tsip.service.system.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
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
     * 认证回调函数, 登录时调用.
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

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
