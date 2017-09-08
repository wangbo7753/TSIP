package com.future.tsip.controller.system;

import com.future.tsip.common.DataTable;
import com.future.tsip.common.base.ErrCode;
import com.future.tsip.controller.BaseController;
import com.future.tsip.model.system.Function;
import com.future.tsip.model.system.Role;
import com.future.tsip.service.system.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述:用户权限管理Controller.
 */
@Controller
@RequestMapping(value = "/system/authManage")
public class AuthManageController extends BaseController {

	private static Logger logger = LoggerFactory.getLogger(AuthManageController.class);

	@Autowired
	private UserService userService;

	@RequestMapping(value = "getRoleList", method = { RequestMethod.GET })
	@ResponseBody
	public DataTable<Role> getRoleList(HttpServletRequest request, HttpSession httpSession) {
		logger.info("查询所有角色url");
        DataTable<Role> view = new DataTable<>();
        view.setData(userService.getRoleList());
        return view;
	}
	
	@RequestMapping(value = "getFuncList", method = { RequestMethod.GET })
	@ResponseBody
	public List<Function> getFunctionList() {
		logger.info("查询功能项");
        return userService.getFuncList();
	}

	@RequestMapping(value = "createFunc", method = { RequestMethod.POST })
	@ResponseBody
	public Function createFunction(@RequestBody Function function) {
		logger.info("新建功能项");
		if (function != null) {
            return userService.createFunc(function);
        }
        return null;
	}

    @RequestMapping(value = "deleteFunc", method = { RequestMethod.GET })
    @ResponseBody
    public Map<String, Object> deleteFunction(@RequestParam("id") String funcId) {
        logger.info("删除功能项");
        int code = 0;
        if (StringUtils.isNotBlank(funcId)) {
            try {
                userService.deleteFunc(funcId);
            } catch (Exception e) {
                code = ErrCode.DB_ERROR;
            }
        } else {
        	code = ErrCode.INCOMPLETE_INFO;
		}
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;
    }

    @RequestMapping(value = "modifyFunc", method = {RequestMethod.POST})
	@ResponseBody
	public Map<String, Object> modifyFunction(@RequestBody Function function) {
	    logger.info("修改功能项");
		int code;
		if (function != null && function.getId() > 0) {
			code = userService.modifyFunc(function);
		} else {
			code = ErrCode.INCOMPLETE_INFO;
		}
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("code", code);
		ret.put("msg", ErrCode.getMessage(code));
		return ret;
	}

	@RequestMapping(value = "getPermission", method = {RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> getPermission(@RequestParam("roleId") String roleId) {
	    logger.info("获取角色权限");
        List<Integer> keys = null;
        if (StringUtils.isNotBlank(roleId)) {
	        keys = userService.getFuncIdsByRole(roleId);
        }
	    Map<String, Object> ret = new HashMap<String, Object>();
	    ret.put("funcIds", keys);
	    return ret;
    }

    @RequestMapping(value = "updatePermission", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> permission(@RequestBody Map<String, String> params) {
        logger.info("更新角色权限");
        int code = 0;
		if (params != null && StringUtils.isNotBlank(params.get("roleId"))) {
            try {
                userService.updatePermission(params);
            } catch (Exception e) {
                code = ErrCode.DB_ERROR;
            }
        } else {
		    code = ErrCode.INCOMPLETE_INFO;
        }
        Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("code", code);
		ret.put("msg", ErrCode.getMessage(code));
        return ret;
    }

    @RequestMapping(value = "createRole", method = { RequestMethod.POST })
    @ResponseBody
    public Map<String, Object> createRole(@RequestBody Role role) {
        logger.info("新建角色");
        Map<String, Object> ret = new HashMap<String, Object>();
        int code;
        if (role != null && StringUtils.isNotBlank(role.getRole_name())) {
            code = userService.createRole(role);
            if (code == ErrCode.SUCCESS) {
                ret.put("data", role);
            }
        } else {
            code = ErrCode.INCOMPLETE_INFO;
        }
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;
    }

    @RequestMapping(value = "modifyRole", method = { RequestMethod.POST })
    @ResponseBody
    public Map<String, Object> modifyRole(@RequestBody Role role) {
        logger.info("修改角色");
        int code;
        if (role != null && role.getRole_id() != 0 && StringUtils.isNotBlank(role.getRole_name())) {
            code = userService.modifyRole(role);
        } else {
            code = ErrCode.INCOMPLETE_INFO;
        }
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;
    }

    @RequestMapping(value = "deleteRole", method = { RequestMethod.GET })
    @ResponseBody
    public Map<String, Object> deleteRole(@RequestParam("roleId") String roleId) {
        logger.info("删除角色");
        int code = ErrCode.SUCCESS;
        if (StringUtils.isNotBlank(roleId)) {
            try {
                userService.deleteRole(roleId);
            } catch (Exception e) {
                code = ErrCode.DB_ERROR;
            }
        } else {
            code = ErrCode.INCOMPLETE_INFO;
        }
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;
    }

}
