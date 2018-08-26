package songshugongyi.action.progress;

import com.google.gson.Gson;
import songshugongyi.Error;
import songshugongyi.ResponseObject;
import songshugongyi.SqlManager;
import songshugongyi.action.IAction;
import songshugongyi.bean.progress.Progress;
import songshugongyi.bean.progress.ProgressImage;
import songshugongyi.bean.progress.Task;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by yuanopen on 2018/5/11/011.
 */
public class CreateProgress extends IAction {
    private static final String RequestParamKey_JSON= "data";
    Connection dbConnection = null;
    Statement stmt = null;
    @Override
    public void doAction(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        request.setCharacterEncoding("utf-8");
        try {

           String data = getParam(request, RequestParamKey_JSON, "fgfgfg");

            String json=new String(new BASE64Decoder().decodeBuffer(data),"UTF-8");
            System.out.println(json);
            if(json.equals("")){
                // 查询失败了
                ResponseObject responseObject = ResponseObject
                        .getFailResponse(Error.errorCode_LoginFail,
                                Error.getMsg(500));
                responseObject.send(response);
                return;
            }

            dbConnection = SqlManager.getConnection();
            stmt = dbConnection.createStatement();
            // 数据库就已经完全建立起来了。

             Progress progress=getProgress(json);
            //插入项目信息
             insertToProgress(progress);
            //插入项目图片
            for (ProgressImage i:progress.getImages()
                 ) {
                insertToProgressImages(i);
            }
            //插入任务
            for (Task t:progress.getTasks()
                    ) {
                insertToProgressTasks(t);
            }


            ResponseObject responseObject = ResponseObject
                    .getSuccessResponse(progress);
            responseObject.send(response);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            SqlManager.release(dbConnection, stmt, null);
        }
    }



    private void insertToProgress(Progress progress) throws SQLException {
        String sqlString="insert into progress values("+
        "'"+progress.getProgress_id()+"',"+
        " '"+progress.getProgress_name()+"',"+
        "'"+progress.getProgress_introduction()+"',"+
        " '"+progress.getProgress_current_people()+"',"+
        "'"+progress.getProgress_start_time()+"',"+
        " '"+progress.getProgress_end_time()+"',"+
        "'"+progress.getProgress_type()+"',"+
        " '"+progress.getCreate_time()+"',"+
        " '"+progress.getUpdate_time()+"',"+
        "'"+progress.getProgress_user_id()+"')";

        stmt.execute(sqlString);

    }

    private void insertToProgressImages(ProgressImage image) throws SQLException {
        String sqlString="insert into progressimage values("+
                "'"+image.getImage_id()+"',"+
                " '"+image.getProgress_id()+"',"+
                "'"+image.getImage_url()+"',"+
                " '"+image.getCreate_time()+"',"+
                "'"+image.getUpdate_time()+"')";
        stmt.execute(sqlString);

    }
    private void insertToProgressTasks(Task task) throws SQLException {
        String sqlString="insert into task values("+
                "'"+task.getTask_id()+"',"+
                " '"+task.getTask_name()+"',"+
                "'"+task.getProgress_id()+"',"+
                "'"+task.getTask_people()+"',"+
                " '"+task.getTask_introduction()+"',"+
                " '"+task.getCreate_time()+"',"+
                "'"+task.getUpdate_time()+"')";
        stmt.execute(sqlString);
    }

    private void createGroup(){

    }
    public Progress getProgress(String json){
        Progress progress=null;
        Gson gson = new Gson();
        progress= gson.fromJson(json,  Progress.class);
        return  progress;
    }


}
