package ProdConsInteraction;
import unibo.actors23.Actor23Utils;
import unibo.basicomm23.enablers.ServerFactory;
import unibo.basicomm23.interfaces.IApplMsgHandler;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.Connection;

/*
Il sistema deve dare le stesse risposte per interazioni basate su
    TCP, UDP, COAP
 */
public class MainProdCons {

    public void configureTheSystemFinal(){
        //Connection.trace   = true;
        ProdConsConfig.setProtocol(ProtocolType.coap);  //tcp udp coap
        //Create the producers
        Producer producer1 = new Producer("prod1");
        Producer producer2 = new Producer("prod2");
        //Create the consumer
        Consumer consumer  = new Consumer("consumer");
        //Activate
        consumer.start();
        producer1.start();
        producer2.start();
    }

    public void configureTheSystemVerboseNoCoap(){
        String hostAddr       = "localhost";
        int port              = 8888;
        ProtocolType protocol = ProtocolType.tcp;  //tcp udp

        //Sottosistemi di produzione
        ProducerLogic prodLogic = new ProducerLogic();
        ProducerCaller prod1 = new ProducerCaller(
                "prod1", prodLogic, protocol, hostAddr, ""+port);
        ProducerCaller prod2 = new ProducerCaller(
                "prod2", prodLogic, protocol, hostAddr, ""+port);

        //Sottosistmea di consumazione
        ConsumerLogic consunerLogic = new ConsumerLogic();
        IApplMsgHandler chandle = new ConsumerMsgHandler("consumerhanlder", consunerLogic );
        ServerFactory server    = new ServerFactory("server", port, protocol);
        server.addMsgHandler(chandle);

        //Activate
        server.start();
        prod1.activate();
        prod2.activate();
    }

    public void configureTheSystemCoapNotYetNecessary(){
        int port              = 7777; //5683;
        String hostAddr       = "localhost:"+port ;
        ProtocolType protocol = ProtocolType.coap;

        //Create the producers
        ProducerLogic prodLogic = new ProducerLogic();
        ProducerCaller prod1  = new ProducerCaller("prod1", prodLogic, protocol, hostAddr, "/basicomm23/consumer");
        ProducerCaller prod2  = new ProducerCaller("prod2", prodLogic, protocol, hostAddr, "/basicomm23/consumer");

        //Create the consumer
        ConsumerLogic consunerLogic = new ConsumerLogic();
        ConsumerCoapResource consumer  = new ConsumerCoapResource("consumer", consunerLogic );
        ServerFactory server           = new ServerFactory("server", port, protocol);
        server.addMsgHandler(consumer);

        //CoapApplServer server          = new CoapApplServer(port);
        //server.addCoapResource(consumer, "basicomm23");

        //Activate
        prod1.activate();
        prod2.activate();
    }
    public static void main( String[] args ){
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "ERROR");

        new MainProdCons().configureTheSystemFinal();
        //new MainProdCons().configureTheSystemVerboseNoCoap();
        //new MainProdCons().configureTheSystemCoapNotYetNecessary();
    }
}
