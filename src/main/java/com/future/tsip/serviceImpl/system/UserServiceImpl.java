package com.future.tsip.serviceImpl.system;

import com.future.tsip.common.base.ErrCode;
import com.future.tsip.mapper.system.AuthMapper;
import com.future.tsip.mapper.system.UserMapper;
import com.future.tsip.model.system.*;
import com.future.tsip.service.system.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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

    @Override
    public int getAllUsersCount() {
        int count = 0;
        try {
            count = userMapper.getAllUsersCount();
        } catch (Exception e) {
            logger.error("getAllUsersCount error: ", e);
        }
        return count;
    }

    @Override
    public List<User> getAllUsers(Map<String, String> params) {
        List<User> users = null;
        try {
            users = userMapper.getAllUsers(params);
        } catch (Exception e) {
            logger.error("getAllUsers error: ", e);
        }
        return users;
    }

    @Override
    @Transactional
    public int addUser(User user, String roleIds) {
        int errCode = 0;
        try {
            userMapper.addUser(user); // 增加用户
            if (StringUtils.isNotBlank(roleIds)) { // 增加用户角色
                String[] array = roleIds.split(",");
                for (String rid : array) {
                    UserRole entity = new UserRole();
                    entity.setUser_id(user.getUser_id());
                    entity.setRole_id(Integer.valueOf(rid));
                    authMapper.addUserRole(entity);
                }
            }
        } catch (Exception e) {
            logger.error("addUser error: ", e);
            throw e;
        }
        return errCode;
    }

    @Override
    public int updateUser(User user, String roleId) {
        int errCode = 0;
        try {
            userMapper.updateUser(user); // 修改用户信息
            authMapper.removeUserRole(user.getUser_id()); // 删除角色信息
            if (StringUtils.isNotBlank(roleId)) { // 增加用户角色
                String[] array = roleId.split(",");
                for (String rid : array) {
                    UserRole entity = new UserRole();
                    entity.setRole_id(Integer.valueOf(rid));
                    entity.setUser_id(user.getUser_id());
                    authMapper.addUserRole(entity);
                }
            }
        } catch (Exception e) {
            logger.error("updateUser error: ", e);
            throw e;
        }
        return errCode;
    }

    @Override
    public int removeUser(String userId) {
        int errCode = 0;
        try {
            userMapper.removeUser(userId);
            authMapper.removeUserRole(userId); // 删除角色信息
        } catch (Exception e) {
            logger.error("removeUser error: ", e);
            throw e;
        }
        return errCode;
    }

    @Override
    public List<Role> getRoleList() {
        List<Role> roles = null;
        try {
            roles = authMapper.getRolesList();
        } catch (Exception e) {
            logger.error("getRoleList error: ", e);
        }
        return roles;
    }

    @Override
    public List<Function> getFuncList() {
        List<Function> func = null;
        try {
            func = authMapper.getFuncList();
        } catch (Exception e) {
            logger.error("getFuncList error: ", e);
        }
        return func;
    }

    @Override
    public Function createFunc(Function function) {
        try {
            if (authMapper.insertFunc(function) > 0) {
                return function;
            }
        } catch (Exception e) {
            logger.error("create function error: ", e);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFunc(String funcId) throws Exception {
        try {
            authMapper.deleteAuthorityByFid(funcId);
            authMapper.deleteFuncByFid(funcId);
        } catch (Exception e) {
            logger.error("delete function error: ", e);
            throw e;
        }
    }

    @Override
    public int modifyFunc(Function function) {
        int code = 0;
        try {
            authMapper.updateFunc(function);
        } catch (Exception e) {
            logger.error("modify function error: ", e);
            throw e;
        }
        return code;
    }

    @Override
    public List<Integer> getFuncIdsByRole(String roleId) {
        List<Integer> funcIds = null;
        try {
            funcIds = authMapper.queryFuncIdsByRole(roleId);
        } catch (Exception e) {
            logger.error("getFuncByRoleId error: ", e);
        }
        return funcIds;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePermission(Map<String, String> params) throws Exception {
        try {
            if (StringUtils.isNotBlank(params.get("add"))) {
                Map<String, Object> addMap = new HashMap<String, Object>();
                addMap.put("roleId", params.get("roleId"));
                addMap.put("newIds", params.get("add").split(","));
                authMapper.insertNewAuth(addMap);
            }
            if (StringUtils.isNotBlank(params.get("cancel"))) {
                Map<String, Object> cancelMap = new HashMap<String, Object>();
                cancelMap.put("roleId", params.get("roleId"));
                cancelMap.put("deleteIds", params.get("cancel").split(","));
                authMapper.deleteOldAuth(cancelMap);
            }
        } catch (Exception e) {
            logger.error("updatePermission error: ", e);
            throw e;
        }
    }

    @Override
    public int createRole(Role role) {
        int code = 0;
        try {
            if (!authMapper.checkRoleNameExists(role)) {
                authMapper.insertRole(role);
            } else {
                code = ErrCode.ROLE_ALREADY_EXISTS;
            }
        } catch (Exception e) {
            logger.error("createRole error: ", e);
            code = ErrCode.DB_ERROR;
        }
        return code;
    }

    @Override
    public int modifyRole(Role role) {
        int code = 0;
        try {
            if (!authMapper.checkRoleNameExists(role)) {
                authMapper.updateRole(role);
            } else {
                code = ErrCode.ROLE_ALREADY_EXISTS;
            }
        } catch (Exception e) {
            logger.error("modifyRole error: ", e);
            code = ErrCode.DB_ERROR;
        }
        return code;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(String roleId) throws Exception {
        try {
            authMapper.deleteRole(roleId);
            authMapper.deletePermissionByRole(roleId);
            authMapper.deleteUserRole(roleId);
        } catch (Exception e) {
            logger.error("deleteRole error: ", e);
            throw e;
        }
    }

    @Override
    public Set<String> getUserRolesAsString(String userId) {
        Set<String> ret = new HashSet<>();
        List<UserRole> roles = getUserRole(userId);
        if (roles != null) {
            for (UserRole role : roles) {
                ret.add(role.getRole_code());
            }
        }
        return ret;
    }

    @Override
    public Set<String> getPermissionAsString(String userId) {
        Set<String> ret = new HashSet<String>();
        try {
            List<Function> funcList = authMapper.queryFuncByUser(userId);
            Map<Integer, Function> map = new HashMap<Integer, Function>();
            for (Function func : funcList) {
                map.put(func.getId(), func);
            }
            for (Function func : funcList) {
                StringBuffer sb = new StringBuffer();
                do {
                    sb.insert(0, String.format("/%s", func.getFunc_url()));
                    func = map.get(func.getParent_id());
                } while (func != null);
                ret.add(sb.toString());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return ret;
    }

}