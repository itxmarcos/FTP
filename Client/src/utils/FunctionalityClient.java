package utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.function.IntUnaryOperator;

import client.Client;

public class FunctionalityClient {
	
	public static void checkCommand(String command) {
		
		if (command.contains("STOR")) {
			String pathDirectory = command.substring(5, command.length());
			uploadFileToServer(pathDirectory);
		} else if (command.contains("RETR")) {
			String pathDirectory = command.substring(5, command.length());
			downloadFileFromServer(pathDirectory);
		} else if (command.contains("LIST")) {
			
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
			
			// ServerSocket dataConnection = new ServerSocket(ParametersClient.serverDataPort);
			Socket dataConnection = new Socket("localhost", ParametersClient.serverDataPort);
			//Socket connection = dataConnection.accept();
			System.out.println("Connected to server at " + ParametersClient.serverDataPort);

			// Para leer nuestro archivo
			FileInputStream input = new FileInputStream(ParametersClient.RESOURCES+filename);
			// Para escribirselo al cliente
			DataOutputStream output = new DataOutputStream(dataConnection.getOutputStream());
			
			System.out.println(ParametersClient.RESOURCES+filename);
			// Buffer de 1000 bytes
			byte[] buffer = new byte[1000];
			// Escribimos al buffer
			int n_bytes = input.read(buffer);
			System.out.println("Received: "+buffer);
			while (n_bytes != -1) { // input.read devuelve -1 cuando no queda nada
				output.write(buffer,0,n_bytes);
				n_bytes = input.read(buffer);
			}
			
			// Close the connections
			input.close();
			output.close();

			dataConnection.close();
			
			// Ojo con las comprobaciones: cosas que ya existen, posibles errores, etc.
			
			return true;
	    }
		catch (Exception e) 
		{
			System.out.println("No ha funcionado la vaina");	
		}
		return false;
	}
	
	
	
	public static boolean downloadFileFromServer(String filename) {	
		try {
				
			// ServerSocket dataConnection = new ServerSocket(ParametersClient.serverDataPort);
			Socket dataConnection = new Socket("localhost", ParametersClient.serverDataPort);
			//Socket connection = dataConnection.accept();
			System.out.println("Connected to server at " + ParametersClient.serverDataPort);
			
			BufferedReader inputaux = new BufferedReader(new InputStreamReader(dataConnection.getInputStream()));
			String  asdasd = inputaux.readLine();
			System.out.print(asdasd);

			if(asdasd.equals(inputaux.readLine())) {
				dataConnection.close();
				System.out.println("Las has liao");
				return true;
			}
			// Para leer nuestro archivo
			DataInputStream input = new DataInputStream(dataConnection.getInputStream());
			// Para escribirselo al cliente
			FileOutputStream output = new FileOutputStream(ParametersClient.RESOURCES+filename);
			
			System.out.print(input);
			// Buffer de 1000 bytes
			byte[] buffer = new byte[1000];
			// Escribimos al buffer
			int n_bytes = input.read(buffer);
			System.out.println("Received: "+buffer);
			while (n_bytes != -1) { // input.read devuelve -1 cuando no queda nada
				output.write(buffer,0,n_bytes);
				n_bytes = input.read(buffer);
			}

			// Close the connections
			input.close();
			output.close();

			dataConnection.close();
			
			// Ojo con las comprobaciones: cosas que ya existen, posibles errores, etc.
			
			return true;
			
		}
		catch (Exception e) {
		}
		return false;
	}

}
