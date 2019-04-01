package handleDate;

import java.nio.channels.SelectionKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Server.AttachMessage;
import Server.ServerMain;
import frame.ServerMainFrame;
import mailServer.DB.DBUse;

public class DataProcess {
	private static DBUse dbuse = DBUse.getDBUse();
	private ServerMainFrame frame = ServerMainFrame.getFrame();
	private static DataProcess dataProcess = new DataProcess();

	private DataProcess() {
	}

	public static DataProcess getDataProcess() {
		return dataProcess;
	}

	public void handleString(AttachMessage am, SelectionKey key) {
		String request = am.getMessage();
		SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = simple.format(new Date());
		String back = "";

		System.out.println(request);
		if (request.indexOf("register") == 0) {

			am.handleString(request.substring(8).trim(), AttachMessage.REGISTER);

			if (dbuse.register(am)) {
				back = "register success";
				frame.addRow(new String[] { am.getRequestData("useName"), back, date });
			} else {
				back = "register fail";
				frame.addRow(new String[] { am.getRequestData("useName"), back, date });
			}
		} else if (request.indexOf("login") == 0) {

			am.handleString(request.substring(5).trim(), AttachMessage.LOGIN);

			if (dbuse.login(am)) {

				back = am.getRequestData("useName") + " login success";
				am.setUseName(am.getRequestData("useName"));
				am.setState(AttachMessage.LOGGED_IN);
				frame.addRow(new String[] { am.getRequestData("useName"), back, date });

				ServerMain.setOnLineMap(am.getUseName(), key);
			} else {

				back = am.getRequestData("useName") + " login fail";
				frame.addRow(new String[] { am.getRequestData("useName"), back, date });
			}
		} else if (request.indexOf("end") == 0) {

			String s[] = request.split("\t");
			back = s[1] + " logged out";

			dbuse.logOut(s[1]);
			am.setState(AttachMessage.LOGGED_OUT);
			frame.addRow(new String[] { s[1], back, date });
			ServerMain.removeOnLineMap(s[1]);

		} else if (request.indexOf("message") == 0) {

			String[] s = request.split("\t");
			SelectionKey receiver = ServerMain.getOnLineMap(s[2]);
			receiver.interestOps(SelectionKey.OP_WRITE);
			AttachMessage receiverAm = (AttachMessage) receiver.attachment();
			receiverAm.setMessage(request);

			back = request;
		} else if (request.indexOf("userIformationRequest") == 0) {

			String[] s = request.split("\t");
			back = "userIformationRequest" + "\r\n";
			back = back + am.returnInformation(dbuse.userIformationRequest(s[1]));

		} else if (request.indexOf("allUserIformationRequest") == 0) {

			String[] s = request.split("\t");
			back = "allUserIformationRequest" + "\r\n";
			back = back + am.returnInformation(dbuse.allUserIformationRequest());

		} else if (request.indexOf("teacherIformationRequest") == 0) {

			String[] s = request.split("\t");
			back = "teacherIformationRequest" + "\r\n";
			back = back + am.returnInformation(dbuse.teacherIformationRequest(s[1]));

		} else if (request.indexOf("allTeacherInformationRequest") == 0) {

			back = "allTeacherInformationRequest" + "\r\n";
			back = back + am.returnInformation(dbuse.allTeacherInformationRequest());
		} else if (request.indexOf("allEssayInformationRequest") == 0) {

			back = "allEssayInformationRequest" + "\r\n";
			back = back + am.returnInformation(dbuse.allEssayInformationRequest());
		} else if (request.indexOf("allVideoInformationRequest") == 0) {

			back = "allVideoInformationRequest" + "\r\n";
			back = back + am.returnInformation(dbuse.allVideoInformationRequest());
		} else if (request.indexOf("allTeacherReviewInformationRequest") == 0) {

			String ti = request.substring(34).trim();
			int teacherid = 0;
			try {
				teacherid = Integer.parseInt(ti);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			back = "allTeacherReviewInformationRequest" + "\r\n";
			back = back + am.returnInformation(dbuse.allTeacherReviewInformationRequest(teacherid));
		} else if (request.indexOf("allEssayReviewInformationRequest") == 0) {

			String ei = request.substring(32).trim();
			int essayid = 0;
			try {
				essayid = Integer.parseInt(ei);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			back = "allEssayReviewInformationRequest" + "\r\n";
			back = back + am.returnInformation(dbuse.allEssayReviewInformationRequest(essayid));
		} else if (request.indexOf("allVideoReviewInformationRequest") == 0) {

			String vi = request.substring(32).trim();
			int videoid = 0;
			try {
				videoid = Integer.parseInt(vi);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			back = "allVideoReviewInformationRequest" + "\r\n";
			back = back + am.returnInformation(dbuse.allVideoReviewInformationRequest(videoid));
		} else if (request.indexOf("allCourseReviewInformationRequest") == 0) {

			String ci = request.substring(33).trim();
			int courseid = 0;
			try {
				courseid = Integer.parseInt(ci);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			back = "allCourseReviewInformationRequest" + "\r\n";
			back = back + am.returnInformation(dbuse.allCourseReviewInformationRequest(courseid));
		} else if (request.indexOf("TeacherInformationbyidRequest") == 0) {

			String ti = request.substring(29).trim();
			int teacherid = 0;
			try {
				teacherid = Integer.parseInt(ti);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			back = "TeacherInformationbyidRequest" + "\r\n";
			back = back + am.returnInformation(dbuse.TeacherInformationbyidRequest(teacherid));
		} else if (request.indexOf("CourseInformationbyidRequest") == 0) {

			String ci = request.substring(28).trim();
			int courseid = 0;
			try {
				courseid = Integer.parseInt(ci);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			back = "CourseInformationbyidRequest" + "\r\n";
			back = back + am.returnInformation(dbuse.CourseInformationbyidRequest(courseid));
		}

		System.out.println(back.trim());
		am.setMessage(back.trim());
		key.attach(am);
	}
}
