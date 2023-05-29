package temp;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ClassWithProperty {
    private PropertyChangeSupport changes = new PropertyChangeSupport(this);
    private int usersOnline = 0;

    public ClassWithProperty() {
    }

    public void setupOnlineUsers() {
        while (usersOnline < 10) {
            changes.firePropertyChange("usersOnline", usersOnline, ++usersOnline);
        }
    }

    public int getUsersOnline() {
        return usersOnline;
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        changes.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        changes.removePropertyChangeListener(l);
    }
    
    public PropertyChangeSupport getPropertyChangeSupport() {
        return changes;
    }
}
