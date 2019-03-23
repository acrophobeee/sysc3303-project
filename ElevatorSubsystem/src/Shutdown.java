
public class Shutdown implements Elevatorstate{
	//1
		@Override
		public void up() {
			System.out.println();
			
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
			System.out.println("shutdown");
			
		}

		public String toString() {
			return "shutdown";
		}
	}
