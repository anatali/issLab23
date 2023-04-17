package unibo.actors23.fsm;
import unibo.basicomm23.interfaces.IApplMessage;

public interface StateActionFun {
	void run(IApplMessage msg);
}
