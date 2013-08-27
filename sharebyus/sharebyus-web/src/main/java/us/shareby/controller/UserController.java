package us.shareby.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import us.shareby.core.dao.interceptor.MySQLDialect;
import us.shareby.core.entity.User;
import us.shareby.core.exception.BaseRuntimeException;
import us.shareby.core.exception.ErrorCode;
import us.shareby.core.service.CompanyService;
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
    private CompanyService companyService;

    @Autowired
    private MD5 md5;

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView register() {
        return new ModelAndView("/register");

    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(@ModelAttribute User user) {

        if (!user.validate()) {
            throw new BaseRuntimeException(ErrorCode.WARN_REQUIRED_PARAMS_NULL);
        }
        user.setPassword(md5.md5(user.getPassword()));
        userService.register(user);
        return new ModelAndView("mail/sent");
    }

    @RequestMapping(value = "activate/{activateCode}", method = RequestMethod.GET)
    public ModelAndView activate(@PathVariable String activateCode) {
        System.out.println(activateCode);
        userService.activate(activateCode);
        return new ModelAndView("/activate");
    }

    @RequestMapping(value = "/email",method = RequestMethod.GET)
    @ResponseBody
    public void emailAvailable(@RequestParam(value="email", required = true) String email){

        //TODO 页面根据此错误码引导用户去申请开通公司
        if(!companyService.canRegister(email)){
            throw new BaseRuntimeException(ErrorCode.WARN_COMPANY_NOT_OPEN);
        }

        User user = userService.queryUser(email);
        //TODO 引导用户重新发送激活邮件.
        if(user !=null){
            throw new BaseRuntimeException(ErrorCode.WARN_EMAIL_USED);
        }

    }
}
