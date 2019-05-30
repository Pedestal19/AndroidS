package hosanna_techvibes.revcollectenumerator.MyLogin;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.ImageViewTarget;

import java.util.Calendar;
import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import hosanna_techvibes.revcollectenumerator.Broadcaster.MyReceiver;
import hosanna_techvibes.revcollectenumerator.R;

public class LoginActivity extends AppCompatActivity {

    public static int identifier=1;
    @BindViews(value = {R.id.logo,R.id.first,R.id.second, R.id.last})
    protected List<ImageView> sharedElements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED||ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED||ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED||ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED||ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED||ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_PRIVILEGED) != PackageManager.PERMISSION_GRANTED||ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            btnTakePhoto.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN,Manifest.permission.BLUETOOTH_PRIVILEGED,Manifest.permission.READ_PHONE_STATE }, 0);
        }

        //

        ButterKnife.bind(this);
        final AnimatedViewPager pager= ButterKnife.findById(this,R.id.pager);
        final ImageView background=ButterKnife.findById(this,R.id.scrolling_background);
        int[] screenSize=screenSize();

        for(ImageView element:sharedElements){
            @ColorRes int color=element.getId()!=R.id.logo?R.color.white_transparent:R.color.color_logo_log_in;
            DrawableCompat.setTint(element.getDrawable(), ContextCompat.getColor(this,color));
        }
        //load a very big image and resize it, so it fits our needs
        Glide.with(this)
                .load(R.drawable.h)
                .asBitmap()
                .override(screenSize[0]*2,screenSize[1])
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(new ImageViewTarget<Bitmap>(background) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        background.setImageBitmap(resource);
                        background.post(()->{
                            //we need to scroll to the very left edge of the image
                            //fire the scale animation
                            background.scrollTo(-background.getWidth()/2,0);
                            ObjectAnimator xAnimator= ObjectAnimator.ofFloat(background, View.SCALE_X,4f,background.getScaleX());
                            ObjectAnimator yAnimator= ObjectAnimator.ofFloat(background, View.SCALE_Y,4f,background.getScaleY());
                            AnimatorSet set=new AnimatorSet();
                            set.playTogether(xAnimator,yAnimator);
                            set.setDuration(getResources().getInteger(R.integer.duration));
                            set.start();
                        });
                        pager.post(()->{
                            AuthAdapter adapter = new AuthAdapter(getSupportFragmentManager(), pager, background, sharedElements);
                            pager.setAdapter(adapter);
                        });
                    }
                });

        // BroadCase Receiver Intent Object
        Intent alarmIntent = new Intent(getApplicationContext(), MyReceiver.class);
        Log.e("StArTiNg YoUr SeRvIcE", "UsInG AlArM ::-))");

        // Pending Intent Object
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Alarm Manager Object
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        // Alarm Manager calls BroadCast for every Ten seconds (10 * 1000), BroadCase further calls service to check if new records are inserted in
        // Remote MySQL DB
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + 5000, 10 * 1000000, pendingIntent);
        Log.e("sErViCe StArTeD", " :-) WiTh AlArM :-)");





    }

    private int[] screenSize(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return new int[]{size.x,size.y};
    }

    @Override
    public void onBackPressed() {
        // disable going back and allow user to choose to quite app
        //super.onBackPressed();
        //Display alert message when back button has been pressed
        backButtonHandler();
        return;
        //moveTaskToBack(true);

    }

    //back button overide method
    public void backButtonHandler() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                LoginActivity.this);
        // Setting Dialog Title
        alertDialog.setTitle("Quit?");
        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want to quit this application?");
        // Setting Icon to Dialog
        alertDialog.setIcon(R.mipmap.ic_launcher);
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //System.exit(0);
                        //  finish();
                        // Runtime.getRuntime().exit(0);
                        //code from akpos on the 5th of jan 2017
                        finishAffinity();
                    }
                });
        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        dialog.cancel();
                    }
                });
        // Showing Alert Message
        alertDialog.show();
    }
}
