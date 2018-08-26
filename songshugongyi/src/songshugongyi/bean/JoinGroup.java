package songshugongyi.bean;

/**
 * Created by yuanopen on 2018/7/26/026.
 */
public class JoinGroup {
    private String user_id;
    private String group_id;
    //创建时间
    private  String create_time;


    public JoinGroup(String user_id, String group_id, String create_time) {
        this.user_id = user_id;
        this.group_id = group_id;
        this.create_time = create_time;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
