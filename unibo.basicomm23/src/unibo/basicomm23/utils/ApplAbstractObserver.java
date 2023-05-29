package unibo.basicomm23.utils;

import java.util.Observable;
import unibo.basicomm23.interfaces.IObserver;

public abstract  class ApplAbstractObserver implements IObserver{

	@Override
	public void update(Observable o, Object arg) {
		//CommUtils.outyellow("ApplAbstractObserver | update/2: "+arg);
		update(arg.toString());		
	}

	@Override
	public abstract void update(String value);

}
