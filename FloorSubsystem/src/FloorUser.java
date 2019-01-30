import java.util.Date;

public class FloorUser {
	private Date date;
	private int toFloor = 0;
	private FloorSubsystem FS;
	private String up, down;
	
	public FloorUser() {
		FS = new FloorSubsystem();
		date = new Date();
		up = "up ";
		down = "down ";		
	}
	
	/*
	 * @desc send request to the FloorSubsystem
	 * */
	public  void sendRequest() {
		FS.sendSocket(date, 1, up);
		FS.sendSocket(date, 1, up);
		
	}
	
	/*
	 * @desc this method will update the information of the request elevator 
	 * */
	public void updateFloorInfo() {
		FS.receiveSocket();	
	}
	
	public static void main(String args[]) {
		FloorUser FU = new FloorUser();
		FU.sendRequest();
	}
}
