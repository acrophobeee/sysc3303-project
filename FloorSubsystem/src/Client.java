
// SimpleEchoClient.java
// This class is the client side for a simple echo server based on
// UDP/IP. The client sends a character string to the echo server, then waits 
// for the server to send it back to the client.
// Last edited January 9th, 2016

import java.io.*;
import java.net.*;
import java.util.*;


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

	public void sendAndReceive(Date date, int floor, String Button) {
		System.out.println("Client: sending a request with \n present time: " + date + "\n mode: " + Button);


		byte But[] = Button.getBytes();
		String floors = String.valueOf(floor);
		byte floorByte[] = floors.getBytes();
        String d = date.toString();
        byte dat[] = d.getBytes();
		byte request[] = new byte[But.length + floorByte.length + dat.length];
		
		System.arraycopy(But, 0, request, 0, But.length);
		System.arraycopy(floorByte, 0, request, But.length, floorByte.length);
		System.arraycopy(dat, 0, request, But.length+ floorByte.length, dat.length);

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
	    Date a = new Date();
		Client c = new Client();
		
		for (int i = 0; i < 10; ++i) {
			if (i % 2 == 0) {
				c.sendAndReceive(a, 1, "up ");
			} else {
				c.sendAndReceive(a, 7 , "down ");
			}
		}
		c.stopClient();
	}

}
