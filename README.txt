Group Number: #8
Group Members:
Pengliang Zhang   101014341
Xinrui Li                  101018938
Xinyu Chen            101031031
Tongdan Zhu         101057752
Lixuan Luo              101019254

Running Instructions:
1. First run ElevatorSubsystem.java
//This class is the server side of a simple echo server based on UDP/IP.
// The server receives from a client a packet containing a character
// string, then echoes the string back to the client.

2. Then run Scheduler.java
//This class is a intermidiate host based on UDP/IP.

3. Next run FloorSubsystem.java
//This class is the client side of a simple echo server based on UDP/IP.
//The client receives from a server a package containing elevator state and date.

4. Finally, run FloorUser.java
//This class is the client side of a simple echo server based on UDP/IP.
//The client sents a package to server containing a requeat and date.

Files included:
1)ElevatorSubsystem.java
2)Elevator.java
3)ElevatorSubsystemTest.java
4)idle.java
5)Downmode.java
6)Upmode.java
<<<<<<< HEAD
7)ReceiveSocket.java

8)Scheduler.java
9)ElevatorStatus.java
10)ElevatorStatusTest.java

11)FloorSubsystem.java
12)FloorUser.java
13)TestFile.java
=======
7)DoorOpen.java
8)ElevatorControlSystem.java
9)Elevatorstate.java
10)ReceiveSocket.java
11)Shutdown.java
12)TimeChecking.java

13)Scheduler.java
14)ElevatorStatus.java
15)ElevatorRequest.java
16)ElevatorStatueTest.java

17)FloorSubsystem.java
18)FloorUser.java
19)TextFile.java
>>>>>>> origin/tongdan


Port:	
	PORT 23: Bond to floor's receive Socket
	PORT 3000: Bond to scheduler's receive Socket

Data Format:
	Packet send from Floor to scheduler: 
		byte 0-1: 00=user, 01=elevator-1
		byte 2-3: 00=up, 01=down
		byte 4-5: 01=01, 02=Cart2, 03=Cart3,...(user's current floor)
		byte 6-7: 01=floor 1, 02=floor 2, ...(destination)		
		byte 8-35: current time
		
	Packet send from elevator to scheduler:
        		byte 0-1: 01 sent to scheduler 
		byte 2-3: elevator number (01=elevator-1)
		byte 4-5: elevator currern mode (01 up , 02 down , 03 idle)
		byte 6-7: elevator floor
		byte 8-35: time 

	Packet send from scheduler to elevator
		byte 0-1: impletement elevator
		byte 2-3: user current floor
		byte 4-5: destination floor
		byte 6-30: time
	
	Packet send from scheduler to Floor:
        		byte 0-1: 01 fix number
		byte 2-3: elevator number (01=elevator-1)
		byte 4-5: elevator currern mode (01 up , 02 down , 03 idle)
		byte 6-7: elevator floor
		byte 8-35: time 