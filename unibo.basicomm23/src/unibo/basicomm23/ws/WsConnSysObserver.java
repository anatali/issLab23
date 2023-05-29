package unibo.basicomm23.ws;

import java.util.Observable;
import unibo.basicomm23.interfaces.IObserver;
import unibo.basicomm23.utils.ColorsOut;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.SystemTimer;
  

public  class WsConnSysObserver implements IObserver{
	protected WsConnection wsconn;

	protected String curReceivedMsg = "nomsgyet";
	protected String moveResult     = null;

	public WsConnSysObserver( WsConnection wsconn ) {
		this.wsconn = wsconn;
	}
	
	//public void startMoveTime() { //see sendALine di WsConnection timer.startTime();}

	@Override
	public void update(Observable source, Object data) {
		//timer.stopTime();
		//actionDuration = ""+timer.getDuration();
		update(data.toString() );
	}
	@Override
	public  void update(String data){
		CommUtils.outblue("WsConnSysObserver update/1 receives:" + data  );
		curReceivedMsg = data;
	}

	public String getReceivedMsg() throws Exception{
		moveResult = null;
		synchronized(this) {
			while( moveResult == null ) { wait(); }
			//if( msg.contains("turn") ) CommUtils.delay(200); //per evitare unexpected not allowed (???)
			//CommUtils.outgreen("VrobotHLMovesWS | "+ msg + " result=" + moveResult);
			return moveResult;
		}		//return curReceivedMsg;
	}

	public String dorequest(String msg) throws Exception {
		moveResult = null;
		//Invio fire-and.forget e attendo modifica di  moveResult da update
		//startTimer();
		//CommUtils.outgreen("VrobotHLMovesWS | request " + msg);
		wsconn.forward(msg);
		synchronized(this) {
			while( moveResult == null ) { wait(); }
			//if( msg.contains("turn") ) CommUtils.delay(200); //per evitare unexpected not allowed (???)
			//CommUtils.outgreen("VrobotHLMovesWS | "+ msg + " result=" + moveResult);
			return moveResult;
		}
	}
}
