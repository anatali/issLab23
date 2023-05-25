%====================================================================================
% radarsystem23analisi description   
%====================================================================================
context(ctxprototipo0, "localhost",  "TCP", "8088").
 qactor( sonar23, ctxprototipo0, "it.unibo.sonar23.Sonar23").
  qactor( led23, ctxprototipo0, "it.unibo.led23.Led23").
  qactor( radar23, ctxprototipo0, "it.unibo.radar23.Radar23").
  qactor( controller23, ctxprototipo0, "it.unibo.controller23.Controller23").
