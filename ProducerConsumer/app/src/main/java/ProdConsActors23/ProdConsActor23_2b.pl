%====================================================================================
% ProdConsActor23_2.pl description
%====================================================================================
context(ctx1, "127.0.0.1",  "TCP", "8823").
context(ctx2, "localhost",  "TCP", "8825").
  qactor( producer1, ctx1, "ProdConsActors23.Producer").
  qactor( consumer, ctx2, "ProdConsActors23.Consumer").  %% prima del producer2
  qactor( producer2, ctx2, "ProdConsActors23.Producer").


