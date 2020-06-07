package utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import server.Server;

public class FunctionalityServer {
	public static HashMap <String,String> Usuarios = new HashMap<String, String>();
	public static String auxUser;
	
	public static String checkCommand(String command) {
        if(command.contains("PRT")) {
            int newPort = Integer.parseInt(command.substring(4, command.length()));
            return changePort(newPort);
            
        } else if(command.contains("LIST")) {
        	String pathDirectory = "";
            if (command.length()==4) pathDirectory = ParametersServer.RESOURCES; //The command is LIST with no path aggregation.
            else pathDirectory = command.substring(5, command.length());  
            return getFilesInADirectory(pathDirectory);
            
        } else if(command.contains("RETR")) {
            String filename = command.substring(5, command.length());
            return downloadFileFromServer(filename); 
            
        } else if(command.contains("STOR")) {
        	// abrir conexion en clientDataPort
        	String filename = command.substring(5, command.length());
            return uploadFileToServer(filename);
        }else if(command.contains("USER")) {
        	String username = command.substring(5,command.length());
        	auxUser=username;
        	return user();
        }else if(command.contains("PASS")) {
        	String password = command.substring(5,command.length());
        	return pass(password);
        }
        
        //ETC.
        
        return null; // HABR� QUE DEVOLVER LA RESPUESTA DEL SERVIDOR
    }
    
    public static String changePort(int newPort) {
        try {
    		ParametersServer.clientDataPort = newPort;
            return "Connection accepted PORT ["+ParametersServer.clientDataPort+"]";
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
		return null;
    }
    
    public static String getFilesInADirectory(String directory) {    	
		try (Stream<Path> walk = Files.walk(Paths.get(directory))) {
			List<String> result = walk.filter(Files::isRegularFile)
					.map(x -> x.toString()).collect(Collectors.toList());
			
			result.forEach(System.out::println);
			return ParametersServer.FILE_STATUS_OKAY+result.toString(); //Completar posibilidades
		} catch (IOException e) {
			e.printStackTrace();
			return ParametersServer.FILE_UNAVAILABLE;
		}
    }
    
	public static String deleteFile(String filename) {
		
		try {
			
			File file = new File(filename);
			if (!file.exists()){

			//	System.out.println(Parameters.FILE_ACTION_UNAVAILABLE);
				return ParametersServer.FILE_ACTION_UNAVAILABLE;

			}
	//		return file.delete();   //Arreglar para que borre
			return ParametersServer.FILE_STATUS_OKAY; //Completar posibilidades
		} catch (Exception e) {
			e.printStackTrace();
			return ParametersServer.ACTION_ABORTED;
		}
	}

	public static String renameFile(String oldFilename, String newFilename) {
		
		try {
			
			File oldFile = new File(oldFilename);
			File newFile = new File(newFilename);
			
			if (!oldFile.exists()){
		//		System.out.println(Parameters.FILE_ACTION_UNAVAILABLE);
				return ParametersServer.FILE_UNAVAILABLE;
			}
			if (newFile.exists()){
				System.out.println(ParametersServer.FILENAME_NOT_ALLOWED);
				return ParametersServer.FILENAME_NOT_ALLOWED;
			}
	//		return oldFile.renameTo(newFile); //Arreglar para devolver string
			return ParametersServer.FILE_STATUS_OKAY; //Completar posibilidades
		} catch (Exception e) {
			e.printStackTrace();
			return ParametersServer.ACTION_ABORTED;
		}
	}

	// PENDIENTE CONTROLAR ERRORES, LOS COMANDOS QUE TIENEN QUE SALIR, ETC.
	// PENDIENTE LOGUEAR LOS COMANDOS DEL SERVIDOR.
	// DEVOLVER STRING CON EL C�DIGO
	public static String downloadFileFromServer(String filename) {
		try {
			ServerSocket dataConnection = new ServerSocket(ParametersServer.serverDataPort);
			Socket connection = dataConnection.accept();
			System.out.println("Hola mundo!");

			// Para leer nuestro archivo
			BufferedInputStream input = new BufferedInputStream(new FileInputStream(ParametersServer.RESOURCES+filename));
			
			// Para escribirselo al cliente
			BufferedOutputStream output = new BufferedOutputStream(connection.getOutputStream());
			
			// Buffer de 1000 bytes
			byte[] buffer = new byte[1000];
			// Escribimos al buffer
			// input.read devuelve -1 cuando no queda nada
			int n_bytes = input.read(buffer);
			// Si hemos recuperado cosas, las escibimos al output y ya las recogera el cliente
			while (n_bytes != -1) {
				output.write(buffer,0,n_bytes);
				n_bytes = input.read(buffer);
			}

			// Close the connections
			input.close();
			output.close();

			connection.close();
			dataConnection.close();
			return ParametersServer.SUCCESS;
		}
		catch (Exception e) {
			System.out.println(ParametersServer.CANT_OPEN_CONNECTION);
		}
		return "Erroooooooor"; // DEVOLVER ERROR
	}
	
	public static String uploadFileToServer(String filename) {
		try {
			
			// Igual hay que controlar d�nde escribimos... directorio resources en parameters
			File file = new File(filename);

			// �Esta conexi�n est� bien? No lo se lo siento
			Socket connection = new Socket("localhost", ParametersServer.clientDataPort);

			if (!file.createNewFile()){
				
				// �QU� ERROR TIENE QUE DAR? Completar posibilidades

				connection.close();
				return ParametersServer.SUCCESS;
			}
			
			// Para coger lo que nos manda el cliente
			BufferedInputStream input = new BufferedInputStream(connection.getInputStream());
			
			// Para escribirlo
			BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file));
			
			// Lo mismo de antes, buffer, escribimos ah�, y a nuestro output
			byte [] buffer = new byte[1000];
			int n_bytes = input.read(buffer);
			while (n_bytes > 0)
			{
				output.write(buffer,0,n_bytes);
				n_bytes=input.read(buffer);
			}

			// Cerramos todo
			input.close();
			output.close();
			connection.close();
			
			//Falta control comandos
			
			return "";
			
		} catch (Exception e) {

			// Control errores
		}
		return "";
	}	
	public static String user() {
		Usuarios.put("Alejandro","Pinedo");
		Usuarios.put("Diego", "Ducha");
		Usuarios.put("Marcos", "Caballero");
		return ParametersServer.USER_NAME_OKAY.toString();
	}
	public static String pass(String password) {
		try {
			if(Usuarios.get(auxUser).equals(password))
			{
				return ParametersServer.USER_LOGIN;
			}
			else {
			
				return ParametersServer.USER_NOT_LOGIN;
			}
		} catch (Exception e) {
			return ParametersServer.USER_NOT_LOGIN;
		}
	}
}
