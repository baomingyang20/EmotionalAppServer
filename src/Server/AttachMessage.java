package Server;

import java.util.ArrayList;
import java.util.HashMap;

public class AttachMessage {

	public final static int NOT_LOGGED_IN = 1;
	public final static int LOGGED_IN = 2;
	public final static int LOGGED_OUT = 3;

	public static final int LOGIN = 4;
	public static final int REGISTER = 5;

	public HashMap<String, String> requestDataHash = new HashMap<>();
	public String[] loginDataKey = { "useName", "password" };
	public String[] registerDataKey = { "identity","useName", "password", "sex", "age"};

	private String useName = "";
	private String message = "";
	private int state = NOT_LOGGED_IN;

	public void handleString(String data, int request) {

		requestDataHash.clear();
		String s[] = data.split("\t");

		if (request == LOGIN) {
			for (int i = 0; i < loginDataKey.length; i++) {
				requestDataHash.put(loginDataKey[i], s[i]);
			}
		} else if (request == REGISTER) {
			for (int i = 0; i < registerDataKey.length; i++) {
				requestDataHash.put(registerDataKey[i], s[i]);
			}
		} 
	}

	public String returnInformation(ArrayList<String[]> s) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < s.size(); i++) {
			buffer.append(s.get(i)[0]);
			for (int j = 1; j < s.get(i).length; j++) {
				buffer.append("\t" + s.get(i)[j]);
			}
			buffer.append("\r\n");
		}
		return buffer.toString().trim();
	}

	public String getRequestData(String key) {
		return requestDataHash.get(key);
	}

	public String getUseName() {
		return useName;
	}

	public void setUseName(String useName) {
		this.useName = useName;
	}

	@Override
	public String toString() {
		return "AttachMessage [useName=" + useName + ", message=" + message + ", state=" + state + "]";
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
