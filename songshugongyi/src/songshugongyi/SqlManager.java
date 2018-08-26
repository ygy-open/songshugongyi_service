package songshugongyi;

import java.sql.*;

//获取数据库连接的类
public class SqlManager {


	private static String driver="com.mysql.jdbc.Driver";
	private static String url="jdbc:mysql://localhost:3306/songshugongyi";
	private static String user="root";
	private static String password="root2306";


	static{
		try {
			System.out.println("驱动成功");
			Class.forName(driver);
			//DriverManager.registerDriver(driver);
		} catch (ClassNotFoundException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}

	//连接数据库
	public static Connection getConnection(){
		try {
			System.out.println("连接数据库");
			return DriverManager.getConnection(url,user,password);


		} catch (SQLException e) {
			// TODO: handle exception
		}
		return null;
	}


	//释放资源
	public static void release(Connection conn, Statement stat, ResultSet rs){
		if(rs!=null)
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO: handle exception
			}finally{
				rs=null;
			}
		if(stat!=null)
			try {
				stat.close();
			} catch (SQLException e) {
				// TODO: handle exception
			}finally{
				stat=null;
			}
		if(conn!=null)
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO: handle exception
			}finally{
				conn=null;
			}
	}

}

