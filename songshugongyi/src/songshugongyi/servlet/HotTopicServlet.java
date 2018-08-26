package songshugongyi.servlet;

import songshugongyi.Error;
import songshugongyi.ResponseObject;
import songshugongyi.action.GetModelStatus;
import songshugongyi.action.hottopic.CreateHotTopic;
import songshugongyi.action.hottopic.GetListHotTopics;
import songshugongyi.action.progress.*;
import songshugongyi.action.user.*;
import songshugongyi.common.ParamsUtils;
import songshugongyi.util.SendImageToClient;
import songshugongyi.util.UpLoadPhotoServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class HotTopicServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;



	// 热点
	// 创建热点
	private static final String RequestAction_Create_Hot_Topic = "create_hot_topic";
	// 获取热点列表
	private static final String RequestAction_GetList_Hot_Topic = "get_list_hot_topic";
	// 获取热点的评论详情
	// 获取项目评论，转发，点赞，收藏与当前用户状态
	private static final String RequestAction_GetList_Hot_Topic_Status = "get_hot_topic_status";

	public static final String RequestAction_GetList_Hot_Topic_Comments = "getList_hot_topic_comments";
	// 评论热点记录
	private static final String RequestAction_Comment_Hot_Topic = "comment_hot_topic";
	// 分享热点记录
	private static final String RequestAction_Share_Hot_Topic = "share_hot_topic";
	// 点赞热点记录
	private static final String RequestAction_Like_Hot_Topic = "like_hot_topic";
	// 收藏热点记录
	private static final String RequestAction_Collect_Hot_Topic= "collect_hot_topic";

	private static final String RequestAction_GetList_Hot_Topic_Detail = "collect_hot_topic_detail";
	// 关注
	public static final String RequestAction_Hot_Topic_Attention = "create_attention";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		   doGet(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 处理用户的请求
		String action = req.getParameter(ParamsUtils.RequestParamKey_Action);
		if (action == null || "".equals(action)) {
			ResponseObject responseObject = ResponseObject.getFailResponse(
					Error.errorCode_NoAction, Error.getNoActionMsg());
			responseObject.send(resp);
			return;
		}
		try {
			if (RequestAction_Create_Hot_Topic.equals(action)) {
				// 创建热点
				new CreateHotTopic().doAction(req, resp);
			}else if (RequestAction_GetList_Hot_Topic.equals(action)) {
				// 获取热点
				new GetListHotTopics().doAction(req, resp);
			}else if (RequestAction_GetList_Hot_Topic_Detail.equals(action)) {
				// 获取项目评论，转发，点赞，收藏数
				new GetProgressDetail("hot_topic").doAction(req, resp);

			}else if (RequestAction_GetList_Hot_Topic_Status.equals(action)) {
				// 获取项目评论，转发，点赞，收藏状态
				new GetModelStatus("hot_topic").doAction(req, resp);

			}else if (RequestAction_Comment_Hot_Topic.equals(action)) {
				// 评论项目列表
				new CommentModel("hot_topic_comment").doAction(req, resp);
			}else if (RequestAction_Share_Hot_Topic.equals(action)) {
				// 分享项目列表
				new ShareModel("hot_topic_count").doAction(req, resp);
			} else if (RequestAction_Like_Hot_Topic.equals(action)) {
				// 点赞项目列表
				new LikeModel("hot_topic_like").doAction(req, resp);
			}else if (RequestAction_Collect_Hot_Topic.equals(action)) {
				// 收藏项目列表
				new CollectModel("hot_topic_collect").doAction(req, resp);
			}else if (RequestAction_GetList_Hot_Topic_Comments.equals(action)) {
				// 获取评论详情
				new GetListComments("hot_topic").doAction(req, resp);

			}else if (RequestAction_Hot_Topic_Attention.equals(action)) {
				// 关注
				new CreateAttention().doAction(req, resp);
			}else {
				ResponseObject responseObject = ResponseObject.getFailResponse(
						Error.errorCode_NoRequestParam,
						Error.getNoRequestParamMsg(ParamsUtils.RequestParamKey_Action));
				responseObject.send(resp);
			}

		} catch (Exception  e) {
			e.printStackTrace();
			// 数据库异常，返回错误信息
			ResponseObject responseObject = ResponseObject.getFailResponse(
					Error.errorCode_Exception,
					Error.getExceptionMsg(e.getMessage()));
			try {
				responseObject.send(resp);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

}
