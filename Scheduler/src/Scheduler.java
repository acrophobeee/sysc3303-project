
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

			// Construct a datagram socket and bind it to port 5000
			// on the local host machine. This socket will be used to
			// receive UDP Datagram packets.
			clientSocket = new DatagramSocket(23);

			// to test socket timeout (2 seconds)
			// receiveSocket.setSoTimeout(2000);
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}

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

			// ---------------------------------------

			try {
				sendPacket = new DatagramPacket(data, receivePacket.getLength(), InetAddress.getLocalHost(), 69);
			} catch (UnknownHostException e) {
				e.printStackTrace();
				System.exit(1);
			}

			System.out.println("Host: Sending packet:");
			System.out.println("To server: " + sendPacket.getAddress());
			System.out.println("Destination server port: " + sendPacket.getPort());
			len = sendPacket.getLength();
			System.out.println("Length: " + len);
			System.out.print("Containing: ");
			System.out.println(new String(sendPacket.getData(), 0, len)); // or could print "s"

			// Send the datagram packet to the server via the send/receive socket.

			try {
				serverSocket.send(sendPacket);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}

			System.out.println("Host: Packet sent.\n");

			// Construct a DatagramPacket for receiving packets up
			// to 100 bytes long (the length of the byte array).

			byte data1[] = new byte[100];
			receivePacket = new DatagramPacket(data1, data1.length);

			try {
				// Block until a datagram is received via sendReceiveSocket.
				serverSocket.receive(receivePacket);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}

			// Process the received datagram.
			System.out.println("Host: Server Packet received:");
			System.out.println("From server: " + receivePacket.getAddress());
			System.out.println("Server port: " + receivePacket.getPort());
			len = receivePacket.getLength();
			System.out.println("Length: " + len);
			System.out.print("Containing: ");

			// Form a String from the byte array.
			String received1 = new String(data1, 0, len);
			System.out.println(received1);

			// ---------------------------------------
			// byte data1[] = data;
			sendPacket = new DatagramPacket(data1, receivePacket.getLength(), receivePacket.getAddress(), clientPort);

			System.out.println("Host: Sending packet:");
			System.out.println("To client: " + sendPacket.getAddress());
			System.out.println("Destination client port: " + sendPacket.getPort());
			len = sendPacket.getLength();
			System.out.println("Length: " + len);
			System.out.print("Containing: ");
			System.out.println(new String(sendPacket.getData(), 0, len));
			StringBuilder temp2 = new StringBuilder();
			for (byte b : sendPacket.getData()) {
				temp2.append(b);
			}
			System.out.println(temp2 + "\n");

			// or (as we should be sending back the same thing)
			// System.out.println(received);

			// Send the datagram packet to the client via the send socket.
			try {
				serverSocket.send(sendPacket);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}

			System.out.println("Host: packet sent");
		}
	}

	public static void main(String args[]) {
		Scheduler c = new Scheduler();
		c.receiveAndEcho();
	}
}
