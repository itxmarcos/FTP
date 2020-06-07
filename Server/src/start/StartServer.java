package start;

import server.Server;
import utils.ParametersServer;

public class StartServer
{
	public static void main(String[] args)
	{
		System.out.println("SERVER\n\n");
		ParametersServer.path = ParametersServer.RESOURCES;
		Server.runServer();
	}
}
