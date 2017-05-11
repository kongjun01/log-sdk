package cn.com.duiba.tuia.log.sdk.mybatis.plugin;

import cn.com.duiba.tuia.log.sdk.cache.CacheKey;
import cn.com.duiba.tuia.log.sdk.cache.ThreadLocalCache;
import cn.com.duiba.tuia.log.sdk.sql.SQLHelp;
import cn.com.duiba.tuia.log.sdk.sql.SQLUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.util.*;

/**
 * @author: <a href="http://www.panaihua.com">panaihua</a>
 * @date: 2017年03月21日 09:46
 * @descript:
 * @version: 1.0
 */
@Intercepts({@Signature(args = {MappedStatement.class, Object.class}, method = "update", type = Executor.class)})
public class LogMybatisPlugin implements Interceptor {

    private final Logger logger = LoggerFactory.getLogger(LogMybatisPlugin.class);

    private final int MAPPED_STATEMENT_INDEX = 0;
    private final int PARAMETER_INDEX = 1;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        String selectSql = null;
        try {

            MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[MAPPED_STATEMENT_INDEX];
            Object parameter = null;
            if (invocation.getArgs().length > 1) {
                parameter = invocation.getArgs()[PARAMETER_INDEX];
            }
            //得到执行的sql
            String executeSql = this.getExecuteSql(mappedStatement.getConfiguration(), mappedStatement.getBoundSql(parameter));
            executeSql = executeSql.trim();
            if (!SQLUtils.isInsertSql(executeSql)) {
                //得到查询的sql语句
                selectSql = this.getSelectSql(executeSql);
                Executor executor = (Executor) invocation.getTarget();
                List<Map<String, Object>> resultList = SQLHelp.executeSQL(executor.getTransaction(), selectSql);
                this.setOrigin(resultList);
            }

        } catch (Exception e) {
            //捕捉异常，不影响业务流程
            logger.error("获取sql语句错误，需要执行的sql:{}", selectSql, e);
        }

        Object returnObj = invocation.proceed();

        return returnObj;
    }

    /**
     * 向缓存里设置原始数据的值
     *
     * @param resultList
     */
    private void setOrigin(List<Map<String, Object>> resultList) {

        Object origin = ThreadLocalCache.get(CacheKey.ORIGIN_KEY);
        if (origin == null) {
            ThreadLocalCache.put(CacheKey.ORIGIN_KEY, resultList);
            return;
        }

        ((List<Map<String, Object>>) origin).addAll(resultList);
    }

    /**
     * 获取变更之后的select语句
     *
     * @param sql
     * @return
     */
    private String getSelectSql(String sql) {

        if (SQLUtils.isUpdateSql(sql)) {
            sql = SQLUtils.getSelectByUpdate(sql);
        }

        if (SQLUtils.isDeleteSql(sql)) {
            sql = SQLUtils.getSelectByDel(sql);
        }

        return sql;
    }


    /**
     * 替换掉占位符之后的sql
     *
     * @param configuration
     * @param boundSql
     * @return
     */
    public String getExecuteSql(Configuration configuration, BoundSql boundSql) {
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (parameterObject == null || parameterMappings.size() == 0) {
            return sql;
        }

        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
            return sql.replaceFirst("\\?", getParameterValue(parameterObject));
        }

        MetaObject metaObject = configuration.newMetaObject(parameterObject);
        for (ParameterMapping parameterMapping : parameterMappings) {
            String propertyName = parameterMapping.getProperty();
            if (metaObject.hasGetter(propertyName)) {
                Object obj = metaObject.getValue(propertyName);
                sql = sql.replaceFirst("\\?", getParameterValue(obj));
            } else if (boundSql.hasAdditionalParameter(propertyName)) {
                Object obj = boundSql.getAdditionalParameter(propertyName);
                sql = sql.replaceFirst("\\?", getParameterValue(obj));
            }
        }

        return sql;
    }

    private String getParameterValue(Object obj) {
        String value = null;
        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format(new Date()) + "'";
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }

        }
        return value;
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor)
            return Plugin.wrap(target, this);
        return target;
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
