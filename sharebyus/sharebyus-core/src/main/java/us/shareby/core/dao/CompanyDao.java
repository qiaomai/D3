package us.shareby.core.dao;

import us.shareby.core.dao.annotation.DataAccessRepository;
import us.shareby.core.entity.Company;

/**
 * User: chengdong
 * Date: 13-8-27
 * Time: 下午9:52
 */
@DataAccessRepository
public interface CompanyDao {

    Company queryByEmailSuffix(String emailSuffix);
    void openRegister(Company company);
}
