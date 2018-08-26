package songshugongyi.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

public abstract class IAction {
	public abstract void doAction(HttpServletRequest request,
			HttpServletResponse response) throws IOException, SQLException;


	public static String getParam(HttpServletRequest req, String key,
								  String defaultValue) {
		String paramValue = null;
		try {
			paramValue = new String(req.getParameter(key).getBytes("iso-8859-1"), "utf-8");

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (paramValue == null || paramValue.equals("")) {
			return defaultValue;
		} else {
			return paramValue;
		}
	}

	public static int getParam(HttpServletRequest req, String key,
							   int defaultValue) {
		String paramValue = req.getParameter(key);
		if (paramValue == null || paramValue.equals("")) {
			return defaultValue;
		} else {
			try {
				return Integer.valueOf(paramValue);
			} catch (Exception e) {
				return defaultValue;
			}
		}
	}
}
