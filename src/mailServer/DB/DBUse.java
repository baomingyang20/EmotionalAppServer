package mailServer.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Server.AttachMessage;

public class DBUse {

	private Connection con = null;
	private PreparedStatement statement = null;
	private static DBUse dbuse = new DBUse();

	private DBUse() {

	}

	public static DBUse getDBUse() {
		return dbuse;
	}

	public ArrayList<String[]> teacherIformationRequest(String useName) {
		String sql = "select * from teacher where useName=" + useName;
		return dataHandle(sql);
	}

	public ArrayList<String[]> userIformationRequest(String useName) {
		String sql = "select * from user where useName=" + useName;
		return dataHandle(sql);
	}

	public ArrayList<String[]> allUserIformationRequest() {
		String sql = "select * from user";
		return dataHandle(sql);
	}

	public boolean login(AttachMessage am) {

		if (am.getRequestData("useName").equals("") || am.getRequestData("password").equals("")) {
			return false;
		}
		boolean check = false;

		con = DBConnect.getCon();
		ResultSet rs = null;
		String sql = "select * from user where useName=? and password=MD5(?)";
		try {
			statement = con.prepareStatement(sql);
			statement.setString(1, am.getRequestData("useName"));
			statement.setString(2, am.getRequestData("password"));
			rs = statement.executeQuery();
			if (rs.next()) {
				boolean state = rs.getBoolean("state");
				if (state) {
					System.out.println("用户已登陆");
				} else {
					sql = "update user set state=1 where useName=?";
					statement = con.prepareStatement(sql);
					statement.setString(1, am.getRequestData("useName"));
					statement.execute();
					check = true;
				}

			} else {
				DBConnect.close(rs);
				sql = "select * from teacher where useName=? and password=MD5(?)";
				statement = con.prepareStatement(sql);
				statement.setString(1, am.getRequestData("useName"));
				statement.setString(2, am.getRequestData("password"));
				rs = statement.executeQuery();
				if (rs.next()) {
					boolean state = rs.getBoolean("state");
					if (state) {
						System.out.println("用户已登陆");
					} else {
						sql = "update teacher set state=1 where useName=?";
						statement = con.prepareStatement(sql);
						statement.setString(1, am.getRequestData("useName"));
						statement.execute();
						check = true;
					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("sql语句错误");
			e.printStackTrace();
		} finally {
			DBConnect.close(rs);
			DBConnect.close(statement);
			DBConnect.close(con);
		}
		return check;
	}

	public boolean register(AttachMessage am) {

		if (am.getRequestData("useName").equals("") || am.getRequestData("password").equals("")) {
			return false;
		}
		boolean check = false;

		con = DBConnect.getCon();
		ResultSet rs = null;
		String sql = "select * from user where useName=?";

		try {
			statement = con.prepareStatement(sql);
			statement.setString(1, am.getRequestData("useName"));
			rs = statement.executeQuery();
			if (rs.next()) {
				System.out.println("已存在该用户");
			} else {
				DBConnect.close(rs);
				sql = "select * from teacher where useName=?";
				statement = con.prepareStatement(sql);
				statement.setString(1, am.getRequestData("useName"));
				rs = statement.executeQuery();
				if (rs.next()) {
					System.out.println("已存在该用户");
				} else {
					sql = "insert into " + am.getRequestData("identity") + "(useName,password,state,sex,age)"
							+ " values(?,MD5(?),0,?,?)";
					statement = con.prepareStatement(sql);
					statement.setString(1, am.getRequestData("useName"));
					statement.setString(2, am.getRequestData("password"));
					statement.setString(3, am.getRequestData("sex"));
					statement.setString(4, am.getRequestData("age"));
					statement.execute();
					check = true;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("sql语句错误");
			e.printStackTrace();
		} finally {
			DBConnect.close(rs);
			DBConnect.close(statement);
			DBConnect.close(con);
		}

		return check;
	}

	public ArrayList<String[]> allTeacherInformationRequest() {
		String sql = "select * from teacher";
		return dataHandle(sql);
	}

	public boolean logOut(String useName) {
		if (useName.equals("")) {
			return false;
		}
		boolean check = false;

		con = DBConnect.getCon();
		ResultSet rs = null;
		String sql = "select * from user where useName=?";

		try {
			statement = con.prepareStatement(sql);
			statement.setString(1, useName);
			rs = statement.executeQuery();
			if (rs.next()) {
				sql = "update user set state=0 where useName=?";
				statement = con.prepareStatement(sql);
				statement.setString(1, useName);
				statement.execute();
				System.out.println(useName + "登出");
				check = true;

			} else {
				DBConnect.close(rs);
				sql = "select * from teacher where useName=?";
				statement = con.prepareStatement(sql);
				statement.setString(1, useName);
				rs = statement.executeQuery();
				if (rs.next()) {
					sql = "update teacher set state=0 where useName=?";
					statement = con.prepareStatement(sql);
					statement.setString(1, useName);
					statement.execute();
					System.out.println(useName + "登出");
					check = true;

				} else {
					System.out.println("不存在该用户");
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("sql语句错误");
			e.printStackTrace();
		} finally {
			DBConnect.close(rs);
			DBConnect.close(statement);
			DBConnect.close(con);
		}

		return check;
	}

	public ArrayList<String[]> allEssayInformationRequest() {
		String sql = "select * from essay";
		return dataHandle(sql);
	}

	public ArrayList<String[]> allVideoInformationRequest() {
		String sql = "select * from video";
		return dataHandle(sql);
	}

	public ArrayList<String[]> allTeacherReviewInformationRequest(int teacherid) {
		String sql = "select * from teacherreview where teacherid=?";
		return dataHandle(sql, teacherid);
	}

	public ArrayList<String[]> allEssayReviewInformationRequest(int essayid) {
		String sql = "select * from essayreview where essayid=?";
		return dataHandle(sql, essayid);
	}

	public ArrayList<String[]> allVideoReviewInformationRequest(int videoid) {
		String sql = "select * from videoreview where videoid=?";
		return dataHandle(sql, videoid);
	}

	public ArrayList<String[]> allCourseReviewInformationRequest(int courseid) {
		String sql = "select * from coursereview where courseid=?";
		return dataHandle(sql, courseid);
	}

	public ArrayList<String[]> TeacherInformationbyidRequest(int teacherid) {
		String sql = "select * from teacher where teacherid=?";
		return dataHandle(sql, teacherid);
	}

	public ArrayList<String[]> CourseInformationbyidRequest(int courseid) {
		String sql = "select * from course where courseid=?";
		return dataHandle(sql, courseid);
	}

	private ArrayList<String[]> dataHandle(String sql) {
		ArrayList<String[]> returnList = new ArrayList<>();
		ArrayList<String> d = new ArrayList<>();
		ResultSet rs = null;
		con = DBConnect.getCon();
		try {
			statement = con.prepareStatement(sql);
			rs = statement.executeQuery();
			while (rs.next()) {
				int count = rs.getMetaData().getColumnCount();

				if (rs.isFirst()) {
					for (int i = 1; i <= count; i++) {
						d.add(rs.getMetaData().getColumnName(i));
					}
					returnList.add(d.toArray(new String[d.size()]));
					d.clear();
				}

				for (int i = 1; i <= count; i++) {
					d.add(rs.getString(i));
				}
				returnList.add(d.toArray(new String[d.size()]));
				d.clear();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("sql语句错误");
			e.printStackTrace();
		} finally {
			DBConnect.close(rs);
			DBConnect.close(statement);
			DBConnect.close(con);
		}

		return returnList;
	}

	private ArrayList<String[]> dataHandle(String sql, int id) {
		ArrayList<String[]> returnList = new ArrayList<>();
		ArrayList<String> d = new ArrayList<>();
		ResultSet rs = null;
		con = DBConnect.getCon();
		try {
			statement = con.prepareStatement(sql);
			statement.setInt(1, id);
			rs = statement.executeQuery();
			while (rs.next()) {
				int count = rs.getMetaData().getColumnCount();

				if (rs.isFirst()) {
					for (int i = 1; i <= count; i++) {
						d.add(rs.getMetaData().getColumnName(i));
					}
					returnList.add(d.toArray(new String[d.size()]));
					d.clear();
				}

				for (int i = 1; i <= count; i++) {
					d.add(rs.getString(i));
				}
				returnList.add(d.toArray(new String[d.size()]));
				d.clear();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("sql语句错误");
			e.printStackTrace();
		} finally {
			DBConnect.close(rs);
			DBConnect.close(statement);
			DBConnect.close(con);
		}

		return returnList;
	}

}
