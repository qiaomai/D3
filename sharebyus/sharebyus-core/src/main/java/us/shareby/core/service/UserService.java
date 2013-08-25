package us.shareby.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.shareby.core.dao.UserDao;
import us.shareby.core.entity.User;

/**
 * User: chengdong
 * Date: 13-8-25
 * Time: 下午5:31
 */
@Service
public interface UserService {


    void register(User user);


}
