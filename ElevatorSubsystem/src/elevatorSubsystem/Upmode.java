package elevatorSubsystem;

public class Upmode implements Elevatorstate{
//1
	@Override
	public void up() {
		System.out.println("The elevator is moving up");
		
	}

	@Override
	public void down() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rest() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void opendoor() {
		System.out.println("arrive, open door");
		
	}

	public String toString() {
		return "up";
	}
}
