package us.shareby.core.dao;

import us.shareby.core.dao.annotation.DataAccessRepository;
import us.shareby.core.entity.Activate;

/**
 * User: chengdong
 * Date: 13-8-25
 * Time: 下午10:35
 */
@DataAccessRepository
public interface ActivateDao {

    void addActivate(Activate activate);

    Activate getActivate(String activateCode);
}
