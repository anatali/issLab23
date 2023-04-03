package unibo.appl1.http;
import unibo.appl1.common.IAppl1Core;
import unibo.basicomm23.enablers.ServerFactory;
import unibo.basicomm23.interfaces.IApplMsgHandler;
import unibo.basicomm23.utils.CommUtils;

public class Appl1 {
    private IAppl1Core appl1Core;
    private ServerFactory server;

    public Appl1(IAppl1Core appl1Core){
        this.appl1Core = appl1Core;
        IApplMsgHandler applMsgHandler = new Appl1MsgHandler("Appl1Sprint3MsgHandler", appl1Core);
        server = new ServerFactory("appl1Server",Appl1Config.port, Appl1Config.protocol, applMsgHandler);
    }

    public void start(){
        CommUtils.outgreen("Appl1 | start"  );
        server.start();
    }
}
