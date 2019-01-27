
// SimpleEchoClient.java
// This class is the client side for a simple echo server based on
// UDP/IP. The client sends a character string to the echo server, then waits 
// for the server to send it back to the client.
// Last edited January 9th, 2016

import java.io.*;
import java.net.*;

public class Client {

	DatagramPacket sendPacket, receivePacket;
	DatagramSocket sendReceiveSocket;

	public Client() {
		try {
			// Construct a datagram socket and bind it to any available
			// port on the local host machine. This socket will be used to
			// send and receive UDP Datagram packets.
			sendReceiveSocket = new DatagramSocket();
		} catch (SocketException se) { // Can't create the socket.
			se.printStackTrace();
			System.exit(1);
		}
	}

	public void sendAndReceive(byte type, String filename, String mode) {
		// Prepare a DatagramPacket and send it via sendReceiveSocket
		// to port 5000 on the destination host.

		// String filename = "test.txt";
		// String mode = "ocTEt";
		System.out.println("Client: sending a request with \n filename: " + filename + "\n mode: " + mode);

		// Java stores characters as 16-bit Unicode values, but
		// DatagramPackets store their messages as byte arrays.
		// Convert the String into bytes according to the platform's
		// default character encoding, storing the result into a new
		// byte array.

		byte nameByte[] = filename.getBytes();
		byte modeByte[] = mode.getBytes();

		byte request[] = new byte[nameByte.length + modeByte.length + 4];
		request[0] = (byte) 0;
		request[1] = type;

		int n = 2;
		for (int i = 0; i < nameByte.length; ++i) {
			request[n] = nameByte[i];
			n++;
		}
		request[n] = (byte) 0;
		n++;
		for (int i = 0; i < modeByte.length; ++i) {
			request[n] = modeByte[i];
			n++;
		}
		request[n] = (byte) 0;

		System.out.println(request);

		try {
			sendPacket = new DatagramPacket(request, request.length, InetAddress.getLocalHost(), 23);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(1);
		}

		// Send the datagram packet to the server via the send/receive socket.

		try {
			sendReceiveSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("Client: Packet sent.\n");

		// Construct a DatagramPacket for receiving packets up
		// to 100 bytes long (the length of the byte array).

		byte data[] = new byte[4];
		receivePacket = new DatagramPacket(data, data.length);

		try {
			// Block until a datagram is received via sendReceiveSocket.
			sendReceiveSocket.receive(receivePacket);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("Client: Packet received:");
		System.out.println(new String(receivePacket.getData(), 0, receivePacket.getLength()));
		// Form a String from the byte array.
		StringBuilder received = new StringBuilder();
		for (byte b : data) {
			received.append(b);
		}
		System.out.println(received);
		System.out.println("\n");
		// We're finished, so close the socket.
		// sendReceiveSocket.close();
	}

	public void stopClient() {
		sendReceiveSocket.close();
	}

	public static void main(String args[]) {
		byte invaild = (byte) 0;
		byte read = (byte) 1;
		byte write = (byte) 2;
		Client c = new Client();
		for (int i = 0; i < 10; ++i) {
			if (i % 2 == 0) {
				c.sendAndReceive(read, "test.txt", "ocTEt");
			} else {
				c.sendAndReceive(write, "test.txt", "ocTEt");
			}
		}
		c.sendAndReceive(invaild, "test.txt", "ocTEt");
		c.stopClient();
	}

}
