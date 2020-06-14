package server;

import java.io.*; 
import java.net.*;

import utils.FunctionalityServer;
import utils.ParametersServer;

public class Server
{
	public static ServerSocket controlConnection;
   
    /*
     * 	 Conexi�n de control
     * |				 	|
     * |				 	|
     * |				 	|
     * |				 	|
     * |  Llega cmd datos 	|
     * |  Inicio conexi�n|
     * |   | start_data	| 	|
     * |   |    data	| 	|
     * |   |    data	| 	|
     * |   |    data	| 	|
     * |   |    data	| 	|
     * |   |    data	| 	|
     * |   |    data	| 	|
     * |   |  fin_data	| 	|
     * |  Cierre conexi�n	|
     * |				 	|
     * |				 	|
     * |				 	|
     * |				 	|
     * |  Llega cmd datos 	|
     * |  Inicio conexi�n |
     * |   | start_data	| 	|
     * |   |    data	| 	|
     * |   |    data	| 	|
     * |   |    data	| 	|
     * |   |    data	| 	|
     * |   |    data	| 	|
     * |   |    data	| 	|
     * |   |  fin_data	| 	|
     * |  Cierre conexi�n	|
     * |				 	|
     * |				 	|
     */
    public static void runServer()
    {
        try
        {
            //Creamos el servidor con sus respectivos puertos
        	controlConnection = new ServerSocket(ParametersServer.serverControlPort);
            
            //Aceptamos la conexion del cliente al puerto por defecto (1400)
            System.out.println("Waiting for a client...\n");
            Socket connection = controlConnection.accept();
            System.out.println("Connection accepted at port " + ParametersServer.serverControlPort);
            
            String command = "";
            
            //Asociamos la informacion de entrada y de salida
            BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            PrintWriter output = new PrintWriter(connection.getOutputStream(), true);

            while(!command.equals("QUIT"))
            {
              
                command =  input.readLine();
                
                System.out.println("Received command " + command);
                
          
                String response = FunctionalityServer.checkCommand(command);
                
                // Mandar respuesta al cliente
                
                output.println(response);
                

            }
            
            // Cerrar la conexion del cliente
            connection.close();
            System.out.println("Se ha cerrado la conexi�n del usuario.");
            controlConnection.close();
            System.out.println("Se ha cerrado el servidor.");
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}