%====================================================================================
% ProdConsActorFsm23.pl description
%====================================================================================
context(ctx1, "localhost",  "TCP", "8923").
  qactor( producer1, ctx1, "ProdConsFsm23.ProducerFsm23").
  qactor( consumer,  ctx1, "ProdConsFsm23.ConsumerFsm23").
  qactor( producer2, ctx1, "ProdConsFsm23.ProducerFsm23").


