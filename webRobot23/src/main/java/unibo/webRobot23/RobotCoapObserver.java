package unibo.webRobot23;

import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import unibo.basicomm23.utils.CommUtils;


public class RobotCoapObserver implements CoapHandler{

    @Override
    public void onLoad(CoapResponse response) {
        CommUtils.outcyan("RobotCoapObserver changed! " + response.getResponseText() );
        //send info over the websocket
        WebSocketConfiguration.wshandler.sendToAll("" + response.getResponseText());
        //simpMessagingTemplate.convertAndSend(WebSocketConfig.topicForTearoomstatemanager, new ResourceRep("" + HtmlUtils.htmlEscape(response.getResponseText())));
    }

    @Override
    public void onError() {
        CommUtils.outred("RobotCoapObserver observe error!");
    }
}
