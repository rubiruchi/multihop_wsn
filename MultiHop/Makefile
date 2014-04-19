COMPONENT=MultiHopAppC

BUILD_EXTRA_DEPS=SensorMsg.class
SensorMsg.class: SensorMsg.java
	javac SensorMsg.java
SensorMsg.java:
	mig  java  -target=null  -java-classname=SensorMsg MultiHop.h SensorMsg  -o  $@

include $(MAKERULES)
