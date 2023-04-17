package unibo.actors23.example0;

import unibo.actors23.Actor23Utils;
import unibo.actors23.ActorContext23;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.CommUtils;

public class MainActors23 {

    public void configureTheSystem(){
        Actor23Utils.trace=true;
        int port1 = 8123;
        int port2 = 8125;
        CommUtils.outblue("MainActors23 CREA I CONTESTI ");
        ActorContext23 ctx1 = new ActorContext23("ctx1", "localhost", port1);
        ActorContext23 ctx2 = new ActorContext23("ctx2", "localhost", port2);
        CommUtils.outblue("MainActors23 CREA GLI ATTORI ");

        Actor1 a1 = new Actor1("a1",ctx1);
        Actor2 a2 = new Actor2("a2",ctx2);
        Actor2 a3 = new Actor2("a3",ctx2);
        ActorConsole console = new ActorConsole("console", ctx1); //Autostart

        ctx1.addActor(a1);
        ctx1.addActor(console);
        ctx2.addActor(a2);
        ctx2.addActor(a3);

        CommUtils.outblue("MainActors23 FISSA GLI ACTOR REMOTI");
        ctx1.setActorAsRemote("a2",""+port2, "localhost", ProtocolType.tcp);
        ctx1.setActorAsRemote("a3",""+port2, "localhost", ProtocolType.tcp);
        ctx2.setActorAsRemote("a1",""+port1, "localhost", ProtocolType.tcp);
        ctx1.showActorNames();
        ctx2.showActorNames();
        //Actor23Utils.showSystemConfiguration();  //funziona solo con la descr Prolog
/*
        CommUtils.outblue("MainActors23 ATTIVA CONSOLE che invia cmd start/stopad a1");
        CmdConsoleRemote myconsole = new CmdConsoleRemote(
                "cmdconsole", ProtocolType.tcp, "localhost", ""+port1 );
        console.activate();*/
    }
    public static void main(String[] args ){
        new MainActors23().configureTheSystem();
    }
}
