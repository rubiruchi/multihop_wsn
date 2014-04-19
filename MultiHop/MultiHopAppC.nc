#include <Timer.h>
#include "MultiHop.h"

configuration MultiHopAppC {
}

implementation {
	components MainC;
	components MultiHopC as App;

	components LedsC;
	components new TimerMilliC() as Timer;
	components ActiveMessageC;
	components new AMSenderC(AM_SENSORMSG);
	components new AMReceiverC(AM_SENSORMSG);
	components new HamamatsuS10871TsrC() as LightSensor;
	components new SensirionSht11C() as TempHumSensor;
	components new VoltageC() as VoltageSensor;

	App.Boot -> MainC;
	App.Leds -> LedsC;
	App.Timer -> Timer;
	App.Packet -> AMSenderC;
	App.AMPacket -> AMSenderC;
	App.AMSend -> AMSenderC;
	App.Receive -> AMReceiverC;
	App.AMControl -> ActiveMessageC;
	App.LightRead -> LightSensor;
	App.TempRead -> TempHumSensor.Temperature;
	App.HumRead -> TempHumSensor.Humidity;
	App.VoltRead -> VoltageSensor;
}
