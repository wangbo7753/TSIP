package com.future.tsip.controller.system;

import com.future.tsip.common.DataTable;
import com.future.tsip.common.Utils;
import com.future.tsip.common.base.ErrCode;
import com.future.tsip.model.system.Role;
import com.future.tsip.model.system.User;
import com.future.tsip.service.system.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述:用户管理Controller.
 */
@Controller
@RequestMapping(value = "/system/userManage")
public class UserManageController {

	private static Logger logger = LoggerFactory.getLogger(UserManageController.class);

	private UserService userService;

	@Autowired
	public UserManageController(UserService userService) {
	    this.userService = userService;
	}

	@RequestMapping(value = "/list", method = { RequestMethod.GET })
    @ResponseBody
	public DataTable<User> userManageList(HttpServletRequest request) {
	    Map<String, String> params = new HashMap<>();
	    params.put("start", request.getParameter("start"));
	    params.put("length", request.getParameter("length"));
	    DataTable<User> dt = new DataTable<>();
	    int total = userService.getAllUsersCount();
        dt.setRecordsTotal(total);
        dt.setRecordsFiltered(total);
        dt.setData(userService.getAllUsers(params));
        dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
        return dt;
    }

	@ResponseBody
	@RequestMapping(value = "/create", method = { RequestMethod.POST })
	public Map<String, Object> userManageNew(@RequestBody User user) {
		logger.info("增加用户信息");
		int code;
		if (user != null && StringUtils.isNotBlank(user.getUser_id())) {
		    Date date = new Date();
            user.setUser_pass(Utils.enPassword(user.getUser_pass()));
            user.setLast_login(date);
            user.setReg_date(date);
            String roleIds = user.getUser_role_name();
            user.setUser_role(0);
            code = userService.addUser(user, roleIds);
        } else {
		    code = ErrCode.INCOMPLETE_INFO;
        }
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("code", code);
		ret.put("msg", ErrCode.getMessage(code));
		return ret;
	}

	@ResponseBody
	@RequestMapping(value = "/modify", method = { RequestMethod.POST })
	public Map<String, Object> userManageEdit(@RequestBody User user) {
		logger.info("编辑用户信息");
		int code = 0;
		if (user != null && StringUtils.isNotBlank(user.getUser_id())) {
            user.setUser_pass(Utils.enPassword(user.getUser_pass()));
            code = userService.updateUser(user, user.getUser_role_name());
        } else {
		    code = ErrCode.INCOMPLETE_INFO;
        }
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("code", code);
		ret.put("msg", ErrCode.getMessage(code));
		return ret;
	}

	@ResponseBody
	@RequestMapping(value = "/delete", method = { RequestMethod.POST })
	public Map<String, Object> userManageDelete(@RequestParam(value = "userId") String userId) {
		logger.info("删除用户信息");
		int code = userService.removeUser(userId);
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("code", code);
		ret.put("msg", ErrCode.getMessage(code));
		return ret;
	}

	@ResponseBody
	@RequestMapping(value = "/find", method = { RequestMethod.POST })
	public Map<String, Object> userManageFind(@RequestParam(value = "userId") String userId) {
		logger.info("查询单独用户信息");
		User user = userService.getUserById(userId);
		int code = 0;
		if (user != null) {
			code = ErrCode.USER_EXIST_ALREADY;
		}
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("code", code);
		ret.put("msg", ErrCode.getMessage(code));
		return ret;
	}

	@ResponseBody
	@RequestMapping(value = "/getRolesList", method = { RequestMethod.GET })
	public List<Role> getRolesList() {
		return userService.getRoleList();
	}

}
