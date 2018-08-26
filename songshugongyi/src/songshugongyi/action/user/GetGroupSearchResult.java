package songshugongyi.action.user;

import songshugongyi.ResponseObject;
import songshugongyi.SqlManager;
import songshugongyi.action.IAction;
import songshugongyi.bean.UserGroup;
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
import static songshugongyi.action.user.GetIsExist.getIsExist;

/**
 * Created by yuanopen on 2018/7/24/024.
 */
public class GetGroupSearchResult extends IAction{
    private String TAG="GetGroupSearchResult";
    Connection dbConnection = null;
    Statement stmt = null;
    private List<UserGroup> groupList ;

    private static final String RequestParamKey_Content = "name";
    private static final String RequestParamKey_UserId = "user_id";
    String user_id;
    String name;
    @Override
    public void doAction(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {

        request.setCharacterEncoding("utf-8");
        try {

            name=getParam(request,RequestParamKey_Content,"null");
            user_id=getParam(request,RequestParamKey_UserId,"null");
              if("null".equals(name)){
                      FailData.sendFailMessageToClint(TAG,response);
                      return;
                   }

            dbConnection = SqlManager.getConnection();
            stmt = dbConnection.createStatement();
            // 数据库就已经完全建立起来了。
             groupList=new ArrayList<UserGroup>();

            getGroupFromDb(name);

            System.out.println(TAG+"----->"+"list_progress:"+groupList.size());
            //查到，并将对象返回客服端
            ResponseObject responseObject = ResponseObject
                    .getSuccessResponse(groupList);
            responseObject.send(response);

        } finally {
            SqlManager.release(dbConnection, stmt, null);
        }

    }



    private void getGroupFromDb(String group_name) throws SQLException {
        System.out.println(TAG+"----->"+"开始查询");

        String sqlStr = "SELECT * FROM `user_group` WHERE group_name LIKE '%"+group_name+"%'";
        stmt.execute(sqlStr);
        ResultSet resultSet = stmt.getResultSet();
        if (resultSet != null && !resultSet.wasNull()) {
            while (resultSet.next()){
                UserGroup group=new UserGroup();
                group.setUser_id(resultSet.getString("user_id"));
                group.setGroup_id(resultSet.getString("group_id"));
                group.setGroup_name(resultSet.getString("group_name"));
                group.setGroup_avatar(resultSet.getString("group_avatar"));
                group.setGroup_signature(resultSet.getString("group_signature"));
                group.setJoin_group(getIsExist("user_join_group","user_id",user_id,"group_id",group.getGroup_id()));
                groupList.add(group);
            }
        }

    }
}
