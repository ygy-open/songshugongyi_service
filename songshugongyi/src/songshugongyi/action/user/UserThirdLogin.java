package songshugongyi.action.user;

import songshugongyi.ResponseObject;
import songshugongyi.SqlManager;
import songshugongyi.action.IAction;
import songshugongyi.bean.User;
import songshugongyi.util.GsonUtils;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static songshugongyi.action.user.InsertUserToDB.insertToDB;
import static songshugongyi.action.user.UserRegister.isUpdate;
import static songshugongyi.util.FailData.sendFailMessageToClint;

/**
 * Created by yuanopen on 2018/4/13/013.
 */
public class UserThirdLogin extends IAction {

    private String TAG="UserThirdLogin";

    private static final String RequestParamKey_JSON= "data";
    Connection dbConnection = null;
    Statement stmt = null;
    @Override
    public void doAction(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        request.setCharacterEncoding("utf-8");
        try {

            String data = getParam(request, RequestParamKey_JSON, "null");

            if("null".equals(data)){
                System.out.println(TAG+"------->接送获取失败,data为null");
                return;
            }

            String json=new String(new BASE64Decoder().decodeBuffer(data),"UTF-8");
            System.out.println(json);
            if(json.equals("")){
                sendFailMessageToClint(TAG,response);
                return;
            }

            dbConnection = SqlManager.getConnection();
            stmt = dbConnection.createStatement();
            // 数据库就已经完全建立起来了。

            User user= GsonUtils.jsonToModule(json,User.class);
            //插入项目信息
            System.out.println(TAG+"------->开始插入user");
           User temp=GetUser.getUser(user.getUserId());
            if(temp==null)
            insertToDB(user,stmt);
           else{
                ResponseObject responseObject1 = ResponseObject
                        .getSuccessResponse(temp);
                responseObject1.send(response);
            }

            isUpdate(stmt,response,user);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            SqlManager.release(dbConnection, stmt, null);
        }
    }


}
