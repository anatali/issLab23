package unibo.actors23;

import org.eclipse.californium.core.coap.CoAP;
//import org.eclipse.californium.core.coap.CoAP.ResponseCode.CHANGED;
//import org.eclipse.californium.core.coap.CoAP.ResponseCode.DELETED;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.server.resources.CoapExchange;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.msg.ApplMessage;
import unibo.basicomm23.utils.CommUtils;


public class CoapResourceCtx extends CoapResource  {

protected String actorResourceRep = "unknown";
    protected String name       = "dummycoapresource";

    public CoapResourceCtx(String name) {
        super(name);
        this.name = getName();
        this.setVisible(true);
        //CommUtils.outblue("            | CoapResourceCtx $name | created  ")
    }
    @Override
    public void handleGET(CoapExchange exchange){
        CommUtils.outred(name + " | handleGET ");
        exchange.respond(actorResourceRep);
    }
    /*
     * POST is NOT idempotent.	Use POST when you want to add a child resource
     */
    @Override
    public void handlePOST(CoapExchange exchange) {
        CommUtils.outred( name + " | handlePOST not implemented") ;
    }
    /*
     * PUT method is idempotent. Use PUT when you want to modify
     */
    @Override
    public void handlePUT(CoapExchange exchange){
        String arg       = exchange.getRequestText();
        IApplMessage msg = new ApplMessage( arg );
        CommUtils.outblue(name + " | handlePUT " + msg);
        fromPutToMsg( msg, exchange );
    }

    protected void fromPutToMsg( IApplMessage msg, CoapExchange exchange ) {
        if( msg.isDispatch() || msg.isEvent() ) {
            //autoMsg( msg );
            exchange.respond( CoAP.ResponseCode.CHANGED );
        }
    }
    @Override
    public void handleDELETE(CoapExchange exchange) {
        CommUtils.outblue( name + " | handleDELETE  ") ;
        delete(); //inherited from CoapResource
    }
 
//-----------------------------------------------------------------------
	
    public void addActorResource( ActorBasic23 actor){
        this.add( actor );
        CommUtils.outgray(name + "  | CoapResourceCtx root addActorResource " + actor.name);
    }


 }
