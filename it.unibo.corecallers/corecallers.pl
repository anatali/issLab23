%====================================================================================
% corecallers description   
%====================================================================================
context(ctxresourcecore, "127.0.0.1",  "TCP", "8045").
context(ctxcorecaller1, "localhost",  "TCP", "8038").
 qactor( resourcecore, ctxresourcecore, "external").
  qactor( corecaller1, ctxcorecaller1, "it.unibo.corecaller1.Corecaller1").
  qactor( alarmoffemitter, ctxcorecaller1, "it.unibo.alarmoffemitter.Alarmoffemitter").
