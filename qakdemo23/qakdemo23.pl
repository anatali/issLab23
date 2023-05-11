%====================================================================================
% qakdemo23 description   
%====================================================================================
context(ctxcallers, "localhost",  "TCP", "8072").
context(ctxcalled, "localhost",  "TCP", "8074").
 qactor( caller, ctxcallers, "it.unibo.caller.Caller").
  qactor( called, ctxcalled, "it.unibo.called.Called").
msglogging.
