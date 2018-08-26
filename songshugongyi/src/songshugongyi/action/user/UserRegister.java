package songshugongyi.action.user;

import songshugongyi.*;
import songshugongyi.Error;
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
import static songshugongyi.util.FailData.sendFailMessageToClint;

/**
 * Created by yuanopen on 2018/4/13/013.
 */
public class UserRegister extends IAction {

    private String TAG="UserRegister";

    private static final String RequestParamKey_JSON= "data";

    @Override
    public void doAction(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        request.setCharacterEncoding("utf-8");

        Connection dbConnection = null;
        Statement stmt = null;

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

            insertToDB(user,stmt);
            //  检查是否插入成功
            isUpdate(stmt,response,user);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            SqlManager.release(dbConnection, stmt, null);
        }
    }

    public static void isUpdate(Statement stmt,HttpServletResponse response,User user) throws SQLException, IOException {
        int updateCount = stmt.getUpdateCount();// 获取受影响的行数
        // 插入成功
        if (updateCount > 0) {
            user.setUser_be_attention(0);;
            user.setUser_attention(0);
            user.setUserSex(1);
            user.setUserAge(18);
            user.setUserAvatar("http://p8la0sgrt.bkt.clouddn.com/user_default.jpg");
            user.setUserIntegral(0);


            System.out.println("------->创建user成功，并将对象返回客服端");
            //查到，并将对象返回客服端
            ResponseObject responseObject1 = ResponseObject
                    .getSuccessResponse(user);
            responseObject1.send(response);

        } else {
            // 插入失败了。
            ResponseObject responseObject = ResponseObject.getFailResponse(
                    Error.errorCode_RegisterFail, Error.getMsg(600));
            responseObject.send(response);
        }
    }
}
