
// SimpleEchoServer.java
// This class is the server side of a simple echo server based on
// UDP/IP. The server receives from a client a packet containing a character
// string, then echoes the string back to the client.
// Last edited January 9th, 2016

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Scheduler {

	DatagramPacket sendPacket, receivePacket;
	DatagramSocket schedulerSocket;
	private ArrayList<ElevatorStatus> elevators;
	private ArrayList<ElevatorRequest> requests;

	public Scheduler() {
		try {
			// Construct a datagram socket and bind it to port 3000
			schedulerSocket = new DatagramSocket(3000);
			// receiveSocket.setSoTimeout(2000);
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
		elevators = new ArrayList<ElevatorStatus>();
		elevators.add(new ElevatorStatus(1));
		elevators.add(new ElevatorStatus(2));
		elevators.add(new ElevatorStatus(3));
		requests = new ArrayList<ElevatorRequest>();
		
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
			
			// Form a String from the byte array.
			String received = new String(data, 8, len-8);
			StringBuilder temp = new StringBuilder();
			for (byte b : data) {
				temp.append(b);
			}
			System.out.println("Data in byte form : " + temp + "\n");
			
			if (data[0] == (byte) 0 && data[1] == (byte) 0) {
				floorRequest(data, received);
			} else if (data[0] == (byte) 0 && data[1] == (byte) 1) {
				elevatorUpdate(data, received);
			}
		}
	}
	
	/**
	 * @desc process floor's request
	 * @param floor's data
	 * @param received time from floor
	 * */
	public void floorRequest(byte data[], String received) {
		
		byte[] request0 = new byte[2];
		byte[] request1 = new byte[2];
		byte[] request2 = new byte[2];
		request0[0] = data[2];
		request0[1] = data[3];
		request1[0] = data[4];
		request1[1] = data[5];
		request2[0] = data[6];
		request2[1] = data[7];
		
		int temp = byteToInt(request0);
		String direction = "unknown";
		if (temp == 0) {
			direction = "up";
		} else if (temp == 1) {
			direction = "down";
		}
		int current = byteToInt(request1);
		int destination = byteToInt(request2);
		
		int elevatorNum = -1;
		int distance = 99999;
		
		for (ElevatorStatus e : elevators) {
			if (e.getState() == "idle" && distance > Math.abs(current - e.getFloor())) {
				distance = Math.abs(current - e.getFloor());
				elevatorNum = e.getNumber();
			} else if (e.getState() == direction) {
				if (direction == "up" && e.getFloor() + 2 < current && distance > Math.abs(current - e.getFloor())) {
					distance = Math.abs(current - e.getFloor());
					elevatorNum = e.getNumber();
				} else if (direction == "down" && e.getFloor() - 2 > current && distance > Math.abs(current - e.getFloor())) {
					distance = Math.abs(current - e.getFloor());
					elevatorNum = e.getNumber();
				}
			}
		}
		
		byte[] timeByte = received.getBytes();
		
		if (elevatorNum == -1) {
			byte[] requestStore = new byte[16];
			System.arraycopy(request1, 0, requestStore, 2, request1.length);
			System.arraycopy(request2, 0, requestStore, 4, request2.length);
			System.arraycopy(timeByte, 0, requestStore, 6, timeByte.length);
			requests.add(new ElevatorRequest(current, destination, direction, requestStore));
			return;
		}
		
		
		
		byte num[] = new byte[2];
		num[0] = (byte) (elevatorNum / 10);
		num[1] = (byte) (elevatorNum % 10);
		
		byte[] dataSend = new byte[18];
		System.arraycopy(num, 0, dataSend, 0, num.length);
		System.arraycopy(request1, 0, dataSend, 2, request1.length);
		System.arraycopy(request2, 0, dataSend, 4, request2.length);
		System.arraycopy(timeByte, 0, dataSend, 6, timeByte.length);
		
		try {
			sendPacket = new DatagramPacket(dataSend, dataSend.length, InetAddress.getLocalHost(), 69);
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

		System.out.println("Scheduler: Order sent.\n");
	}
	
	/**
	 * @desc send elevator's information to scheduler.
	 * @param an array of bytes.
	 * @param received time from elevator
	 * */
	public void elevatorUpdate(byte data[], String received) {
		byte[] elevatorNum = new byte[2];
		byte[] mode = new byte[2];
		byte[] floor = new byte[2];
		elevatorNum[0] = data[2];
		elevatorNum[1] = data[3];
		mode[0] = data[4];
		mode[1] = data[5];
		floor[0] = data[6];
		floor[1] = data[7];
		
		String state = decodeState(mode);
		
		for (ElevatorStatus e : elevators) {
			if (e.getNumber() == byteToInt(elevatorNum)) {
				if (e.getState() != "idle" && e.getState() != state) {
					e.statusUpdate(byteToInt(floor), state);
					checkAllRequest(e);
				} else {
					e.statusUpdate(byteToInt(floor), state);
				}
				
				break;
			}
		}
		
		System.out.println("the elevator number is : " + byteToInt(elevatorNum));
		System.out.println("the mode is : " + state);
		System.out.println("the floor is : " + byteToInt(floor));
		System.out.println("the time is : " + received);
		
		try {
			sendPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 23);
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

		System.out.println("Scheduler: Status sent.\n");
	}
	
	/**
	 * Check all avairable request and sent the new order to elevator
	 * 
	 * @param e
	 */
	public void checkAllRequest(ElevatorStatus e) {
		
		ElevatorRequest shortest;
		for (ElevatorRequest r : requests) {
			
		}
	}
	
	
	/**
	 * @desc convert bytes to integer.
	 * @param an array o bytes
	 * @return converted integer
	 * */
	public int byteToInt(byte data[]) {
		int result = 0;
		result = data[0] * 10 + data[1];
		return result;
	}
	
	
	/**
	 * @desc determine the elevator's status.
	 * @param an array of bytes
	 * @return "up" or "down" or "idle"
	 * */
	public String decodeState(byte data[]) {
		int temp = byteToInt(data);
		if (temp == 1) {
			return "up";
		} else if (temp == 2) {
			return "down";
		} else if (temp == 3) {
			return "idle";
		}
		return "unknown";
	}
	
	
	public static void main(String args[]) {
		Scheduler c = new Scheduler();
		c.waitForEvent();
	}
}
