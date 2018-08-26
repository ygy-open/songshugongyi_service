package songshugongyi.action.user;

import com.google.gson.Gson;
import songshugongyi.Error;
import songshugongyi.ResponseObject;
import songshugongyi.SqlManager;
import songshugongyi.action.IAction;
import songshugongyi.bean.User;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static songshugongyi.util.getCurrentTime.getTime;

/**
 * Created by yuanopen on 2018/5/17/017.
 */
public class UserEdit  extends IAction {


    private static final String RequestParamKey_Data = "data";
    @Override
    public void doAction(HttpServletRequest req, HttpServletResponse response) throws IOException, SQLException {
        Connection dbConnection = null;
        Statement stmt = null;

        req.setCharacterEncoding("utf-8");
        try {
            String data = getParam(req, RequestParamKey_Data, "");

            if(data.equals("")){
                // 查询失败了
                ResponseObject responseObject = ResponseObject
                        .getFailResponse(Error.errorCode_LoginFail,
                                Error.getMsg(500));
                responseObject.send(response);
                return;
            }
            else {
                String json=new String(new BASE64Decoder().decodeBuffer(data),"UTF-8");
                System.out.println(json);
                // 数据库就已经完全建立起来了。
                dbConnection = SqlManager.getConnection();
                stmt = dbConnection.createStatement();
                   User user=getUser(json);

                String sqlStr = "UPDATE  user  SET  "
                        +"user_name='"+user.getUserName()
                        +"',user_age="+user.getUserAge()
                        +",user_sex="+user.getUserSex()
                        +",user_avatar='"+user.getUserAvatar()
                        +"',update_time='"+getTime()+"' where user_id='"+user.getUserId()+"'";

                stmt.execute(sqlStr);
                int updateCount = stmt.getUpdateCount();// 获取受影响的行数
                // 插入成功
                if (updateCount > 0) {
                        //查到，并将对象返回客服端
                        ResponseObject responseObject = ResponseObject
                                .getSuccessResponse(user);
                        responseObject.send(response);

                    } else {
                        // 查询失败了
                        ResponseObject responseObject = ResponseObject
                                .getFailResponse(Error.errorCode_QueryFail,
                                        Error.getQueryFailMsg());
                        responseObject.send(response);
                    }
            }
        }finally{
            SqlManager.release(dbConnection, stmt, null);
        }

    }

    private User getUser(String json){
        User user;
        Gson gson = new Gson();
        user= gson.fromJson(json,  User.class);
        return user;
    }
}

