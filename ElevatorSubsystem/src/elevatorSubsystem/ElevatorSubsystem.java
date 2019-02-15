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

	public int get(int elenumber, int currentfloor, Elevatorstate state) {
		int destination = 0;
		try {
			if (elenumber == 1) {
				while (listE1.isEmpty()) {
					wait();
				}
				destination = (int) listE1.get(0);
				if (currentfloor == destination) {
					listE1.remove(0);
					return 0;
				}
			} else if (elenumber == 2) {
				while (listE2.isEmpty()) {
					wait();
				}
				destination = (int) listE2.get(0);
				if (currentfloor == destination) {
					listE2.remove(0);
					return 0;
				}
			} else if (elenumber == 3) {
				while (listE3.isEmpty()) {
					wait();
				}
				destination = (int) listE3.get(0);
				if (currentfloor == destination) {
					listE3.remove(0);
					return 0;
				}
			} else {
				System.out.println("Elevator Error");
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (currentfloor > destination) {
			return -1;
		}
		return 1;
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
	 */
	public void put(byte data[]) {
	
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
