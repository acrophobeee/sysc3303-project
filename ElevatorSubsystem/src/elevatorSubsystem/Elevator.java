package elevatorSubsystem;

import java.util.*;

public class Elevator implements Runnable{
	private int elenumber;
	private int currentfloor;
	private Elevatorstate state;
	private ElevatorSubsystem subsystem;

	public Elevator(int number, ElevatorSubsystem refSystem) {
		elenumber = number;
		subsystem = refSystem;
		currentfloor = 1;
		state = new idle();
	}
	
	public void move(int order) {
		if (order == 0) {
			state = new idle();
		} else if (order == 1) {
			state = new Upmode();
			currentfloor++;
		} else if (order == -1) {
			state = new Downmode();
			currentfloor--;
		} else {
			System.out.println("ERROR!!!!!!!!!!");
		}
	}
	
	@Override
	public void run() {
		while (true) {
			int moveOrder = 0;
			try {
				moveOrder = subsystem.elevatorAction(elenumber, currentfloor, state);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			move(moveOrder);
		}
	}
}
