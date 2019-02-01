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