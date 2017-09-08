package com.future.tsip.mapper.system;

import com.future.tsip.model.system.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserMapper {

    User getUserById(String userId);

    void login(Map<String, Object> params);

    void addUserLog(Map<String, Object> params);

    void logout(String userId);

    void updateUser(User user);

    void updateUserUrl(Map<String, Object> map);

    int getAllUsersCount();

    List<User> getAllUsers(Map<String, String> params);

    void addUser(User user);

    void removeUser(String userId);

}
