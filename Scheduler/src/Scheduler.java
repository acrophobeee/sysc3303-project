
// SimpleEchoServer.java
// This class is the server side of a simple echo server based on
// UDP/IP. The server receives from a client a packet containing a character
// string, then echoes the string back to the client.
// Last edited January 9th, 2016

import java.io.*;
import java.net.*;

public class Scheduler {

	DatagramPacket sendPacket, receivePacket;
	DatagramSocket serverSocket, clientSocket;

	public Scheduler() {
		try {
			// Construct a datagram socket and bind it to any available
			// port on the local host machine. This socket will be used to
			// send UDP Datagram packets.
			serverSocket = new DatagramSocket();

			// Construct a datagram socket and bind it to port 3000
			// on the local host machine. This socket will be used to
			// receive UDP Datagram packets.
			clientSocket = new DatagramSocket(3000);

			// receiveSocket.setSoTimeout(2000);
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}

	/*
	 * @desc This method will create an method that receive request from client and schedule an elevator to client
	 * 		 After schedule an elevator, send an packet back to the client include elevator's information
	 * */
	public void receiveAndEcho() {
		while (true) {
			// Construct a DatagramPacket for receiving packets up
			// to 100 bytes long (the length of the byte array).

			byte data[] = new byte[100];
			receivePacket = new DatagramPacket(data, data.length);
			System.out.println("Host: Waiting for Packet.\n");

			// Block until a datagram packet is received from receiveSocket.
			try {
				System.out.println("Waiting..."); // so we know we're waiting
				clientSocket.receive(receivePacket);
			} catch (IOException e) {
				System.out.print("IO Exception: likely:");
				System.out.println("Receive Socket Timed Out.\n" + e);
				e.printStackTrace();
				System.exit(1);
			}

			// Process the received datagram.
			int clientPort = receivePacket.getPort();
			System.out.println("Host: Client Packet received:");
			System.out.println("From host: " + receivePacket.getAddress());
			System.out.println("Host port: " + receivePacket.getPort());
			int len = receivePacket.getLength();
			System.out.println("Length: " + len);
			System.out.print("Containing: ");

			// Form a String from the byte array.
			String received = new String(data, 0, len);
			System.out.println(received);
			StringBuilder temp = new StringBuilder();
			for (byte b : data) {
				temp.append(b);
			}
			System.out.println(temp + "\n");

			// Slow things down (wait 0.5 seconds)
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

	public static void main(String args[]) {
		Scheduler c = new Scheduler();
		c.receiveAndEcho();
	}
}
