%====================================================================================
% basicrobot23 description   
%====================================================================================
context(ctxbasicrobot, "localhost",  "TCP", "8020").
 qactor( basicrobot, ctxbasicrobot, "it.unibo.basicrobot.Basicrobot").
  qactor( eventobserver, ctxbasicrobot, "it.unibo.eventobserver.Eventobserver").
