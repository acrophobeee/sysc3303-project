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
import java.util.ArrayList;
import java.util.Date;

public class ElevatorSubsystem {

	DatagramPacket sendPacket, receivePacket;
	DatagramSocket sendSocket;
	
    public Elevator elevator;
    private Date date;
    
    private Thread E1, E2, E3, receiveS;
    private ArrayList<Integer> listE1, listE2, listE3;
    
	public ElevatorSubsystem() {
		try {
			// Construct a datagram socket and bind it to any available
			// port on the local host machine. This socket will be used to
			// send UDP Datagram packets.
			sendSocket = new DatagramSocket();
			E1 = new Thread(new Elevator(1, this), "Elevator 1");
			E2 = new Thread(new Elevator(2, this), "Elevator 2");
			E3 = new Thread(new Elevator(3, this), "Elevator 3");
			receiveS = new Thread(new ReceiveSocket(this), "ReceiveSocket");
			// Construct a datagram socket and bind it to port 5000
			// on the local host machine. This socket will be used to
			// receive UDP Datagram packets.
			E1.start();
			E2.start();
			E3.start();
			receiveS.start();
			
			listE1 = new ArrayList<Integer>();
			listE2 = new ArrayList<Integer>();
			listE3 = new ArrayList<Integer>();
			// to test socket timeout (2 seconds)
			// receiveSocket.setSoTimeout(2000);
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}

	public int get(int eleNum, int currFloor, Elevatorstate state) throws Exception {
		while (true) {

			// Block until a datagram packet is received from receiveSocket
			return 0;
		}
		
	}
	
	/**
	 * @desc decide which elevator should use
	 * */
	public Thread getCar(int car) {
		if(car==1) {
			return E1;
		}else if(car==2) {
			return E2;
		}else {
			return E3;
		}
	}
	
	/**
	 * @desc receive packet from Receive Scoket class
	 * @param packet datagram
	 * */
	public void put(byte[] data){
		System.out.println("Put: Packet received:");
		System.out.print("Containing: ");
		
		StringBuilder temp = new StringBuilder();
		for (byte b : data) {
			temp.append(b);
		}
		
		System.out.println(temp);
		
		int currFloor = data[0]*10 + data[1];
		int destination = data[2]*10 + data[3];
		int carNum = data[4]*10 + data[5];
		
		if(carNum==1) {
			if(true) {
				
			}
		}else if(carNum==2){
			
		}else if(carNum==3) {
			
		}
	}

	public void stopServer() {
		sendSocket.close();
	}

	public static void main(String args[]) throws Exception {
		ElevatorSubsystem c = new ElevatorSubsystem();
		while (true) {
			try {
//				c.receive();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
