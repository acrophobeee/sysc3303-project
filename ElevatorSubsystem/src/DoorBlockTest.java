import static org.junit.Assert.*;
import org.junit.Test;

import jdk.nashorn.internal.ir.Block;
	
public class DoorBlockTest {
	   
		@Test
		public void test() {
			DoorBlock block = new DoorBlock();
			assert (block.toString() == "doorblock");
		}

}
