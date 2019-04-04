import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ElevatorSubsystemTest {
   
	@Before
	public void setUp() throws Exception {
		
	}

	@Test
	public void test() {	
		ElevatorControlSystem e = new ElevatorControlSystem();	
		ElevatorSubsystem esub = new ElevatorSubsystem(1,e);
		Elevator e1 = new Elevator(1, esub);

	   assert(esub.elevator.elenumber == 1);
	}

}
