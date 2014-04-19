#ifndef MULTIHOP_H
#define MULTIHOP_H
enum {
	AM_SENSORMSG = 6,
	SAMPLING_FREQUENCY = 2000
};

typedef nx_struct PathCalcMsg {
	nx_uint16_t node_id;
	nx_uint16_t num_hops;
} PathCalcMsg;

typedef nx_struct SensorMsg {
	nx_uint16_t node_id;
	nx_uint16_t dest_id;

	nx_uint16_t light;
	nx_uint16_t temp;
	nx_uint16_t hum;
	nx_uint16_t volt;

	nx_uint16_t counter;
} SensorMsg;
#endif
