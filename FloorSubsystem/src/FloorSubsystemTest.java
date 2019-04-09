import static org.junit.Assert.*;
import org.junit.Test;


public class FloorSubsystemTest {
	
	
	public void test() {
		
	   FloorSubsystem fs =new FloorSubsystem();
	   fs.mainFrame();
	   
	   assert (fs.frame.getHeight() == 800);
		
	}
}
