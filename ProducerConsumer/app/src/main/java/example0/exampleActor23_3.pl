%====================================================================================
% exampleActor23.pl description
%====================================================================================
context(ctx0, "localhost",  "TCP", "8121").
context(ctx1, "localhost",  "TCP", "8123").
context(ctx2, "localhost",  "TCP", "8125").
  qactor( a1, ctx1, "example0.Actor1").
  qactor( a2, ctx2, "example0.Actor2").
  qactor( a3, ctx2, "example0.Actor2").
  qactor( console, ctx0, "example0.ActorConsole").

%%% C:\Didattica2023\issLab23\ProducerConsumer\app\src\main\java\example0