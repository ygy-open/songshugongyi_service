package songshugongyi.action.user;

import songshugongyi.ResponseObject;
import songshugongyi.SqlManager;
import songshugongyi.action.IAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by yuanopen on 2018/7/19/019.
 */
public class VerifyUserIsExist extends IAction{

    private String key =null;

    public VerifyUserIsExist(String table) {
        this.key = table;
    }

    private  String TAG="VerifyUserByPhone";
    private  final String RequestParamKey_Key = "what";
    @Override
    public void doAction(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {

        String what=getParam(request,RequestParamKey_Key,"null");
        verifUserByPhone(key,what,response);
    }

    /**
     * @fun 通过不同方式验证用户是否注册过
     * @param key 验证的方式 有user_id ,user_phone
     * @param value 对应的值
     * @return
     */
    public void  verifUserByPhone(String key,String value,HttpServletResponse response) throws IOException {

        if("null".equals(value))
        {
            System.out.println(TAG+"---->服务器接收到数据为空");
            //查到，并将对象返回客服端
            ResponseObject responseObject = ResponseObject
                    .getFailResponse(ResponseObject.CODE_FAIL,"服务器接收到数据为空");
            responseObject.send(response);

        }else {
            Connection dbConnection = null;
            Statement stmt = null;

            try {

                dbConnection = SqlManager.getConnection();
                stmt = dbConnection.createStatement();

                // 数据库就已经完全建立起来了。

                String sqlStr = "SELECT * FROM `user` WHERE " + key + "='" + value + "'";
                stmt.execute(sqlStr);

                ResultSet resultSet = stmt.getResultSet();
                if (resultSet != null && !resultSet.wasNull() && resultSet.next()) {
                    System.out.println(TAG + "---->此账号已存在");
                    //查到，并将对象返回客服端,已经存在 返回false
                    ResponseObject responseObject = ResponseObject
                            .getFailResponse(ResponseObject.CODE_FAIL, "此账号已存在！");
                    responseObject.send(response);

                } else {
                    System.out.println(TAG + "---->此账号可以注册");
                    //查到，并将对象返回客服端
                    ResponseObject responseObject = ResponseObject
                            .getSuccessResponse("此账号可以注册！");
                    responseObject.send(response);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {

            } finally {
                SqlManager.release(dbConnection, stmt, null);
            }

            System.out.println(TAG+"---->user:"+value);
        }

    }
}
