package us.shareby.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import us.shareby.core.dao.UserDao;
import us.shareby.core.dao.interceptor.MySQLDialect;
import us.shareby.core.entity.User;
import us.shareby.core.service.UserService;
import us.shareby.core.utils.MD5;

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

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public void register(String username,String password,String email){
        User user = new User();
        user.setName(username);
        user.setPassword(md5.md5(password));
        user.setEmail(email);
        userService.register(user);

    }
}
