package songshugongyi.action.hottopic;

import songshugongyi.ResponseObject;
import songshugongyi.SqlManager;
import songshugongyi.action.IAction;
import songshugongyi.action.user.GetUser;
import songshugongyi.bean.HotTopic;
import songshugongyi.bean.Image;
import songshugongyi.util.GsonUtils;
import songshugongyi.util.SQLUtils;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import static songshugongyi.util.FailData.sendFailMessageToClint;

/**
 * Created by yuanopen on 2018/5/11/011.
 */
public class CreateHotTopic extends IAction {

    private String TAG="CreateHotTopic";

    private static final String RequestParamKey_JSON= "data";
    Connection dbConnection = null;
    Statement stmt = null;
    @Override
    public void doAction(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        request.setCharacterEncoding("utf-8");
        try {

           String data = getParam(request, RequestParamKey_JSON, "null");

            if("null".equals(data)){
                System.out.println(TAG+"------->接送获取失败,data为null");
                return;
            }

            String json=new String(new BASE64Decoder().decodeBuffer(data),"UTF-8");
            System.out.println(json);
            if(json.equals("")){
                sendFailMessageToClint(TAG,response);
                return;
            }

            dbConnection = SqlManager.getConnection();
            stmt = dbConnection.createStatement();
            // 数据库就已经完全建立起来了。

             HotTopic hotTopic= GsonUtils.jsonToModule(json,HotTopic.class);
            //插入项目信息
            System.out.println(TAG+"------->开始插入HotTopic");
             insertToHotTopic(hotTopic);
            //插入项目图片
            System.out.println(TAG+"------->开始插入HotTopic的图片");
            for (Image i:hotTopic.getImages()
                 ) {
                insertImagesToDb(i);
            }

            //
            System.out.println(TAG+"------->开始插入HotTopic的User");

          hotTopic.setUser(GetUser.getUser(hotTopic.getUser_id()));

            System.out.println(TAG+"------->创建HotTopic成功，并将对象返回客服端");

            ResponseObject responseObject = ResponseObject
                    .getSuccessResponse(hotTopic);
            responseObject.send(response);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            SqlManager.release(dbConnection, stmt, null);
        }
    }



    private void insertToHotTopic(HotTopic hotTopic) throws SQLException {


        Map<String ,String > stringMap=new HashMap<String, String>();

        stringMap.put("hot_topic_id",hotTopic.getHot_topic_id());
        stringMap.put("user_id",hotTopic.getUser_id());
        stringMap.put("content",hotTopic.getContent());
        stringMap.put("create_time",hotTopic.getCreate_time());
        stringMap.put("update_time",hotTopic.getCreate_time());

        stmt.execute(SQLUtils.InsertModule("hot_topic",stringMap));

    }

    private void insertImagesToDb(Image image) throws SQLException {

        Map<String ,String > stringMap=new HashMap<String, String>();

        stringMap.put("image_id",image.getImage_url());
        stringMap.put("model_id",image.getModel_id());
        stringMap.put("image_url",image.getImage_url());
        stringMap.put("create_time",image.getCreate_time());
        stringMap.put("update_time",image.getCreate_time());

        stmt.execute(SQLUtils.InsertModule("hot_topic_image",stringMap));

    }

}
