
// SimpleEchoClient.java
// This class is the client side for a simple echo server based on
// UDP/IP. The client sends a character string to the echo server, then waits 
// for the server to send it back to the client.
// Last edited January 9th, 2016

import java.io.*;
import java.net.*;
import java.util.*;


public class FloorSubsystem implements Runnable {

	private DatagramPacket sendPacket, receivePacket;
	private DatagramSocket sendSocket, receiveSocket;
	private Date reqDate;
	
	/**
	 * @desc receive an socket from scheduler, 
	 * socket will contain the info that which elevator will come. 
	 * and which floor the elevator are
	 * */
	public void receiveSocket() {
		// Construct a DatagramPacket for receiving packets up
		// to 100 bytes long (the length of the byte array).
		
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
//		System.out.println("From scheduler host: " + receivePacket.getAddress());
//		System.out.println("Scheduler host port: " + receivePacket.getPort());
		System.out.println("Elevator number: " + data[2] +"" +data[3]);
		if(data[5]==1) {
			System.out.println("Elevator mode: " +"up");
		}else if(data[5]==2) {
			System.out.println("Elevator mode: " +"down");
		}else if(data[5]==3) {
			System.out.println("Elevator mode: " +"idle");
		}
		
		
		System.out.println("Elevator current floow: " + data[6] +"" +data[7]);

		// Form a String from the byte array.
		System.out.println("Receiving finished. \n");
		
		receiveSocket();
	}
	/**
	 * @desc terminate the client socket
	 * */
	public void stopClient() {
		sendSocket.close();
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			// Construct a datagram socket and bind it to any available
			// port on the local host machine. This socket will be used to
			// send and receive UDP Datagram packets.
			
			receiveSocket = new DatagramSocket(23);//receive port is 23
			System.out.println("Floor socket is running on port 23 and wait for packet: ");
			while(true) {
				receiveSocket();
			}
			
		} catch (SocketException se) { // Can't create the socket.
			se.printStackTrace();
			System.exit(1);
		}
		
	}

}
