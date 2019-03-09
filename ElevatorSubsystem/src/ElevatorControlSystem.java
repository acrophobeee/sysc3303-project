// SimpleEchoServer.java
// This class is the server side of a simple echo server based on
// UDP/IP. The server receives from a client a packet containing a character
// string, then echoes the string back to the client.
// Last edited January 9th, 2016

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;

public class ElevatorControlSystem {

	DatagramPacket sendPacket, receivePacket;
	DatagramSocket sendSocket;
	
	private ElevatorSubsystem E1, E2, E3;
	public Elevator elevator;
	private Date date;
	private Thread receiveS;
	private ArrayList<Integer> listE1, listE2, listE3;
	private int elevatorMode[] = new int[3];// elevatorMode[0] = elevator 1, elevatorMode[1] = elevator 2,
											// elevatorMode[2] = elevator 3,

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
			String d = date.toString();
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
	public Thread getCar(int car) {
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
			System.out.println("Elevator 1 received task from: " + currFloor + " to " + destination);
			if (listE1.isEmpty() || elevatorMode[0]==0) {
				System.out.println("List 1 size "+listE1.size());
				listE1.add(0, currFloor);
				listE1.add(1, destination);
				System.out.println("P1 " +listE1.get(0) + " P2 " +listE1.get(1));
			} else {
				System.out.println(elevatorMode[0]);
				if (elevatorMode[0] == 1) {

					// insert user's current floor to the list
					for (int i = 0; i < listE1.size(); i++) {
						System.out.println(listE1.get(i));
						if (listE1.get(i) > listE1.get(i + 1) || listE1.size() == i) {
							listE1.add(i, currFloor);
							System.out.println("Add floor " + currFloor + " to position " + i + " in E1");
						}
						if (currFloor < listE1.get(i)) {
							listE1.add(i, currFloor);
							System.out.println("Add floor " + currFloor + " to position " + i + " in E1");
							break;
						} else if (currFloor == listE1.get(i)) {
							break;
						}
					}

					// insert user's destination to the list
					for (int i = 0; i < listE1.size(); i++) {
						System.out.println(listE1.get(i));
						if (listE1.get(i) > listE1.get(i + 1) || listE1.size() - 1 == i) {
							listE1.add(i, currFloor);
							System.out.println("Add floor " + currFloor + " to position " + i + " in E1");
						}
						if (destination < listE1.get(i)) {
							listE1.add(i, destination);
							System.out.println("Add floor " + currFloor + " to position " + i + " in E1");
							break;
						} else if (destination == listE1.get(i)) {
							break;
						}
					}
				} else if (elevatorMode[0] == -1) {
					// insert user's current floor to the list
					for (int i = 0; i < listE1.size(); i++) {
						System.out.println(listE1.get(i));
						if (listE1.get(i) < listE1.get(i + 1) || listE1.size() == i) {
							listE1.add(i, destination);
							System.out.println("Add floor " + currFloor + " to position " + i + " in E1");
						}
						if (destination > listE1.get(i)) {
							listE1.add(i + 1, destination);
							System.out.println("Add floor " + currFloor + " to position " + i + " in E1");
							break;
						} else if (destination == listE1.get(i)) {
							break;
						}
					}

					// insert user's destination to the list
					for (int i = 0; i < listE1.size(); i++) {
						if (listE1.get(i) < listE1.get(i + 1) || listE1.size() - 1 == i) {
							listE1.add(i, destination);
						}
						if (destination > listE1.get(i)) {
							listE1.add(i + 1, destination);
							break;
						} else if (destination == listE1.get(i)) {
							break;
						}
					}
				}
			}
		} else if (carNum == 2) {
			System.out.println("Elevator 2 received task from: " + currFloor + " to " + destination);
			if (listE2.isEmpty() || elevatorMode[0]==0) {
				listE2.add(0, currFloor);
				listE2.add(1, destination);
			} else {
				if (elevatorMode[0] == 1) {
					// insert user's current floor to the list
					for (int i = 0; i < listE2.size(); i++) {
						if (listE2.get(i) > listE2.get(i + 1) || listE2.size() == i) {
							listE2.add(i, currFloor);
						}
						if (currFloor < listE2.get(i)) {
							listE2.add(i, currFloor);
							break;
						} else if (currFloor == listE2.get(i)) {
							break;
						}
					}

					// insert user's destination to the list
					for (int i = 0; i < listE2.size(); i++) {
						if (listE2.get(i) > listE2.get(i + 1) || listE2.size() - 1 == i) {
							listE2.add(i, currFloor);
						}
						if (destination < listE2.get(i)) {
							listE2.add(i, destination);
							break;
						} else if (destination == listE2.get(i)) {
							break;
						}
					}
				} else if (elevatorMode[0] == -1) {
					// insert user's current floor to the list
					for (int i = 0; i < listE2.size(); i++) {
						if (listE2.get(i) < listE2.get(i + 1) || listE2.size() == i) {
							listE2.add(i, destination);
						}
						if (destination > listE2.get(i)) {
							listE2.add(i + 1, destination);
							break;
						} else if (destination == listE2.get(i)) {
							break;
						}
					}

					// insert user's destination to the list
					for (int i = 0; i < listE2.size(); i++) {
						if (listE2.get(i) < listE2.get(i + 1) || listE2.size() - 1 == i) {
							listE2.add(i, destination);
						}
						if (destination > listE2.get(i)) {
							listE2.add(i + 1, destination);
							break;
						} else if (destination == listE2.get(i)) {
							break;
						}
					}
				}

			}
		} else if (carNum == 3) {
			System.out.println("Elevator 3 received task from: " + currFloor + " to " + destination);
			if (listE3.isEmpty() || elevatorMode[0]==0) {
				listE3.add(0, currFloor);
				listE3.add(1, destination);
			} else {
				if (elevatorMode[0] == 1) {
					// insert user's current floor to the list
					for (int i = 0; i < listE3.size(); i++) {
						if (listE3.get(i) > listE3.get(i + 1) || listE3.size() == i) {
							listE3.add(i, currFloor);
						}
						if (currFloor < listE3.get(i)) {
							listE3.add(i, currFloor);
							break;
						} else if (currFloor == listE3.get(i)) {
							break;
						}
					}

					// insert user's destination to the list
					for (int i = 0; i < listE3.size(); i++) {
						if (listE3.get(i) > listE3.get(i + 1) || listE3.size() - 1 == i) {
							listE3.add(i, currFloor);
						}
						if (destination < listE3.get(i)) {
							listE3.add(i, destination);
							break;
						} else if (destination == listE3.get(i)) {
							break;
						}
					}
				} else if (elevatorMode[0] == -1) {
					// insert user's current floor to the list
					for (int i = 0; i < listE3.size(); i++) {
						if (listE3.get(i) < listE3.get(i + 1) || listE3.size() == i) {
							listE3.add(i, destination);
						}
						if (destination > listE3.get(i)) {
							listE3.add(i + 1, destination);
							break;
						} else if (destination == listE3.get(i)) {
							break;
						}
					}

					// insert user's destination to the list
					for (int i = 0; i < listE3.size(); i++) {
						if (listE3.get(i) < listE3.get(i + 1) || listE3.size() - 1 == i) {
							listE3.add(i, destination);
						}
						if (destination > listE3.get(i)) {
							listE3.add(i + 1, destination);
							break;
						} else if (destination == listE3.get(i)) {
							break;
						}
					}
				}
				
			}
		}
		for(Integer i: listE1) {
			System.out.print(i + " ");
		}
		System.out.print(" ");
		for(Integer i: listE2) {
			System.out.print(i + " ");
		}
		System.out.print(" ");
		for(Integer i: listE3) {
			System.out.print(i + " ");
		}
		System.out.print(" ");
	}

	public void stopServer() {
		sendSocket.close();
	}

	public static void main(String args[]) throws Exception {
		ElevatorControlSystem c = new ElevatorControlSystem();
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
