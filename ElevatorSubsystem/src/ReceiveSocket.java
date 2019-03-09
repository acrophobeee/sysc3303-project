

import java.io.IOException;
import java.net.*;

public class ReceiveSocket implements Runnable {
	private ElevatorControlSystem system;
	DatagramSocket receiveSocket;
	DatagramPacket receivedPacket;

	public ReceiveSocket(ElevatorControlSystem system) {
		try {
			// Construct a datagram socket and bind it to any available
			// port on the local host machine. This socket will be used to
			// send UDP Datagram packets.
			receiveSocket = new DatagramSocket(69);
			this.system = system;
			// to test socket timeout (2 seconds)
			// receiveSocket.setSoTimeout(2000);
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			byte data[] = new byte[16];
			receivedPacket = new DatagramPacket(data, data.length);
			System.out.println("ReceiveSocket: Waiting for Packet.\n");

			// Block until a datagram packet is received from receiveSocket.
			try {
				System.out.println("Waiting..."); // so we know we're waiting
				receiveSocket.receive(receivedPacket);
			} catch (IOException e) {
				System.out.print("IO Exception: likely:");
				System.out.println("Receive Socket Timed Out.\n" + e);
				e.printStackTrace();
				System.exit(1);
			}

			// Process the received datagram.
			System.out.println("ReceiveSocket: Packet received:");
			System.out.println("From host: " + receivedPacket.getAddress());
			System.out.println("Host port: " + receivedPacket.getPort());
			int len = receivedPacket.getLength();
			System.out.println("Length: " + len);
			StringBuilder temp = new StringBuilder();
			for (byte b : data) {
				temp.append(b);
			}
			System.out.println(temp);
			System.out.println();

			system.put(data);
			// Slow things down (wait 0.5 seconds)
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}

	}
}
