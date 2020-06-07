package utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import client.Client;

public class FunctionalityClient {
	
	public static void checkCommand(String command) {
		
		if (command.contains("STOR")) {
			
			// Opcion 1: new Socket (clientDataPort)
			
		} else if (command.contains("RETR")) {
			String pathDirectory = command.substring(5, command.length());
			receiveFile(pathDirectory);
		} else if (command.contains("LIST")) {
			
		} else if (command.contains("PORT")) {
			//Client.output.print(command); //Notifico al servidor del cambio de puerto
			int newPort = Integer.parseInt(command.substring(5, command.length()));
			ParametersClient.clientDataPort = newPort;
		}
	}
	
	public static boolean sendFile(String filename) {
				
		try {
			ServerSocket dataConnection = new ServerSocket(ParametersClient.serverDataPort);
			Socket connection = dataConnection.accept();

			// Para leer nuestro archivo
			BufferedInputStream input = new BufferedInputStream(new FileInputStream(filename));
			
			// Para escribirselo al cliente
			BufferedOutputStream output = new BufferedOutputStream(connection.getOutputStream());
			
			// Buffer de 1000 bytes
			byte[] buffer = new byte[1000];
			// Escribimos al buffer
			// input.read devuelve -1 cuando no queda nada
			int n_bytes = input.read(buffer);
			// Si hemos recuperado cosas, las escibimos al output y ya las recoger� el cliente
			while (n_bytes != -1)
			{
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
	
	// Cerrar input, output y conexion
	// Ojo con las comprobaciones: cosas que ya existen, posibles errores, etc.
	public static boolean receiveFile(String filename){
		try {
			// Abrir conexion de datos
			Socket dataConnection = new Socket("localhost", ParametersClient.serverDataPort);

			// Coger input y output
			BufferedReader input = new BufferedReader(new InputStreamReader(dataConnection.getInputStream()));
			BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(filename));
			
			System.out.println("Hola 1");

			// Pasar datos con el buffer
			/*byte[] buffer = new byte[1000]; 
			int n_bytes = input.read(buffer);
			int bytesRead;
			
			System.out.println("Hola 2");
			do {
		         bytesRead =
		            input.read(buffer, n_bytes, (buffer.length-n_bytes));
		         if(bytesRead >= 0) n_bytes += bytesRead;
		      } while(bytesRead > -1);

			System.out.println("Hola 3");
		      output.write(buffer, 0 , n_bytes);
		      output.flush();
		      System.out.println("File " + filename
		          + " downloaded (" + n_bytes + " bytes read)");
		      System.out.println("Hola ");
			return true;*/
		
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
