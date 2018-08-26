package songshugongyi.bean;

/**
 * Created by yuanopen on 2018/7/18/018.
 */
public class data {
    private String code ;
    private String msg;

    public data(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {

        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
