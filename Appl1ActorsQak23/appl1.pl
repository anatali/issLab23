%====================================================================================
% appl1 description   
%====================================================================================
context(ctxall, "localhost",  "TCP", "8720").
 qactor( appl, ctxall, "it.unibo.appl.Appl").
  qactor( sonarobs, ctxall, "it.unibo.sonarobs.Sonarobs").
  qactor( obsforpath, ctxall, "it.unibo.obsforpath.Obsforpath").
