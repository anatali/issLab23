%====================================================================================
% radargui23 description   
%====================================================================================
mqttBroker("mqtt.eclipseprojects.io", "1883", "unibo/sonar/events").
context(ctxradargui, "localhost",  "TCP", "8038").
 qactor( radargui, ctxradargui, "it.unibo.radargui.Radargui").
