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
			
	        Socket socket = new Socket("127.0.0.1", ParametersClient.clientDataPort);

	        File file = new File(ParametersClient.RESOURCES+filename);
	        // Get the size of the file
	        long length = file.length();
	        byte[] bytes = new byte[16 * 1024];
	        InputStream in = new FileInputStream(file);
	        OutputStream out = socket.getOutputStream();

	        int count;
	        while ((count = in.read(bytes)) > 0) {
	            out.write(bytes, 0, count);
	        }

	        out.close();
	        in.close();
	        socket.close();
	    }
		catch (Exception e) 
		{
			System.out.println("No ha funcionado la vaina");	
		}
		return false;
	}
	
	
	
	public static boolean downloadFileFromServer(String filename) {	
		try {
				
			ServerSocket dataConnection = new ServerSocket(ParametersClient.serverDataPort);
			Socket connection = dataConnection.accept();

			// Para leer nuestro archivo
			DataInputStream input = new DataInputStream(connection.getInputStream());
			// Para escribirselo al cliente
			DataOutputStream output = new DataOutputStream(new FileOutputStream(ParametersClient.RESOURCES+filename));
			
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

			connection.close();
			dataConnection.close();
			
			// Ojo con las comprobaciones: cosas que ya existen, posibles errores, etc.
			
			return true;
			
		}
		catch (Exception e) {
		}
		return false;
	}
	
	public static boolean downloadFileFromServerOLD(String filename){
		try {
			// Abrir conexion de datos
			Socket dataConnection = new Socket("localhost", ParametersClient.serverDataPort);

			// Coger input y output
			DataInputStream input = new DataInputStream(dataConnection.getInputStream());
			DataOutputStream output = new DataOutputStream(new FileOutputStream(ParametersClient.RESOURCES+filename));
			
			// Pasar datos con el buffer
			byte[] buffer = new byte[1000]; 
			int n_bytes = input.read(buffer);
			int bytesRead;
			
			do {
		         bytesRead = input.read(buffer, n_bytes, (buffer.length-n_bytes));
		         if(bytesRead >= 0) n_bytes += bytesRead;
		    } while(bytesRead > -1);

			output.write(buffer, 0 , n_bytes);
			output.flush();
			System.out.println("File " + filename+ " downloaded (" + n_bytes + " bytes read)");		      
		      
		    input.close();
		    output.close();
		    dataConnection.close();
			return true;
		
		} catch (Exception e) {
			System.out.println(ParametersClient.CANT_OPEN_CONNECTION);
		}
		return false;
	}
	

	public static boolean printFileList() {
		try {
			// Cuando invocamos LIST, el servidor nos deberia mandar la lista de archivos
			// (bssicamente, un string bien construido)
			// Habr� que llamar a este m�todo para imprimirlo bien
			// (es leer l�nea a l�nea...)
			// Y si le pasamos el BufferedReader?
			
			/*Estamos atascados. Tenemos 2 opciones: crear un string genérico en ParametersServer que contenga la lista de archivos
			 * o utilizar el BufferedReader, que no sabemos cómo usar. ¿Cómo se usaría?
			 * BufferedOutputStream output = new BufferedOutputStream(connection.getOutputStream());
			 */
			
			return true;
			
		} catch (Exception e) {
			return false;
		}
	}



	

}
