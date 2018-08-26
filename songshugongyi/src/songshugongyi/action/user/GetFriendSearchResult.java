package songshugongyi.action.user;

import songshugongyi.ResponseObject;
import songshugongyi.SqlManager;
import songshugongyi.action.IAction;
import songshugongyi.bean.UserFriend;
import songshugongyi.util.FailData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static songshugongyi.action.user.GetGroupOrFriendList.getGroupOrFriendList;

/**
 * Created by yuanopen on 2018/7/24/024.
 */
public class GetFriendSearchResult extends IAction{

    private String TAG="GetFriendSearchResult";
    Connection dbConnection = null;
    Statement stmt = null;
    private List<UserFriend> friendList ;

    private static final String RequestParamKey_UserId = "name";
    String name;
    @Override
    public void doAction(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        request.setCharacterEncoding("utf-8");
        try {

            name=getParam(request,RequestParamKey_UserId,"null");

              if("null".equals(name)){
                      FailData.sendFailMessageToClint(TAG,response);
                      return;
                   }

            dbConnection = SqlManager.getConnection();
            stmt = dbConnection.createStatement();
            // 数据库就已经完全建立起来了。

            friendList=new ArrayList<UserFriend>();
            // 先查询出用户好友id
            // 在根据id查询user

             getFriendListFromDb(name);

            System.out.println(TAG+"----->"+"friendList:"+friendList.size());
            //查到，并将对象返回客服端
            ResponseObject responseObject = ResponseObject
                    .getSuccessResponse(friendList);
            responseObject.send(response);

        } finally {
            SqlManager.release(dbConnection, stmt, null);
        }

    }


    private void getFriendListFromDb(String name) throws SQLException {
        System.out.println(TAG+"----->"+"开始查询");

        String sqlStr = "SELECT * FROM `user` WHERE user_name LIKE  '%"+name+"%'";
        System.out.println(sqlStr);
        stmt.execute(sqlStr);
        ResultSet resultSet = stmt.getResultSet();
        if (resultSet != null && !resultSet.wasNull()) {
            while (resultSet.next()){
                UserFriend friend=new UserFriend();
                friend.setUser_id("");
                friend.setFriend_id(resultSet.getString("user_id"));
                friend.setFriend_name(resultSet.getString("user_name"));
                friend.setFriend_avatar(resultSet.getString("user_avatar"));

                friendList.add(friend);
            }
        }

    }

}
