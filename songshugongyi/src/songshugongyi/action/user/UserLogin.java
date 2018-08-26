package songshugongyi.action.user;

import songshugongyi.Error;
import songshugongyi.ResponseObject;
import songshugongyi.SqlManager;
import songshugongyi.action.IAction;
import songshugongyi.bean.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by yuanopen on 2018/4/13/013.
 */
public class UserLogin extends IAction {


    private static final String RequestParamKey_UserName = "user_phone";
    private static final String RequestParamKey_Password = "user_password";

    @Override
    public void doAction(HttpServletRequest req, HttpServletResponse response) throws IOException, SQLException {
        Connection dbConnection = null;
        Statement stmt = null;
        req.setCharacterEncoding("utf-8");

        try {

            String user_phone = getParam(req, RequestParamKey_UserName, "");
            String user_password = getParam(req, RequestParamKey_Password, "");

            dbConnection = SqlManager.getConnection();
            stmt = dbConnection.createStatement();
            // 数据库就已经完全建立起来了。


            String sqlStr = "SELECT * FROM `user` WHERE `user_phone`='" + user_phone + "' and user_password='" + user_password
                    + "'";
            stmt.execute(sqlStr);

            ResultSet resultSet = stmt.getResultSet();
            GetUserByResualtSet.getUserByResualtSet(resultSet,response);
        } finally {
            SqlManager.release(dbConnection, stmt, null);
        }

    }
}


