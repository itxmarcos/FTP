import jpcap.*;
import jpcap.packet.Packet;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Scanner;

public class Layer1 extends Layer{
	NetworkInterface[] devices;
	JpcapCaptor captor;
	JpcapSender sender;

	public static void main(String[] args){
		try {
			Layer1 miLayer=new Layer1();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public Layer1 () throws IOException{
		devices = JpcapCaptor.getDeviceList();
		int number=configuration();
		//Open an interface with openDevice(NetworkInterface intrface, int snaplen, boolean promics, int to_ms)
		captor=JpcapCaptor.openDevice(devices[number], 65535, true, 20); //boolean promics changed to true	
		//open a network interface to send a packet to
		sender=JpcapSender.openDevice(devices[number]);
		run(number);
	}
	
	public int configuration() throws IOException {
		//Obtain the list of network interfaces
		//for each network interface
		for (int i = 0; i < devices.length; i++) {
		  //print out its name and description
		  System.out.println(i+": "+devices[i].name + "(" + devices[i].description+")");

		  //print out its datalink name and description
		  System.out.println(" datalink: "+devices[i].datalink_name + "(" + devices[i].datalink_description+")");

		  //print out its MAC address
		  System.out.print(" MAC address:");
		  for (byte b : devices[i].mac_address)
		    System.out.print(Integer.toHexString(b&0xff) + ":");
		  System.out.println();

		  //print out its IP address, subnet mask and broadcast address
		  for (NetworkInterfaceAddress a : devices[i].addresses)
		    System.out.println(" address:"+a.address + " " + a.subnet + " "+ a.broadcast);
		}
		
		//Ask the user which interface to use
		System.out.println("/n/nSelect an interface number from before: ");
		Scanner input = new Scanner(System.in);
		int number = input.nextInt();
		input.close();
		
		return number;
	}
	
	public void run(int number) {
		//call processPacket() to let Jpcap call PacketPrinter.receivePacket() for every packet capture.
		captor.processPacket(10,new PacketPrinter());
		/*for(int i=0;i<10;i++){
			  //capture a single packet and print it out
			  System.out.println(captor.getPacket());
			}*/
		captor.close();
		
		//create a TCP packet with specified port numbers, flags, and other parameters
		TCPPacket p=new TCPPacket(12,34,56,78,false,false,false,false,true,true,true,true,10,10);

		//specify IPv4 header parameters
		p.setIPv4Parameter(0,false,false,false,0,false,false,false,0,1010101,100,IPPacket.IPPROTO_TCP,
		InetAddress.getByName("www.microsoft.com"),InetAddress.getByName("www.google.com"));

		//set the data field of the packet
		p.data=("data").getBytes();

		//create an Ethernet packet (frame)
		EthernetPacket ether=new EthernetPacket();
		//set frame type as IP
		ether.frametype=EthernetPacket.ETHERTYPE_IP;
		//set source and destination MAC addresses
		ether.src_mac=new byte[]{(byte)0,(byte)1,(byte)2,(byte)3,(byte)4,(byte)5};
		ether.dst_mac=new byte[]{(byte)0,(byte)6,(byte)7,(byte)8,(byte)9,(byte)10};

		//set the datalink frame of the packet p as ether
		p.datalink=ether;

		//send the packet p
		sender.sendPacket(p);

		sender.close();
	}

}

class PacketPrinter implements PacketReceiver {
	  //this method is called every time Jpcap captures a packet
	@Override
	public void receivePacket(Packet arg0) {
		//just print out a captured packet
	    System.out.println(arg0);		
	}
}