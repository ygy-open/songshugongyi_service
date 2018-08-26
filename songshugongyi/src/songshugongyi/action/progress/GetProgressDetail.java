package songshugongyi.action.progress;

import songshugongyi.ResponseObject;
import songshugongyi.SqlManager;
import songshugongyi.action.IAction;

import songshugongyi.bean.progress.ProgressDetailCount;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**@fun 用来获取项目的评论，转发，点赞，收藏数
 * Created by yuanopen on 2018/7/15/015.
 */
public class GetProgressDetail extends IAction {

    Connection dbConnection = null;
    Statement stmt = null;
    String table_name=null;

    public GetProgressDetail(String table_name) {
        this.table_name = table_name;
    }

    private static final String RequestParamKey_Progress_Id = "model_id";
    String model_id;
    @Override
    public void doAction(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        request.setCharacterEncoding("utf-8");
        model_id = getParam(request, RequestParamKey_Progress_Id, "");
        if(!"".equals(model_id))
        try {

            ProgressDetailCount count=new ProgressDetailCount();
            dbConnection = SqlManager.getConnection();
            stmt = dbConnection.createStatement();
            // 数据库就已经完全建立起来了。
            System.out.println("model_id:"+model_id);

            count.setProgress_id(model_id);
            count.setComment_counts(getCount(table_name+"_comment_count"));
            count.setShare_counts(getCount(table_name+"_share_count"));
            count.setLike_counts(getCount(table_name+"_like_count"));
            count.setCollect_counts(getCount(table_name+"_collect_count"));

            //查到，并将对象返回客服端
            ResponseObject responseObject = ResponseObject
                    .getSuccessResponse(count);
            responseObject.send(response);

        } finally {
            SqlManager.release(dbConnection, stmt, null);
        }
    }

    private int  getCount(String tableName) throws SQLException {
        String sqlStr = "SELECT * FROM  " + tableName + " WHERE `model_id`='" + model_id+"'";

        stmt.execute(sqlStr);

        ResultSet resultSet = stmt.getResultSet();
        if (resultSet != null && !resultSet.wasNull()) {
            if (resultSet.next()) {
                return Integer.parseInt(resultSet.getString("counts"));
            }

        }
        return  0;
    }

}
