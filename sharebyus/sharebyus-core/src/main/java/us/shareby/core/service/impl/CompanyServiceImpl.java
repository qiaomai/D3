package us.shareby.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.shareby.core.dao.CompanyDao;
import us.shareby.core.entity.Company;
import us.shareby.core.service.CompanyService;

/**
 * User: chengdong
 * Date: 13-8-27
 * Time: 下午9:42
 */
@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyDao companyDao;

    @Override
    public boolean canRegister(String email) {

        Company company = companyDao.queryByEmailSuffix(email.substring(email.indexOf("@")));
        if (company != null) {
            return true;
        }
        return false;
    }

    @Override
    public void openRegister(Company company) {
        //TODO 调用地图api，根据地址获取经纬度信息
        companyDao.openRegister(company);
    }
}
