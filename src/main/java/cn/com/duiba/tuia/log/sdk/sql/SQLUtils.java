package cn.com.duiba.tuia.log.sdk.sql;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import java.util.List;

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

        int set_position = getSetPosition(sql);
        int where_position = getWherePosition(sql);
        StringBuffer sqlBuffer = new StringBuffer("select ");
        String setStrs = sql.substring(set_position + 3,where_position);
        List<String> strings = Splitter.on(",").splitToList(setStrs);
        List<String> fileds = Lists.newArrayListWithCapacity(strings.size());
        for (String s : strings){
            String filed = Splitter.on("=").splitToList(s).get(0);
            fileds.add(filed);
        }

        String tableName = sql.substring(getUpdatePosition(sql) + 6,set_position -1);
        sqlBuffer.append(Joiner.on(",").join(fileds)).append(" from ").append(tableName).append(" ").append(sql.substring(where_position));
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


    private static int getUpdatePosition(String sql){

        int updatePosition = sql.indexOf("update");
        if (updatePosition == -1) {
            updatePosition = sql.indexOf("UPDATE");
        }

        return updatePosition;
    }

    private static int getWherePosition(String sql){

        int wherePosition = sql.indexOf("where");
        if (wherePosition == -1) {
            wherePosition = sql.indexOf("WHERE");
        }

        return wherePosition;
    }

    private static int getSetPosition(String sql){

        int set_position = sql.indexOf("set");
        if (set_position == -1) {
            set_position = sql.indexOf("SET");
        }

        return set_position;
    }

    public static void main(String args[]){

        String sql = "UPDATE\n" +
                "\t\t\tadvert_orientation_package\n" +
                "\t\tSET\n" +
                "\t\t\tfee = #{fee},\n" +
                "\t\t\ta_fee = #{cpaFee},\n" +
                "\t\t\tcharge_type = #{chargeType},\n" +
                "\t\t\tplatform = #{platform},\n" +
                "\t\t\tregion_ids = #{regionIds},\n" +
                "\t\t\tphone_level = #{phoneLevel},\n" +
                "\t\t\tdirectional_num = #{directionalNum},\n" +
                "\t\t\tis_default = #{isDefault},\n" +
                "\t\t\tnetwork_type = #{networkType},\n" +
                "\t\t\toperators = #{operators},\n" +
                "\t\t\tage_region = #{ageRegion},\n" +
                "\t\t\tsex = #{sex},\n" +
                "\t\t\tbanned_tag_nums = #{bannedTagNums}\n" +
                "\t\tWHERE\n" +
                "\t\t\tid = ";

        System.out.println(getSelectByUpdate(sql));
    }
}
