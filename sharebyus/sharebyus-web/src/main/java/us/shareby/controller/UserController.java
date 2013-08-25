package us.shareby.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import us.shareby.dao.dataaccess.UserDao;
import us.shareby.dao.interceptor.MySQLDialect;

/**
 * Created with IntelliJ IDEA.
 * User: chengdong
 * Date: 13-8-25
 * Time: 上午10:30
 * To change this template use File | Settings | File Templates.
 */

@Controller
public class UserController {
    MySQLDialect sqlDialect;

    @Autowired
    private UserDao userDao;


    @RequestMapping(value = "/register", method = RequestMethod.GET)
    @ResponseBody
    public String register() {
        return "success";
    }

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public void register(String username,String password){

    }
}
