%====================================================================================
% appl1CoreActorFsm23.pl description
%====================================================================================
context(ctxall, "localhost",  "TCP", "8720").
  qactor( appl,        ctxall, "appl1Systems.actorFsm.Appl1ActorFsm23").
  qactor( sonarobs,    ctxall, "appl1Actors23.SonarObserverActor23").
  qactor( obsforpath,  ctxall, "appl1Actors23.observeractor.ObserverActorForPath").
  %%qactor( appl1state,  ctxall, "appl1Actors23.Appl1StateActor23").
  qactor( console,     ctxall, "appl1Actors23.CmdConsoleActor23").

