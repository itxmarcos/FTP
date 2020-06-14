package utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.Socket;

public class FunctionalityClient {
	
	public static void checkCommand(String command) {
		
		if (command.contains("STOR")) {
			String pathDirectory = command.substring(5, command.length());
			uploadFileToServer(pathDirectory);
		} else if (command.contains("RETR")) {
			String pathDirectory = command.substring(5, command.length());
			downloadFileFromServer(pathDirectory);
		} else if (command.contains("LIST")) {
			getFilesInADirectory();
		} else if (command.contains("PRT")) {
			try {
			 	int newPort = Integer.parseInt(command.substring(5, command.length()));
				ParametersClient.clientDataPort = newPort;
		        }
		        catch (Exception e)
		        {
		            e.printStackTrace();
		        }
		}
	}
	
	public static boolean uploadFileToServer(String filename) {	
		try {
			
			Socket dataConnection = new Socket("localhost", ParametersClient.serverDataPort);
			System.out.println("Connected to server at " + ParametersClient.serverDataPort);

			// Para leer nuestro archivo
			FileInputStream input = new FileInputStream(ParametersClient.RESOURCES+filename);
			// Para escribirselo al cliente
			DataOutputStream output = new DataOutputStream(dataConnection.getOutputStream());
			
			// Buffer de 1000 bytes
			byte[] buffer = new byte[1000];
			// Escribimos al buffer
			int n_bytes = input.read(buffer);
		
			while (n_bytes != -1) { // input.read devuelve -1 cuando no queda nada
				output.write(buffer,0,n_bytes);
				n_bytes = input.read(buffer);
			}
			
			// Close the connections
			input.close();
			output.close();

			dataConnection.close();
			
			return true;
	    }
		catch (Exception e) 
		{
			System.out.println("Error");	
		}
		return false;
	}
	
	public static boolean downloadFileFromServer(String filename) {	
		try {
				
			Socket dataConnection = new Socket("localhost", ParametersClient.serverDataPort);
			System.out.println("Connected to server at " + ParametersClient.serverDataPort);
			
			// Para leer nuestro archivo
			DataInputStream input = new DataInputStream(dataConnection.getInputStream());
			// Para escribirselo al cliente
			FileOutputStream output = new FileOutputStream(ParametersClient.RESOURCES+filename);
			
			// Buffer de 1000 bytes
			byte[] buffer = new byte[1000];
			// Escribimos al buffer
			int n_bytes = input.read(buffer);
		
			while (n_bytes != -1) { // input.read devuelve -1 cuando no queda nada
				output.write(buffer,0,n_bytes);
				n_bytes = input.read(buffer);
			}

			// Close the connections
			input.close();
			output.close();

			dataConnection.close();
			
			return true;
			
		}
		catch (Exception e) {
		}
		return false;
	}
	public static boolean getFilesInADirectory() {
		try {
			Socket dataConnection = new Socket("localhost", ParametersClient.serverDataPort);
			System.out.println("Connected to server at " + ParametersClient.serverDataPort);
			dataConnection.close();
			return true;
		}
		catch (Exception e) {
			
		}
		return false;
	}

}
