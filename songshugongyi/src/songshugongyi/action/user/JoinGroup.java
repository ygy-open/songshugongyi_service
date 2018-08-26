package songshugongyi.action.user;

import io.rong.models.group.GroupMember;
import songshugongyi.ResponseObject;
import songshugongyi.SqlManager;
import songshugongyi.action.IAction;
import songshugongyi.bean.UserGroup;
import songshugongyi.bean.message.FriendMessage;
import songshugongyi.bean.message.GroupMessage;
import songshugongyi.util.FailData;
import songshugongyi.util.GsonUtils;
import songshugongyi.util.SQLUtils;
import songshugongyi.util.SuccessData;
import songshugongyi.util.rongyun.grouptalk.RongyunGroup;
import songshugongyi.util.rongyun.message.RongyunMessage;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import static songshugongyi.util.getCurrentTime.getTime;

/**
 * Created by yuanopen on 2018/7/31/031.
 */
public class JoinGroup extends IAction{

    private String TAG="JoinGroup";

    private  final String RequestParamKey_DATA= "data";
    Connection dbConnection = null;
    RongyunMessage message;
    @Override
    public void doAction(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {

        Statement stmt = null;
        String data = getParam(request, RequestParamKey_DATA, "null");
        System.out.println(TAG+"----->"+data);
        String json=new String(new BASE64Decoder().decodeBuffer(data),"UTF-8");
        System.out.println(TAG+"----->"+json);
        try {

            dbConnection = SqlManager.getConnection();
            message =new RongyunMessage();
            stmt = dbConnection.createStatement();

            // 数据库就已经完全建立起来了。

            if("null".equals(json)){
                FailData.sendFailMessageToClint(TAG,response);
            }else{

                // 数据库就已经完全建立起来了。
                // json转实体
                GroupMessage groupMessage= GsonUtils.jsonToModule(json,GroupMessage.class);

                System.out.println(TAG+"----》content:"+groupMessage.getUser_name()+groupMessage.getContent()+groupMessage.getTarget_name());
                String content;
                if(groupMessage.getType()==1){

                    System.out.println(groupMessage.getUser_name()+"向"+groupMessage.getTarget_id()+"申请加入"+groupMessage.getGroup_name());
                    content=groupMessage.getUser_name()+"请求加入"+groupMessage.getGroup_name()+",请在 我的->消息 查看";
                    message.sendAddFreindMessage(groupMessage.getTarget_id(),content);

                    message.sendAddNotification(groupMessage.getTarget_id(),groupMessage.getUser_name(),GsonUtils.ModuleTojosn(groupMessage),4);

                }else if(groupMessage.getType()==2){
                    // 发送消息
                    SaveToDb(groupMessage);

                }else{
                    System.out.println(groupMessage.getUser_name()+"拒绝"+groupMessage.getTarget_id()+"加入"+groupMessage.getGroup_name());
                    content=groupMessage.getUser_name()+"拒绝了你为加入"+groupMessage.getGroup_name();
                    message.sendAddFreindMessage(groupMessage.getTarget_id(),content);
                }

                ResponseObject responseObject = ResponseObject
                        .getSuccessResponse(SuccessData.getData());
                responseObject.send(response);
            }
        }catch (Exception e){

        }finally {
            SqlManager.release(dbConnection, stmt, null);
        }

    }


    public  void  SaveToDb(GroupMessage groupMessage) throws SQLException {
        Statement stmt = null;
        Map<String ,String > params=new HashMap<String, String>();
        params.put("user_id",groupMessage.getTarget_id());
        params.put("group_id",groupMessage.getGroup_id());
        params.put("create_time",getTime());

        stmt = dbConnection.createStatement();
        String sql=SQLUtils.InsertModule("user_join_group",params);
        System.out.println(sql);
        // 数据库就已经完全建立起来了。
        stmt.execute(sql);
        int upDateCounts=stmt.getUpdateCount();
        ResultSet resultSet = stmt.getResultSet();
        // 说明存在
        if (upDateCounts>0) {
           String content=groupMessage.getUser_name()+"同意了你加入群组("+groupMessage.getGroup_name()+")";
            System.out.println(content);
            message.sendAgreeGroupMessage(groupMessage.getTarget_id(),content);
            // 发送给融云
            RongyunGroup.getInstance().JoinGroup(groupMessage.getGroup_id(),groupMessage.getGroup_name(),
                    new GroupMember[]{new GroupMember(groupMessage.getTarget_id(),groupMessage.getGroup_id(),null)});
        }


    }
}
