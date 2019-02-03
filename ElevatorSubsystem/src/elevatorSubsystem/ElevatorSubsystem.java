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
import java.util.Date;

public class ElevatorSubsystem {

	DatagramPacket sendPacket, receivePacket;
	DatagramSocket sendSocket, receiveSocket;
	
    public Elevator elevator;
    private Date date;
	public ElevatorSubsystem() {
		try {
			// Construct a datagram socket and bind it to any available
			// port on the local host machine. This socket will be used to
			// send UDP Datagram packets.
			sendSocket = new DatagramSocket();

			// Construct a datagram socket and bind it to port 5000
			// on the local host machine. This socket will be used to
			// receive UDP Datagram packets.
			receiveSocket = new DatagramSocket(69);
			elevator = new Elevator();
			// to test socket timeout (2 seconds)
			// receiveSocket.setSoTimeout(2000);
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}

	public void receive() throws Exception {
		while (true) {
			byte data[] = new byte[16];
			receivePacket = new DatagramPacket(data, data.length);
			System.out.println("Server: Waiting for Packet.\n");

			// Block until a datagram packet is received from receiveSocket.
			try {
				System.out.println("Waiting..."); // so we know we're waiting
				receiveSocket.receive(receivePacket);
			} catch (IOException e) {
				System.out.print("IO Exception: likely:");
				System.out.println("Receive Socket Timed Out.\n" + e);
				e.printStackTrace();
				System.exit(1);
			}

			// Process the received datagram.
			System.out.println("Server: Packet received:");
			System.out.println("From host: " + receivePacket.getAddress());
			System.out.println("Host port: " + receivePacket.getPort());
			int len = receivePacket.getLength();
			System.out.println("Length: " + len);
			System.out.print("Containing: ");
			
			String received = new String(data, 4, len-4);
			System.out.println(received);
			StringBuilder temp = new StringBuilder();
			for (byte b : data) {
				temp.append(b);
			}
			
			String hour, mins, second;			
			String[] splittedDate = received.split(":");			
			String[] splittedSecond = splittedDate[2].split("\\.");
			
			hour = splittedDate[0];
			mins = splittedDate[1];
			second = splittedSecond[0];
			
			System.out.println("hour: " + hour + " mins: " + mins + " second: " + second);

			// Slow things down (wait 0.5 seconds)
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}

			int a = data[0] * 10 + data[1];
			int b = data[2] * 10 + data[3];

			elevator.add(a);
			elevator.add(b);
			
			int resH, resM, resS;
			resH = Integer.parseInt(hour);
			resM = Integer.parseInt(mins);
			resS = Integer.parseInt(second);

			while (!elevator.commandClear()) {
				elevator.changemode();

				System.out.println(elevator.getstate());

				int floor = elevator.getCurrentfloor();
				byte databack[] = new byte[50];
				
				resS += 2;
				if(resS/60!=0) {
					resS = resS%60;
					resM += 1;
					if(resM/60!=0) {
						resM= resM%60;
						resH += 1;
						if(resH==25) {
							resH=00;
						}
					}
				}
				
				date = new Date();
				String strDateFormat = "HH:mm:ss.mmm";
				DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
				String formattedDate = resH+":"+resM+":"+resS+".000";
//				String formattedDate = dateFormat.format(date);
				byte time[] = formattedDate.getBytes();		

				byte ele[] = new byte[2];
				ele[0] = 0;
				ele[1] = 1;

				byte mode[] = new byte[2];
				if (elevator.getstate() == "up") {
					mode[0] = 0;
					mode[1] = 1;
				} else if (elevator.getstate() == "down") {
					mode[0] = 0;
					mode[1] = 2;
				} else if (elevator.getstate() == "idle") {
					mode[0] = 0;
					mode[1] = 3;
				}

				byte[] f = new byte[2];
				f[0] = (byte) (elevator.getCurrentfloor() / 10);
				f[1] = (byte) (elevator.getCurrentfloor() % 10);

				byte direction[] = new byte[2];
				direction[0] = 0;
				direction[1] = 1;

				System.arraycopy(direction, 0, databack, 0, 2);
				System.arraycopy(ele, 0, databack, 2, 2);
				System.arraycopy(mode, 0, databack, 4, 2);
				System.arraycopy(f, 0, databack, 6, f.length);
				System.arraycopy(time, 0, databack, 8, time.length);

				

				// ----------------------------------------
				sendPacket = new DatagramPacket(databack, databack.length, receivePacket.getAddress(),
						receivePacket.getPort());

				System.out.println("Server: Sending packet:");
				System.out.println("To host: " + sendPacket.getAddress());
				System.out.println("Destination host port: " + sendPacket.getPort());

				System.out.println("Containing: ");
				
				StringBuilder temp2 = new StringBuilder();
				for (byte bb : sendPacket.getData()) {
					temp2.append(bb);
				}
				System.out.println(temp2 + "\n");
				// or (as we should be sending back the same thing)
				// System.out.println(received);

				// Send the datagram packet to the client via the send socket.
				try {
					sendSocket.send(sendPacket);
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(1);
				}

				System.out.println("Server: packet sent");
				
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					e.printStackTrace();
					System.exit(1);
				}
			}
		}

	}

	public void stopServer() {
		sendSocket.close();
		receiveSocket.close();
	}

	public static void main(String args[]) throws Exception {
		ElevatorSubsystem c = new ElevatorSubsystem();
		while (true) {
			try {
				c.receive();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
