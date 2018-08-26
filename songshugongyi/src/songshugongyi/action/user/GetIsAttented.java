package songshugongyi.action.user;

import songshugongyi.SqlManager;
import songshugongyi.bean.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by yuanopen on 2018/7/19/019.
 */
public class GetIsAttented {
    /**
     * @fun 获取用户之间的关注状态
     * @param attented_user_id
     * @param user_id
     * @return false：没有互相关注 反之
     */
   public static boolean isAttented(String attented_user_id,String user_id){
       Connection dbConnection = null;
       Statement stmt = null;

       try {

           dbConnection = SqlManager.getConnection();
           stmt = dbConnection.createStatement();

           // 数据库就已经完全建立起来了。

           String sqlStr = "SELECT * FROM  `attention` WHERE  `attented_user_id`='" + attented_user_id + "'  AND  `user_id`='" + user_id + "'";
           stmt.execute(sqlStr);

           ResultSet resultSet = stmt.getResultSet();
           // 说明存在
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
