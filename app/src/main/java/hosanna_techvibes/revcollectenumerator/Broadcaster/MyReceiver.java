package hosanna_techvibes.revcollectenumerator.Broadcaster;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import hosanna_techvibes.revcollectenumerator.helpers.GenericHelpers;
import hosanna_techvibes.revcollectenumerator.services.MyService;


public class MyReceiver extends BroadcastReceiver {

    public MyReceiver() {
        super();
    }

    static int countRunningService;
    private static final String TAG = "broadcast receiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "This is response " + countRunningService);
        countRunningService++;
        Log.e(TAG, "Service Running for " + countRunningService + " times now");


        //check if device is connected to the internet
        if(GenericHelpers.isOnline(context))
        {
//            Toast.makeText(context, "Device Online :-)", Toast.LENGTH_SHORT).show();
            try {
                final Intent intnt = new Intent(context, MyService.class);
                intnt.putExtra("intntdata", "Connected ");
                context.startService(intnt);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
            Toast.makeText(context, "Device offline :-(", Toast.LENGTH_SHORT).show();
        }
    }
}
