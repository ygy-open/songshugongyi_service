package songshugongyi.action.user;

import songshugongyi.Error;
import songshugongyi.ResponseObject;
import songshugongyi.SqlManager;
import songshugongyi.action.IAction;
import songshugongyi.bean.User;
import songshugongyi.util.SuccessData;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static songshugongyi.util.getCurrentTime.getTime;

/**
 * Created by yuanopen on 2018/7/22/022.
 */
public class SetNewPassword extends IAction {

    private  String TAG="SetNewPassword";
    private static final String RequestParamKey_User_Phone = "user_phone";
    private static final String RequestParamKey_User_New_Password = "user_password";
    @Override
    public void doAction(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        Connection dbConnection = null;
        Statement stmt = null;
        request.setCharacterEncoding("utf-8");

        String phone,password;
        phone=getParam(request,RequestParamKey_User_Phone,"null");
        password=getParam(request,RequestParamKey_User_New_Password,"null");
        if("null".equals(password)||"".equals(phone))
        {
            System.out.println(TAG+"----->phone:"+phone+"password:"+password);
            ResponseObject responseObject = ResponseObject
                    .getFailResponse(Error.errorCode_LoginFail,
                            Error.getMsg(500));
            responseObject.send(response);
        }else {
            try {
                  System.out.println(TAG+"----->phone:"+phone+"password:"+password);
                dbConnection = SqlManager.getConnection();
                stmt = dbConnection.createStatement();

                String sqlStr = "UPDATE  user  SET  "
                        +"user_password='"+password +"'  where user_phone='"+phone+"'";
                stmt.execute(sqlStr);
                int updateCount = stmt.getUpdateCount();// 获取受影响的行数
                // 插入成功
                if (updateCount > 0) {
                    //查到，并将对象返回客服端
                    ResponseObject responseObject = ResponseObject
                            .getSuccessResponse(SuccessData.getData());
                    responseObject.send(response);

                } else {
                    // 查询失败了
                    ResponseObject responseObject = ResponseObject
                            .getFailResponse(Error.errorCode_QueryFail,
                                    Error.getQueryFailMsg());
                    responseObject.send(response);
            }
        }finally{
            SqlManager.release(dbConnection, stmt, null);
        }
        }
    }
}
