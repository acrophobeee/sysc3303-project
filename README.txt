Instruction


Port
	PORT 23: Bond to floor's receive Socket
	PORT 3000: Bond to scheduler's receive Socket
Data Format
	Packet send from Floor: 
		byte 0-1: 00=up, 01=down
		byte 2-3: 01=floor 1, 02=fl 2, ...
		byte 4-5: 01=Cart1, 02=Cart2, 03=Cart3,...
		byte 6-33: current time