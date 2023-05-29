package unibo.basicomm23.enablers;

import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.IApplMsgHandler;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.msg.ApplMessage;
import unibo.basicomm23.utils.CommUtils;

public class ConnectionHandler extends Thread{
    private String name;
    private IApplMsgHandler handler ;
    private Interaction conn;

    public ConnectionHandler(String name, IApplMsgHandler handler, Interaction conn ) {
        this.name    = name;
        this.handler = handler;
        this.conn    = conn;
        //CommUtils.outblue("ServerMsgHandler | STARTING with user handler:" + handler.getName()  );
        this.start();
    }

    @Override
    public void run() {
        //String name = handler.getName();
        //CommUtils.outyellow(getName() + " | TcpApplMessageHandler  STARTS with user-handler=" + name + " conn=" + conn );
        while( true ) {
            try {
                //CommUtils.outblue(name + " | waits for message  ...");
                String msg = conn.receiveMsg();
                //CommUtils.outblue(name + "  | received:" + msg   );
                if( msg == null ) {
                    //conn.close();	//Feb23
                    break;
                } else{
                    IApplMessage m = new ApplMessage(msg);
                    handler.elaborate( m, conn );
                }
            }catch( Exception e) {
                CommUtils.outred( name + "  | ERROR: " + e.getMessage()  );
            }
        }
        //CommUtils.outblue(name + " |  BYE"   );
    }

}
