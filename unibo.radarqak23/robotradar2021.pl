%====================================================================================
% robotradar2021 description   
%====================================================================================
mqttBroker("broker.hivemq.com", "1883", "unibo/basicrobot").
context(ctxradargui, "localhost",  "TCP", "8038").
 qactor( radargui, ctxradargui, "it.unibo.radargui.Radargui").
