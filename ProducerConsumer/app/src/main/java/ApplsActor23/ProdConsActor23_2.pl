%====================================================================================
% ProdConsActor23_2.pl description
%====================================================================================
context(ctx1, "192.168.1.132",  "TCP", "8823").
context(ctx2, "localhost",  "TCP", "8825").
  qactor( producer1, ctx1, "ApplsActor23.Producer").
  qactor( consumer, ctx2, "ApplsActor23.Consumer").  %% prima del producer2
  qactor( producer2, ctx2, "ApplsActor23.Producer").


