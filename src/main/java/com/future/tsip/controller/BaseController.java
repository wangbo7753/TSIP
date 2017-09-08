package com.future.tsip.controller;

import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;



public class BaseController implements ApplicationContextAware {

    public static final String SUCCESS = "success";
    public static final String ERROR = "error";

    public Map<String, Object> parseParams(String paramStr) {
        Map<String, Object> param = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(paramStr)) {
            JSONObject jsonObject = JSONObject.fromObject(paramStr);
            Iterator<String> it = jsonObject.keys();
            while (it.hasNext()) {
                String key = it.next();
                String value = jsonObject.getString(key);
                param.put(key, value);
            }
        }
        return param;
    }

    public Map<String, String> parseParams(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        Enumeration<String> keys = request.getParameterNames();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            if (!(key.startsWith("columns") || key.startsWith("search") || key.startsWith("_"))) {
                if (key.startsWith("order")) {
                    if (key.lastIndexOf("column") != -1) {
                        params.put("orderColumn", request.getParameter(String.format("columns[%s][data]", request.getParameter(key))));
                    } else if (key.lastIndexOf("dir") != -1) {
                        params.put("orderDir", request.getParameter(key));
                    }
                } else {
                    params.put(key, request.getParameter(key));
                }
            }
        }
        return params;
    }

    public void setApplicationContext(ApplicationContext arg0) throws BeansException {
        // TODO Auto-generated method stub

    }

}
