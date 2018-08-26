package songshugongyi.action.progress;

import io.rong.models.group.GroupMember;
import songshugongyi.ResponseObject;
import songshugongyi.SqlManager;
import songshugongyi.action.IAction;
import songshugongyi.util.FailData;
import songshugongyi.util.SuccessData;
import songshugongyi.util.rongyun.grouptalk.RongyunGroup;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by yuanopen on 2018/8/24/024.
 */
public class JoinProgressTask extends IAction {
    private String TAG = "LikeModel";
    private static final String RequestParamKey_User_Id = "user_id";
    private static final String RequestParamKey_Task_Id = "task_id";
    private static final String RequestParamKey_Progress_Id = "progress_id";

    Connection dbConnection = null;
    Statement stmt = null;
    String progress_id;
    @Override
    public void doAction(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {

        request.setCharacterEncoding("utf-8");
        try {

            String user_id = getParam(request, RequestParamKey_User_Id, "null");
            String task_id = getParam(request, RequestParamKey_Task_Id, "null");
             progress_id = getParam(request, RequestParamKey_Progress_Id, "null");
            dbConnection = SqlManager.getConnection();
            stmt = dbConnection.createStatement();

            insertDB(user_id, task_id, response);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlManager.release(dbConnection, stmt, null);
        }

    }

    private void insertDB(String user_id, String task_id, HttpServletResponse response) throws SQLException, IOException {
        String sql = "insert into joinTask(user_id,task_id) values('" + user_id + "','" + task_id + "')";
        System.out.println(sql);
        stmt.execute(sql);

        if (stmt.getUpdateCount() > 0) {
            String upadate = "UPDATE  task set task_current_people=task_current_people+1 where task_id='" + task_id + "'";
            System.out.println(upadate);
            stmt = dbConnection.createStatement();
            stmt.execute(upadate);
            // 更新任务人数
            if (stmt.getUpdateCount() > 0) {

                RongyunGroup group=new RongyunGroup();
                System.out.println("加入群组");
                group.JoinGroup(progress_id,"项目群组",new GroupMember[]{new GroupMember(user_id,progress_id,null)});


                String sqlP = "insert into joinProgress(user_id,progress_id) values('" + user_id + "','" + progress_id + "')";
                System.out.println(sqlP);
                stmt = dbConnection.createStatement();
                stmt.execute(sqlP);

                ResponseObject responseObject = ResponseObject
                        .getSuccessResponse(SuccessData.getData());
                responseObject.send(response);

                return;
            }

            FailData.sendFailMessageToClint(TAG, response);

        }
    }
}
