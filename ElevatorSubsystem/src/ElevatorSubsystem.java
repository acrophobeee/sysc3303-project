
import java.util.ArrayList;
import java.util.Date;

public class ElevatorSubsystem {
	private ElevatorControlSystem controlSystem;
	private Elevator elevator;
	private Date date;
	private ArrayList<Integer> order;
	private int elevatorMode;
	private int eleNum;
	
	public ElevatorSubsystem(int i, ElevatorControlSystem controlSystem) {
		
		eleNum=i;
		this.controlSystem = controlSystem;
		elevator = new Elevator(i, this);
		Thread t = new Thread(elevator, "Elevator" + i);
		date = new Date();
		String d = date.toString();
		order = new ArrayList<Integer>();
		t.start();
	}
	
	public void statusUpdate(int elenumber, int currentfloor, Elevatorstate state) {
		controlSystem.updateElevator(elenumber, currentfloor, state);
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
		int destination = 0;
		while (order.isEmpty()) {
			if (elevator.getState() == 0) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				return 0;
			}
		}
		destination = order.get(0);
		if (currentfloor == destination) {
			order.remove(0);
			return 3;
		}
		
		if (currentfloor < destination) {
			return 1;
		}
		
		return 2;
	}
	
	/**
	 * @desc put new client request into order list 
	 * */
	public void putNewRequest(int currFloor, int destination) {
		System.out.println("Elevator "+eleNum+" received task from: " + currFloor + " to " + destination);
		elevatorMode = elevator.getState();
		if (order.isEmpty() || elevatorMode==0) {
			order.add(0, currFloor);
			order.add(1, destination);
		} else {
			System.out.println(elevatorMode);
			if (elevatorMode == 1) {

				// insert user's current floor to the list
				for (int i = 0; i < order.size(); i++) {
					System.out.println(order.get(i));
					if (order.get(i) > order.get(i + 1) || order.size() == i) {
						order.add(i, currFloor);						
					}
					if (currFloor < order.get(i)) {
						order.add(i, currFloor);
						break;
					} else if (currFloor == order.get(i)) {
						break;
					}
				}

				// insert user's destination to the list
				for (int i = 0; i < order.size(); i++) {
					System.out.println(order.get(i));
					if (order.get(i) > order.get(i + 1) || order.size() - 1 == i) {
						order.add(i, currFloor);
					}
					if (destination < order.get(i)) {
						order.add(i, destination);
						break;
					} else if (destination == order.get(i)) {
						break;
					}
				}
			} else if (elevatorMode == -1) {
				// insert user's current floor to the list
				for (int i = 0; i < order.size(); i++) {
					System.out.println(order.get(i));
					if (order.get(i) < order.get(i + 1) || order.size() == i) {
						order.add(i, destination);
					}
					if (destination > order.get(i)) {
						order.add(i + 1, destination);
						break;
					} else if (destination == order.get(i)) {
						break;
					}
				}

				// insert user's destination to the list
				for (int i = 0; i < order.size(); i++) {
					if (order.get(i) < order.get(i + 1) || order.size() - 1 == i) {
						order.add(i, destination);
					}
					if (destination > order.get(i)) {
						order.add(i + 1, destination);
						break;
					} else if (destination == order.get(i)) {
						break;
					}
				}
			}
		}
		
	}
}
