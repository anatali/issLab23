package ProdConsActors23;

import unibo.actors23.Actor23Utils;
import unibo.actors23.ActorContext23;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.CommUtils;

//ctx1-8123 : prod1, consumer
//ctx2-8125 : prod2, prod3

public class MainProdConsActors23Verbose {
    public void configureTheSystem(){
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "ERROR");
        //Actor23Utils.trace=true;
        CommUtils.aboutThreads(  "MainProdConsActors23Verbose | At START ");

        int port1 = 8123;
        int port2 = 8125;
        CommUtils.outmagenta("MainActors23 CREA I CONTESTI ");
        ActorContext23 ctx1 = new ActorContext23("ctx1", "localhost", port1);
        ActorContext23 ctx2 = new ActorContext23("ctx2", "localhost", port2);
        CommUtils.outmagenta("MainActors23 CREA GLI ATTORI ");

        Producer prod1 = new Producer("prod1",ctx1);
        Consumer consumer = new Consumer("consumer", ctx1); //Autostart
        Producer prod2 = new Producer("prod3",ctx2);
        Producer prod3 = new Producer("prod3",ctx2);

        ctx1.addActor(prod1);
        ctx1.addActor(consumer);
        ctx2.addActor(prod2);
        ctx2.addActor(prod3);

        CommUtils.outmagenta("MainActors23 FISSA GLI ACTOR REMOTI");
        ctx1.setActorAsRemote("prod2",""+port2, "localhost", ProtocolType.tcp);
        ctx2.setActorAsRemote("consumer",""+port1, "localhost", ProtocolType.tcp);
        ctx1.setActorAsRemote("prod3",""+port2, "localhost", ProtocolType.tcp);

        ctx1.showActorNames();
        ctx2.showActorNames();
        CommUtils.aboutThreads(  "MainProdConsActors23Verbose | BEFORE ACTIVATE ");
        Actor23Utils.activateActorsInContext(ctx1);
        ctx2.activateLocalActors();
        CommUtils.aboutThreads(  "MainProdConsActors23Verbose | AFTER ACTIVATE ");

    }
    public static void main(String[] args ){
        new MainProdConsActors23Verbose().configureTheSystem();
    }
}
