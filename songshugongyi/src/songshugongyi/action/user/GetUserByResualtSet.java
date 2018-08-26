package songshugongyi.action.user;

import songshugongyi.Error;
import songshugongyi.ResponseObject;
import songshugongyi.bean.User;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by yuanopen on 2018/7/23/023.
 */
public class GetUserByResualtSet {
    public static void   getUserByResualtSet(ResultSet resultSet, HttpServletResponse response) throws SQLException, IOException {

        if (resultSet != null && !resultSet.wasNull()&&resultSet.next()) {

            System.out.println(resultSet.toString()+"找到了");
            User user = new User();
            user.setUserId(resultSet.getString("user_id"));
//                    user.setUserName(resultSet.getString("user_phone"));
            user.setUser_phone(resultSet.getString("user_phone"));
            user.setUserAge(resultSet.getInt("user_age"));
            user.setUserSex(resultSet.getInt("user_sex"));

            // 登录是用手机号登录的，用户名只作为昵称来用，如果为空，默认为手机号
            String user_name=resultSet.getString("user_name");
            if(user_name==null)
                user.setUserName(user.getUser_phone());
            else
                user.setUserName(user_name);

            user.setUserAvatar(resultSet.getString("user_avatar"));
            user.setUserPassward(resultSet.getString("user_password"));
            user.setUserToken(resultSet.getString("user_token"));

            user.setCreateTime(resultSet.getString("create_time"));
            user.setUpdateTime(resultSet.getString("update_time"));

//                    user.setUser_signature(resultSet.getString("user_signature"));
            String user_signature=resultSet.getString("user_signature");
            if(user_signature==null)
                user.setUser_signature("无");
            else
                user.setUser_signature(user_signature);

            user.setUser_attention(resultSet.getInt("user_attention"));
            user.setUser_be_attention(resultSet.getInt("user_be_attention"));

            //查到，并将对象返回客服端
            ResponseObject responseObject = ResponseObject
                    .getSuccessResponse(user);
            responseObject.send(response);

        } else {
            // 查询失败了
            ResponseObject responseObject = ResponseObject
                    .getFailResponse(Error.errorCode_LoginFail,
                            Error.getMsg(500));
            responseObject.send(response);
        }
    }
}
