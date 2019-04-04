
import static org.junit.Assert.*;
import org.junit.Test;

public class ElevatorTest {
	@Test
	public void test() {
		ElevatorControlSystem g = new ElevatorControlSystem();
		ElevatorSubsystem subsystem = new ElevatorSubsystem(1, g);
		Elevator e = new Elevator(1, subsystem);
		assert (e.state.toString() == "idle");
	}

}
