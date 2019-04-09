import static org.junit.Assert.*;
import org.junit.Test;

public class SchedulerTest {

		@Test
		public void test() {	
	       Scheduler s = new Scheduler();
		   assert(s.elevators.size() == 0);
		   
		   s.elevators.add(new ElevatorStatus(1));
		   assert(s.elevators.size() == 1);
		}

	}

