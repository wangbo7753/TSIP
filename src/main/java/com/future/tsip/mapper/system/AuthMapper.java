package com.future.tsip.mapper.system;

import com.future.tsip.model.system.Function;
import com.future.tsip.model.system.Role;
import com.future.tsip.model.system.UserRole;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AuthMapper {

    List<UserRole> getRoleByUser(String userId);

    List<Function> queryFuncByUser(String userId);

    void addUserRole(UserRole entity);

    void removeUserRole(String userId);

    List<Role> getRolesList();

    List<Function> getFuncList();

    int insertFunc(Function function);

    void deleteAuthorityByFid(String funcId);

    void deleteFuncByFid(String funcId);

    void updateFunc(Function function);

    List<Integer> queryFuncIdsByRole(String roleId);

    void insertNewAuth(Map<String, Object> addMap);

    void deleteOldAuth(Map<String, Object> cancelMap);

    boolean checkRoleNameExists(Role role);

    void insertRole(Role role);

    void updateRole(Role role);

    void deleteRole(String roleId);

    void deletePermissionByRole(String roleId);

    void deleteUserRole(String roleId);
}
