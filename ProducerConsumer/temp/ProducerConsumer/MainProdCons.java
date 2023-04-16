package ProdConsInteraction;
import unibo.basicomm23.enablers.ServerFactory;
import unibo.basicomm23.interfaces.IApplMsgHandler;
import unibo.basicomm23.msg.ProtocolType;

/*
Il sistema deve dare le stesse risposte per interazioni basate su
    TCP, UDP, COAP
Al contro del sistema: ProducerLogic e ConsumerLogic   POJO
Componenti astratti:   ActorNaiveCaller e ActorNaiveReceiver
Next level:            ProducerActor e ( ConsumerActor | ConsumerCoapResource )
Next level:            EnablerAsServer e CoapApplServer
 */
public class MainProdCons {

    public void configureTheSystem(){
        unibo.basicomm23.examples.prodcons.ProdConsConfig.setProtocol(ProtocolType.coap);
        //Create the producers
        Producer producer1 = new Producer("prod1");
        Producer producer2 = new Producer("prod2");
        //Create the consumer
        unibo.basicomm23.examples.prodcons.Consumer consumer  = new unibo.basicomm23.examples.prodcons.Consumer("consumer");
        //Activate
        consumer.start();
        producer1.start();
        producer2.start();
    }

    public void configureTheSystemOld(){
        String hostAddr       = "localhost";
        int port              = 8888;
        ProtocolType protocol = ProtocolType.tcp;

        //Sottosistemi di produzione
        unibo.basicomm23.examples.prodcons.ProducerLogic prodLogic = new unibo.basicomm23.examples.prodcons.ProducerLogic();
        unibo.basicomm23.examples.prodcons.ProducerCaller prod1 = new unibo.basicomm23.examples.prodcons.ProducerCaller("prod1", prodLogic, protocol, hostAddr, ""+port);
        unibo.basicomm23.examples.prodcons.ProducerCaller prod2 = new unibo.basicomm23.examples.prodcons.ProducerCaller("prod2", prodLogic, protocol, hostAddr, ""+port);

        //Sottosistmea di consumazione
        unibo.basicomm23.examples.prodcons.ConsumerLogic consunerLogic = new unibo.basicomm23.examples.prodcons.ConsumerLogic();
        IApplMsgHandler chandle = new unibo.basicomm23.examples.prodcons.ConsumerMsgHandler("consumerhanlder", consunerLogic );
        ServerFactory server    = new ServerFactory("server", port, protocol);
        server.addMsgHandler(chandle);

        //Activate
        server.start();
        prod1.activate();
        prod2.activate();
    }

    public void configureCoapSystem(){
        int port              = 7777; //5683;
        String hostAddr       = "localhost:"+port ;
        ProtocolType protocol = ProtocolType.coap;

        //Create the producers
        unibo.basicomm23.examples.prodcons.ProducerLogic prodLogic = new unibo.basicomm23.examples.prodcons.ProducerLogic();
        unibo.basicomm23.examples.prodcons.ProducerCaller prod1  = new unibo.basicomm23.examples.prodcons.ProducerCaller("prod1", prodLogic, protocol, hostAddr, "/basicomm23/consumer");
        unibo.basicomm23.examples.prodcons.ProducerCaller prod2  = new unibo.basicomm23.examples.prodcons.ProducerCaller("prod2", prodLogic, protocol, hostAddr, "/basicomm23/consumer");

        //Create the consumer
        unibo.basicomm23.examples.prodcons.ConsumerLogic consunerLogic = new unibo.basicomm23.examples.prodcons.ConsumerLogic();
        unibo.basicomm23.examples.prodcons.ConsumerCoapResource consumer  = new unibo.basicomm23.examples.prodcons.ConsumerCoapResource("consumer", consunerLogic );
        ServerFactory server           = new ServerFactory("server", port, protocol);
        server.addMsgHandler(consumer);

        //CoapApplServer server          = new CoapApplServer(port);
        //server.addCoapResource(consumer, "basicomm23");

        //Activate
        prod1.activate();
        prod2.activate();
    }
    public static void main( String[] args ){
        new MainProdCons().configureTheSystem();
        //new MainProdCons().configureCoapSystem();
    }
}
