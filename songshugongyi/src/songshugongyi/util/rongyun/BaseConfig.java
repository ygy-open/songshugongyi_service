package songshugongyi.util.rongyun;

import io.rong.RongCloud;
import io.rong.methods.message._private.Private;

/**
 * Created by yuanopen on 2018/6/29/029.
 */
public class BaseConfig {
    private static String appKey = "8luwapkv8jdkl";
    private static String appSecret = "P0c5Fj613eYG";
    public static RongCloud rongCloud = RongCloud.getInstance(appKey, appSecret);
    private static Private mPrivate;
    public static RongCloud getRongCloudInstance(){
        return rongCloud;
    }

    public  static Private getRongyunPrivateInstance(){
            mPrivate=new Private(appKey,appSecret);

        return mPrivate;
    }
}
