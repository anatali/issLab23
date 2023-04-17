%====================================================================================
% ProdConsActor23_1.pl description
%====================================================================================
context(ctx1, "localhost",  "TCP", "8823").
%%context(ctx2, "127.0.0.1",  "TCP", "8825").
  qactor( consumer, ctx1, "ProdConsEvents.Consumer").
  qactor( producer1, ctx1, "ProdConsEvents.Producer").
  qactor( producer2, ctx1, "ProdConsEvents.Producer").


