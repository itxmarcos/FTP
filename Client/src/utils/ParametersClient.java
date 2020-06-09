package utils;

public class ParametersClient {
	
	public static int serverControlPort = 21;
	public static int serverDataPort = 20;
	public static int clientControlPort = 1500;
	public static int clientDataPort = 1400;
	
	public static final String CANT_OPEN_CONNECTION = "425 Can't open data connection.";
	public static final String RESOURCES = System.getProperty("user.dir")+"/src/resources";

}
