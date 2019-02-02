package elevatorSubsystem;

import java.util.ArrayList;

public class Elevator {
	private int elenumber;
	private int currentfloor = 1;
	private Elevatorstate idle = new idle();
	private Elevatorstate e;
	private ArrayList<Integer> floor = new ArrayList<>();

	public Elevator() {
		this.e = idle;
		this.elenumber = 1;
	}

	public String getstate() {
		return e.toString();
	}

	public Elevatorstate get() {
		return e;
	}

	public void setstate(Elevatorstate state) {
		e = state;
	}

	public void add(int requestfloor) {
		floor.add(requestfloor);
	}
	
	public void changemode(int requestfloor) {
		if (requestfloor > currentfloor) {
			setstate(new Upmode());
		}
		else if (requestfloor < currentfloor) {
			setstate(new Upmode());
		} 
		else if (requestfloor == currentfloor){
			setstate(new idle());
		}
	}

	public void incrasefloor() {
		currentfloor++;
	}

	public void decreasefloor() {
		currentfloor--;
	}

	public int getCurrentfloor() {
		return currentfloor;
	}

	public int getElenumber() {
		return elenumber;
	}
}
