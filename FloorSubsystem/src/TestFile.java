
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TestFile {
	public String time;
	public int floor;
	public String floorbutton;
	public int carnumber;
	/**
	 * @desc read data from a file named file.txt
	 */
	public void getdata() {
		try {
			File f = new File("file.txt");
			FileReader filereader = new FileReader(f);
			BufferedReader readTxt = new BufferedReader(filereader);
			String seperate = "";
			String str = "";

			while ((seperate = readTxt.readLine()) != null) {
				str += " " + seperate;
			}
			String[] s = str.split(" ");
			time = s[1];
			floor = Integer.parseInt(s[2]);
			floorbutton = s[3];
			carnumber = Integer.parseInt(s[4]);
		} catch (IOException i) {
			i.printStackTrace();
		}
	}
	/**
	 * @desc get the time of passenger arrival 
	 * @return the time
	 */
	public String getTime() {
		return time;
	}
	/**
	 * @desc set the time of passenger arrival
	 * @param the arrive time
	 */
	public void setTime(String time) {
		this.time = time;
	}
	
	/**
	 * @desc get the current floor of the passenger
	 * @return the current floor
	 */
	public int getFloor() {
		return floor;
	}

	/**
	 * @desc set the number of floor of the passenger
	 * @param the number of floor
	 */
	public void setFloor(int floor) {
		this.floor = floor;
	}
	/**
	 * @desc get the direction button of the passenger pressed
	 * @return the floor button 
	 */
	public String getFloorbutton() {
		System.out.println(floorbutton);
		return floorbutton;
	}

	/**
	 * @desc set the direction of the passenger pressed
	 * @param the floor button
	 */
	public void setFloorbutton(String floorbutton) {
		this.floorbutton = floorbutton;

	}

	/**
	 * @desc get the floor number that the passenger wants to go 
	 * @return the number of floor 
	 */
	public int getCarnumber() {
		return carnumber;
	}
	
	/**
	 * @desc set the floor number that the passenger wants to go 
	 * @param the number of floor
	 */
	public void setCarnumber(int carnumber) {
		this.carnumber = carnumber;
	}

	/**
	 * main
	 */
	public static void main(String[] args) {
		TestFile c = new TestFile();
		c.getdata();
		c.getFloorbutton();

	}
}
