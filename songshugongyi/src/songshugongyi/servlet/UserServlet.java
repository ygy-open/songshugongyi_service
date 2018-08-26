package songshugongyi.servlet;

import songshugongyi.Error;
import songshugongyi.ResponseObject;
import songshugongyi.action.GetModelStatus;
import songshugongyi.action.hottopic.CreateHotTopic;
import songshugongyi.action.hottopic.GetListHotTopics;
import songshugongyi.action.progress.*;
import songshugongyi.action.user.*;
import songshugongyi.util.SendImageToClient;
import songshugongyi.util.UpLoadPhotoServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class UserServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final String RequestParamKey_Action = "action";

	// 用户注册
	private static final String RequestAction_Register = "register";
	// 用户第三方注册
	public static final String RequestAction_The_Third_Register = "third_register";

	// 用户注册验证手机号是否存在
	private static final String RequestAction_Verify_Phone = "verify_phone";
	// 用户注册验证手机号是否存在
	private static final String RequestAction_Verify_Phone_User_id= "verify_user_id";
	// 修改密码
	public static final String RequestAction_User_Set_New_Password = "set_new_password";
	// 用户更新信息
	private static final String RequestAction_UserEdit = "useredit";
	// 用户登录
	private static final String RequestAction_Login = "login";
	// 用户第三方登录
	public static final String RequestAction_The_Third_Login = "third_login";

	// 获取用户的群组
	public static final String RequestAction_Get_User_Group = "get_user_group";
	// 获取用户的好友
	public static final String RequestAction_Get_User_Friend = "get_user_friend";

	// 获取用户的好友
	public static final String RequestAction_Get_New_RongYun_Token = "get_rongyun_token";

	// 根据id获取群组信息
	public static final String RequestAction_Get_GroupInfo_By_Id = "get_group_by_id";
	// 根据id获取好友信息
	public static final String RequestAction_Get_FriendInfo_By_Id = "get_friend_by_id";
	// 根据id获取用户信息
	public static final String RequestAction_Get_User_By_Id = "get_user_by_id";
	// 根据id获取群组信息
	public static final String RequestAction_Get_Group_By_Id = "get_group_by_id";

	// 根据输入内容获取群组信息
	public static final String RequestAction_Search_Group_By_Name = "search_group_by_name";
	// 根据username获取好友信息
	public static final String RequestAction_Search_User_By_User_Nmae = "search_user_by_name";

	private static final String RequestAction_Upload = "upload";
	private static final String RequestAction_Send_Image_To_Client = "send";

	// 添加好友
	public static final String RequestAction_Add_Friend = "add_friend";
	// 创建群组
	public static final String RequestAction_Create_Group = "create_group";
	// 加入群组
	public static final String RequestAction_Join_Group = "join_group";
	// 退出群组
	public static final String RequestAction_Quit_Group = "quit_group";

	// 关注
	public static final String RequestAction_Hot_Topic_Attention = "create_attention";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String action = req.getParameter(RequestParamKey_Action);
		System.out.println("UserServlet--->>> action:"+action);
		   doGet(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 处理用户的请求
		String action = req.getParameter(RequestParamKey_Action);
		if (action == null || "".equals(action)) {
			ResponseObject responseObject = ResponseObject.getFailResponse(
					Error.errorCode_NoAction, Error.getNoActionMsg());
			responseObject.send(resp);
			return;
		}
		try {
			//  用户注册。
			if (RequestAction_Register.equals(action)) {
				new UserRegister().doAction(req, resp);
			}else if (RequestAction_Verify_Phone.equals(action)) {
				// 通过手机判断用户是否存在
				new VerifyUserIsExist("user_phone").doAction(req, resp);
			} else if (RequestAction_Verify_Phone_User_id.equals(action)) {
				//  通过id判断用户是否存在
				new VerifyUserIsExist("user_id").doAction(req, resp);
			}else if (RequestAction_The_Third_Register.equals(action)) {
				//  通过id判断用户是否存在
				new UserRegister().doAction(req, resp);
			}else if (RequestAction_The_Third_Login.equals(action)) {
				// 用户登录。
				new UserThirdLogin().doAction(req, resp);
			}else if (RequestAction_Login.equals(action)) {
				// 用户登录。
				new UserLogin().doAction(req, resp);
			}else if (RequestAction_UserEdit.equals(action)) {
				// 更新用户信息
				new UserEdit().doAction(req, resp);
			}else if (RequestAction_Get_User_Group.equals(action)) {
				// 获取用户群组
				new GetGroupList().doAction(req, resp);
			}else if (RequestAction_Get_User_Friend.equals(action)) {
				// 获取用户好友
				new GetFriendList().doAction(req, resp);
			}else if (RequestAction_Get_New_RongYun_Token.equals(action)) {
				// 获取用户好友
				new GetNewRongyunToken().doAction(req, resp);
			}else if (RequestAction_User_Set_New_Password.equals(action)) {
				// 更新用户设置新密码
				new SetNewPassword().doAction(req, resp);
			}else if (RequestAction_Upload.equals(action)) {
				// 更新
				new UpLoadPhotoServlet().doAction(req, resp);
			} else if (RequestAction_Send_Image_To_Client.equals(action)) {
				// 更新
				new SendImageToClient().doAction(req, resp);
			}else if (RequestAction_Get_FriendInfo_By_Id.equals(action)) {
				// 关注
				new GetRongyunUserInfo().doAction(req, resp);
			}else if (RequestAction_Get_GroupInfo_By_Id.equals(action)) {
				// 获取群
				new GetRongyunGroupInfo().doAction(req, resp);
			}else if (RequestAction_Search_Group_By_Name.equals(action)) {
				// 搜索群组
				new GetGroupSearchResult().doAction(req, resp);
			}else if (RequestAction_Create_Group.equals(action)) {
				// 搜索群组
				new CreateGroup().doAction(req, resp);
			}else if (RequestAction_Join_Group.equals(action)) {
				// 加入群组
				new JoinGroup().doAction(req, resp);
			}else if (RequestAction_Quit_Group.equals(action)) {
				// 退出群组
				new UserQuitGroup().doAction(req, resp);
			}else if (RequestAction_Search_User_By_User_Nmae.equals(action)) {
				// 搜索用户
				new GetFriendSearchResult().doAction(req, resp);
			}else if (RequestAction_Add_Friend.equals(action)) {
				// 搜索用户
				new AddFriend().doAction(req, resp);
			}else if (RequestAction_Hot_Topic_Attention.equals(action)) {
				// 关注
				new CreateAttention().doAction(req, resp);
			}else {
				songshugongyi.ResponseObject responseObject = songshugongyi.ResponseObject.getFailResponse(
						songshugongyi.Error.errorCode_NoRequestParam,
						songshugongyi.Error.getNoRequestParamMsg(RequestParamKey_Action));
				responseObject.send(resp);
			}

		} catch (Exception  e) {
			e.printStackTrace();
			// 数据库异常，返回错误信息
			songshugongyi.ResponseObject responseObject = songshugongyi.ResponseObject.getFailResponse(
					songshugongyi.Error.errorCode_Exception,
					songshugongyi.Error.getExceptionMsg(e.getMessage()));
			try {
				responseObject.send(resp);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

}
