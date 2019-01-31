package elevatorSubsystem;

public class Elevator {
	
	private int currentfloor;
	private Elevatorstate up = new Upmode();
	private Elevatorstate down = new Downmode();
	private Elevatorstate idle = new idle();
	private Elevatorstate e;
	
	public Elevator(int f) {
		this.currentfloor =f;
		this.e = idle;
	}
	
	public Elevatorstate getE() {
		return e;
	}

	public void setstate(Elevatorstate state) {
		this.e = state;
	}
	
	public void changemode (int requestfloor) {
		if(requestfloor > currentfloor) {
		 currentfloor++;
		 e=up;	
		}
		if(requestfloor < currentfloor) {
		 e=down;
		 currentfloor--;
		}
		else {
		 e=idle;
		}
	}
    
}
