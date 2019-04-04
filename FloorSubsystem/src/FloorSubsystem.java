
// SimpleEchoClient.java
// This class is the client side for a simple echo server based on
// UDP/IP. The client sends a character string to the echo server, then waits 
// for the server to send it back to the client.
// Last edited January 9th, 2016

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class FloorSubsystem implements Runnable {

	private DatagramPacket sendPacket, receivePacket;
	private DatagramSocket sendSocket, receiveSocket;
	private Date reqDate;
	
	private static GraphicsConfiguration gc;
	private JFrame frame;
	private JPanel panel;
	private JButton[][] buttons;
	
	/**
	 * @desc receive an socket from scheduler, 
	 * socket will contain the info that which elevator will come. 
	 * and which floor the elevator are
	 * */
	public void receiveSocket() {
		// Construct a DatagramPacket for receiving packets up
		// to 100 bytes long (the length of the byte array).
		
		byte data[] = new byte[100];
		receivePacket = new DatagramPacket(data, data.length);
		
		try {
			receiveSocket.receive(receivePacket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Process the received datagram.
		System.out.println("Floor: Packet received:");
		int elevatorNumber = data[3];
		int currentFloor = data[6]*10+data[7];
		int elevatorMode = data[5];
		System.out.println("Elevator number: " + elevatorNumber);
		
		if(data[5]==1) {
			System.out.println("Elevator mode: up");
		}else if(data[5]==2) {
			System.out.println("Elevator mode: down");
		}else if(data[5]==3) {
			System.out.println("Elevator mode: idle");
		}else if(data[5]==4) {
			System.out.println("Elevator mode: door open");
		}
		
		updateDisplay(elevatorNumber, currentFloor, elevatorMode);
		
		System.out.println("Elevator current floor: " + currentFloor);

		// Form a String from the byte array.
		System.out.println("Receiving finished. \n");
	}
	/**
	 * @desc terminate the client socket
	 * */
	public void stopClient() {
		sendSocket.close();
	}
	
	/**
	 * @desc create a new frame
	 * */
	public void mainFrame() {
		frame = new JFrame(gc);	
		frame.setTitle("Elevator Virtural Display System");
		frame.setResizable(true);
		frame.setLocation(700, 200);
		frame.setSize(500, 800);
		
		panel=new JPanel();
		panel.setBackground(Color.LIGHT_GRAY);  
		addDisplayBox();
		frame.add(panel);
		frame.setVisible(true);
	}
	
	/**
	 * @desc add button into the 
	 * */
	public void addDisplayBox() {
		panel = new JPanel();
		panel.setLayout(new GridLayout(23, 4));
		frame.add(panel, BorderLayout.CENTER);
		buttons = new JButton[23][4];
		for (int i = 22; i >=0; i--) {
			for (int j = 0; j < 4; ++j) {
				buttons[i][j] = new JButton();
				panel.add(buttons[i][j]);
				if (i > 0) {
					buttons[i][j].setText(i+"");;
				}
				buttons[i][j].setEnabled(false);
			}
		}
		buttons[0][0].setText("Elevator 1");
		buttons[0][1].setText("Elevator 2");
		buttons[0][2].setText("Elevator 3");
		buttons[0][3].setText("Elevator 4");
	}
	
	/**
	 * @desc found the suitable color to display elevator's status
	 * @param status: elevator's current status
	 * @param status: 1 or 2 - running (Green)
	 * 		  		  3 - idle (YELLOW)
	 * 				  4 - door open (BLUE)
	 * 		  		  5 - Stuck door (ORANGE)
	 * 		 		  6 - Stuck elevator (RED)
	 * */
	public Color findColor(int status) {
		if(status==1 ||status==2) {
			return Color.GREEN;
		}else if(status==3) {
			return Color.YELLOW;
		}else if(status==4) {
			return new Color(0, 255, 255);
		}else if(status==5) {
			return Color.ORANGE;
		}else if(status==6) {
			return Color.RED;
		}else {
			return Color.WHITE;
		}
	}
	
	/**
	 * @desc update elevator's display block
	 * @param elevatorNumver elevator number to be updated
	 * @param current floor to be updated of the elevator
	 * @param status: 1 or 2 - running
	 * 		  		  3 - idle
	 * 				  4 - door open
	 * 		  		  5 - Stuck door
	 * 		 		  6 - Stuck elevator
	 * 				  
	 * */
	public void updateDisplay(int elevatorNumber, int currentFloor, int status) {
		elevatorNumber--;
		Color suitableColor = findColor(status);
		for(int i = 22; i >0; --i) {
			for(int j = 0; j < 4; ++j) {
				if(elevatorNumber==j && currentFloor==i) {
					buttons[i][j].setBackground(suitableColor);
				}else if(j==elevatorNumber) {
					buttons[i][j].setBackground(null);
				}				
			}
		}		
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			// Construct a datagram socket and bind it to any available
			// port on the local host machine. This socket will be used to
			// send and receive UDP Datagram packets.
			
			receiveSocket = new DatagramSocket(23);//receive port is 23
			mainFrame();
			System.out.println("Floor socket is running on port 23 and wait for packet: ");
			while(true) {
				receiveSocket();
			}
			
		} catch (SocketException se) { // Can't create the socket.
			se.printStackTrace();
			System.exit(1);
		}
		
	}

}
