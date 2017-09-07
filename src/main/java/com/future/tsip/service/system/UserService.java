package com.future.tsip.service.system;

import com.future.tsip.model.system.MenuNode;
import com.future.tsip.model.system.User;
import com.future.tsip.model.system.UserRole;

import java.util.List;

public interface UserService {

    User getUserById(String userId);

    void login(String userId, String loginIP);

    void logout(String userId);

    List<UserRole> getUserRole(String user_id);

    List<MenuNode> buildUserMenu(String user_id);

    int updateUser(User user);

    int updateUserIdxUrl(String userId, String userUrl);
}
