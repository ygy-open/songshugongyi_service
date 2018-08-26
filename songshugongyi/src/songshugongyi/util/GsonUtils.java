package songshugongyi.util;

import com.google.gson.Gson;

/**
 * Created by yuanopen on 2018/7/14/014.
 */

public class GsonUtils {

    private static Gson GsonUtilsGson = new Gson();

    /**
     * @fun 根据不同类型进行json到实体间的转化
     * @param jsonString json字符串
     * @param cls 需要转化的类型
     * @param <T> 需要转化的类型
     * @return 返回实体对象
     */
    public static  <T> T jsonToModule(String jsonString, Class<T> cls) {
          T list ;
            list=GsonUtilsGson.fromJson(jsonString,cls);
        return list;
    }

    /**
     * @fun 根据不同类型进行实体到json间的转化
     * @param cls 需要转化的类型
     * @param <T> 需要转化的类型
     * @return 返回Json字符串
     */
    public static  <T> String  ModuleTojosn(T cls) {
        return  GsonUtilsGson.toJson(cls);
    }

}
