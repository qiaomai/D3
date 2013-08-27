package us.shareby.core.service;

import us.shareby.core.entity.Company;

/**
 * User: chengdong
 * Date: 13-8-27
 * Time: 下午9:42
 */
public interface CompanyService {

    boolean canRegister(String email);

    void openRegister(Company company);

}
