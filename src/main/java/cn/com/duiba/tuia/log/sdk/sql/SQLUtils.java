package cn.com.duiba.tuia.log.sdk.sql;

/**
 * @author: <a href="http://www.panaihua.com">panaihua</a>
 * @date: 2017年03月21日 15:12
 * @descript: sql语句工具类
 * @version: 1.0
 */
public class SQLUtils {

    /**
     * 更新语句改为查询语句
     * warn: 使用之前需要判断是否是update语句
     * @param sql
     * @return
     */
    public static String getSelectByUpdate(String sql) {

        sql = sql.startsWith("update") ? sql.replace("update", "select * from ") : sql.replace("UPDATE", "select * from ");
        int set_position = getSetPosition(sql);
        int where_position = getWherePosition(sql);
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append(sql.substring(0,set_position -1)).append(" ").append(sql.substring(where_position));
        return sqlBuffer.toString();
    }

    /**
     * 删除语句改为查询语句
     * warn: 使用之前判断是否是insert语句
     * @param sql
     * @return
     */
    public static String getSelectByDel(String sql){
        return sql.startsWith("delete") ? sql.replace("delete", "select * ") : sql.replace("DELETE", "select * ");
    }

    /**
     * 是否是插入语句
     * @param sql
     * @return
     */
    public static boolean isInsertSql(String sql){
        return sql.startsWith("insert") || sql.startsWith("INSERT");
    }

    public static void main(String args[]){
        System.out.println(isInsertSql("delete from aaa"));
    }

    /**
     * 是否是更新语句
     * @param sql
     * @return
     */
    public static boolean isUpdateSql(String sql){
        return sql.startsWith("update") || sql.startsWith("UPDATE");
    }

    /**
     * 是否是删除语句
     * @param sql
     * @return
     */
    public static boolean isDeleteSql(String sql){
        return sql.startsWith("delete") || sql.startsWith("DELETE");
    }

    private static int getWherePosition(String sql){

        int where_position = sql.indexOf("where");
        if (where_position == -1) {
            where_position = sql.indexOf("WHERE");
        }

        return where_position;
    }

    private static int getSetPosition(String sql){

        int set_position = sql.indexOf("set");
        if (set_position == -1) {
            set_position = sql.indexOf("SET");
        }

        return set_position;
    }
}
