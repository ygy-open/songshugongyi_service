package songshugongyi.util;

import songshugongyi.bean.data;

/**
 * Created by yuanopen on 2018/7/18/018.
 */
public class SuccessData {

    private static data successData;
    public static data getData(){
  if(successData==null){
      successData=new data("1","成功");
  }
        return successData;
    }
}
