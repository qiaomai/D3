package us.shareby.core.dao;

import org.springframework.stereotype.Service;
import us.shareby.core.dao.annotation.DataAccessRepository;
import us.shareby.core.entity.User;

import java.util.Map;

/**
 * User: chengdong
 * Date: 13-8-25
 * Time: 上午11:23
 */
@DataAccessRepository
public interface UserDao {

    void register(User user);

    User queryUser(Map<String,String> parameters);

    int activate(long userId);
}
