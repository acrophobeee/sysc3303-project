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
	private Byte time[];
	private Thread E1, E2, E3, receiveS;
    private ArrayList<Object> listE1, listE2, listE3;
    private int elevatorMode[] = new int[3];
    
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
			
			listE1 = new ArrayList<Object>();
			listE2 = new ArrayList<Object>();
			listE3 = new ArrayList<Object>();
			elevatorMode[0] = 0; elevatorMode[1] = 0; elevatorMode[2] = 0;
			// to test socket timeout (2 seconds)
			// receiveSocket.setSoTimeout(2000);
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Order the elevator to perform a specific action (i.e. up, down, wait)
	 * 
	 * @param elenumber Elevator number
	 * @param currentfloor Current floor
	 * @param state The elevator state
	 * @return Return 0 for elevator wait, 1 for elevator up, -1 for elevator down
	 */
	public int elevatorAction(int elenumber, int currentfloor, Elevatorstate state) {
		updateElevator(elenumber, currentfloor, state);
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
	 * Sending a updated information to scheduler.
	 * 
	 * @param elevatorNum The elevator number
	 * @param currentFloor Current Floor
	 * @param state The elevator state
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
		System.arraycopy(time, 0, databack, 8, time.length);
		
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
