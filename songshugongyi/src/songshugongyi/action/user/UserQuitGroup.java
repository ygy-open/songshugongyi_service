package songshugongyi.action.user;

import io.rong.models.group.GroupMember;
import songshugongyi.ResponseObject;
import songshugongyi.SqlManager;
import songshugongyi.action.IAction;
import songshugongyi.bean.JoinGroup;
import songshugongyi.util.FailData;
import songshugongyi.util.GsonUtils;
import songshugongyi.util.SQLUtils;
import songshugongyi.util.getCurrentTime;
import songshugongyi.util.rongyun.grouptalk.RongyunGroup;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import static songshugongyi.util.SuccessData.getData;

/**
 * Created by yuanopen on 2018/7/19/019.
 */
public class UserQuitGroup extends IAction{
    private String TAG="UserQuitGroup";
    public static final String RequestParamKey_Group_Id = "group_id";
    public static final String RequestParamKey_user_id = "user_id";
    Connection dbConnection = null;
    Statement stmt = null;

    @Override
    public void doAction(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        request.setCharacterEncoding("utf-8");
        try {

            String group_id = getParam(request, RequestParamKey_Group_Id, "null");
            String user_id = getParam(request, RequestParamKey_user_id, "null");
            System.out.println(TAG+"----->"+group_id);

            if(group_id.equals("null")){
                FailData.sendFailMessageToClint(TAG,response);
            }else{

                dbConnection = SqlManager.getConnection();
                stmt = dbConnection.createStatement();
                // 数据库就已经完全建立起来了。
                // json转
                // 插入数据
                System.out.println(TAG+"----->"+"开始删除数据");
                insertDb(group_id,user_id,response);

            }


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            SqlManager.release(dbConnection, stmt, null);
        }
    }


    private void insertDb( String group_id,String user_id,HttpServletResponse response) throws SQLException, IOException {


        stmt.execute("delete from user_join_group where group_id='"+group_id+"' and  user_id='"+user_id+"'");

        int updateCount=stmt.getUpdateCount();
        if(updateCount>0){
            // 想融云发送请求,创建者加入群组
            RongyunGroup.getInstance().quitGroup(group_id,
                    new GroupMember[]{new GroupMember(user_id,group_id,null)});
            //返回客服端一个成功码
            ResponseObject responseObject = ResponseObject
                    .getSuccessResponse(getData());
            responseObject.send(response);
        }
    }

}
