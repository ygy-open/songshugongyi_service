package songshugongyi.util;

import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import songshugongyi.ResponseObject;
import songshugongyi.action.IAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by yuanopen on 2018/5/8/008.
 */
public class SendImageToClient extends IAction {

    //读取图片,转成String
    public static String readImage( )
    {
        String headImagePath="E:\\jxl\\IMG_20180411_163201.jpg";
        File file = new File(headImagePath);
        try {
            FileInputStream inputStream = new FileInputStream(file);
            byte[] bitmapArray = new byte[(int) file.length()];
            inputStream.read(bitmapArray);
            inputStream.close();
            return Base64.encode(bitmapArray);
        } catch(Exception e)
        {
            return "";
        }
    }

    @Override
    public void doAction(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
           ResponseObject  res=ResponseObject.getSuccessResponse(get(response));
            res.send(response);
            }

    public JSONObject get(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");

        JSONObject jsonObject = new JSONObject();
            jsonObject.put("userImageContent", readImage());
        return jsonObject;
    }
}
