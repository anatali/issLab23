%====================================================================================
% appl1qak description   
%====================================================================================
context(ctxall, "localhost",  "TCP", "8720").
 qactor( appl, ctxall, "it.unibo.appl.Appl").
  qactor( consoleobs, ctxall, "it.unibo.consoleobs.Consoleobs").
  qactor( sonarobs, ctxall, "it.unibo.sonarobs.Sonarobs").
  qactor( obsforpath, ctxall, "it.unibo.obsforpath.Obsforpath").
  qactor( console, ctxall, "it.unibo.console.Console").
