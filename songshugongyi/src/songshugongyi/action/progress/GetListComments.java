package songshugongyi.action.progress;

import songshugongyi.ResponseObject;
import songshugongyi.SqlManager;
import songshugongyi.action.IAction;
import songshugongyi.bean.progress.CommentWitnUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**@fun 用来获取项目的评论，转发，点赞，收藏数
 * Created by yuanopen on 2018/7/15/015.
 */
public class GetListComments extends IAction {

    private String TAG="GetListComments";
    Connection dbConnection = null;
    Statement stmt = null;
    ResultSet resultSet=null;
    List<CommentWitnUser> list ;
    private static final String RequestParamKey_Model_Id = "model_id";

    String model_id;
    String table_name;

    public GetListComments(String table_name) {
        this.table_name = table_name+"_comment";
    }

    @Override
    public void doAction(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        request.setCharacterEncoding("utf-8");
        model_id = getParam(request, RequestParamKey_Model_Id, "null");

        if(!"null".equals(model_id))
        try {

            dbConnection = SqlManager.getConnection();
            stmt = dbConnection.createStatement();
            // 数据库就已经完全建立起来了。
            System.out.println(TAG+"----->:model_id:"+model_id);

            getProgressComments(table_name,response);

        } finally {
            SqlManager.release(dbConnection, stmt, null);
        }
    }

    private int  getProgressComments(String tableName,HttpServletResponse response) throws SQLException, IOException {
        list = new ArrayList<CommentWitnUser>();
        String sqlStr = "SELECT * FROM  " + tableName + " WHERE `model_id`='" + model_id+"'";

        stmt.execute(sqlStr);

         resultSet = stmt.getResultSet();

        if (resultSet != null && !resultSet.wasNull()) {

            while (resultSet.next()) {

                CommentWitnUser temp=new CommentWitnUser();

                temp.setContent(resultSet.getString("content"));
                System.out.println(TAG+"----->:model_id:"+temp.getContent());

                temp.setUser_id(resultSet.getString("user_id"));
                temp.setComment_time(resultSet.getString("comment_time"));
                list.add(temp);
            }
            getUser(response);
        }
        return  0;
    }

    private void getUser(HttpServletResponse response) throws SQLException, IOException {

        for (int i = 0; i < list.size(); i++) {

                String sqlStr = "SELECT * FROM   `user`  WHERE `user_id`='" + list.get(i).getUser_id() + "'";
                stmt.execute(sqlStr);

                resultSet = stmt.getResultSet();

                if (resultSet != null && !resultSet.wasNull()) {

                    while (resultSet.next()) {
                        list.get(i).setUser_name(resultSet.getString("user_name"));
                        list.get(i).setUser_avatar(resultSet.getString("user_avatar"));
                    }
                }

        }

        // 返回给客户端
        ResponseObject responseObject = ResponseObject
                .getSuccessResponse(list);
        responseObject.send(response);

    }

    private boolean isHas(CommentWitnUser commentWitnUser){
        for (int i = 0; i < list.size(); i++) {
            if(commentWitnUser.getUser_id().equals(list.get(i).getUser_id())){
                commentWitnUser.setUser_id(list.get(i).getUser_id());
                commentWitnUser.setUser_avatar(list.get(i).getUser_avatar());
                return true;
            }
        }
        return false;
    }
}
