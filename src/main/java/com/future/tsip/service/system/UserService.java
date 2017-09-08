package com.future.tsip.service.system;

import com.future.tsip.model.system.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface UserService {

    User getUserById(String userId);

    void login(String userId, String loginIP);

    void logout(String userId);

    List<UserRole> getUserRole(String user_id);

    List<MenuNode> buildUserMenu(String user_id);

    int updateUser(User user);

    int updateUserIdxUrl(String userId, String userUrl);

    int getAllUsersCount();

    List<User> getAllUsers(Map<String, String> params);

    int addUser(User user, String roleIds);

    int updateUser(User user, String user_role_name);

    int removeUser(String userId);

    List<Role> getRoleList();

    List<Function> getFuncList();

    Function createFunc(Function function);

    int modifyFunc(Function function);

    void deleteFunc(String funcId) throws Exception;

    List<Integer> getFuncIdsByRole(String roleId);

    int createRole(Role role);

    void updatePermission(Map<String, String> params) throws Exception;

    int modifyRole(Role role);

    void deleteRole(String roleId) throws Exception;

    Set<String> getUserRolesAsString(String userId);

    Set<String> getPermissionAsString(String userId);
}
