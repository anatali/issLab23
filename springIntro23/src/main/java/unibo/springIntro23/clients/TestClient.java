package unibo.springIntro23.clients;
import unibo.basicomm23.interfaces.Interaction2021;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.ApplAbstractObserver;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.ConnectionFactory;
import unibo.basicomm23.ws.WsConnection;

public class TestClient extends ApplAbstractObserver {
private Interaction2021 clientConn;

public TestClient(){
        clientConn = ConnectionFactory.createClientSupport(
                ProtocolType.ws, "localhost", "8085/socket"); //!!! NOTE 8085/socket
        ((WsConnection)clientConn ).addObserver(this);
    }

    @Override
    public void update(String s) {
        CommUtils.outblue("TestClient update=" + s);
    }

    public void doJob(){
        try {
            clientConn.forward("hello from Java client");
            CommUtils.outblue("TestClient forward done " );
            Thread.sleep(2000); //To sse the update
        } catch (Exception ex) {
            CommUtils.outred("TestClient ERROR: " + ex.getMessage());
        }
    }
    public static void main(String[] args) {
        TestClient appl = new TestClient();
        appl.doJob();
    }
}
