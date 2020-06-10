package utils;

public class ParametersServer {
	
	public static int serverControlPort = 21;
	public static int serverDataPort = 20;
	public static int clientControlPort = 1500;
	public static int clientDataPort = 1400;
	
	public static boolean renameAccepted = false;
	public static String oldPath;
	public static String path;
	
	public static final String SERVICE_READY = "220 Service ready for new user.";
	public static final String OKAY = "200 Okay";
	public static final String BAD_SEQUENCE = "503 Bad sequence of commands.";
	public static final String FILE_STATUS_OKAY = "150 File status okay; about to open data connection.";
	public static final String SUCCESS = "226 Closing data connection. Requested file action successful.";
	public static final String CANT_OPEN_CONNECTION = "425 Can't open data connection.";
	public static final String ACTION_ABORTED = "451 Requested action aborted: local error in processing.";
	public static final String FILE_ACTION_UNAVAILABLE= "450 Requested file action not taken. File unavailable.";
	public static final String FILE_UNAVAILABLE = "550 Requested action not taken. File unavailable.";
	public static final String TRANSFER_ABORTER = "426 Connection closed; transfer aborted.";
	public static final String INSUFFICIENT_STORAGE = "452 Requested action not taken. Insufficient storage space in system.";
	public static final String FILENAME_NOT_ALLOWED = "553 Requested action not taken. File name not allowed.";
	public static final String CLOSING = "221 Service closing control connection.";
	public static final String RESOURCES = System.getProperty("user.dir")+"/src/resources/";
	public static final String USER_NAME_OKAY ="331 User name okay, need password";
	public static final String USER_LOGIN ="230 User logged in , proceed";
	public static final String USER_NOT_LOGIN ="530 User not logged in";
	public static final String FILE_ACTION_OKAY ="250 Requested file action okay, completed";
}
