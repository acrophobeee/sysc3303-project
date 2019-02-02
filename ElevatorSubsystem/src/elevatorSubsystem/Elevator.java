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

	/**
	 * @desc get elevator state in string
	 * @return elevator state in string
	 * */
	public String getstate() {
		return e.toString();
	}
	
	/**
	 * @desc get elevator state
	 * @return elevator state
	 * */
	public Elevatorstate get() {
		return e;
	}

	/**
	 * @desc set elevator state
	 * 
	 * */
	public void setstate(Elevatorstate state) {
		e = state;
	}

	
	/**
	 * @desc change elevator mode
	 *
	 * */
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
	
	/**
	 * @desc add the request into an arraylist
	 *
	 * */
    public void add(int requestfloor) {
       floor.add(requestfloor);	
    }
    
    /**
	 * @desc increase floor number
	 * */
	public void incrasefloor() {
		currentfloor++;
	}

	
	/**
	 * @desc decrease floor number
	 * */
	public void decreasefloor() {
		currentfloor--;
	}

	/**
	 * @desc get current floor number
	 * @return number of current floor
	 * */
	public int getCurrentfloor() {
		return currentfloor;
	}

	
	/**
	 * @desc get elevator number
	 * @return elevator number
	 * */
	public int getElenumber() {
		return elenumber;
	}
}
