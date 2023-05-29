%====================================================================================
% ProdConsActor23_all.pl description
%====================================================================================
context(ctx1, "localhost",  "TCP", "8923").
context(ctx2, "localhost",  "TCP", "8925").
context(ctx3, "localhost",  "TCP", "8927").  %% per vedere ctx3CtxMsgHandler comune a due conn
  qactor( producer1, ctx1, "unibo.basicomm23.actors23.ApplsActor23.Producer").
  qactor( consumer,  ctx2, "unibo.basicomm23.actors23.ApplsActor23.Consumer").  %% prima del producer2
  qactor( producer2, ctx3, "unibo.basicomm23.actors23.ApplsActor23.Producer"). %%ctx2


