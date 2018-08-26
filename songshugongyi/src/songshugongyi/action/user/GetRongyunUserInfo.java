package songshugongyi.action.user;

import songshugongyi.ResponseObject;
import songshugongyi.action.IAction;
import songshugongyi.bean.User;
import songshugongyi.bean.Friend;
import songshugongyi.util.FailData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

import static songshugongyi.action.user.GetIsExist.getIsExist;

/**
 * Created by yuanopen on 2018/7/19/019.
 */
public class GetRongyunUserInfo extends IAction{

    private String TAG="GetRongyunUserInfo";

    private  final String RequestParamKey_Freind_User_ID= "friend_user_id";
    private  final String RequestParamKey_User_ID= "user_id";

    @Override
    public void doAction(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        String user_id=getParam(request,RequestParamKey_User_ID,"null");
        String friend_user_id=getParam(request,RequestParamKey_Freind_User_ID,"null");
        getUserInfo(friend_user_id,user_id,response);

    }

    /**
     * @fun 通过不同方式验证用户是否注册过
     * @param user_id
     * @return
     */
    public void  getUserInfo(String friend_user_id,String user_id,HttpServletResponse response) throws IOException {

        if ("null".equals(friend_user_id)) {
            System.out.println(TAG + "---->服务器接收到数据为空");
            FailData.sendFailMessageToClint(TAG, response);
        } else {

            // 数据库就已经完全建立起来了。
            User user = GetUser.getUser(friend_user_id);
            Friend f=new Friend();
            f.setUser(user);

            if("null".equals(user_id))
            f.setFriend(getIsExist("user_friend","user_id",user_id,"user_friend_id",friend_user_id));
            else
            f.setFriend(false);

            if (f != null) {
                System.out.println(TAG + "---->获取成功");
                //查到，并将对象返回客服端
                ResponseObject responseObject = ResponseObject
                        .getSuccessResponse(f);
                responseObject.send(response);

            } else {
                System.out.println(TAG + "---->获取失败");
                ResponseObject responseObject = ResponseObject
                        .getFailResponse(ResponseObject.CODE_FAIL, "获取失败！");
                responseObject.send(response);
            }
        }
    }


}
