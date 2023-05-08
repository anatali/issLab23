%====================================================================================
% basicrobot23 description   
%====================================================================================
context(ctxbasicrobot, "localhost",  "TCP", "8020").
 qactor( engager, ctxbasicrobot, "it.unibo.engager.Engager").
  qactor( basicrobot, ctxbasicrobot, "it.unibo.basicrobot.Basicrobot").
  qactor( basicrobotusage, ctxbasicrobot, "it.unibo.basicrobotusage.Basicrobotusage").
