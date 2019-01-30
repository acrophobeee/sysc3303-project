
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TestFile  {
	public String time;
	public int floor;
	public String floorbutton;
	public int carnumber;
  
	public void getdata() {
	try {	
	File f = new File("file.txt");
    FileReader filereader = new FileReader(f);
    BufferedReader readTxt=new BufferedReader(filereader);
    String seperate="";
    String str="";

    while(( seperate=readTxt.readLine())!=null){
              str+=" "+ seperate;
     }
    String[] s=str.split(" ");
    time = s[1];
    floor = Integer.parseInt(s[2]);
    floorbutton = s[3];
    carnumber =Integer.parseInt(s[4]);
	}
	catch(IOException i) {
		i.printStackTrace();
	}
	}

	
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public String getFloorbutton() {
		System.out.println(floorbutton);
		return floorbutton;
	}

	public void setFloorbutton(String floorbutton) {
		this.floorbutton = floorbutton;
		
	}

	public int getCarnumber() {
		return carnumber;
	}

	public void setCarnumber(int carnumber) {
		this.carnumber = carnumber;
	}

	public static void main(String[] args) {
		TestFile c =new TestFile();
		c.getdata();
		c.getFloorbutton();
		
	}
}
