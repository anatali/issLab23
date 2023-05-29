%====================================================================================
% exampleActor23_2b.pl description
%====================================================================================
context(ctx1, "192.168.1.13",  "TCP", "8123").
context(ctx2, "localhost",  "TCP", "8125").
  qactor( a1, ctx1, "unibo.basicomm23.actors23.example.Actor1").
  qactor( a2, ctx2, "unibo.basicomm23.actors23.example.Actor2").
  qactor( a3, ctx2, "unibo.basicomm23.actors23.example.Actor2").
  %%qactor( console, ctx1, "unibo.basicomm23.actors23.example.ActorConsole").

