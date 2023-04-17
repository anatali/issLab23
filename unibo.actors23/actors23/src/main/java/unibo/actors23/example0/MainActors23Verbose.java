package unibo.actors23.example0;

import unibo.actors23.Actor23Utils;
import unibo.actors23.ActorContext23;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.CommUtils;

public class MainActors23Verbose {

    public void configureTheSystem(){
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "ERROR");
        //Actor23Utils.trace=true;
        int port1 = 8123;
        int port2 = 8125;
        CommUtils.outblue("MainActors23Verbose CREA I CONTESTI ");
        ActorContext23 ctx1 = new ActorContext23("ctx1", "localhost", port1);
        ActorContext23 ctx2 = new ActorContext23("ctx2", "localhost", port2);
        CommUtils.outblue("MainActors23Verbose CREA GLI ATTORI ");

        Actor1 a1 = new Actor1("a1",ctx1);
        Actor2 a2 = new Actor2("a2",ctx2);
        Actor2 a3 = new Actor2("a3",ctx2);
        ActorConsole console = new ActorConsole("console", ctx1); //Autostart

        ctx1.addActor(a1);
        ctx1.addActor(console);
        ctx2.addActor(a2);
        ctx2.addActor(a3);

        CommUtils.outblue("MainActors23Verbose FISSA GLI ACTOR REMOTI");
        ctx1.setActorAsRemote("a2",""+port2, "localhost", ProtocolType.tcp);
        ctx1.setActorAsRemote("a3",""+port2, "localhost", ProtocolType.tcp);
        ctx2.setActorAsRemote("a1",""+port1, "localhost", ProtocolType.tcp);
        ctx1.showActorNames();
        ctx2.showActorNames();
        //Actor23Utils.showSystemConfiguration();  //funziona solo con la descr Prolog

        ctx1.activateLocalActors();
        ctx2.activateLocalActors();

    }
    public static void main(String[] args ){
        new MainActors23Verbose().configureTheSystem();
    }
}
