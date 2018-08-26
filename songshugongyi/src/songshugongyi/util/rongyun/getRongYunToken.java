package songshugongyi.util.rongyun;

import com.alibaba.fastjson.JSONObject;
import io.rong.RongCloud;
import io.rong.methods.group.Group;
import io.rong.methods.user.User;
import io.rong.models.Result;
import io.rong.models.group.GroupMember;
import io.rong.models.group.GroupModel;
import io.rong.models.response.TokenResult;
import io.rong.models.user.UserModel;

import static songshugongyi.util.rongyun.BaseConfig.getRongCloudInstance;

/**
 * Created by yuanopen on 2018/5/7/007.
 */
public class getRongYunToken {

private static RongCloud mRongCloud;


    /**
     *
     * @param userId
     * @return token
     */
    public static  String  getToken(String userId,String userName,String Image)  {
       mRongCloud=getRongCloudInstance();

        User user = mRongCloud.user;

        /**
         * API 文档: http://rongcloud.github.io/server-sdk-nodejs/docs/v1/user/user.html#register
         *
         * 注册用户，生成用户在融云的唯一身份标识 Token
         */
        UserModel userModel = new UserModel()
                .setId(userId)
                .setName(userName)
                .setPortrait(Image);

        System.out.println("getToken:  " + userModel.toString());
        TokenResult result = null;
        try {
            result = user.register(userModel);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject jsonObject= JSONObject.parseObject(result.toString());
        String token=jsonObject.getString("token");
       return token;
    }


}
