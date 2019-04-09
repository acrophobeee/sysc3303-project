import static org.junit.Assert.*;
import org.junit.Test;

	
public class TimeCheckingTest {	   
		
	ElevatorControlSystem g = new ElevatorControlSystem();
	ElevatorSubsystem subsystem = new ElevatorSubsystem(1, g);
	Elevator e = new Elevator(1, subsystem);
	String s = "shj";
	TimeChecking t = new TimeChecking(100, s, e );
		
	@Test
		public void test() {
		
		    t.actionFinish();
			assert (t.isFinished == true);
			
		}

}
