
public class Elevator implements Runnable{
	private int elenumber;
	private int currentfloor;
	private Elevatorstate state;
	private ElevatorSubsystem subsystem;
	
	private long timeOfElevatorMoving;
	private long timeOfDoorOpen;
	private static long TIMECHECKFORMOVE = 5000;
	private static long TIMECHECKFORDOOROPEN = 5500;
	private long operationCount, operationCount1, operationCount2;

	public Elevator(int number, ElevatorSubsystem refSystem) {
		elenumber = number;
		subsystem = refSystem;
		currentfloor = 1;
		state = new idle();
		timeOfElevatorMoving = 2000;
		timeOfDoorOpen = 5000;
		subsystem.statusUpdate(elenumber, currentfloor, state);
		operationCount=0;
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
			TimeChecking tm;
			if(elenumber==3) {
				operationCount++;
				if(operationCount==2) {
					timeOfElevatorMoving=6000;
					 tm= new TimeChecking(timeOfElevatorMoving,"move", this);
				}
				tm = new TimeChecking(TIMECHECKFORMOVE,"move", this);
			}else {
				 tm = new TimeChecking(TIMECHECKFORMOVE,"move", this);
			}
			
			Thread temp = new Thread(tm, "Timer");
			temp.start();
			state = new Upmode();
			subsystem.statusUpdate(elenumber, currentfloor, state);
			execute(timeOfElevatorMoving);
			tm.actionFinish();
			currentfloor++;
		} else if (order == 2) {
			TimeChecking tm;
			if(elenumber==1) {
				operationCount1++;
				if(operationCount1==2) {
					timeOfElevatorMoving=6000;
					 tm= new TimeChecking(timeOfElevatorMoving,"move", this);
				}
				tm = new TimeChecking(TIMECHECKFORMOVE,"move", this);
			}else {
				 tm = new TimeChecking(TIMECHECKFORMOVE,"move", this);
			}
			
			
			Thread temp = new Thread(tm, "Timer");
			temp.start();
			state = new Downmode();
			subsystem.statusUpdate(elenumber, currentfloor, state);
			execute(timeOfElevatorMoving);
			tm.actionFinish();
			currentfloor--;
		} else if (order == 3) {
			TimeChecking tm;
			if(elenumber==2) {
				operationCount2++;
				System.out.println("ele: "+ elenumber+" operation " + operationCount2);
				if(operationCount2==1) {
					timeOfDoorOpen=6000;
					 tm= new TimeChecking(timeOfDoorOpen,"open", this);
				}else {
					 tm = new TimeChecking(TIMECHECKFORDOOROPEN,"open", this);
				}
			}else {
				 tm = new TimeChecking(TIMECHECKFORDOOROPEN,"open", this);
			}
			
			
			Thread temp = new Thread(tm, "Timer");
			temp.start();
			state = new DoorOpen();
			subsystem.statusUpdate(elenumber, currentfloor, state);
			execute(timeOfDoorOpen);
			tm.actionFinish();
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
	
	public void emegencyShutdown() {
		state = new Shutdown();
		subsystem.statusUpdate(elenumber, currentfloor, state);
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
			if (moveOrder == -1) {
				break;
			}
			move(moveOrder);
		}
	}
}
