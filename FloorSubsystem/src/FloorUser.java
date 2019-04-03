import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class FloorUser implements ActionListener {
	private Date date;
	private int toFloor = 0;
	private FloorSubsystem FS;
	private String up, down;
	
	private DatagramSocket sendSocket;
	private DatagramPacket sendPacket;
	
	
	/**
	 * @desc construct a floor user
	 * */
	public FloorUser() {
//		FS = ;
		Thread t = new Thread(new FloorSubsystem(), "FloorSubsystem");
		
		
		up = "up ";
		down = "down ";		
		try {
			sendSocket = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		t.start();
		sendRequest();		
	}
	
	
	/**
	 * @desc send request to the FloorSubsystem
	 * */
	public  void sendRequest() {
		date = new Date();
		String strDateFormat = "HH:mm:ss.mmm";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate= dateFormat.format(date);
        
		sendSocket(formattedDate, 2, up, 5);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sendSocket(formattedDate, 9, down, 5);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sendSocket(formattedDate, 4, up, 10);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sendSocket(formattedDate, 5, up, 9);		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sendSocket(formattedDate, 9, down, 5);		
	}
	
	/**
	 * @desc send packet to scheduler
	 * @param date request time
	 * @param floor the floor user wants to go
	 * @param Button up/down button
	 * @param curr user's current floor
	 * */
	public void sendSocket(String date, int curr, String Button, int destination) {
		System.out.println("Client: sending a request with \npresent time: " + date + "\nmode: " + Button);
		
		byte But[] = Button.getBytes();
		String floors = String.valueOf(destination);
		byte floorByte[] = floors.getBytes();
        String d = date.toString();
        byte dat[] = d.getBytes();
		byte request[] = new byte[20];
		
		// handle request info
		// up or down request
		request[0] = 0;
		request[1] = 0;
		if(Button.equals("up")) {
			request[2]=0;
			request[3]=0;
		}else {
			request[2]=0;
			request[3]=1;
		}
		// user's current floor
		if(curr<10) {
			request[4]=0;
			request[5]=(byte) curr;
		}else {
			request[4]=(byte) (curr/10);
			request[5]=(byte) (curr%10);
		}
		// destination floor info
		if(destination<10) {
			request[6]=0;
			request[7]=(byte) destination;
		}else {
			request[6]=(byte) (destination/10);
			request[7]=(byte) (destination%10);
		}
		
		System.arraycopy(dat, 0, request, 8, dat.length);
		
		StringBuilder requestString = new StringBuilder();
		for (byte b : request) {
			requestString.append(b);
		}

		System.out.println("request: "+requestString);

		try {
//			InetAddress addr = InetAddress.getByName("134.117.59.70");
			sendPacket = new DatagramPacket(request, request.length, InetAddress.getLocalHost() , 3000);
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
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Client: Packet sent 1.\n");		
	}
	
	/**
	 * @desc this method will update the information of the request elevator 
	 * */
	public void updateFloorInfo() {
		FS.receiveSocket();	
	}
	
	/**
	 * @desc main
	 * */
	public static void main(String args[]) {
		FloorUser FU = new FloorUser();
//		FU.sendRequest();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
