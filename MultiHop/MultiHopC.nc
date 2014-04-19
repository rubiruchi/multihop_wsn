#include <Timer.h>
#include "MultiHop.h"

module MultiHopC {
	uses interface Boot;
	uses interface Leds;
	uses interface Timer<TMilli> as Timer;
	uses interface Packet;
	uses interface AMPacket;
	uses interface AMSend;
	uses interface Receive;
	uses interface SplitControl as AMControl;
	uses interface Read<uint16_t> as LightRead;
	uses interface Read<uint16_t> as TempRead;
	uses interface Read<uint16_t> as HumRead;
	uses interface Read<uint16_t> as VoltRead;
}

implementation {
	bool busy = FALSE;
	message_t pkt;
	uint16_t counter = 0;
	uint16_t light = 0;
	uint16_t temp = 0;
	uint16_t hum = 0;
	uint16_t node_id = 19;
	uint16_t num_hops = 10;
	uint16_t next_id;

	event void Boot.booted() {
		call AMControl.start();
	}

	event void AMControl.startDone(error_t err) {
		if(err == SUCCESS) {
		}
		else {
			call AMControl.start();
		}
	}

	event void AMControl.stopDone(error_t err) {
	}

	event void Timer.fired() {
		call LightRead.read();
	}

	event void LightRead.readDone(error_t result, uint16_t data) {
		light = data;
		call TempRead.read();		
	}

	event void TempRead.readDone(error_t result, uint16_t data) {
		temp = data;
		call HumRead.read();
	}

	event void HumRead.readDone(error_t result, uint16_t data) {
		hum = data;
		call VoltRead.read();
	}

	event void VoltRead.readDone(error_t result, uint16_t data) {
		if (!busy) {
			SensorMsg* spkt = (SensorMsg*)
			   (call Packet.getPayload(&pkt, sizeof(SensorMsg)));
			spkt->node_id = node_id;
			spkt->dest_id = next_id;
			spkt->counter = counter;
			spkt->light = light;
			spkt->temp = temp;
			spkt->hum = hum;
			spkt->volt = data;

			if (call AMSend.send(AM_BROADCAST_ADDR, &pkt,
		   	   sizeof(SensorMsg)) == SUCCESS) {
				busy = TRUE;
				counter++;
			}
		}
	}

	event void AMSend.sendDone(message_t* msg, error_t error) {
		if(&pkt == msg) {
			busy = FALSE;
		}
	}

	event message_t* Receive.receive(message_t* msg, void* payload, uint8_t len) {
		if(len == sizeof(PathCalcMsg)) {
			PathCalcMsg* pcm_pkt = (PathCalcMsg*)payload;
			PathCalcMsg* pcm_pkt2 = (PathCalcMsg*)(call Packet.getPayload(&pkt, sizeof(PathCalcMsg)));

			if (pcm_pkt->node_id == 255)
				call Leds.led2Toggle();

			if(counter == 0)
				call Timer.startPeriodic(SAMPLING_FREQUENCY);
			
			if(pcm_pkt->num_hops < num_hops) {
				num_hops = pcm_pkt->num_hops + 1;
				next_id = pcm_pkt->node_id;
			}

			pcm_pkt2->num_hops = num_hops;
			pcm_pkt2->node_id = node_id;

			if(call AMSend.send(AM_BROADCAST_ADDR, &pkt, sizeof(PathCalcMsg)) == SUCCESS) {
				busy = TRUE;
			}

			
		}
		else if(len == sizeof(SensorMsg)) {
			SensorMsg* s_pkt = (SensorMsg*)payload;
			SensorMsg* s_pkt2 = (SensorMsg*)(call Packet.getPayload(&pkt, sizeof(SensorMsg)));

			if(s_pkt->dest_id == node_id) {
				if(!busy) {
					s_pkt->node_id = s_pkt2->node_id;
					s_pkt->dest_id = next_id;
					s_pkt->counter = s_pkt2->counter;
					s_pkt->light = s_pkt2->light;
					s_pkt->temp = s_pkt2->temp;
					s_pkt->hum = s_pkt2->hum;
					s_pkt->volt = s_pkt2->volt;

					if (call AMSend.send(AM_BROADCAST_ADDR, &pkt, sizeof(SensorMsg)) == SUCCESS) {
						busy = TRUE;
						call Leds.led1Toggle();
					}
				}
			}
						
		}
		return msg;
	}
}
