import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ElevatorStatusTest {
	ElevatorStatus a;
	
	@Before
	public void setup () {
		a = new ElevatorStatus(1, 666);
	}
	
	@Test
	public void testInitial() {
		assertEquals(a.getNumber(), 1);
		assertEquals(a.getPort(), 666);
		assertEquals(a.getFloor(), 1);
		assertEquals(a.getState(), "idle");
	}

	@Test
	public void testUpdate() {
		a.statusUpdate(8, "up");
		assertEquals(a.getNumber(), 1);
		assertEquals(a.getPort(), 666);
		assertEquals(a.getFloor(), 8);
		assertEquals(a.getState(), "up");
	}
}
