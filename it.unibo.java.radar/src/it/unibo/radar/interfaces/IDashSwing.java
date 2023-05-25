package it.unibo.radar.interfaces;
import java.awt.Component;

public interface IDashSwing {
 	public void addComponent(Component component, int rowIdx, int colIdx,
		      int rowCount, int colCount, double horizontalWeight, double verticalWeight);
}
