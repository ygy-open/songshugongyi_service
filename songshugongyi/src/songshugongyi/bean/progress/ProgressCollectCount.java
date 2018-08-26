package songshugongyi.bean.progress;

/**
 * Created by yuanopen on 2018/7/15/015.
 */
public class ProgressCollectCount {
    // id 自增
    private int id;
    // 项目id;
    private  String model_id;
    // 分享数
    private int counts;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel_id() {
        return model_id;
    }

    public void setModel_id(String model_id) {
        this.model_id = model_id;
    }

    public int getCounts() {
        return counts;
    }

    public void setCounts(int counts) {
        this.counts = counts;
    }
}
