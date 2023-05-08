%====================================================================================
% qakdemo23 description   
%====================================================================================
context(ctxdemoreq, "localhost",  "TCP", "8010").
 qactor( caller, ctxdemoreq, "it.unibo.caller.Caller").
  qactor( called, ctxdemoreq, "it.unibo.called.Called").
msglogging.
