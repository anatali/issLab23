package unibo.basicomm23.serial;

 
import jssc.SerialPort;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.Interaction;

public class SerialConnection implements Interaction{

	public SerialConnection(SerialPort port) {
		
	}

	@Override
	public void forward(String msg) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String request(String msg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reply(String reqid) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String receiveMsg() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void forward(IApplMessage msg) throws Exception {

	}

	@Override
	public IApplMessage request(IApplMessage msg) throws Exception {
		return null;
	}

	@Override
	public IApplMessage request(IApplMessage msg, int tout) throws Exception {
		return null;
	}

	@Override
	public void reply(IApplMessage msg) throws Exception {

	}

	@Override
	public IApplMessage receive() throws Exception {
		return null;
	}
}
