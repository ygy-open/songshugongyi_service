package songshugongyi.action.user;

import io.rong.models.user.UserModel;
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

import static songshugongyi.action.user.GetIsExist.getIsExist;

/**
 * Created by yuanopen on 2018/7/19/019.
 */
public class GetRongyunGroupInfo extends IAction{

    private String TAG="GetRongyunGroupInfo";

    private  final String RequestParamKey_Group_ID= "group_id";
    private static final String RequestParamKey_UserId = "user_id";
    String user_id;

    @Override
    public void doAction(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {

        String group_id=getParam(request,RequestParamKey_Group_ID,"null");
        user_id=getParam(request,RequestParamKey_UserId,"null");
        getGroupInfo(group_id,response);
    }

    /**
     * @fun 通过不同方式验证用户是否注册过
     * @param group_id
     * @return
     */
    public void  getGroupInfo(String group_id,HttpServletResponse response) throws IOException {

        if ("null".equals(group_id)) {
            System.out.println(TAG + "---->服务器接收到数据为空");
            FailData.sendFailMessageToClint(TAG, response);

        } else {

            // 数据库就已经完全建立起来了。

             UserGroup userGroup=getUserGroup(group_id);
            if("null".equals(user_id))
            userGroup.setJoin_group(getIsExist("user_join_group","user_id",user_id,"group_id",group_id));

            if (userGroup != null) {

                System.out.println(TAG + "---->获取成功");
                //查到，并将对象返回客服端
                ResponseObject responseObject = ResponseObject
                        .getSuccessResponse(userGroup);
                responseObject.send(response);
            } else {
                System.out.println(TAG + "---->获取失败");
              FailData.sendFailMessageToClint(TAG,response);

            }
        }
    }

    public static UserGroup getUserGroup(String group_id){
        Connection dbConnection = null;
        Statement stmt = null;

        try {

            dbConnection = SqlManager.getConnection();
            stmt = dbConnection.createStatement();

            // 数据库就已经完全建立起来了。

            String sqlStr = "SELECT * FROM `user_group` WHERE `group_id`='" + group_id + "'";
            stmt.execute(sqlStr);

            ResultSet resultSet = stmt.getResultSet();
            if (resultSet != null && !resultSet.wasNull()&&resultSet.next()) {

                UserGroup group = new UserGroup();

                group.setUser_id(resultSet.getString("user_id"));
                group.setGroup_id(resultSet.getString("group_id"));
                group.setGroup_name(resultSet.getString("group_name"));
                group.setGroup_avatar(resultSet.getString("group_avatar"));
                group.setGroup_signature(resultSet.getString("group_signature"));

                return group;
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
