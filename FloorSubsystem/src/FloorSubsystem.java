
// SimpleEchoClient.java
// This class is the client side for a simple echo server based on
// UDP/IP. The client sends a character string to the echo server, then waits 
// for the server to send it back to the client.
// Last edited January 9th, 2016

import java.io.*;
import java.net.*;
import java.util.*;


public class FloorSubsystem {

	private DatagramPacket sendPacket, receivePacket;
	private DatagramSocket sendSocket, receiveSocket;
	private Date reqDate;
	
	public FloorSubsystem() {
		try {
			// Construct a datagram socket and bind it to any available
			// port on the local host machine. This socket will be used to
			// send and receive UDP Datagram packets.
			sendSocket = new DatagramSocket();
			receiveSocket = new DatagramSocket(23);
		} catch (SocketException se) { // Can't create the socket.
			se.printStackTrace();
			System.exit(1);
		}
	}

	public void sendSocket(Date date, int floor, String Button) {
		System.out.println("Client: sending a request with \npresent time: " + date + "\nmode: " + Button);

		reqDate = date; //store request date
		
		byte But[] = Button.getBytes();
		String floors = String.valueOf(floor);
		byte floorByte[] = floors.getBytes();
        String d = date.toString();
        byte dat[] = d.getBytes();
		byte request[] = new byte[But.length + floorByte.length + dat.length];
		
		System.arraycopy(But, 0, request, 0, But.length);
		System.arraycopy(floorByte, 0, request, But.length, floorByte.length);
		System.arraycopy(dat, 0, request, But.length+ floorByte.length, dat.length);
		
		StringBuilder requestString = new StringBuilder();
		for (byte b : request) {
			requestString.append(b);
		}
		System.out.println("request: "+requestString);
		//System.out.println(request);

		try {
			sendPacket = new DatagramPacket(request, request.length, InetAddress.getLocalHost(), 3000);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(1);
		}

		// Send the datagram packet to the server via the send/receive socket.

		try {
			sendSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("Client: Packet sent.\n");

		// sendReceiveSocket.close();
	}
	
	/*
	 * @desc receive an socket from scheduler, socket will contain the info that which elevator will come. and which floor the elevator are
	 * 
	 * */
	public void receiveSocket() {
		// Construct a DatagramPacket for receiving packets up
		// to 100 bytes long (the length of the byte array).
		System.out.println("Floor socket is running on port 23 and wait for packet: ");
		byte data[] = new byte[100];
		receivePacket = new DatagramPacket(data, data.length);
		
		try {
			receiveSocket.receive(receivePacket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Process the received datagram.
		System.out.println("Floor: Packet received:");
		System.out.println("From host: " + receivePacket.getAddress());
		System.out.println("Host port: " + receivePacket.getPort());
		System.out.println("Length: " + receivePacket.getLength());
		System.out.print("Containing: ");

		// Form a String from the byte array.
		StringBuilder received = new StringBuilder();
		for (byte b : data) {
			received.append(b);
		}
		System.out.println(received + "\n");
		receiveSocket();
		
	}

	public void stopClient() {
		sendSocket.close();
	}

	public static void main(String args[]) {
	    Date a = new Date();
		FloorSubsystem c = new FloorSubsystem();
		
		for (int i = 0; i < 10; ++i) {
			if (i % 2 == 0) {
				c.sendSocket(a, 1, "up ");
			} else {
				c.sendSocket(a, 7 , "down ");
			}
		}
		c.stopClient();
	}

}
