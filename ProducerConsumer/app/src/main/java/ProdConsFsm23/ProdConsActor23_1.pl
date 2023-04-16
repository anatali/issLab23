%====================================================================================
% ProdConsActor23_1.pl description
%====================================================================================
context(ctx1, "localhost",  "TCP", "8923").
%%context(ctx2, "localhost",  "TCP", "8925").
  qactor( producer1, ctx1, "ProdConsFsm23.ProducerFsm23").
  qactor( consumer, ctx1, "ProdConsFsm23.ConsumerFsm23").
  qactor( producer2, ctx1, "ProdConsFsm23.ProducerFsm23").


