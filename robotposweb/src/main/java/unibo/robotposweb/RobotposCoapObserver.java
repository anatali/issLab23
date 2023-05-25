package unibo.robotposweb;

import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import unibo.basicomm23.utils.CommUtils;


public class RobotposCoapObserver implements CoapHandler{

    @Override
    public void onLoad(CoapResponse response) {
        CommUtils.outcyan("RobotposCoapObserver changed! " + response.getResponseText() );
        //send info over the websocket
        try {
            Thread.sleep(300); //Per dare tempo alla pagina di visualizzarsi
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebSocketConfiguration.wshandler.sendToAll("" + response.getResponseText());
    }

    @Override
    public void onError() {
        CommUtils.outred("RobotposCoapObserver observe error!");
    }
}
