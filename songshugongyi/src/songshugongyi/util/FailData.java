package songshugongyi.util;

import songshugongyi.Error;
import songshugongyi.ResponseObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by yuanopen on 2018/7/24/024.
 */
public class FailData {

    public  static  void sendFailMessageToClint(String TAG, HttpServletResponse response) throws IOException {
        System.out.println(TAG+"------->接送获取失败");
        // 查询失败了
        ResponseObject responseObject = ResponseObject
                .getFailResponse(Error.errorCode_LoginFail,
                        Error.getMsg(500));
        responseObject.send(response);
    }
}
