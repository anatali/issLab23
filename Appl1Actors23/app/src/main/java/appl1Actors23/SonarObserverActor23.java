package appl1Actors23;

import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import unibo.actors23.ActorBasic23;
import unibo.actors23.ActorContext23;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.utils.CommUtils;

/*
SonarObserverActor23 percepisce gli eventi  sonardata generati da VrobotHLMovesActors23
 */
public class SonarObserverActor23 extends ActorBasic23 {
    protected IApplMessage stopMsg;
    protected IApplMessage resumeMsg;
    protected String sonarDistance = null;

    public SonarObserverActor23(String name, ActorContext23 ctx) {
        super(name, ctx);
        stopMsg   = CommUtils.buildDispatch(name, "stopcmd", "stop",   "appl");
        resumeMsg = CommUtils.buildDispatch(name, "resumecmd", "resume", "appl");
        //this.subscribeLocalActor("appl");
    }

    @Override
    protected void elabMsg(IApplMessage message) throws Exception {
        CommUtils.outblack(name + " | elabMsg:"
                + message + " " + sonarDistance + " " + Thread.currentThread().getName());
        if( sonarDistance != null && message.msgContent().equals(sonarDistance) ) {
            //evito di gestire l'evento .
            CommUtils.outgray(name + " | elabMsg avoids:"
                    + message + " msgQueue:" + msgQueue.size() + " " + Thread.currentThread().getName());
            //ACCESSO ALLA CODA: potrebbe/dovrebbe   non essere consentito
            //if( msgQueue.isEmpty() ) firstEvent = true; //potrebbe arrivare dopo ...
            return;
        }
        sonarDistance = message.msgContent();
        CommUtils.outblack(name + " | elabMsg:"
                + message + " " + Thread.currentThread().getName());
        if( ! message.isEvent() || ! message.msgId().equals("sonardata") ) return;

        String data  = message.msgContent().replaceAll("'","");
        String value = ((Struct)Term.createTerm(data)).getArg(0).toString();
        int v       = Integer.parseInt(value);
        CommUtils.outblack(name + " | sonar distance=" + v + " " + sonarDistance + " " + Thread.currentThread().getName() );
        forward( stopMsg );  //come da requisito
        //CommUtils.delay(500); //avoid to remain under sonar
        CommUtils.delay(2000);
        forward(resumeMsg);
    }
}
