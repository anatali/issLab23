package ProdConsAsynch;

import ProducerConsumer.ProdConsConfig;
import ProducerConsumer.ProducerCaller;
import ProducerConsumer.ProducerLogic;

public class ProducerAsynch {
    private ProducerCallerAsynch prodCaller;

    public ProducerAsynch(String name){

        ProducerLogic prodLogic = new ProducerLogic();
        prodCaller = new ProducerCallerAsynch(name, prodLogic,
                ProdConsConfig.protocol, ProdConsConfig.hostAddr, ProdConsConfig.entry);
    }

    public void start(){
        prodCaller.activate();
    }
}
