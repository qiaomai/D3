/*
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 *
 * Created on Dec 25, 2012 3:50:06 PM 
 */
package us.shareby.core.dao.interceptor;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.Properties;

/**
 * based on database Dialect interceptor query method
 * 
 * @author chengdong
 * @version 2.0
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class})})
public class PagingInterceptor implements Interceptor {

	private static Logger log = LoggerFactory.getLogger(PagingInterceptor.class);

	@Override
	public Object intercept(Invocation invocation) throws Throwable {

		StatementHandler statementHandler = (StatementHandler) invocation
				.getTarget();

		BoundSql boundSql = statementHandler.getBoundSql();

		MetaObject metaStatementHandler = MetaObject
				.forObject(statementHandler);

		RowBounds rowBounds = (RowBounds) metaStatementHandler
				.getValue("delegate.rowBounds");

		String originalSql = (String) metaStatementHandler
				.getValue("delegate.boundSql.sql");

		if (rowBounds == null || rowBounds == RowBounds.DEFAULT) {

			return invocation.proceed();

		}

		Configuration configuration = (Configuration) metaStatementHandler
				.getValue("delegate.configuration");

		String databaseType = null;

		try {

			databaseType = configuration.getVariables().getProperty("dialect")
					.toUpperCase();

		} catch (Exception e) {

			// ignore

		}

		if (databaseType == null) {

			throw new RuntimeException(
					"the value of the dialect property in configuration.xml is not defined : "
							+ configuration.getVariables().getProperty(
									"dialect"));

		}

		Dialect dialect = null;

        if (databaseType.equals("MYSQL")) {
            dialect = new MySQLDialect();
        }

		metaStatementHandler.setValue("delegate.boundSql.sql", dialect
				.getLimitString(originalSql, rowBounds.getOffset(),
						rowBounds.getLimit()));

		metaStatementHandler.setValue("delegate.rowBounds.offset",
				RowBounds.NO_ROW_OFFSET);

		metaStatementHandler.setValue("delegate.rowBounds.limit",
				RowBounds.NO_ROW_LIMIT);

		if (log.isDebugEnabled()) {

			log.debug("Paging list SQL : " + boundSql.getSql());

		}

		return invocation.proceed();
	}
	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {

	}

}
