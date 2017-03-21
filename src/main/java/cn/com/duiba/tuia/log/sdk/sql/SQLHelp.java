package cn.com.duiba.tuia.log.sdk.sql;

import org.apache.ibatis.transaction.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: <a href="http://www.panaihua.com">panaihua</a>
 * @date: 2017年03月21日 16:17
 * @descript:
 * @version: 1.0
 */
public class SQLHelp {

    /**
     * 执行sql语句
     * @param transaction
     * @param sql
     * @return
     * @throws SQLException
     */
    public static List<Map<String, Object>> executeSQL(Transaction transaction, String sql) throws SQLException {

        Connection connection = transaction.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        List<Map<String, Object>> result = new ArrayList();
        while (resultSet.next()) {
            result.add(getResultMap(resultSet));
        }

        return result;
    }

    /**
     * 获取一行记录
     * @param resultSet
     * @return
     * @throws SQLException
     */
    private static Map<String, Object> getResultMap(ResultSet resultSet) throws SQLException {

        ResultSetMetaData resultSetMD = resultSet.getMetaData();
        Map<String, Object> resultMap = new ConcurrentHashMap<>();
        for (int i = 1, len = resultSetMD.getColumnCount(); i <= len; i++) {

            if(resultSetMD.getColumnLabel(i) == null || resultSet.getObject(i) == null){
                continue;
            }

            resultMap.put(resultSetMD.getColumnLabel(i), resultSet.getObject(i));
        }

        return resultMap;
    }

}
