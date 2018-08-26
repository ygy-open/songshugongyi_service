package songshugongyi.action;

import songshugongyi.ResponseObject;
import songshugongyi.SqlManager;
import songshugongyi.bean.ModelStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by yuanopen on 2018/7/20/020.
 */
public class GetModelStatus extends IAction {

    private String ATG="GetModelStatus";
    private static final String RequestParamKey_Model_Id = "model_id";
    private static final String RequestParamKey_UserId = "user_id";

    String model_id=null;
    String user_id=null;
    Connection dbConnection = null;
    Statement stmt = null;

    String table_name=null;

    public GetModelStatus(String table_name) {
        this.table_name = table_name;
    }

    @Override
    public void doAction(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {

        model_id = getParam(request, RequestParamKey_Model_Id, "null");
        user_id=getParam(request,RequestParamKey_UserId,"null");

        ModelStatus modelStatus=new ModelStatus();
        modelStatus.setModel_id(model_id);

        if(!"null".equals(model_id)){
            System.out.println(ATG+"----->"+"mode_id:"+model_id+" user_id:"+user_id);
            if(!"null".equals(user_id)){

                try{
                    // 数据库就已经完全建立起来了。
                    dbConnection = SqlManager.getConnection();

                    modelStatus.setShare(isExsit(table_name+"_share"));
                    modelStatus.setComment(isExsit(table_name+"_comment"));
                    modelStatus.setLike(isExsit(table_name+"_like"));
                    modelStatus.setCollect(isExsit(table_name+"_collect"));
                }catch (Exception e){

                }finally {
                    SqlManager.release(dbConnection, stmt, null);
                }

            }
            else{
                modelStatus.setComment(false);
                modelStatus.setShare(false);
                modelStatus.setLike(false);
                modelStatus.setCollect(false);
            }
        }

        // 返回给客户端
        ResponseObject responseObject = ResponseObject
                .getSuccessResponse(modelStatus);
        responseObject.send(response);

    }


    boolean isExsit(String table_name) throws SQLException {

            stmt = dbConnection.createStatement();
            String sqlStr = "SELECT * FROM  "+table_name+" WHERE  `model_id`='" + model_id + "'  AND  `user_id`='" + user_id + "'";
           System.out.println(sqlStr);
            stmt.execute(sqlStr);

            ResultSet resultSet = stmt.getResultSet();
            // 说明存在
            if (resultSet != null && !resultSet.wasNull()&&resultSet.next()) {
                return true;
            }

        return false;

    }
}
