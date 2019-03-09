// SimpleEchoServer.java
// This class is the server side of a simple echo server based on
// UDP/IP. The server receives from a client a packet containing a character
// string, then echoes the string back to the client.
// Last edited January 9th, 2016

import java.io.*;
import java.net.*;
import java.util.Date;

public class ElevatorControlSystem {

	DatagramPacket sendPacket, receivePacket;
	DatagramSocket sendSocket;
	
	private ElevatorSubsystem E1, E2, E3;
	public Elevator elevator;
	private Date date;
	private Thread receiveS;

	public ElevatorControlSystem() {
		try {
			// Construct a datagram socket and bind it to any available
			// port on the local host machine. This socket will be used to
			// send UDP Datagram packets.
			sendSocket = new DatagramSocket();
			E1 = new ElevatorSubsystem(1, this);
			E2 = new ElevatorSubsystem(2, this);
			E3 = new ElevatorSubsystem(3, this);
			receiveS = new Thread(new ReceiveSocket(this), "ReceiveSocket");
			// Construct a datagram socket and bind it to port 5000
			// on the local host machine. This socket will be used to
			// receive UDP Datagram packets.
			receiveS.start();
			date = new Date();
			// to test socket timeout (2 seconds)
			// receiveSocket.setSoTimeout(2000);
			
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}

	

	/**
	 * Sending a updated information to scheduler.
	 * 
	 * @param elevatorNum  The elevator number
	 * @param currentFloor Current Floor
	 * @param state        The elevator state
	 */
	public void updateElevator(int elevatorNum, int currentFloor, Elevatorstate state) {
		byte fixvalue[] = new byte[2];
		fixvalue[0] = (byte) 0;
		fixvalue[1] = (byte) 1;

		byte num[] = new byte[2];
		num[0] = (byte) (elevatorNum / 10);
		num[1] = (byte) (elevatorNum % 10);

		byte mode[] = new byte[2];
		if (state instanceof idle) {
			mode[0] = (byte) 0;
			mode[1] = (byte) 3;
		} else if (state instanceof Upmode) {
			mode[0] = (byte) 0;
			mode[1] = (byte) 1;
		} else if (state instanceof Downmode) {
			mode[0] = (byte) 0;
			mode[1] = (byte) 2;
		} else if (state instanceof DoorOpen) {
			mode[0] = (byte) 0;
			mode[1] = (byte) 4;
		}

		byte floor[] = new byte[2];
		floor[0] = (byte) (currentFloor / 10);
		floor[1] = (byte) (currentFloor % 10);

		byte databack[] = new byte[50];

		System.arraycopy(fixvalue, 0, databack, 0, 2);
		System.arraycopy(num, 0, databack, 2, 2);
		System.arraycopy(mode, 0, databack, 4, 2);
		System.arraycopy(floor, 0, databack, 6, floor.length);
//		System.arraycopy(time, 0, databack, 8, time.length);

		try {
			sendPacket = new DatagramPacket(databack, databack.length, InetAddress.getLocalHost(), 3000);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(1);
		}

		try {
			sendSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

	}

	/**
	 * @desc decide which elevator should use
	 */
	public ElevatorSubsystem getCar(int car) {
		if (car == 1) {
			return E1;
		} else if (car == 2) {
			return E2;
		} else {
			return E3;
		}
	}

	/**
	 * @desc sort the ArrayList after append an integer into required position
	 */

	/**
	 * @desc receive packet from Receive Scoket class
	 * @param data the info data that get from schedule contain request information
	 */
	public void put(byte data[]) {
		int carNum = data[0] * 10 + data[1];
		int currFloor = data[2] * 10 + data[3];
		int destination = data[4] * 10 + data[5];
//		System.arraycopy(data, 4, time, 0, data.length);
//		System.arraycopy(data, 4, time, 0, data.length);

		// check whether the request should go up or down
		
		System.out.println(carNum);
		if (carNum == 1) {
			E1.putNewRequest(currFloor, destination);			
		} else if (carNum == 2) {
			E2.putNewRequest(currFloor, destination);	
		} else if (carNum == 3) {
			E3.putNewRequest(currFloor, destination);	
		}
		
	}
	
	/**
	 * Stop the elevater system
	 */
	public void stopServer() {
		sendSocket.close();
	}

	public static void main(String args[]) throws Exception {
		ElevatorControlSystem c = new ElevatorControlSystem();
	}
}
