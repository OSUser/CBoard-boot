package org.cboard.controller;

import org.apache.commons.lang.StringUtils;
import org.cboard.dto.CBoardActionLog;
import org.cboard.dto.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

/**
 * Created by zyong on 2017/9/28.
 */
public class BaseController {

    Logger LOG = LoggerFactory.getLogger(this.getClass());


    protected ThreadLocal<User> tlUser = new ThreadLocal<>();

    @Value("${log.negativeFilter}")
    protected String negativeFilter;

    @Value("${log.positveFilter}")
    protected String positveFilter;

    @ModelAttribute
    public void initialAuthUser(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        //User user = authenticationService.getCurrentUser();
        //FIXME 获取当前已登录用户信息
        User user = new User();
        user.setUserId("1");
        user.setName("admin");
        tlUser.set(user);
        String log = new CBoardActionLog(user, url).toString();

        boolean isNegtiveMatch = false, isPositveMatch = true;

        if (StringUtils.isNotBlank(positveFilter)) {
            isPositveMatch = Pattern.compile(positveFilter).matcher(log).find();
        }

        if (StringUtils.isNotBlank(negativeFilter)) {
            isNegtiveMatch = Pattern.compile(negativeFilter).matcher(log).find();
        }

        if (user != null && !isNegtiveMatch && isPositveMatch) {
            LOG.info(log);
        }
    }
}
