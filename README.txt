Instruction


Port
	PORT 23: Bond to floor's receive Socket
	PORT 3000: Bond to scheduler's receive Socket
Data Format
	Packet send from Floor: 
		byte 0-1: 00=user, 01=elevator-1
		byte 2-3: 00=up, 01=down
		byte 4-5: 01=floor 1, 02=floor 2, ...
		byte 6-7: 01=01, 02=Cart2, 03=Cart3,...
		byte 8-35: current time
		
Packet from elevator:
                byte 0-1: 01 sent to scheduler 
		byte 2-3: elevator number (01=elevator-1)
		byte 4-5: elevator currern mode (01 up , 02 down , 03 idle)
		byte 6-7: elevator floor
		byte 8-50: time 
