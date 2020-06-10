package utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
	public static ServerSocket dataConnection;
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
            ParametersServer.path = pathDirectory;
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
        else if(command.contains("RNFR")) 
        {
        	String pathDirectory = command.substring(5, command.length());
        	System.out.println(checkRenameFile(pathDirectory));
        	ParametersServer.path = pathDirectory;
        }
        else if(command.contains("RNTO") && ParametersServer.renameAccepted) 
        {
        	System.out.println("Do not forget the extension too");
        	String pathDirectory = command.substring(5, command.length());
        	return renameFile(pathDirectory);
        }
        else if (command.contains("DELE"))
        {
        	String pathDirectory = command.substring(5, command.length());
        	return deleteFile(pathDirectory);
        }
        else if (command.contains("RMD"))
        {
        	String pathDirectory = command.substring(4, command.length());
        	return deleteDirectory(pathDirectory);
        }
        else if (command.contains("PWD"))
        {        	
        	return "257 " +getPath();
        }
        else if (command.contains("CWD"))
        {
        	String folder = command.substring(4, command.length());
        	return changeDirectory(folder);
        }
        else if (command.contains("MKD"))
        {
        	String pathDirectory = command.substring(4, command.length());
        	return createDirectory(pathDirectory);
        }
        //ETC.
        
        return null; // HABR� QUE DEVOLVER LA RESPUESTA DEL SERVIDOR
    }
    
    private static String changeDirectory(String folder) {
    	File file = new File(ParametersServer.path);
    	if (folder.equals("..")) {
			File parent = new File(file.getParent());
    		ParametersServer.path=parent.getPath();
    	} else {
    		File nextFile = new File(file+"\\"+folder);
    		if (nextFile.getPath() == null)
    		return ParametersServer.FILE_UNAVAILABLE;
    		
    		ParametersServer.path=nextFile.getPath();
    	}
    	System.out.println(ParametersServer.path);
    	return ParametersServer.FILE_ACTION_OKAY;
	}

    
	public static String changePort(int newPort) {
        try {
    		ParametersServer.clientDataPort = newPort;
    		dataConnection = new ServerSocket(ParametersServer.serverDataPort);
            return "Connection accepted PORT ["+ParametersServer.clientDataPort+"]";
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
		return null;
    }
	
	
    public static String createDirectory(String pathname)
    {
        //Creating a File object
        File file = new File(pathname);
        //Creating the directory
        boolean bool = file.mkdir();
        if(!bool){
        	return ParametersServer.FILE_UNAVAILABLE;
        }
		return "257 " +pathname +" created";    	
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
			file.delete();   //Arreglar para que borre
			return ParametersServer.FILE_STATUS_OKAY; //Completar posibilidades
		} catch (Exception e) {
			e.printStackTrace();
			return ParametersServer.ACTION_ABORTED;
		}
	}
public static String deleteDirectory(String filename) {
		
		try {
			
			File file = new File(filename);
			File parent = new File(file.getParent());
			if (!file.exists()){

			//	System.out.println(Parameters.FILE_ACTION_UNAVAILABLE);
				return ParametersServer.FILE_UNAVAILABLE;

			}
			parent.delete();   //Arreglar para que borre
			return ParametersServer.FILE_STATUS_OKAY; //Completar posibilidades
		} catch (Exception e) {
			e.printStackTrace();
			return ParametersServer.ACTION_ABORTED;
		}
	}
	public static String checkRenameFile(String oldFilename) {
		try {
			File oldFile = new File(oldFilename);
			//File newFile = new File(newFilename);
			
			if (!oldFile.exists()){
		//		System.out.println(Parameters.FILE_ACTION_UNAVAILABLE);
				return ParametersServer.FILE_UNAVAILABLE;
			}
			/*if (newFile.exists()){
				System.out.println(ParametersServer.FILENAME_NOT_ALLOWED);
				return ParametersServer.FILENAME_NOT_ALLOWED;
			}*/
	//		return oldFile.renameTo(newFile); //Arreglar para devolver string
			ParametersServer.renameAccepted = true;
			ParametersServer.oldPath = oldFilename;
			return ParametersServer.FILE_STATUS_OKAY; //Completar posibilidades
		} catch (Exception e) {
			e.printStackTrace();
			return ParametersServer.ACTION_ABORTED;
		}
	}
	public static String renameFile(String newFilename) {
		try {
			File oldFile = new File(ParametersServer.oldPath);
			File newFile = new File(oldFile.getParent() + "\\"+ newFilename);		

			//	return oldFile.renameTo(newFile); //Arreglar para devolver string
			ParametersServer.renameAccepted = false;			
			if(oldFile.renameTo(newFile)){
				return ParametersServer.FILE_STATUS_OKAY;
	        }else{
	        	return ParametersServer.FILENAME_NOT_ALLOWED;
	        }
			
			 //Completar posibilidades
		} catch (Exception e) {
			e.printStackTrace();
			return ParametersServer.ACTION_ABORTED;
		}
	}
	
	public static String getPath()
	{		
		
		return ParametersServer.path;
	}
	// PENDIENTE CONTROLAR ERRORES, LOS COMANDOS QUE TIENEN QUE SALIR, ETC.
	// PENDIENTE LOGUEAR LOS COMANDOS DEL SERVIDOR.
	// DEVOLVER STRING CON EL C�DIGO
	public static String downloadFileFromServer(String filename) {
		try {
			
			Socket connection = dataConnection.accept();
			
			// Para leer nuestro archivo
			DataInputStream input = new DataInputStream(new FileInputStream(ParametersServer.RESOURCES+filename));	
			// Para escribirselo al cliente
			DataOutputStream output = new DataOutputStream(connection.getOutputStream());
			
			// Buffer de 1000 bytes
			byte[] buffer = new byte[1000];
			// Escribimos al buffer
			int n_bytes = input.read(buffer);
			System.out.println("Sending: "+buffer);
			// Si hemos recuperado cosas, las escibimos al output y ya las recogera el cliente
			while (n_bytes != -1) { // input.read devuelve -1 cuando no queda nada
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
			System.out.println("Entro a la funcion");
			Socket connection = dataConnection.accept();
			System.out.println("Pillo socket");
			
			InputStream in = null;
		    OutputStream out = null;
		    
	        try {
	        	
	            in = connection.getInputStream();
	        } catch (IOException ex) {
	            System.out.println("Can't get socket input stream. ");
	        }

	        try {
				System.out.println("Cojo la ruta");

	            out = new FileOutputStream(ParametersServer.RESOURCES+filename);
	            System.out.println("Tengo la ruta");
	        } catch (FileNotFoundException ex) {
	            System.out.println("File not found. ");
	        }

	        byte[] bytes = new byte[16*1024];

	        int count;
	        while ((count = in.read(bytes)) > 0) {
	            out.write(bytes, 0, count);
	        }

	        out.close();
	        in.close();
	        connection.close();
	        return ParametersServer.SUCCESS;
	        //serverSocket.close();
	    }	
		 catch (Exception e) {

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
