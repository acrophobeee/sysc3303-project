
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
		subsystem.statusUpdate(elenumber, currentfloor, state);
	}
	
	/**
	 * The elevator perform the action
	 * 
	 * @param order The order provide by subsystem
	 */
	public void move(int order) {
		if (order == 0) {
			state = new idle();
			subsystem.statusUpdate(elenumber, currentfloor, state);
		} else if (order == 1) {
			state = new Upmode();
			subsystem.statusUpdate(elenumber, currentfloor, state);
			execute(2000);
			currentfloor++;
		} else if (order == 2) {
			state = new Downmode();
			subsystem.statusUpdate(elenumber, currentfloor, state);
			execute(2000);
			currentfloor--;
		} else if (order == 3) {
			state = new DoorOpen();
			subsystem.statusUpdate(elenumber, currentfloor, state);
			execute(5000);
		} else {
			System.out.println("ERROR!!!!!!!!!!");
		}
		
	}
	
	/**
	 * Simulate the elevator execution
	 * 
	 * @param time the time to perform a action
	 */
	public void execute(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Return the elevator state
	 * 
	 * @return 1 if upmode, -1 if downmode, 0 if idle, 2 if door popen
	 */
	public int getState() {
		if (state instanceof Upmode) {
			return 1;
		} else if (state instanceof Downmode) {
			return -1;
		} else if (state instanceof idle) {
			return 0;
		} else if (state instanceof DoorOpen) {
			return 2;
		}
		return -99999;
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

			move(moveOrder);
		}
	}
}
