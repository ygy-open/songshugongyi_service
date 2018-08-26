package songshugongyi.bean.progress;

/**
 * Created by yuanopen on 2018/7/15/015.
 */
public class ProgressCommentCount {
    // id 自增
    private int id;
    // 项目id;
    private  String progress_id;
    // 分享数
    private int counts;

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

    public int getCounts() {
        return counts;
    }

    public void setCounts(int counts) {
        this.counts = counts;
    }
}
