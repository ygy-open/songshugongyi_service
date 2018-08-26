package songshugongyi.action.progress;


import songshugongyi.ResponseObject;
import songshugongyi.SqlManager;
import songshugongyi.action.IAction;
import songshugongyi.bean.progress.Progress;
import songshugongyi.bean.progress.ProgressImage;
import songshugongyi.bean.progress.Task;
import songshugongyi.bean.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanopen on 2018/5/11/011.
 */
public class GetListProgress extends IAction {

 Connection dbConnection = null;
Statement stmt = null;
private List<Progress>  list_progress=new ArrayList<Progress>();
private static final String RequestParamKey_Type = "type";
    @Override
    public void doAction(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {

        request.setCharacterEncoding("utf-8");
        try {

            int progress_type = getParam(request, RequestParamKey_Type, 0);
            dbConnection = SqlManager.getConnection();
            stmt = dbConnection.createStatement();
            // 数据库就已经完全建立起来了。
            System.out.println("type:"+progress_type);
              getProgressFromDb(progress_type);

            System.out.println("list_progress:"+list_progress.size());
            //查到，并将对象返回客服端
            ResponseObject responseObject = ResponseObject
                    .getSuccessResponse(list_progress);
            responseObject.send(response);

        } finally {
            SqlManager.release(dbConnection, stmt, null);
        }

    }

    private void getProgressFromDb(int type) throws SQLException {

        String sqlStr = "SELECT * FROM `progress` WHERE `progress_type`=" +  type;
        stmt.execute(sqlStr);
        ResultSet resultSet = stmt.getResultSet();
        if (resultSet != null && !resultSet.wasNull()) {
            while (resultSet.next()){
                Progress progress=new Progress();
                progress.setProgress_id(resultSet.getString("progress_id"));
                progress.setProgress_name(resultSet.getString("progress_name"));
                progress.setProgress_user_id(resultSet.getString("progress_user_id"));
                progress.setProgress_introduction(resultSet.getString("progress_introduction"));
                progress.setProgress_current_people(resultSet.getInt("progress_current_people"));
                progress.setProgress_start_time(resultSet.getString("progress_start_time"));
                progress.setProgress_end_time(resultSet.getString("progress_end_time"));
                progress.setProgress_type(resultSet.getInt("progress_type"));
                progress.setCreate_time(resultSet.getString("create_time"));
                progress.setUpdate_time(resultSet.getString("update_time"));
                list_progress.add(progress);
            }
        }
        //添加图片
        System.out.println("list_progress:1"+list_progress.size());
        getProgressImageFromDb();
        System.out.println("getProgressImageFromDb:"+list_progress.size());
        //添加任务
        getProgressTasksFromDb();
        System.out.println("getProgressTasksFromDb:"+list_progress.size());
        //项目发起者用户
        getProgressUserFromDb();

    }


    private void getProgressImageFromDb() throws SQLException {

        for (int i = 0; i < list_progress.size(); i++) {
            String sqlStr = "SELECT * FROM `progressimage` WHERE `progress_id`='" + list_progress.get(i).getProgress_id()+"'";
            stmt.execute(sqlStr);
           List<ProgressImage> imageslist=new ArrayList<ProgressImage>();
            ResultSet resultSet = stmt.getResultSet();
            if (resultSet != null && !resultSet.wasNull()) {
                while (resultSet.next()) {
                    ProgressImage image = new ProgressImage();
                    image.setProgress_id(resultSet.getString("progress_id"));
                    image.setImage_id(resultSet.getString("image_id"));
                    image.setImage_url(resultSet.getString("image_url"));
                    image.setCreate_time(resultSet.getString("create_time"));
                    image.setUpdate_time(resultSet.getString("update_time"));

                    imageslist.add(image);
                }
                list_progress.get(i).setImages(imageslist);
            }

        }
    }

    private void getProgressTasksFromDb() throws SQLException {
        for (int i = 0; i < list_progress.size(); i++) {
            String sqlStr = "SELECT * FROM `task` WHERE `progress_id`='" + list_progress.get(i).getProgress_id()+"'";
            stmt.execute(sqlStr);
            List<Task> taskList=new ArrayList<Task>();
            ResultSet resultSet = stmt.getResultSet();
            if (resultSet != null && !resultSet.wasNull()) {
                while (resultSet.next()) {
                    Task task = new Task();
                    task.setProgress_id(resultSet.getString("progress_id"));
                    task.setTask_id(resultSet.getString("task_id"));
                    task.setTask_people(resultSet.getInt("task_people"));
                    task.setTask_introduction(resultSet.getString("task_introduction"));
                    task.setTask_current_people(resultSet.getInt("task_current_people"));
                    task.setTask_name(resultSet.getString("task_name"));
                    task.setCreate_time(resultSet.getString("create_time"));
                    task.setUpdate_time(resultSet.getString("update_time"));
                    taskList.add(task);

                }
                list_progress.get(i).setTasks(taskList);
            }

        }
    }
    private void getProgressUserFromDb() throws SQLException {
        for (int i = 0; i < list_progress.size(); i++) {
            String sqlStr = "SELECT * FROM `user` WHERE `user_id`='" + list_progress.get(i).getProgress_user_id()+"'";
            stmt.execute(sqlStr);
            User  user=new User();
            ResultSet resultSet = stmt.getResultSet();
            if (resultSet != null && !resultSet.wasNull()&&resultSet.next()) {
                user.setUserId(resultSet.getString("user_id"));
                user.setUserName(resultSet.getString("user_name"));
                user.setUserPassward(resultSet.getString("user_password"));
                user.setUserToken(resultSet.getString("user_token"));
                user.setCreateTime(resultSet.getString("create_time"));
                user.setUpdateTime(resultSet.getString("update_time"));

                }
            list_progress.get(i).setProgress_user(user);
            }

    }
    }



