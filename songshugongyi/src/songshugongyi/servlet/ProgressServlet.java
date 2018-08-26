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


public class ProgressServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	// 创建项目
	private static final String RequestAction_Greate_Progress = "create_progress";
	// 获取项目列表
	private static final String RequestAction_GetList_Progress_Comments = "getList_progress_comments";
	// 获取项目的评论详情
	private static final String RequestAction_GetList_Progress = "getList_progresses";
	// 获取项目评论，转发，点赞，收藏数
	private static final String RequestAction_GetList_Progress_Detail = "collect_progress_detail";
	// 获取项目评论，转发，点赞，收藏与当前用户状态
	private static final String RequestAction_GetList_Proogress_Status = "get_progress_status";

	// 评论项目记录
	private static final String RequestAction_Comment_Progress = "comment_progress";
	// 分享项目记录
	private static final String RequestAction_Share_Progress = "share_progress";
	// 点赞项目记录
	private static final String RequestAction_Like_Progress = "like_progress";
	// 收藏项目记录
	private static final String RequestAction_Collect_Progress = "collect_progress";
	// 报名加入项目任务
	public static final String RequestAction_Join_Progress_Task = "join_progress_task";
	// 报名加入项目任务
	public static final String RequestAction_Is_Join_Progress = "is_join_progress";

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
			 if (RequestAction_Greate_Progress.equals(action)) {
				// 创建项目
				new CreateProgress().doAction(req, resp);
			}
			else if (RequestAction_GetList_Progress.equals(action)) {
				// 获取项目列表
				new GetListProgress().doAction(req, resp);
			}else if (RequestAction_GetList_Progress_Detail.equals(action)) {
					// 获取项目评论，转发，点赞，收藏数
					new GetProgressDetail("progress").doAction(req, resp);
			}
			else if (RequestAction_GetList_Proogress_Status.equals(action)) {
				// 获取项目评论，转发，点赞，收藏状态
				new GetModelStatus("progress").doAction(req, resp);

			}else if (RequestAction_GetList_Progress_Comments.equals(action)) {
				// 获取评论详情
				new GetListComments("progress").doAction(req, resp);

			}else if (RequestAction_Comment_Progress.equals(action)) {
				// 评论项目列表
				new CommentModel("progress_comment").doAction(req, resp);
			}else if (RequestAction_Share_Progress.equals(action)) {
				// 分享项目列表
				new ShareModel("progress_count").doAction(req, resp);
			} else if (RequestAction_Like_Progress.equals(action)) {
				// 点赞项目列表
				LikeModel likeModel=new LikeModel("progress_like");
				likeModel.doAction(req, resp);
			}else if (RequestAction_Collect_Progress.equals(action)) {
				// 收藏项目列表
				new CollectModel("progress_collect").doAction(req, resp);

			}else if (RequestAction_Join_Progress_Task.equals(action)) {
				 // 收藏项目列表
				 new JoinProgressTask().doAction(req, resp);

			 }else if (RequestAction_Is_Join_Progress.equals(action)) {
				 // 收藏项目列表
				 new GetIsJoin().doAction(req, resp);

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
