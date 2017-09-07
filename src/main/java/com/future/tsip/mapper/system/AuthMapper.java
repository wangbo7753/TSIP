package com.future.tsip.mapper.system;

import com.future.tsip.model.system.Function;
import com.future.tsip.model.system.UserRole;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthMapper {

    List<UserRole> getRoleByUser(String userId);

    List<Function> queryFuncByUser(String userId);
}
