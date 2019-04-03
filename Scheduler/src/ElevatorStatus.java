/**
 * This class is used to store the status of the elevators.
 *
 */
public class ElevatorStatus {
	private int number; // the no. of elevator
	private int floor; // the elevator's current position
	private String state; // there are three states: idle, up, or down
	
	/**
	 * Construct a new elevator record
	 * 
	 * @param number The no. of elevator
	 * @param port The port of elevator
	 */
	public ElevatorStatus (int number) {
		this.number = number;
		floor = 1;
		state = "idle";
	}
	
	/**
	 * Return the no. of elevator
	 * 
	 * @return the no. of elevator
	 */
	public int getNumber() {
		return number;
	}
	
	/**
	 * Set the elevator N.O.
	 * 
	 * @param number the elevator N.O.
	 */
	public void setNumber(int number) {
		this.number = number;
	}
	
	/**
	 * Return the elevator position (which floor)
	 * 
	 * @return the elevator position (which floor)
	 */
	public int getFloor() {
		return floor;
	}
	
	/**
	 * Update the elevator position (which floor)
	 * 
	 * @param floor the elevator position (which floor)
	 */
	public void setFloor(int floor) {
		this.floor = floor;
	}
	
	/**
	 * Return the state of the elevator
	 * 
	 * @return the state of the elevator
	 */
	public String getState() {
		return state;
	}
	
	/**
	 * Update the elevator state
	 * 
	 * @param state the new elevator state
	 */
	public void setState(String state) {
		if (state.equals("idle") || state.equals("up") || state.equals("down") || state.equals("shut down")) {
			this.state = state;
		}
	}
	
	public void statusUpdate(int floor, String state) {
		setFloor(floor);
		setState(state);
	}
}
