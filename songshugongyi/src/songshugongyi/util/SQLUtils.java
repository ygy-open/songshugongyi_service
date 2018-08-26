package songshugongyi.util;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuanopen on 2018/7/15/015.
 */
public class SQLUtils {

    /**
     * @fun 根据参数生成SQL-insert语句
     * @param tableName 表名
     * @param params  字段参数
     * @return SQL 插入语言
     */
    public static String InsertModule(String tableName, Map<String,String> params){

        String sql="insert into "+tableName+"(";
        for (String key:params.keySet()){
            sql+=key+",";
        }

        sql=sql.substring(0,sql.length()-1);

        sql+=") values(";

        for (String key:params.keySet()){
            sql+="'"+params.get(key)+"',";
        }
        sql= sql.substring(0,sql.length()-1);
        sql+=")";

        return sql;
    }

    /**
     * @fun 根据参数生成SQL-insert语句
     * @param tableName 表名
     * @param params  字段参数
     * @return SQL 插入语言
     */
    public static String InsertModule(String tableName, Map<String,Integer> params1,Map<String,String> params2){

        String sql="insert into "+tableName+"(";
        if(params1!=null)
        for (String key:params1.keySet()){
            sql+=key+",";
        }
        if(params2!=null)
        for (String key:params2.keySet()){
            sql+=key+",";
        }
        sql=sql.substring(0,sql.length()-1);

        sql+=") values( ";

        if(params1!=null)
        for (String key:params1.keySet()){
            sql+=""+params1.get(key)+",";
        }
        if(params2!=null)
            for (String key:params2.keySet()){
                sql+="'"+params2.get(key)+"',";
            }
        sql= sql.substring(0,sql.length()-1);
        sql+=")";
        System.out.println(sql);
        return sql;
    }

    /**
     * @fun 生成查询SQL语句
     * @param tableName 表名
     * @param wheres 带有条件的参加集合
     * @return SQL 查询语言
     */
    public static String QueryModule(String tableName, Map<String,String> wheres){
        String sql="select * from "+tableName+" where ";

        if(wheres!=null){
            for (String key:wheres.keySet()){
                sql+="  "+key+"='"+wheres.get(key)+"' "+"and" ;
            }
            sql= sql.substring(0,sql.length()-3);
        }

        return sql;
    }

    /**
     * @fun 生成更新Sql语句
     * @param tableName 表名
     * @param sets1 带有整数的参加集合
     * @param sets2 带有字符串的参加集合
     * @param wheres 带有条件的参加集合
     * @return SQL 更新语言
     */
    public static String UpdateModule(String tableName,Map<String,Integer> sets1,Map<String,String> sets2, Map<String,String> wheres){
        String sql="update  "+tableName+" set ";

        if(sets1!=null){
            for (String key:sets1.keySet()){
                sql+="  "+key+"="+sets1.get(key)+"," ;
            }
        }

        if(sets2!=null){
            for (String key:sets2.keySet()){
                sql+="  "+key+"='"+sets2.get(key)+"'," ;
            }
        }

        sql= sql.substring(0,sql.length()-1);

        if(wheres!=null){
            sql+=" where ";
            for (String key:wheres.keySet()){
                sql+="  "+key+"='"+wheres.get(key)+"' "+"and" ;
            }
            sql= sql.substring(0,sql.length()-3);
        }
        System.out.println(sql);
        return sql;
    }

    public static void main(String[] args) {

        Map<String ,Integer >set=new HashMap<String, Integer>();
        set.put("counts",1232);
        Map<String ,String >set1=new HashMap<String, String>();
        set1.put("progress_id","dfdfd");
        Map<String ,String >where=new HashMap<String, String>();
        where.put("progress_id","dfdfd");

        // 更新项目分享数
        System.out.println(SQLUtils.UpdateModule("progress_share_count",set,set1,where));

    }
}
