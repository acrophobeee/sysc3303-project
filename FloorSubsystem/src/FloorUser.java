import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FloorUser {
	private Date date;
	private int toFloor = 0;
	private FloorSubsystem FS;
	private String up, down;
	
	private DatagramSocket sendSocket;
	private DatagramPacket sendPacket;
	
	public FloorUser() {
		FS = new FloorSubsystem();
		
		up = "up ";
		down = "down ";		
		try {
			sendSocket = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * @desc send request to the FloorSubsystem
	 * */
	public  void sendRequest() {
		date = new Date();
		String strDateFormat = "HH:mm:ss.mmm";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate= dateFormat.format(date);
        
		sendSocket(formattedDate, 12, up, 1);		
	}
	
	public void sendSocket(String date, int floor, String Button, int curr) {
		System.out.println("Client: sending a request with \npresent time: " + date + "\nmode: " + Button);
		
		byte But[] = Button.getBytes();
		String floors = String.valueOf(floor);
		byte floorByte[] = floors.getBytes();
        String d = date.toString();
        byte dat[] = d.getBytes();
		byte request[] = new byte[18];
		
		// handle request info
		// up or down request
		if(Button.equals("up")) {
			request[0]=0;
			request[1]=0;
		}else {
			request[0]=0;
			request[1]=1;
		}
		// floor info
		if(floor<10) {
			request[2]=0;
			request[3]=(byte) floor;
		}else {
			request[2]=(byte) (floor/10);
			request[3]=(byte) (floor%10);
		}
		// cart info
		if(curr<10) {
			request[4]=0;
			request[5]=(byte) curr;
		}else {
			request[4]=(byte) (curr/10);
			request[5]=(byte) (curr%10);
		}
//		System.arraycopy(But, 0, request, 0, But.length);
//		System.arraycopy(floorByte, 0, request, But.length, floorByte.length);
		System.arraycopy(dat, 0, request, 6, dat.length);
		
		StringBuilder requestString = new StringBuilder();
		for (byte b : request) {
			requestString.append(b);
		}
		System.out.println("request: "+requestString);

		try {
//			InetAddress addr = InetAddress.getByName("25.14.52.204");
			sendPacket = new DatagramPacket(request, request.length, InetAddress.getLocalHost(), 3000);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(1);
		}

		// Send the datagram packet to the server via the send/receive socket.

		try {
			sendSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("Client: Packet sent.\n");

		// sendReceiveSocket.close();
	}
	
	/*
	 * @desc this method will update the information of the request elevator 
	 * */
	public void updateFloorInfo() {
		FS.receiveSocket();	
	}
	
	/*
	 * */
	public static void main(String args[]) {
		FloorUser FU = new FloorUser();
		FU.sendRequest();
	}
}
