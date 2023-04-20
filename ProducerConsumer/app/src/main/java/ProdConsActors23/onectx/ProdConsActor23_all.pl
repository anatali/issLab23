%====================================================================================
% ProdConsActor23_all.pl description
%====================================================================================
context(ctx1, "localhost",  "TCP", "8823").
  qactor( consumer,  ctx1, "ProdConsActors23.Consumer").
  qactor( producer1, ctx1, "ProdConsActors23.Producer").
  qactor( producer2, ctx1, "ProdConsActors23.Producer").


