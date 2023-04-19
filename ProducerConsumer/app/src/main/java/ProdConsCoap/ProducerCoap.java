package ProdConsCoap;

import unibo.actors23.ActorContext23;
import unibo.actors23.annotations.State;
import unibo.actors23.annotations.Transition;
import unibo.actors23.annotations.TransitionGuard;
import unibo.actors23.fsm.ActorBasicFsm23;
import unibo.basicomm23.coap.CoapConnection;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.utils.CommUtils;

public class ProducerCoap extends ActorBasicFsm23 {
    private String item             = "";
    private final String continueId = "goon";
    private Interaction coapconn  ;

    public ProducerCoap(String name, ActorContext23 ctx) {
        super(name, ctx);
        try {
            coapconn = CoapConnection.create("localhost:8923", "actors/consumer");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected  final IApplMessage prodMsg( String msgId, String content, String receiver )   {
        return CommUtils.buildDispatch(name, msgId, content, receiver );
    }

    @State( name = "s0", initial=true)
    @Transition( state = "produce"   )       //empty move
    protected void s0( IApplMessage msg ) {
        CommUtils.outblue(name + " | s0 "+msg  );

    }

    @State( name = "produce" )
    @Transition( state = "produce",  msgId = continueId, guard="notCompleted"  )
    @Transition( state = "endWork" , msgId = continueId, guard="completed" )
    protected void produce( IApplMessage inputmsg ) {
        CommUtils.outyellow(name + " | produce "+ inputmsg  );
        //for( int i=1; i<= 3; i++ ) {  //CICLO => autoMsg
            item = item + "a";
            IApplMessage msg = prodMsg("prodinfo", item, "consumer");
            CommUtils.outblue(name + " | produce " + item);
            //this.forward(msg);
            try {
                coapconn.forward(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        CommUtils.delay(1000);
            //CICLO => transizione con guardia
            IApplMessage msg1 = prodMsg(continueId, "ok", name); //automsg
            this.forward(msg1);

    }

    @State( name = "endWork" )
    protected void endWork( IApplMessage msg ) {
        CommUtils.outblue(name + " | endWork BYE" );
        //System.exit(0);
    }

    @TransitionGuard
    protected boolean completed() {
        //CommUtils.outyellow("GUARD completed "  );
        return item.length() >= 3 ;
    }
    @TransitionGuard
    protected boolean notCompleted() {
        //CommUtils.outyellow("GUARD notCompleted "  );
        return item.length() < 3 ;
    }
}
