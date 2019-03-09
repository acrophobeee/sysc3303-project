
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;

public class ElevatorSubsystem {
	private ElevatorControlSystem controlSystem;
	private Thread elevator;
	private Date date;
	private ArrayList<Integer> order;
	private int elevatorMode;
	
	public ElevatorSubsystem(int i, ElevatorControlSystem controlSystem) {
		
		this.controlSystem = controlSystem;
		elevator = new Thread(new Elevator(i, this), "Elevator " + i);
		date = new Date();
		String d = date.toString();
		order = new ArrayList<Integer>();
		elevator.start();
	}
	
	/**
	 * Order the elevator to perform a specific action (i.e. up, down, wait)
	 * 
	 * @param elenumber    Elevator number
	 * @param currentfloor Current floor
	 * @param state        The elevator state
	 * @return Return 0 for elevator wait, 1 for elevator up, -1 for elevator down
	 */
	public int elevatorAction(int elenumber, int currentfloor, Elevatorstate state) {
		controlSystem.updateElevator(elenumber, currentfloor, state);
		int destination = 0;
		while (order.isEmpty()) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		destination = order.get(0);
		if (currentfloor == destination) {
			order.remove(0);
			return 0;
		}
		
		if (currentfloor > destination) {
			return -1;
		}
		
		return 1;
	}
}
