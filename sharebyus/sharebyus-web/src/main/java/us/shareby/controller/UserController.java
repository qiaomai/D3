package us.shareby.controller;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import us.shareby.core.constants.NotificationConstants;
import us.shareby.core.dao.UserDao;
import us.shareby.core.dao.interceptor.MySQLDialect;
import us.shareby.core.entity.User;
import us.shareby.core.exception.BaseRuntimeException;
import us.shareby.core.exception.ErrorCode;
import us.shareby.core.notification.TransportService;
import us.shareby.core.service.UserService;
import us.shareby.core.utils.MD5;

import java.util.Map;

/**
 * User: chengdong
 * Date: 13-8-25 10:30
 */

@Controller
public class UserController {
    MySQLDialect sqlDialect;

    @Autowired
    private UserService userService;

    @Autowired
    private MD5 md5;


    @RequestMapping(value = "/register", method = RequestMethod.GET)
    @ResponseBody
    public String register() {
        return "success";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(@ModelAttribute User user) {

        if (!user.validate()) {
            throw new BaseRuntimeException(ErrorCode.WARN_REQUIRED_PARAMS_NULL);
        }
        userService.register(user);


        return new ModelAndView("mail/sent");
    }

    @RequestMapping(value="activate/{activateCode}",method = RequestMethod.GET)
    public ModelAndView activate(@PathVariable String activateCode){
        System.out.println(activateCode);
        userService.activate(activateCode);
        return new ModelAndView("/login");
    }
}
