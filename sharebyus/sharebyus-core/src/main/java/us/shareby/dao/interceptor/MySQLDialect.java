package us.shareby.dao.interceptor;

/**
 * @author chengdong
 * @version 2.0
 */
public class MySQLDialect implements Dialect {

	@Override
	public boolean supportsLimit() {
		return true;
	}

	@Override
	public String getLimitString(String sql, int offset, int limit) {
		if (offset > 0) {
			return sql + " limit " + offset + "," + limit;
		} else {
			return sql + " limit " + limit;
		}
	}

}
