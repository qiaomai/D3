package us.shareby.dao.interceptor;

/**
 * @author chengdong
 * @version 2.0
 */
public interface Dialect {

	/**
	 * database supports limit if true then return true else return false
	 * 
	 * @return true：
	 */
	public boolean supportsLimit();

	/**
	 * change sql into limit sql by Dialect
	 * 
	 * @param sql
	 *            SQL
	 * @param offset
	 * 
	 * @param limit
	 * 
	 * @return paging sql
	 */
	public String getLimitString(String sql, int offset, int limit);
}
