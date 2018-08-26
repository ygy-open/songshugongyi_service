package songshugongyi.util;

import songshugongyi.action.IAction;
import sun.misc.BASE64Decoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;


/**
 * Created by yuanopen on 2018/5/8/008.
 */
public class UpLoadPhotoServlet extends IAction {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    public void doAction(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html");
        // 获取文件名
        String name = request.getParameter("name");
        // 获取图片
        String photo = request.getParameter("photo");
        // 将传进来的图片的string格式进行处理
        byte[] bs = new BASE64Decoder().decodeBuffer(photo);
        // 写到E盘Img文件夹下的a.jpg文件。注：Img文件夹一定要存在
        FileOutputStream fos = new FileOutputStream("E:/jxl/" + name);
        fos.write(bs);
        fos.flush();
        fos.close();

        PrintWriter writer = response.getWriter();
        writer.print("上传成功");
    }

}

