package hosanna_techvibes.revcollectenumerator.helpers;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UniqueID {

    private Date date;
    private String uniqueID;

    public UniqueID() {
        date = new Date();
    }

    public String getUniqueID(){

        uniqueID = new SimpleDateFormat("yy").format(date)+
                new SimpleDateFormat("MM").format(date)+
                new SimpleDateFormat("dd").format(date)+
                new SimpleDateFormat("HH").format(date)+
                new SimpleDateFormat("mm").format(date)+
                new SimpleDateFormat("ss").format(date)+
                new SimpleDateFormat("SSS").format(date);

        return uniqueID;
    }

}
