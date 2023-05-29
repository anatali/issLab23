package unibo.basicomm23.interfaces;

//Observer deprecato in Java9
//https://stackoverflow.com/questions/11619680/why-should-the-observer-pattern-be-deprecated/11632412#11632412
import java.util.Observer;

public interface IObserver extends Observer{
	public void update( String value );
	//From Observer: public void update(Observable o, Object news)
}
