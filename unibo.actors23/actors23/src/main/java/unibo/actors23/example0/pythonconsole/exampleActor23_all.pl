%====================================================================================
% exampleActor23_all.pl description
%====================================================================================
context(ctxall, "localhost",  "TCP", "8120").
  qactor( a1, ctxall, "unibo.actors23.example0.Actor1").
  qactor( a2, ctxall, "unibo.actors23.example0.Actor2").
  qactor( a3, ctxall, "unibo.actors23.example0.Actor2").
  %%% qactor( console, ctxall, "unibo.actors23.example0.ActorConsole").
