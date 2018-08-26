package songshugongyi.action.user;

import songshugongyi.ResponseObject;
import songshugongyi.SqlManager;
import songshugongyi.action.IAction;
import songshugongyi.bean.RongyunToken;
import songshugongyi.bean.User;
import songshugongyi.bean.UserFriend;
import songshugongyi.util.FailData;
import songshugongyi.util.rongyun.getRongYunToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanopen on 2018/7/25/025.
 */
public class GetNewRongyunToken extends IAction {

    private String TAG="GetNewRongyunToken";
    Connection dbConnection = null;
    Statement stmt = null;

    private static final String RequestParamKey_UserId = "user_id";
    private static final String RequestParamKey_UserName= "user_name";
    private static final String RequestParamKey_User_Avatar = "user_avatar";

    @Override
    public void doAction(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {

        request.setCharacterEncoding("utf-8");
        try {
            String user_id,user_name,user_avatar;
            user_id=getParam(request,RequestParamKey_UserId,"null");
            user_avatar=getParam(request,RequestParamKey_User_Avatar," ");
            user_name=getParam(request,RequestParamKey_UserName,"null");

            if("null".equals(user_id)||"null".equals(user_name)){
                FailData.sendFailMessageToClint(TAG,response);
                return;
            }

            dbConnection = SqlManager.getConnection();
            stmt = dbConnection.createStatement();
            // 数据库就已经完全建立起来了。

            RongyunToken token=new RongyunToken();
            token.setUser_id(user_id);
            token.setToken(getRongYunToken.getToken(user_id,user_name,user_avatar));

            UpdateUserToken(token,response);

        } finally {
            SqlManager.release(dbConnection, stmt, null);
        }

    }

    private void UpdateUserToken(RongyunToken token,HttpServletResponse response) throws SQLException, IOException {
        System.out.println(TAG+"----->"+"开始查询");

        String sqlStr = "UPDATE  `user` SET user_token ='"+token.getToken()+"' WHERE user_id='"+token.getUser_id()+"'";
        stmt.execute(sqlStr);
        ResultSet resultSet = stmt.getResultSet();
        if (resultSet != null && !resultSet.wasNull()) {
            System.out.println(TAG+"----->"+"token:"+token.getToken());
            //查到，并将对象返回客服端
            ResponseObject responseObject = ResponseObject
                    .getSuccessResponse(token);
            responseObject.send(response);
            }
        }

}
