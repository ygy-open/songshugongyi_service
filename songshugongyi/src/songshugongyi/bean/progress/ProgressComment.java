package songshugongyi.bean.progress;

/**
 * Created by yuanopen on 2018/7/13/013.
 */

public class ProgressComment {
    // id 自增
    private int id;
    // 项目id;
    private  String progress_id;
    // 用户id
    private  String user_id;
    // 分享平台
    private String content;
    // 分享时间
    private String comment_time;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProgress_id() {
        return progress_id;
    }

    public void setProgress_id(String progress_id) {
        this.progress_id = progress_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getComment_time() {
        return comment_time;
    }

    public void setComment_time(String comment_time) {
        this.comment_time = comment_time;
    }

}
