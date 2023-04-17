%====================================================================================
% ProdConsActor23_1.pl description
%====================================================================================
context(ctx1, "localhost",  "TCP", "8823").
  qactor( producer, ctx1, "ProdConsStream.Producer").
  qactor( cleaner,  ctx1, "ProdConsStream.DataCleaner").
  qactor( consumer, ctx1, "ProdConsStream.Consumer").



