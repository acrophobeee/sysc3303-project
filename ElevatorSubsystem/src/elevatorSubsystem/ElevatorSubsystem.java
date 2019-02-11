package elevatorSubsystem;

// SimpleEchoServer.java
// This class is the server side of a simple echo server based on
// UDP/IP. The server receives from a client a packet containing a character
// string, then echoes the string back to the client.
// Last edited January 9th, 2016

import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ElevatorSubsystem {

	DatagramPacket sendPacket, receivePacket;
	DatagramSocket sendSocket;
	
    public Elevator elevator;
    private Date date;
    
    private Thread E1, E2, E3, receiveS;
    
	public ElevatorSubsystem() {
		try {
			// Construct a datagram socket and bind it to any available
			// port on the local host machine. This socket will be used to
			// send UDP Datagram packets.
			sendSocket = new DatagramSocket();
			receiveS = new Thread(new ReceiveSocket(this), "ReceiveSocket");
			E1 = new Thread(new Elevator(1, this), "Elevator 1");
			E2 = new Thread(new Elevator(2, this), "Elevator 2");
			E3 = new Thread(new Elevator(3, this), "Elevator 3");
			// Construct a datagram socket and bind it to port 5000
			// on the local host machine. This socket will be used to
			// receive UDP Datagram packets.
			E1.start();
			E2.start();
			E3.start();
			receiveS.start();
			// to test socket timeout (2 seconds)
			// receiveSocket.setSoTimeout(2000);
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}

	public void get() throws Exception {
		while (true) {

			// Block until a datagram packet is received from receiveSocket
		}

	}
	
	/**
	 * @desc receive packet from Receive Scoket class
	 * @param packet datagram
	 * */
	public void put(DatagramPacket packet){
		receivePacket = packet;
		byte data[] = new byte[16];
		receivePacket = new DatagramPacket(data, data.length);
		// Process the received datagram.
		System.out.println("Server: Packet received:");
		System.out.println("From host: " + receivePacket.getAddress());
		System.out.println("Host port: " + receivePacket.getPort());
		int len = receivePacket.getLength();
		System.out.println("Length: " + len);
		System.out.print("Containing: ");
		
		String received = new String(data, 4, len-4);
		System.out.println(received);
		StringBuilder temp = new StringBuilder();
		for (byte b : data) {
			temp.append(b);
		}
		
		String hour, mins, second;			
		String[] splittedDate = received.split(":");			
		String[] splittedSecond = splittedDate[2].split("\\.");
		
		hour = splittedDate[0];
		mins = splittedDate[1];
		second = splittedSecond[0];
		
		System.out.println("hour: " + hour + " mins: " + mins + " second: " + second);
		
		int a = data[0] * 10 + data[1];
		int b = data[2] * 10 + data[3];

		elevator.add(a);
		elevator.add(b);
		
		int resH, resM, resS;
		resH = Integer.parseInt(hour);
		resM = Integer.parseInt(mins);
		resS = Integer.parseInt(second);
	}

	public void stopServer() {
		sendSocket.close();
	}

	public static void main(String args[]) throws Exception {
		ElevatorSubsystem c = new ElevatorSubsystem();
		while (true) {
			try {
				c.receive();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
