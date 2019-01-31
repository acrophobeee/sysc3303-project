
// SimpleEchoServer.java
// This class is the server side of a simple echo server based on
// UDP/IP. The server receives from a client a packet containing a character
// string, then echoes the string back to the client.
// Last edited January 9th, 2016

import java.io.*;
import java.net.*;

public class Scheduler {

	DatagramPacket sendPacket, receivePacket;
	DatagramSocket schedulerSocket;
	ElevatorStatus e;

	public Scheduler() {
		try {
			// Construct a datagram socket and bind it to port 3000
			schedulerSocket = new DatagramSocket(3000);
			e = new ElevatorStatus(1, 69);
			// receiveSocket.setSoTimeout(2000);
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * @desc This method will create an method that receive request from client and schedule an elevator to client
	 * 		 After schedule an elevator, send an packet back to the client include elevator's information
	 */
	public void waitForEvent() {
		
		while (true) {
			// Construct a DatagramPacket for receiving packets up
			// to 100 bytes long (the length of the byte array).

			byte data[] = new byte[50];
			receivePacket = new DatagramPacket(data, data.length);
			System.out.println("Scheduler: Waiting for Event.\n");
			try {
				System.out.println("Scheduler address: " + InetAddress.getLocalHost() + "\n");
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			// Block until a datagram packet is received an event.
			try {
				System.out.println("Waiting..."); // so we know we're waiting
				schedulerSocket.receive(receivePacket);
			} catch (IOException e) {
				System.out.print("IO Exception: likely:");
				System.out.println("Receive Socket Timed Out.\n" + e);
				e.printStackTrace();
				System.exit(1);
			}
			
			// Decode the received datagram.
			System.out.println("Scheduler: Event received.");
			System.out.println("From host: " + receivePacket.getAddress());
			System.out.println("Host port: " + receivePacket.getPort());
			int len = receivePacket.getLength();
			System.out.println("Length: " + len);
			System.out.print("Containing: ");

			// Form a String from the byte array.
			String received = new String(data, 8, len-8);
			System.out.println(received);
			StringBuilder temp = new StringBuilder();
			for (byte b : data) {
				temp.append(b);
			
			if (data[0] == (byte) 0) {
				floorRequest(data);
			} else if (data[0] == (byte) 1) {
				elevatorUpdate(data);
			}
			
			
			
			
			
			
			
//			int len = receivePacket.getLength();
//			
//			System.out.println("Length: " + len);
//			System.out.print("Containing: ");
//
//			// Form a String from the byte array.
//			String received = new String(data, 0, len);
//			System.out.println(received);
//			StringBuilder temp = new StringBuilder();
//			for (byte b : data) {
//				temp.append(b);
//			}
//			System.out.println(temp + "\n");
			}
		}
	}
	
	/**
	 * @desc process floor's request
	 * @param floor's data
	 * */
	public void floorRequest(byte data[]) {
		byte[] request = new byte[2];
		request[0] = data[4];
		request[1] = data[5];
		try {
			sendPacket = new DatagramPacket(request, request.length, InetAddress.getLocalHost(), 69);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		try {
			schedulerSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("Client: Packet sent.\n");
	}
	
	public void elevatorUpdate(byte data[]) {
		
	}
	
	public static void main(String args[]) {
		Scheduler c = new Scheduler();
		c.waitForEvent();
	}
}
