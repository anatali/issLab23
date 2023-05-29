%====================================================================================
% userobot description   
%====================================================================================
context(ctxbasicrobot, "127.0.0.1",  "TCP", "8020").
context(ctxuserobot, "localhost",  "TCP", "8111").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( pathobs, ctxbasicrobot, "observers.planexecCoapObserver").
  qactor( userobotclient, ctxuserobot, "it.unibo.userobotclient.Userobotclient").
