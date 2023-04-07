%====================================================================================
% PingPongActor23_all.pl description
%====================================================================================
context(ctx1, "localhost",  "TCP", "8523").
context(ctx2, "localhost",  "TCP", "8525").
  qactor( ping, ctx1, "PingPongDispatch.Ping").
  qactor( pong, ctx2, "PingPongDispatch.Pong").



