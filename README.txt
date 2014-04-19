Each of the wireless sensor nodes was programmed to be able to monitor their 
environment with sensors, transmit their data, and forward data from other nodes 
if necessary.

Instructions:
 - On one mote install the BaseStation app located in MultiHop/BaseStation

 - On the other motes install the MultiHop app in the MultiHop folder

 - Before running the BaseStation app on the node ensure the other nodes
 
   are in the desired positions

 - Run the java application to display the data with the 
   
following command:

	java SensorMsgReader -comm serial@/dev/ttyUSB0:telosb SensorMsg
