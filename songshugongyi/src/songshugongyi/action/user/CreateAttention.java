package songshugongyi.action.user;

import songshugongyi.ResponseObject;
import songshugongyi.SqlManager;
import songshugongyi.action.IAction;
import songshugongyi.bean.Attention;
import songshugongyi.bean.Comment;
import songshugongyi.bean.progress.ProgressCommentCount;
import songshugongyi.util.GsonUtils;
import songshugongyi.util.SQLUtils;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import static songshugongyi.util.SuccessData.getData;

/**
 * Created by yuanopen on 2018/7/19/019.
 */
public class CreateAttention extends IAction{
    private String TAG="CreateAttention";
    private static final String RequestParamKey_JSON= "data";
    Connection dbConnection = null;
    Statement stmt = null;

    @Override
    public void doAction(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        request.setCharacterEncoding("utf-8");
        try {

            String data = getParam(request, RequestParamKey_JSON, "fgfgfg");
            System.out.println(TAG+"----->"+data);
            String json=new String(new BASE64Decoder().decodeBuffer(data),"UTF-8");
            System.out.println(TAG+"----->"+json);
            if(json.equals("")){

                System.out.println(TAG+"----->"+"json获取失败");
                return;
            }else{
                System.out.println(TAG+"----->"+"json获取成功");
                dbConnection = SqlManager.getConnection();
                stmt = dbConnection.createStatement();
                // 数据库就已经完全建立起来了。
                // json转实体
                Attention attention= GsonUtils.jsonToModule(json,Attention.class);
                // 插入数据
                System.out.println(TAG+"----->"+"开始插入数据");

                insertDb(attention);

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

    private void UpdateUserAttentions( String atttention,String user_id) throws SQLException {

        Map<String ,String > where=new HashMap<String, String>();
        where.put("user_id",user_id);

        stmt.execute(SQLUtils.QueryModule("user",where));
         // 暂存关注数
        int count=0;

        ResultSet resultSet = stmt.getResultSet();

        if (resultSet != null && !resultSet.wasNull()) {
            if (resultSet.next()) {
              count=resultSet.getInt(atttention);
            }

            Map<String ,Integer >set=new HashMap<String, Integer>();
            set.put(atttention,count+1);
            Map<String ,String>where1=new HashMap<String, String>();
            where1.put("user_id",user_id);

                // 更新项目分享数
                stmt.execute(SQLUtils.UpdateModule("user",set,null,where1));
        }

    }

    private void insertDb( Attention attention) throws SQLException {

        Map<String ,String > stringMap=new HashMap<String, String>();
        stringMap.put("attented_user_id",attention.getAttented_user_id());
        stringMap.put("user_id",attention.getUser_id());
        stringMap.put("create_time",attention.getUser_id());
        stringMap.put("update_time",attention.getUser_id());

        stmt.execute(SQLUtils.InsertModule("attention",stringMap));

        // 更新被关注的用户的被关注数
        UpdateUserAttentions("user_be_attention",attention.getAttented_user_id());
        // 更新关注的用户的关注数
        UpdateUserAttentions("user_attention",attention.getUser_id());
    }

}
