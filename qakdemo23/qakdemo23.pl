%====================================================================================
% qakdemo23 description   
%====================================================================================
context(ctxdemocodedqactor, "localhost",  "TCP", "8065").
 qactor( w1, ctxdemocodedqactor, "codedActor.workactor").
  qactor( w2, ctxdemocodedqactor, "codedActor.workactor").
  qactor( w3, ctxdemocodedqactor, "codedActor.workactor").
  qactor( datahandler, ctxdemocodedqactor, "it.unibo.datahandler.Datahandler").
