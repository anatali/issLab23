%====================================================================================
% exampleActor23.pl description
%====================================================================================
context(ctx1, "localhost",  "TCP", "8823").
%%context(ctx2, "localhost",  "TCP", "8825").
  qactor( consumer,  ctx1, "ProdConsActors23.Consumer").
  qactor( producer1, ctx1, "ProdConsActors23.Producer").
  qactor( producer2, ctx1, "ProdConsActors23.Producer").


