%====================================================================================
% demo_req_b description   
%====================================================================================
context(ctxcaller, "localhost",  "TCP", "8076").
context(ctxcalled, "127.0.0.1",  "TCP", "8078").
 qactor( caller, ctxcaller, "it.unibo.caller.Caller").
  qactor( called, ctxcalled, "it.unibo.called.Called").
