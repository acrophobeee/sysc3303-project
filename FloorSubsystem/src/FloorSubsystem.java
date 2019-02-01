
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
			
			receiveSocket = new DatagramSocket(23);//receive port is 23
		} catch (SocketException se) { // Can't create the socket.
			se.printStackTrace();
			System.exit(1);
		}
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
	}

}
