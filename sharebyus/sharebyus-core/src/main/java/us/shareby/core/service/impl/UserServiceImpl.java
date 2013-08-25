package us.shareby.core.service.impl;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import us.shareby.core.constants.NotificationConstants;
import us.shareby.core.dao.UserDao;
import us.shareby.core.entity.User;
import us.shareby.core.service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * User: chengdong
 * Date: 13-8-25
 */
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserDao userDao;

    @Value("${mail.notification.pwd}")
    private String mailPassword;

    @Override
    public void register(User user) {


    }




}
