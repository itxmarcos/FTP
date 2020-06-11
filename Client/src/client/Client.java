package client;

import java.io.*; 
import java.net.*;

import utils.FunctionalityClient;
import utils.ParametersClient;

public class Client
{
	
	public static void runClient() {

		try {

			String command = "";

			// Connect with the server
			Socket connection = new Socket("localhost", ParametersClient.serverControlPort);

			// Recover input & output from connection
			BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			PrintWriter output = new PrintWriter(connection.getOutputStream(), true);
			
            String response = "";
			
			// Input for reading from keyboard
			BufferedReader inputKeyboard = new BufferedReader(new InputStreamReader(System.in));

			while(!command.equals("END")) {
				System.out.print("Write command (END to close the server): ");
				command = inputKeyboard.readLine();
				output.println(command);
				FunctionalityClient.checkCommand(command); //Detectar comando	
                response =  input.readLine();
				System.out.println(response);
			}

			// Close input & output
			input.close();
			output.close();
			System.out.println("Se ha cerrado la conexion del usuario.");
			// Close the connection
			connection.close();

		}  catch(IOException e) {
			System.out.println("Error: " + e);		
		}
	}
	
}
