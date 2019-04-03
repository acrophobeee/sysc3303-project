
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
	private long start, end;
	private ArrayList<Double> timePerform;

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
		start = 0;
		end = 0;
		timePerform = new ArrayList<Double>();
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
			System.out.println("Scheduler: Waiting for Event: ");
//			try {
//				System.out.println("Scheduler address: " + InetAddress.getLocalHost() + "\n");
//			} catch (UnknownHostException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}

			// Block until a datagram packet is received an event.
			try {
				schedulerSocket.receive(receivePacket);
			} catch (IOException e) {
				System.out.print("IO Exception: likely:");
				System.out.println("Receive Socket Timed Out.\n" + e);
				e.printStackTrace();
				System.exit(1);
			}
			
			start = System.nanoTime();
			
			// Decode the received datagram.
			System.out.println("Scheduler: Event received.");
//			System.out.println("From host: " + receivePacket.getAddress());
//			System.out.println("Host port: " + receivePacket.getPort());
			int len = receivePacket.getLength();
			
			// Form a String from the byte array.
			String received = new String(data, 8, len-8);
			StringBuilder temp = new StringBuilder();
			for (byte b : data) {
				temp.append(b);
			}
			System.out.println("Data in byte form: " + temp);
			
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
		
		System.out.println("Scheduler: This event is floor request.");
		
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
			System.arraycopy(request1, 0, requestStore, 0, request1.length);
			System.arraycopy(request2, 0, requestStore, 2, request2.length);
			System.arraycopy(timeByte, 0, requestStore, 4, timeByte.length);
			end = System.nanoTime();
			requests.add(new ElevatorRequest(current, destination, direction, requestStore, end - start));
			return;
		}
		
		for (ElevatorStatus e : elevators) {
			if (e.getNumber() == elevatorNum) {
				String state = "up";
				if (e.getFloor() > current || (e.getFloor() == current && e.getFloor() > destination)) {
					state = "down";
				}
				e.statusUpdate(e.getFloor(), state);
			}
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
		
		end = System.nanoTime();
		System.out.println("Scheduler: Request sent.");
		System.out.println("Request is assigned to elevator " + elevatorNum);
		double timeInSecond = (end - start);
		System.out.println("Performance time: " + timeInSecond/1000000 + " milliseconds.\n");
		timePerform.add(timeInSecond/1000000);
		calculate();
	}
	
	/**
	 * @desc send elevator's information to scheduler.
	 * @param an array of bytes.
	 * @param received time from elevator
	 * */
	public void elevatorUpdate(byte data[], String received) {
		System.out.println("Scheduler: This event is elevator update.");
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
		
//		System.out.println("the elevator number is : " + byteToInt(elevatorNum));
//		System.out.println("the mode is : " + state);
//		System.out.println("the floor is : " + byteToInt(floor));
//		System.out.println("the time is : " + received);
		
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

		System.out.println("Scheduler: Update completed.\n");
	}
	
	/**
	 * Check all available request and sent the new order to elevator
	 * @param e
	 */
	public void checkAllRequest(ElevatorStatus e) {
		if (e.getState() == "idle") {
			int distance = 99999;
			ElevatorRequest temp = null;
			for (ElevatorRequest r : requests) {
				if (distance > Math.abs(e.getFloor() - r.getCurrentFloor())) {
					distance = Math.abs(e.getFloor() - r.getCurrentFloor());
					temp = r;
				}
			}
			
			if (temp == null) {
				return;
			}
			
			// Sending request
			continuteRequest(e.getNumber(), temp.getRequestData(), temp.getPerformanceTime());
			e.statusUpdate(e.getFloor(), temp.getDirection());
			requests.remove(temp);
		}
		ArrayList<ElevatorRequest> tempArray = new ArrayList<ElevatorRequest>();
		for (ElevatorRequest r : requests) {
			if (r.getDirection() == e.getState()) {
				if (e.getState() == "up" && e.getFloor() + 2 < r.getCurrentFloor()) {
					// Sending request
					continuteRequest(e.getNumber(), r.getRequestData(), r.getPerformanceTime());
					e.statusUpdate(e.getFloor(), r.getDirection());
					tempArray.add(r);
				} else if (e.getState() == "down" && e.getFloor() - 2 > r.getCurrentFloor())  {
					// Sending request
					continuteRequest(e.getNumber(), r.getRequestData(), r.getPerformanceTime());
					e.statusUpdate(e.getFloor(), r.getDirection());
					tempArray.add(r);
				}
			}
		}
		for (ElevatorRequest r : tempArray) {
			requests.remove(r);
		}
	}
	
	/**
	 * Sending request from request list to the elevator
	 * @param elevatorNum the number of elevator to be sent the request
	 * @param the request data from client and send to elevator
	 */
	public void continuteRequest(int elevatorNum, byte data[], long performanceTime) {
		start = System.nanoTime();
		byte request[] = new byte[18];
		
		byte num[] = new byte[2];
		num[0] = (byte) (elevatorNum / 10);
		num[1] = (byte) (elevatorNum % 10);
		
		System.arraycopy(num, 0, request, 0, num.length);
		System.arraycopy(data, 0, request, 2, data.length);
		
		try {
			sendPacket = new DatagramPacket(request, request.length, InetAddress.getLocalHost(), 69);
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
		
		end = System.nanoTime();
		System.out.println("Scheduler: Request sent.");
		System.out.println("Request is assigned to elevator " + elevatorNum);
		double timeInSecond = (end - start + performanceTime);
		System.out.println("Performance time: " + timeInSecond/1000000 + " milliseconds.\n");
		timePerform.add(timeInSecond/1000000);
		calculate();
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
		} else if (temp == 4) {
			return "door open";
		}
		return "shutdown";
	}
	
	/**
	 * Print the mean time and variance of schduler requesting time
	 */
	public void calculate() {
		double total = 0;
		for (double i : timePerform) {
			total += i;
		}
		double mean = total/timePerform.size();
		System.out.println("Total mean time: " + mean + " milliseconds.");
		
		if (timePerform.size() > 1) {
			double temp = 0;
			
			for (double i : timePerform) {
				temp += Math.pow((i - mean), 2);
			}
			System.out.println("Variance: " + temp/(timePerform.size()-1));
		}
		System.out.println();
	}
	
	public static void main(String args[]) {
		Scheduler c = new Scheduler();
		c.waitForEvent();
	}
}
