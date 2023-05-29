package temp;

import unibo.basicomm23.utils.CommUtils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class MainListener implements PropertyChangeListener {
    private ClassWithProperty test;

    public MainListener() {
        test = new ClassWithProperty();
        //test.addPropertyChangeListener(this);
        initializeListeners();
        test.setupOnlineUsers();
    }

    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println(test.getUsersOnline());
    }
//https://stackoverflow.com/questions/4987476/java-propertychangelistener
    private void initializeListeners() {
        test.getPropertyChangeSupport().addPropertyChangeListener(
                (PropertyChangeEvent event) -> {
                    CommUtils.outyellow(event.getPropertyName().toString());
                    if (event.getPropertyName().equals("usersOnline"))   {
                        String passedEventData =  event.getNewValue().toString();  //getNewData() method undefined
                        CommUtils.outgreen(passedEventData);
                    }
        });
    }
    public static void main(String[] args) {
        new MainListener(); // do everything in the constructor
    }
}