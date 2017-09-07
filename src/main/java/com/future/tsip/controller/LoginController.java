package com.future.tsip.controller;

import com.future.tsip.common.base.ErrCode;
import com.future.tsip.model.system.User;
import com.future.tsip.service.system.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("login")
    public String login() {
        return "login";
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> login(@RequestBody Map<String, String> params, HttpServletRequest request) {
        String userId = params.get("userId");
        String password = params.get("password");
        String remember = params.get("remember");
        int errCode = 0;
        logger.info("login user is: " + userId);
        if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(password)) {
            Subject currentUser = SecurityUtils.getSubject();
            if (currentUser.isAuthenticated()) {
                errCode = ErrCode.USER_EXIST_ALREADY;
            } else {
                UsernamePasswordToken token = new UsernamePasswordToken(userId, password);
                if ("checked".equals(remember)) {
                    token.setRememberMe(true);
                }
                try {
                    currentUser.login(token);
                    userService.login(userId, request.getRemoteAddr());
                } catch (UnknownAccountException e) {
                    errCode = ErrCode.USER_NOT_EXIST;
                } catch (IncorrectCredentialsException e) {
                    errCode = ErrCode.PASSWORD_ERROR;
                } catch (Exception e) {
                    logger.error("login error: ", e);
                    errCode = ErrCode.DB_ERROR;
                }
            }
        } else {
            errCode = ErrCode.USER_PASSWORD_FORMAT_ERROR;
        }
        Map<String, Object> result = new HashMap<>();
        result.put("errCode", errCode);
        result.put("message", ErrCode.getMessage(errCode));
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "logout", method = {RequestMethod.GET})
    public void logout(HttpServletResponse response, HttpSession httpSession) throws IOException {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            try {
                User user = (User) httpSession.getAttribute("AUTH_USER");
                if (user != null) {
                    logger.info("登出用户:" + user.getUser_id());
                    userService.logout(user.getUser_id());
                }
                subject.logout();
            } catch (Exception e) {
                logger.error("logout error: ", e);
            }
        }
        response.sendRedirect("login");
    }

}
