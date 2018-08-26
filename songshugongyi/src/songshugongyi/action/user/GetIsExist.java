package songshugongyi.action.user;

import songshugongyi.SqlManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanopen on 2018/7/26/026.
 */
public class GetIsExist {

    public static boolean getIsExist(String table_name, String key1,String vaules1,String key2,String vaules2){
        Connection dbConnection = null;
        Statement stmt = null;

        try {

            dbConnection = SqlManager.getConnection();
            stmt = dbConnection.createStatement();
            // 数据库就已经完全建立起来了。

            String sqlStr = "SELECT * FROM "+table_name+"  WHERE "+key1+"='" + vaules1 + "'  and  "+key2+"='"+vaules2+"'";
            stmt.execute(sqlStr);

            ResultSet resultSet = stmt.getResultSet();
            if (resultSet != null && !resultSet.wasNull()&&resultSet.next()) {

                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            SqlManager.release(dbConnection, stmt, null);
        }

        return false;
    }
}
