package songshugongyi.action.progress;

import songshugongyi.bean.Comment;
import songshugongyi.bean.progress.ProgressCommentCount;
import songshugongyi.util.SQLUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuanopen on 2018/7/19/019.
 */
public class ModelUtils {

    public static  void InsertCountToDb(String model_id, String table_name, Statement statement) throws SQLException {

        Map<String ,Integer > param1=new HashMap<String, Integer>();
        Map<String ,String >param2=new HashMap<String, String>();

        param1.put("counts",1);
        param2.put("model_id",model_id);
        statement.execute(SQLUtils.InsertModule(table_name,param1,param2));
    }


    public static void UpdateProgressShareCount(String model_id, String table_name, Statement stmt) throws SQLException {

        Map<String ,String >stringMap=new HashMap<String, String>();
        stringMap.put("model_id",model_id);
        stmt.execute(SQLUtils.QueryModule(table_name,stringMap));

        ProgressCommentCount count=new ProgressCommentCount();
        ResultSet resultSet = stmt.getResultSet();
        if (resultSet != null && !resultSet.wasNull()) {
            if (resultSet.next()) {

                count.setProgress_id(model_id);
                count.setCounts(Integer.parseInt(resultSet.getString("counts")));
            }

            Map<String ,Integer >set=new HashMap<String, Integer>();
            set.put("counts",count.getCounts()+1);
            Map<String ,String >where=new HashMap<String, String>();
            where.put("model_id",count.getProgress_id());

            if(count.getCounts()==0){
                ModelUtils.InsertCountToDb(model_id,table_name,stmt);
            }else
                // 更新项目分享数
                stmt.execute(SQLUtils.UpdateModule(table_name,set,null,where));
        }

    }

    public  static void Execute(String model_id, String table_name,String  table_count_name,Statement stmt,Map<String ,String>params){
        try {
            stmt.execute(SQLUtils.InsertModule(table_name,params));

            ModelUtils. UpdateProgressShareCount(model_id,table_count_name,stmt);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
