%====================================================================================
% sonar description   
%====================================================================================
context(ctxsonar, "localhost",  "TCP", "8068").
context(ctxradargui, "127.0.0.1",  "TCP", "8038").
 qactor( sonarsimulator, ctxsonar, "rx.sonarSimulator").
  qactor( datalogger, ctxsonar, "rx.dataLogger").
  qactor( datacleaner, ctxsonar, "rx.dataCleaner").
  qactor( distancefilter, ctxsonar, "rx.distanceFilter").
  qactor( radargui, ctxradargui, "external").
  qactor( sonar, ctxsonar, "it.unibo.sonar.Sonar").
