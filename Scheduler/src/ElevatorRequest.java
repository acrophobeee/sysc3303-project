
public class ElevatorRequest {
	private int currect; // the user currect floor
	private int destination; // the destination floor
	private String direction; 
	private byte data[];
	
	/**
	 * 
	 * @param currect
	 * @param destination
	 * @param direction
	 */
	public ElevatorRequest(int currect, int destination, String direction, byte data[]) {
		this.currect = currect;
		this.destination = destination;
		this.direction = direction;
		this.data = data;
	}
	
	/**
	 * Return user current floor
	 * 
	 * @return user current floor
	 */
	public int getCurrentFloor() {
		return currect;
	}
	
	/**
	 * Return destination floor
	 * 
	 * @return destination floor
	 */
	public int getDestination() {
		return destination;
	}
	
	/**
	 * Return user direction
	 * 
	 * @return direction
	 */
	public String getDirection() {
		return direction;
	}
	
	/**
	 * Return the request data for fast socket sending
	 * 
	 * @return the request data
	 */
	public byte[] getRequestData() {
		return data;
	}
}
