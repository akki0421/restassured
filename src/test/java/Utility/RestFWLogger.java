package Utility;

import org.apache.log4j.Logger;

public class RestFWLogger {

	private static Logger Log = Logger.getLogger(RestFWLogger.class.getName());

	public static void startTestCase(String sTestCaseName)

	{

		Log.info("*******************************************************************************************");
		Log.info("####################             " + sTestCaseName + "           ########################  ");
		Log.info("*******************************************************************************************");

	}

	public static void endTestCase()

	{

		
		Log.info("*******************************************************************************************");
		Log.info("##############              " + "-E---N---D--" + "     ####################################");
		Log.info("*******************************************************************************************");
	}

	public static void info(String message) {
		Log.info(message);
	}

	public static void warn(String message) {
		Log.info(message);
	}

	public static void error(String message) {
		Log.info(message);
	}

}
