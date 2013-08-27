package us.shareby.core.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import us.shareby.core.constants.NotificationConstants;
import us.shareby.core.dao.ActivateDao;
import us.shareby.core.dao.UserDao;
import us.shareby.core.entity.Activate;
import us.shareby.core.entity.User;
import us.shareby.core.exception.BaseRuntimeException;
import us.shareby.core.exception.ErrorCode;
import us.shareby.core.notification.TransportService;
import us.shareby.core.service.UserService;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * User: chengdong
 * Date: 13-8-25
 */
@Service
public class UserServiceImpl implements UserService{

    private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private ActivateDao activateDao;


    @Autowired
    private TransportService transportService;

    @Override
    public void register(User user) {
        User exist =   queryUser(user.getEmail());
        if(exist!=null){
            throw new BaseRuntimeException(ErrorCode.WARN_NAME_OR_EMAIL_USED);
        }

        //TODO 验证用户邮箱，是否是已开通的公司，防止用户通过接口直接提交数据.
        userDao.register(user);

        Activate activate = new Activate();
        String activateCode = UUID.randomUUID().toString();
        activate.setActivateCode(activateCode);
        activate.setUserId(user.getId());
        activateDao.addActivate(activate);


        Map<String, Object> paramsMap = Maps.newHashMap();
        paramsMap.put("activateUrl","http://shareBy.us/activate/"+activateCode);
        paramsMap.put("receiverName",user.getName());


        transportService.sendMailNotification(null, 0, user.getEmail(), "ShareBy激活邮件",
                NotificationConstants.REGISTER_ACTIVE_NOTIFY_TEMPLATE,
                paramsMap, NotificationConstants.REGISTER_ACTIVE_NOTIFY_TEMPLATE_PLAIN, paramsMap);


    }

    @Override
    public User queryUser(String email) {
        return userDao.queryUser(email);
    }

    @Override
    public void activate(String activateCode) {
        if(Strings.isNullOrEmpty(activateCode)){
            throw new BaseRuntimeException(ErrorCode.WARN_INVALIDATE_ACTIVATE_CODE);
        }
        Activate activate = activateDao.getActivate(activateCode);
        if(activate == null){
            throw new BaseRuntimeException(ErrorCode.WARN_INVALIDATE_ACTIVATE_CODE);
        }
        int activateCount = userDao.activate(activate.getUserId());
        if(activateCount == 0){
            logger.error("user used activate code :"+activateCode +" activate user :"+activate.getUserId() +"failed!");
        }
    }


}
