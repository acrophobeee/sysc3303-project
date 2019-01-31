/**
 * This class is used to store the status of the elevators.
 * 
 * @author Xinrui Li 
 *
 */
public class ElectvatorStatus {
	private int number; // the no. of elevator
	private int port; // the port of elevator
	private int floor; // the elevator position
	private String state; // there are three states: idle, up, or down
	
	/**
	 * Construct a new elevator record
	 * 
	 * @param number The no. of elevator
	 * @param port The port of elevator
	 */
	public ElectvatorStatus (int number, int port) {
		this.number = number;
		this.port = port;
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
	 * Return the port of elevator
	 * 
	 * @return the port of elevator
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * Set the elevator port N.O.
	 * 
	 * @param port the elevator port N.O.
	 */
	public void setPort(int port) {
		this.port = port;
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
		if (state.equals("idle") || state.equals("up") || state.equals("down")) {
			this.state = state;
		}
	}
}
