%====================================================================================
% boundaryWalkerAnnot.pl description
%====================================================================================
context(ctxall, "localhost",  "TCP", "8820").
  qactor( walker, ctxall, "unibo.actors23.fsm.example.BoundaryWalkerAnnot").
  %%qactor( sonarobs,  ctxall, "unibo.appl1.actors23.SonarObserverActor23").
  %%qactor( console, ctxall, "unibo.appl1.actors23.CmdConsoleActor23").

%%% C:\Didattica2023\issLab2023\unibo.actors23\actors23\src\main\java\unibo\actors23\fsm\example\BoundaryWalkerAnnot.java