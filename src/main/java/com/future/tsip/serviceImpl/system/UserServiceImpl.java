package com.future.tsip.serviceImpl.system;

import com.future.tsip.mapper.system.AuthMapper;
import com.future.tsip.mapper.system.UserMapper;
import com.future.tsip.model.system.Function;
import com.future.tsip.model.system.MenuNode;
import com.future.tsip.model.system.User;
import com.future.tsip.model.system.UserRole;
import com.future.tsip.service.system.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserMapper userMapper;

    private AuthMapper authMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, AuthMapper authMapper) {
        this.userMapper = userMapper;
        this.authMapper = authMapper;
    }

    @Override
    public User getUserById(String userId) {
        User user = null;
        try {
            user = userMapper.getUserById(userId);
        } catch (Exception e) {
            logger.error("getUserById error: ", e);
        }
        return user;
    }

    @Override
    @Transactional
    public void login(String userId, String loginIP) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("loginIP", loginIP);
            params.put("loginDate", new Date());
            userMapper.login(params);
            userMapper.addUserLog(params);
        } catch (Exception e) {
            logger.error("User {} login error {}", userId, e);
            throw e;
        }
    }

    @Override
    public void logout(String userId) {
        try {
            userMapper.logout(userId);
        } catch (Exception e) {
            logger.error("User {} logout error {}", userId, e);
            throw e;
        }
    }

    @Override
    public List<UserRole> getUserRole(String userId) {
        List<UserRole> roles = null;
        try {
            roles = authMapper.getRoleByUser(userId);
        } catch (Exception e) {
            logger.error("User {} getUserRole error {}", userId, e);
        }
        return roles;
    }

    @Override
    public List<MenuNode> buildUserMenu(String userId) {
        List<MenuNode> menus = new ArrayList<>();
        try {
            List<Function> funcList = authMapper.queryFuncByUser(userId);
            if (CollectionUtils.isNotEmpty(funcList)) {
                Map<Integer, List<MenuNode>> map = new HashMap<Integer, List<MenuNode>>();
                for (Function f : funcList) {
                    if (f.getFunc_level().compareTo("2") <= 0) {
                        MenuNode m = new MenuNode(f.getId(), f.getFunc_name(), f.getFunc_url(), f.getShow_order());
                        if (f.getParent_id() != null && f.getParent_id() > 0) {
                            if (map.get(f.getParent_id()) == null) {
                                map.put(f.getParent_id(), new ArrayList<MenuNode>());
                            }
                            map.get(f.getParent_id()).add(m);
                        } else {
                            menus.add(m);
                        }
                    }
                }
                for (MenuNode m : menus) {
                    m.setChildren(map.get(m.getId()));
                    if (m.getChildren() != null) {
                        Collections.sort(m.getChildren());
                    }
                }
                Collections.sort(menus);
            }
        } catch (Exception e) {
            logger.error("build user {}'s menu error {}", userId, e);
        }
        return menus;
    }

    @Override
    public int updateUser(User user) {
        int errCode = 0;
        try {
            userMapper.updateUser(user);
        } catch (Exception e) {
            logger.error("update user {} error {}", user.getUser_id(), e);
            throw e;
        }
        return errCode;
    }

    @Override
    public int updateUserIdxUrl(String userId, String idx_url) {
        int errCode = 0;
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("user_id", userId);
            map.put("idx_url", idx_url);
            userMapper.updateUserUrl(map);
        } catch (Exception e) {
            logger.error("updateUserIdxUrl error: ", e);
            throw e;
        }
        return errCode;
    }

}