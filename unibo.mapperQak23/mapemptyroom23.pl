%====================================================================================
% mapemptyroom23 description   
%====================================================================================
context(ctxbasicrobot, "127.0.0.1",  "TCP", "8020").
context(ctxmapemptyroom23, "localhost",  "TCP", "8032").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( mapemptyroom23, ctxmapemptyroom23, "it.unibo.mapemptyroom23.Mapemptyroom23").
