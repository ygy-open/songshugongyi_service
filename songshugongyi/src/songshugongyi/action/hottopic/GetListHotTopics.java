package songshugongyi.action.hottopic;


import songshugongyi.ResponseObject;
import songshugongyi.SqlManager;
import songshugongyi.action.IAction;
import songshugongyi.action.user.GetIsAttented;
import songshugongyi.bean.HotTopic;
import songshugongyi.bean.Image;
import songshugongyi.bean.User;
import songshugongyi.bean.progress.Progress;
import songshugongyi.bean.progress.ProgressImage;
import songshugongyi.bean.progress.Task;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static songshugongyi.action.user.GetUser.getUser;

/**
 * Created by yuanopen on 2018/5/11/011.
 */
public class GetListHotTopics extends IAction {

    private String TAG="GetListHotTopics";
    Connection dbConnection = null;
    Statement stmt = null;
    private List<HotTopic>  hotTopicList=new ArrayList<HotTopic>();
    private static final String RequestParamKey_Page_Size = "page_size";
    private static final String RequestParamKey_UserId = "user_id";
    String user_id;
    @Override
    public void doAction(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {

        request.setCharacterEncoding("utf-8");
        try {

            String  pageSize = getParam(request, RequestParamKey_Page_Size, "10");
            user_id=getParam(request,RequestParamKey_UserId,"null");

            dbConnection = SqlManager.getConnection();
            stmt = dbConnection.createStatement();
            // 数据库就已经完全建立起来了。
            System.out.println("type:"+pageSize);
            getHotTopicFromDb(Integer.parseInt(pageSize));

            System.out.println(TAG+"----->"+"list_progress:"+hotTopicList.size());
            //查到，并将对象返回客服端
            ResponseObject responseObject = ResponseObject
                    .getSuccessResponse(hotTopicList);
            responseObject.send(response);

        } finally {
            SqlManager.release(dbConnection, stmt, null);
        }

    }

    private void getHotTopicFromDb(int type) throws SQLException {
        System.out.println(TAG+"----->"+"开始查询");

        String sqlStr = "SELECT * FROM `hot_topic`";
        stmt.execute(sqlStr);
        ResultSet resultSet = stmt.getResultSet();
        if (resultSet != null && !resultSet.wasNull()) {
            while (resultSet.next()){
                HotTopic hotTopic=new HotTopic();
                hotTopic.setHot_topic_id(resultSet.getString("hot_topic_id"));
                hotTopic.setUser_id(resultSet.getString("user_id"));
                hotTopic.setContent(resultSet.getString("content"));
                hotTopic.setUser(getUser(hotTopic.getUser_id()));
                hotTopic.setCreate_time(resultSet.getString("create_time"));
                hotTopic.setUpdate_time(resultSet.getString("update_time"));
                hotTopicList.add(hotTopic);
            }
        }
        //添加图片
        System.out.println(TAG+"----->"+"添加图片");
        getProgressImageFromDb();
        // 查询与当前用户是否关注
        setUserAttented();

    }

    private void setUserAttented() {
        for (int i = 0; i < hotTopicList.size(); i++) {
            System.out.println( hotTopicList.get(i).getUser_id()+"--"+user_id);
            if(!"null".equals(user_id))
                hotTopicList.get(i).setIs_attented(GetIsAttented.isAttented( hotTopicList.get(i).getUser_id(),user_id));
            else
            hotTopicList.get(i).setIs_attented(false);
            }
    }


    private void getProgressImageFromDb() throws SQLException {

        for (int i = 0; i < hotTopicList.size(); i++) {
            String sqlStr = "SELECT * FROM `hot_topic_image` WHERE `model_id`='" + hotTopicList.get(i).getHot_topic_id()+"'";
            stmt.execute(sqlStr);
            List<Image> imageslist=new ArrayList<Image>();
            ResultSet resultSet = stmt.getResultSet();
            if (resultSet != null && !resultSet.wasNull()) {
                while (resultSet.next()) {
                  Image image = new Image();
                    image.setModel_id(resultSet.getString("model_id"));
                    image.setImage_id(resultSet.getString("image_id"));
                    image.setImage_url(resultSet.getString("image_url"));

                    imageslist.add(image);
                }
                hotTopicList.get(i).setImages(imageslist);
            }

        }
    }

    }



