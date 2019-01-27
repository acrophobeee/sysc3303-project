
// SimpleEchoServer.java
// This class is the server side of a simple echo server based on
// UDP/IP. The server receives from a client a packet containing a character
// string, then echoes the string back to the client.
// Last edited January 9th, 2016

import java.io.*;
import java.net.*;

public class ElevatorSubsystem {
	/*
	 * jfie
	 */

	DatagramPacket sendPacket, receivePacket;
	DatagramSocket sendSocket, receiveSocket;

	public ElevatorSubsystem() {
		try {
			// Construct a datagram socket and bind it to any available
			// port on the local host machine. This socket will be used to
			// send UDP Datagram packets.
			sendSocket = new DatagramSocket();

			// Construct a datagram socket and bind it to port 5000
			// on the local host machine. This socket will be used to
			// receive UDP Datagram packets.
			receiveSocket = new DatagramSocket(69);

			// to test socket timeout (2 seconds)
			// receiveSocket.setSoTimeout(2000);
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}

	public void receiveAndEcho() throws Exception {
		while (true) {
			// Construct a DatagramPacket for receiving packets up
			// to 100 bytes long (the length of the byte array).
			byte data[] = new byte[60];
			receivePacket = new DatagramPacket(data, data.length);
			System.out.println("Server: Waiting for Packet.\n");

			// Block until a datagram packet is received from receiveSocket.
			try {
				System.out.println("Waiting..."); // so we know we're waiting
				receiveSocket.receive(receivePacket);
			} catch (IOException e) {
				System.out.print("IO Exception: likely:");
				System.out.println("Receive Socket Timed Out.\n" + e);
				e.printStackTrace();
				System.exit(1);
			}

			// Process the received datagram.
			System.out.println("Server: Packet received:");
			System.out.println("=============");
			System.out.println("From host: " + receivePacket.getAddress());
			System.out.println("Host port: " + receivePacket.getPort());
			int len = receivePacket.getLength();
			System.out.println("Length: " + len);
			System.out.print("Containing: ");

			// Form a String from the byte array.
			String received = new String(data, 0, len);
			System.out.println(received);
			StringBuilder temp = new StringBuilder();
			for (byte b : receivePacket.getData()) {
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

			
			byte dataBack[] = new byte[4];
				dataBack[0] = (byte) 0;
				dataBack[1] = (byte) 3;
				dataBack[2] = (byte) 0;
				dataBack[3] = (byte) 1;
			

			// ----------------------------------------

			sendPacket = new DatagramPacket(dataBack, dataBack.length, receivePacket.getAddress(),
					receivePacket.getPort());

			System.out.println("Server: Sending packet:");
			System.out.println("To host: " + sendPacket.getAddress());
			System.out.println("Destination host port: " + sendPacket.getPort());
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
				sendSocket.send(sendPacket);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}

			System.out.println("Server: packet sent");

		}

	}

	public void stopServer() {
		sendSocket.close();
		receiveSocket.close();
	}

	public static void main(String args[]) {
		ElevatorSubsystem c = new ElevatorSubsystem();
		try {
			c.receiveAndEcho();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
