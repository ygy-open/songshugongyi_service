package songshugongyi.action.progress;

import songshugongyi.ResponseObject;
import songshugongyi.action.IAction;
import songshugongyi.action.user.GetIsExist;
import songshugongyi.util.FailData;
import songshugongyi.util.SuccessData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by yuanopen on 2018/8/26/026.
 */
public class GetIsJoin extends IAction {

    private  final String RequestParamKey_Progress_ID= "progress_id";
    private  final String RequestParamKey_User_ID= "user_id";

    @Override
    public void doAction(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {

        String user_id=getParam(request,RequestParamKey_User_ID,"null");
        String progress_id=getParam(request,RequestParamKey_Progress_ID,"null");

       boolean isExist= GetIsExist.getIsExist("joinProgress","user_id",user_id,"progress_id",progress_id);
        if(isExist){
            ResponseObject responseObject = ResponseObject
                    .getSuccessResponse(SuccessData.getData());
            responseObject.send(response);
        }else
        {
            FailData.sendFailMessageToClint("GetIsJoin",response);
        }
    }
}
