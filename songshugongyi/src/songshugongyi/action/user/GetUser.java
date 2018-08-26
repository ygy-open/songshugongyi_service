package songshugongyi.action.user;

import songshugongyi.Error;
import songshugongyi.ResponseObject;
import songshugongyi.SqlManager;
import songshugongyi.bean.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by yuanopen on 2018/7/19/019.
 */
public class GetUser {

    /**
     * @fun 通过id获取用户
     * @param user_id
     * @return
     */
    public static User getUser(String user_id){
        Connection dbConnection = null;
        Statement stmt = null;

        try {

            dbConnection = SqlManager.getConnection();
            stmt = dbConnection.createStatement();

            // 数据库就已经完全建立起来了。

            String sqlStr = "SELECT * FROM `user` WHERE `user_id`='" + user_id + "'";
            stmt.execute(sqlStr);

            ResultSet resultSet = stmt.getResultSet();
            if (resultSet != null && !resultSet.wasNull()&&resultSet.next()) {

                User user = new User();

                user.setUserId(resultSet.getString("user_id"));
                user.setUserName(resultSet.getString("user_name"));

                user.setUserAge(resultSet.getInt("user_age"));
                user.setUserSex(resultSet.getInt("user_sex"));

                String phone = resultSet.getString("user_phone");
                if (phone == null)
                    user.setUser_phone("无");
                else
                    user.setUser_phone(phone);

                user.setUserAvatar(resultSet.getString("user_avatar"));
                user.setUserPassward(resultSet.getString("user_password"));
                user.setUserToken(resultSet.getString("user_token"));

                user.setCreateTime(resultSet.getString("create_time"));
                user.setUpdateTime(resultSet.getString("update_time"));

                user.setUser_attention(resultSet.getInt("user_integral"));
                user.setUser_attention(resultSet.getInt("user_attention"));
                user.setUser_be_attention(resultSet.getInt("user_be_attention"));

                return user;
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
