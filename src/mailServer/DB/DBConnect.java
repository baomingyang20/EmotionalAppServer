package mailServer.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnect {
//	private static String jdbcName = "com.mysql.cj.jdbc.Driver";
	//连接地址+ssl连接关闭+时区为hk+字符集为utf-8+数据库自动连接
	private static String dbURL = "jdbc:mysql://localhost:3306/emotionalappdatabase"
			+ "?useSSL=false"
			+ "&serverTimezone=Hongkong"
			+ "&characterEncoding=utf-8"
			+ "&autoReconnect=true";
	private static String dbUseName = "root";
	private static String dbPassword = "739321755";

	public static Connection getCon() {
//		try {
//			Class.forName(jdbcName);
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		Connection con = null;
		try {
			con = DriverManager.getConnection(dbURL, dbUseName, dbPassword);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return con;
	}

	public static void close(Statement statement) {
		try {
			if (statement != null) {
				statement.close();
				statement = null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void close(Connection con) {
		try {
			if (con != null) {
				con.close();
				con = null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void close(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}