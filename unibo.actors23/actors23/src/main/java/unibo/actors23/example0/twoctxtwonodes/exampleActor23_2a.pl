%====================================================================================
% exampleActor23_2a.pl description
%====================================================================================
context(ctx1, "localhost",  "TCP", "8123").
context(ctx2, "127.0.0.1",  "TCP", "8125").
  qactor( a1, ctx1, "unibo.actors23.example0.Actor1").
  qactor( a2, ctx2, "unibo.actors23.example0.Actor2").
  qactor( a3, ctx2, "unibo.actors23.example0.Actor2").
  qactor( console, ctx1, "unibo.actors23.example0.ActorConsole").

