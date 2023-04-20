%============================================
% ProdConsActor23_2a.pl description
%============================================
context(ctx1, "localhost",  "TCP", "8823").
context(ctx2, "127.0.0.1",  "TCP", "8825").
  qactor( producer1, ctx1, "ProdConsActors23.Producer").
  qactor( consumer,  ctx1, "ProdConsActors23.Consumer").
  qactor( producer2, ctx2, "ProdConsActors23.Producer").
  %%qactor( producer3, ctx2, "ProdConsActors23.Producer").


