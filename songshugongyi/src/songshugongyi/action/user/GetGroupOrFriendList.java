package songshugongyi.action.user;

import songshugongyi.SqlManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanopen on 2018/7/25/025.
 */
public class GetGroupOrFriendList {

    public static List<String> getGroupOrFriendList(String table_name,String user_id,String key){
        Connection dbConnection = null;
        Statement stmt = null;

        try {

            dbConnection = SqlManager.getConnection();
            stmt = dbConnection.createStatement();

            // 数据库就已经完全建立起来了。

            String sqlStr = "SELECT * FROM "+table_name+"  WHERE `user_id`='" + user_id + "'";
            stmt.execute(sqlStr);
            List<String>list=new ArrayList<String>();

            ResultSet resultSet = stmt.getResultSet();
            if (resultSet != null && !resultSet.wasNull()) {
                while (resultSet.next())
                    list.add(resultSet.getString(key));

                return list;
            }else
                return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            SqlManager.release(dbConnection, stmt, null);
        }

        return null;
    }

}
