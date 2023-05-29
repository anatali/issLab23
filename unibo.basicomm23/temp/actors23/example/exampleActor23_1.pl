%====================================================================================
% exampleActor23_0.pl description
%====================================================================================
context(ctxall, "localhost",  "TCP", "8120").
  qactor( a1, ctxall, "unibo.basicomm23.actors23.example.Actor1").
  qactor( a2, ctxall, "unibo.basicomm23.actors23.example.Actor2").
  qactor( a3, ctxall, "unibo.basicomm23.actors23.example.Actor2").
  qactor( console, ctxall, "unibo.basicomm23.actors23.example.ActorConsole").

