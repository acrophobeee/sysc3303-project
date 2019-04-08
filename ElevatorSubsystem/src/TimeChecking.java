
public class TimeChecking extends Thread {

	private long elapsedTime;
	private String type;
	private Elevator e;
	public boolean isFinished;

	public TimeChecking(long i, String type, Elevator e) {
		elapsedTime = i;
		this.type = type;
		this.e = e;
		isFinished = false;
	}
	
	/**
	 * Elevator is performed currectly
	 */
	public void actionFinish() {
		isFinished = true;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		try {
			Thread.sleep(elapsedTime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (!isFinished) {
			if (type == "move") {
				e.emegencyShutdown();
				System.out.println("Elevator is shutdown");
			} else if (type == "open") {
				e.doorIsBlock();
				System.out.println("Please do not block the door.");
			}
		} 
	}
}
