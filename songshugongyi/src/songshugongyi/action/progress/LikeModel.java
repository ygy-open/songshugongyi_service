package songshugongyi.action.progress;

import songshugongyi.ResponseObject;
import songshugongyi.SqlManager;
import songshugongyi.action.IAction;
import songshugongyi.bean.Like;

import songshugongyi.util.GsonUtils;

import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import static songshugongyi.util.SuccessData.getData;

/**
 * Created by yuanopen on 2018/7/15/015.
 */
public class LikeModel extends IAction {

     private String TAG="LikeModel";
    private static final String RequestParamKey_JSON= "data";
    Connection dbConnection = null;
    Statement stmt = null;

    String table_name=null;
    String table_count_name=null;

    public LikeModel(String table_name) {
        this.table_name = table_name;
        this.table_count_name = table_name+"_count";
    }

    @Override
    public void doAction(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        request.setCharacterEncoding("utf-8");
        try {

            String data = getParam(request, RequestParamKey_JSON, "fgfgfg");

            String json=new String(new BASE64Decoder().decodeBuffer(data),"UTF-8");
            System.out.println(json);
            if(json.equals("")){

                System.out.println(TAG+"----->"+"json获取失败");
                return;
            }else{
                System.out.println(TAG+"----->"+"json获取成功");
                dbConnection = SqlManager.getConnection();
                stmt = dbConnection.createStatement();
                // 数据库就已经完全建立起来了。
                // json转实体
              Like like=GsonUtils.jsonToModule(json,Like.class);
                // 插入数据
                System.out.println(TAG+"----->"+"开始插入数据");
                insertDb(like);


                //返回客服端一个成功码
                ResponseObject responseObject = ResponseObject
                        .getSuccessResponse(getData());
                responseObject.send(response);
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            SqlManager.release(dbConnection, stmt, null);
        }
    }


    private void insertDb(Like like) throws SQLException {


        Map<String ,String >stringMap=new HashMap<String, String>();

        stringMap.put("model_id",like.getModel_id());
        stringMap.put("user_id",like.getUser_id());
        stringMap.put("like_time",like.getLike_time());

        ModelUtils. Execute(like.getModel_id(),table_name,table_count_name,stmt,stringMap);
    }

}
