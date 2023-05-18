%====================================================================================
% robotpos description   
%====================================================================================
context(ctxrobotpos, "localhost",  "TCP", "8111").
context(ctxbasicrobot, "127.0.0.1",  "TCP", "8020").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( worker, ctxrobotpos, "it.unibo.worker.Worker").
  qactor( client, ctxrobotpos, "it.unibo.client.Client").
