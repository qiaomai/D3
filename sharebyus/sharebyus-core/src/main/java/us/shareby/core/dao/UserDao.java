package us.shareby.core.dao;

import org.springframework.stereotype.Service;
import us.shareby.core.dao.annotation.DataAccessRepository;
import us.shareby.core.entity.User;

/**
 * User: chengdong
 * Date: 13-8-25
 * Time: 上午11:23
 */
@DataAccessRepository
public interface UserDao {

    void register(User user);

}
