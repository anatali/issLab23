package unibo.basicomm23.interfaces;

public interface IApplMessage {

    public String msgId();
    public String msgType();
    public String msgSender();
    public String msgReceiver();
    public String msgContent();
    public String msgNum();

    public boolean isDispatch();
    public boolean isRequest();
    public boolean isReply();
    public boolean isEvent();

    public void setConn( Interaction conn );
    public Interaction getConn(   );

    
}
