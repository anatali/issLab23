%====================================================================================
% sonarqak23 description   
%====================================================================================
context(ctxsonarqak23, "localhost",  "TCP", "8128").
context(ctxbasicrobot, "192.168.1.9",  "TCP", "8020").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( sonar, ctxsonarqak23, "sonarSimulator").
  qactor( datacleaner, ctxsonarqak23, "rx.dataCleaner").
  qactor( distancefilter, ctxsonarqak23, "rx.distanceFilter").
  qactor( sonar23, ctxsonarqak23, "it.unibo.sonar23.Sonar23").
  qactor( appl, ctxsonarqak23, "it.unibo.appl.Appl").
