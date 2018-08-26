package songshugongyi.action.user;

import songshugongyi.bean.User;
import songshugongyi.util.SQLUtils;
import songshugongyi.util.rongyun.getRongYunToken;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import static songshugongyi.util.getCurrentTime.getTime;

/**
 * Created by yuanopen on 2018/7/24/024.
 */
public class InsertUserToDB {

    public static void insertToDB(User user, Statement stmt) throws SQLException {
        Map<String ,String > stringMap=new HashMap<String, String>();

        stringMap.put("user_id",user.getUserId());
        stringMap.put("user_name",user.getUserName());
        stringMap.put("user_phone",user.getUser_phone());
        stringMap.put("user_password",user.getUserPassward());
        stringMap.put("user_avatar",user.getUserAvatar());
        stringMap.put("user_sex",user.getUserSex()+"");
        //获取融云token
       String usertoken = new getRongYunToken().getToken(user.getUserId(), user.getUser_phone(), user.getUserAvatar());
        stringMap.put("user_token",usertoken);
        stringMap.put("create_time",getTime());
        stringMap.put("update_time",getTime());

        stmt.execute(SQLUtils.InsertModule("user",stringMap));

    }
}
