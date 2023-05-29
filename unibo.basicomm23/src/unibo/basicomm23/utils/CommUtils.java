package unibo.basicomm23.utils;
import org.json.simple.JSONObject;


import org.json.simple.parser.JSONParser;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.msg.ApplMessage;
import unibo.basicomm23.msg.ApplMessageType;
import unibo.basicomm23.msg.ProtocolType;

public class CommUtils {
	private static JSONParser simpleparser = new JSONParser();
 	
	public static boolean isCoap() {
		return CommSystemConfig.protcolType==ProtocolType.coap ;
	}
	public static boolean isMqtt() {
		return CommSystemConfig.protcolType==ProtocolType.mqtt ;
	}
	public static boolean isTcp() {
		return CommSystemConfig.protcolType==ProtocolType.tcp ;
	}
	
	public static String getContent( String msg ) {
		String result = "";
		try {
			ApplMessage m = new ApplMessage(msg);
			result        = m.msgContent();
		}catch( Exception e) {
			result = msg;
		}
		return result;	
	}
	
	public static JSONObject parseForJson(String message) {
	   try {
 	       JSONObject jsonObj = (JSONObject) simpleparser.parse(message);
	       return jsonObj;
	   } catch (Exception e) {
	       //CommUtils.outred("CommUtils | parseJson ERROR:"+e.getMessage());
	       return null;
	   }
	}

	
	//String MSGID, String MSGTYPE, String SENDER, String RECEIVER, String CONTENT, String SEQNUM
	private static int msgNum=0;	

	public static IApplMessage buildDispatch(String sender, String msgId, String payload, String dest) {
		try {
			return new ApplMessage(msgId, ApplMessageType.dispatch.toString(),sender,dest,payload,""+(msgNum++));
		} catch (Exception e) {
			ColorsOut.outerr("buildDispatch ERROR:"+ e.getMessage());
			return null;
		}
	}
	
	public static IApplMessage buildRequest(String sender, String msgId, String payload, String dest) {
		try {
			return new ApplMessage(msgId, ApplMessageType.request.toString(),sender,dest,payload,""+(msgNum++));
		} catch (Exception e) {
			ColorsOut.outerr("buildRequest ERROR:"+ e.getMessage());
			return null;
		}
	}
	public static IApplMessage buildReply(String sender, String msgId, String payload, String dest) {
		try {
			return new ApplMessage(msgId, ApplMessageType.reply.toString(),sender,dest,payload,""+(msgNum++));
		} catch (Exception e) {
			ColorsOut.outerr("buildRequest ERROR:"+ e.getMessage());
			return null;
		}
	}	
	public static IApplMessage prepareReply(IApplMessage requestMsg, String answer) {
		String sender  = requestMsg.msgSender();
		String receiver= requestMsg.msgReceiver();
		String reqId   = requestMsg.msgId();
		IApplMessage reply = null;
		if( requestMsg.isRequest() ) { //DEFENSIVE
			//The msgId of the reply must be the id of the request !!!!
 			reply = buildReply(receiver, reqId, answer, sender) ;
		}else { 
			ColorsOut.outerr( "Utils | prepareReply ERROR: message not a request");
		}
		return reply;
    }


	public static IApplMessage buildEvent( String emitter, String msgId, String payload ) {
		try {
			return new ApplMessage(msgId, ApplMessageType.event.toString(),emitter,"ANY",payload,""+(msgNum++));
		} catch (Exception e) {
			ColorsOut.outerr("buildEvent ERROR:"+ e.getMessage());
			return null;
		}
	}
	public static void outyellow( String msg ) { ColorsOut.outappl(msg, ColorsOut.YELLOW); }
	public static void  outgreen( String msg ) { ColorsOut.outappl(msg, ColorsOut.GREEN); }
	public static void  outblue( String msg ) {  ColorsOut.outappl(msg, ColorsOut.BLUE);  }
	public static void  outred(String msg) { ColorsOut.outappl(msg, ColorsOut.RED) ; }
	public static void  outblack(String msg) { ColorsOut.outappl(msg, ColorsOut.BLACK) ; }
	public static void  outcyan(String msg) { ColorsOut.outappl(msg, ColorsOut.CYAN) ; }
	public static void  outmagenta(String msg) {  ColorsOut.outappl(msg, ColorsOut.MAGENTA); }
	public static void  outgray(String msg) { ColorsOut.outappl(msg, ColorsOut.GRAY) ; }

	public static void delay( int dt ) {
		try {
			Thread.sleep(dt);
		} catch (InterruptedException e) {
				e.printStackTrace();
		}		
	} 
	
	public static void aboutThreads(String msg)   { 
		String tname    = Thread.currentThread().getName();
		String nThreads = ""+Thread.activeCount() ;
		outcyan( msg + " curthread=T n=N".replace("T", tname).replace("N", nThreads) );
	}

	public static void forwardOnInterconn( Interaction conn,String cmd )  {
		//CommUtils.outblue( name+"  | forwardOnInterconn " + cmd + " conn=" + conn );
		try {
			conn.forward(cmd);
		} catch (Exception e) {
			CommUtils.outred(  "CommUtils | forwardOnInterconn ERROR=" + e.getMessage()  );
		}
	}
	public static void replyOnInterconn( Interaction conn,String cmd )  {
		//CommUtils.outblue( name+"  | replyOnInterconn " + cmd + " conn=" + conn );
		try {
			conn.reply(cmd);
		} catch (Exception e) {
			CommUtils.outred(  "CommUtils | replyOnInterconn ERROR=" + e.getMessage()  );
		}
	}
	public static String requestSynchOnInterconn(Interaction conn, String request )  {
		//CommUtils.outblue( name+"  | requestOnInterconn request=" + request + " conn=" + conn );
		try {
			String answer = conn.request(request);
			//CommUtils.outblue( name+"  | requestOnInterconn-answer=" + answer  );
			return  answer  ;
		} catch (Exception e) {
			CommUtils.outred(  "CommUtils  | requestOnInterconn ERROR=" + e.getMessage()  );
			return null;
		}
	}
	public static void waitTheUser(String msg) {
		try {
			outblue(msg );
			int v = System.in.read();
			System.in.read(); //discard CR
			//outblue("CommUtils v="+v);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void beep(){
		java.awt.Toolkit.getDefaultToolkit().beep();
		//System.out.print("\007");// ASCII bell
		// System.out.flush();
	}

	public static String convertToSend(String s){
		return "'"+s.replace("\n","@!@")+"'";
	}
	public static String restoreFromConvertToSend(String s){
		return s.replace("@!@","\n");
	}
}
