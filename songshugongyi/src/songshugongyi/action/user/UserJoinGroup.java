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
public class UserJoinGroup extends IAction{
    private String TAG="UserJoinGroup";
    private static final String RequestParamKey_JSON= "data";
    Connection dbConnection = null;
    Statement stmt = null;

    @Override
    public void doAction(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        request.setCharacterEncoding("utf-8");
        try {

            String data = getParam(request, RequestParamKey_JSON, "null");
            System.out.println(TAG+"----->"+data);
            String json=new String(new BASE64Decoder().decodeBuffer(data),"UTF-8");
            System.out.println(TAG+"----->"+json);
            if(json.equals("null")){
                FailData.sendFailMessageToClint(TAG,response);
            }else{

                System.out.println(TAG+"----->"+"json获取成功");
                dbConnection = SqlManager.getConnection();
                stmt = dbConnection.createStatement();
                // 数据库就已经完全建立起来了。
                // json转实体
                JoinGroup group= GsonUtils.jsonToModule(json,JoinGroup.class);
                // 插入数据
                System.out.println(TAG+"----->"+"开始插入数据");

                insertDb(group);

                //返回客服端一个成功码
                ResponseObject responseObject = ResponseObject
                        .getSuccessResponse(getData());
                responseObject.send(response);
            }


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            SqlManager.release(dbConnection, stmt, null);
        }
    }


    private void insertDb( JoinGroup group) throws SQLException {

        Map<String ,String > stringMap=new HashMap<String, String>();
        stringMap.put("group_id",group.getGroup_id());
        stringMap.put("user_id",group.getUser_id());
        stringMap.put("create_time", getCurrentTime.getTime());

        stmt.execute(SQLUtils.InsertModule("user_join_group",stringMap));

        int updateCount=stmt.getUpdateCount();
        if(updateCount>0){
            // 想融云发送请求,创建者加入群组
            RongyunGroup.getInstance().JoinGroup(group.getGroup_id(),"",
                    new GroupMember[]{new GroupMember(group.getUser_id(),group.getGroup_id(),null)});
        }
    }

}
