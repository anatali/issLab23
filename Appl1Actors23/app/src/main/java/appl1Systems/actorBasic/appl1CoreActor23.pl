%====================================================================================
% appl1CoreActor23.pl description
%====================================================================================
context(ctxall, "localhost",  "TCP", "8720").
  qactor( appl,        ctxall, "appl1Systems.actorBasic.Appl1CoreActor23").
  qactor( sonarobs,    ctxall, "appl1Actors23.SonarObserverActor23").
  qactor( obsforpath,  ctxall, "appl1Actors23.observeractor.ObserverActorForPath").
  qactor( console,     ctxall, "appl1Actors23.CmdConsoleActor23").

