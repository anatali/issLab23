%====================================================================================
% demoaskfor description   
%====================================================================================
context(ctxdemoaskfor, "localhost",  "TCP", "8076").
 qactor( caller, ctxdemoaskfor, "it.unibo.caller.Caller").
  qactor( called, ctxdemoaskfor, "it.unibo.called.Called").
