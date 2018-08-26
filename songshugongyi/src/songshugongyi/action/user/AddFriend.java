package songshugongyi.action.user;

import com.sun.xml.internal.ws.resources.SenderMessages;
import songshugongyi.ResponseObject;
import songshugongyi.SqlManager;
import songshugongyi.action.IAction;
import songshugongyi.bean.UserGroup;
import songshugongyi.bean.message.FriendMessage;
import songshugongyi.util.FailData;
import songshugongyi.util.GsonUtils;
import songshugongyi.util.SQLUtils;
import songshugongyi.util.SuccessData;
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

import static songshugongyi.action.user.GetIsExist.getIsExist;
import static songshugongyi.util.getCurrentTime.getTime;

/**
 * Created by yuanopen on 2018/7/24/024.
 */
public class AddFriend extends IAction {
    private String TAG="AddFriend";

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
            FriendMessage addMessage= GsonUtils.jsonToModule(json,FriendMessage.class);

            System.out.println(TAG+"----》content:"+addMessage.getUser_name()+addMessage.getContent()+addMessage.getTarget_name());
            String content;
            if(addMessage.getType()==1){
                System.out.println(addMessage.getUser_name()+"申请添加"+addMessage.getTarget_id()+"为好友");
                content=addMessage.getUser_name()+"请求添加你为好友,请在 我的->消息 查看";

                message.sendAddFreindMessage(addMessage.getTarget_id(),content);

                message.sendAddNotification(addMessage.getTarget_id(),addMessage.getUser_name(),json,1);

            }else if(addMessage.getType()==2){
                System.out.println(addMessage.getUser_name()+"同意添加"+addMessage.getTarget_id()+"为好友");
                // 发送消息
                content=addMessage.getUser_name()+"同意了你的好友申请";
                if(!getIsExist("user_friend","user_id",addMessage.getUser_id(),"user_friend_id",addMessage.getTarget_id()))
                SaveToDb(addMessage.getUser_id(),addMessage.getTarget_id(),content);

                content="你同意了添加"+addMessage.getTarget_name()+"为好友";
                if(!getIsExist("user_friend","user_friend_id",addMessage.getUser_id(),"user_id",addMessage.getTarget_id()))
                SaveToDb(addMessage.getTarget_id(),addMessage.getUser_id(),content);

            }else{
                System.out.println(addMessage.getUser_name()+"拒绝添加"+addMessage.getTarget_id()+"为好友");
                content=addMessage.getUser_name()+"拒绝了添加你为好友";
                message.sendAddFreindMessage(addMessage.getTarget_id(),content);
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


    public  void  SaveToDb(String user_id,String user_friend_id,String content) throws SQLException {
        Statement stmt = null;
        Map<String ,String > params=new HashMap<String, String>();
        params.put("user_id",user_id);
        params.put("user_friend_id",user_friend_id);
        params.put("create_time",getTime());

         stmt = dbConnection.createStatement();
            // 数据库就已经完全建立起来了。
        String sql=SQLUtils.InsertModule("user_friend",params);
        System.out.println(sql);
         stmt.execute(sql);

        int updateCount=stmt.getUpdateCount();
        if(updateCount>0){
            System.out.println(content);
            message.sendAddFreindMessage(user_friend_id,content);
        }

    }
}
