package ProdConsActors23;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConsumerLogic {
    private static final SimpleDateFormat sdf3 =
            new SimpleDateFormat("yyyy-MM-dd | HH:mm:ss");

    public String evalDistance(String d){
        Date date    = new Date();
        Timestamp ts = new Timestamp(date.getTime());
        String m     = "'"+sdf3.format(ts)+" | "+d+"'";
        return m;
    }
}
