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

	public void changemode() {
		if (floor.get(0)!=currentfloor) {
		    if (floor.get(0) > currentfloor) {
			setstate(new Upmode());
			incrasefloor();
		}
		    else if (floor.get(0) < currentfloor) {
			setstate(new Downmode());
			decreasefloor();
		}   
		    else{
				setstate(new idle());}
	}
		else {  floor.remove(0);
				if (floor.get(0) > currentfloor) {
					setstate(new Upmode());
					incrasefloor();
				}
				else if (floor.get(0) < currentfloor) {
					setstate(new Downmode());
					decreasefloor();	
				}
			    else{
					setstate(new idle());}
			    }
     	
}	
    public void add(int requestfloor) {
       floor.add(requestfloor);	
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
