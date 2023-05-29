package unibo.basicomm23.coap;

import org.eclipse.californium.core.server.resources.CoapExchange;
import unibo.basicomm23.utils.Connection;

public class CoapInteraction extends Connection {
private CoapExchange exchange;

    public CoapInteraction( CoapExchange exchange ){
        this.exchange = exchange;
    }
    @Override
    public void forward(String msg) throws Exception {
    }

    @Override
    public String request(String msg) throws Exception {
        return null;
    }

    @Override
    public void reply(String msg) throws Exception {
        exchange.respond(msg);
    }

    @Override
    public String receiveMsg() throws Exception {
        return null;
    }

    @Override
    public void close() throws Exception {

    }
}
