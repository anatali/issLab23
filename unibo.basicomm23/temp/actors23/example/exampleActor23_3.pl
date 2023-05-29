%====================================================================================
% exampleActor23.pl description
%====================================================================================
context(ctx0, "localhost",  "TCP", "8121").
context(ctx1, "localhost",  "TCP", "8123").
context(ctx2, "localhost",  "TCP", "8125").
  qactor( a1, ctx1, "unibo.basicomm23.actors23.example.Actor1").
  qactor( a2, ctx2, "unibo.basicomm23.actors23.example.Actor2").
  qactor( a3, ctx2, "unibo.basicomm23.actors23.example.Actor2").
  qactor( console, ctx0, "unibo.basicomm23.actors23.example.ActorConsole").

