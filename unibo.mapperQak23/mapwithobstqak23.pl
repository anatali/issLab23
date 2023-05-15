%====================================================================================
% mapwithobstqak23 description   
%====================================================================================
context(ctxbasicrobot, "127.0.0.1",  "TCP", "8020").
context(ctxmapwithobstqak23, "localhost",  "TCP", "8032").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( mapwithobstqak23, ctxmapwithobstqak23, "it.unibo.mapwithobstqak23.Mapwithobstqak23").
  qactor( mapusage, ctxmapwithobstqak23, "it.unibo.mapusage.Mapusage").
  qactor( mover, ctxmapwithobstqak23, "it.unibo.mover.Mover").
