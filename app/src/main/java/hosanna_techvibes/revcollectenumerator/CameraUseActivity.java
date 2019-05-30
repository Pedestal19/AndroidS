package hosanna_techvibes.revcollectenumerator;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.loopj.android.http.Base64;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import hosanna_techvibes.revcollectenumerator.databases.DataDB;
import hosanna_techvibes.revcollectenumerator.databases.DataDB;
import hosanna_techvibes.revcollectenumerator.helpers.CameraPreview;
import hosanna_techvibes.revcollectenumerator.model.App;

public class CameraUseActivity extends AppCompatActivity {
    Button button_cancel;
    Button button_capture;
    Button button_success;
    App app;
    byte[] picData;
    DataDB dataDB = new DataDB();

    private Camera mCamera;
    private CameraPreview mCameraPreview;

    private static final String TAG = "VideoCamera";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_use);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        app = ((App)getApplication());

        //get the buttons
        button_cancel = (Button)findViewById(R.id.button_cancel);
        button_capture = (Button) findViewById(R.id.button_capture);
        button_success = (Button)findViewById(R.id.button_success);

        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // cameraStartPreview();
                /*app.setCameraByte(null);
                app.setSituationImage(null);
                CameraUseActivity.this.finish();
                startActivity(new Intent(getApplicationContext(),app.getActivity().getClass()));*/
                Toast.makeText(getApplicationContext(), "Please take picture to complete reg", Toast.LENGTH_LONG).show();
                //cameraStartPreview(); //start previewing camera



            }
        });
        button_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                mCamera.takePicture(null, null, mPicture);
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(), "got photo :-)", Toast.LENGTH_LONG).show();
                }
                if (mPicture == null) {
                    //Log.d("MyCameraApp", "Picture taken is null");
                    Toast.makeText(getApplicationContext(), "No Picture was taken", Toast.LENGTH_LONG).show();
                } else {

                    Toast.makeText(getApplicationContext(), "Picture taken", Toast.LENGTH_LONG).show();
                    System.out.println("Picture callback : " + mPicture);
                }

            }
        });
        button_success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(app.getActiveActivity().getClass() == AddBuilding.class) {

                    Log.e("saving building image: " ,":}}}");


                    if(picData != null && picData.length > 0)
                    {
                        Log.e("saving buildimagetodb: " ,":}");

                        ///
                       /* String base64String = Base64.encodeToString(picData, Base64.NO_WRAP);
                        byte[] decoded64String = Base64.decode(base64String, Base64.NO_WRAP);
                        Log.e("BYTELENGTH","The size in byte is " + picData.length);

                        Log.e("BASE64LENGTH","The size in base64 is " + base64String.length());

                        Bitmap bmp = BitmapFactory.decodeByteArray(decoded64String, 0, decoded64String.length);

                        ByteArrayOutputStream stream=new ByteArrayOutputStream();
                        bmp.compress(Bitmap.CompressFormat.PNG, 75, stream);
                        byte[] image=stream.toByteArray();
                        //System.out.println("byte array:"+image);
                        //final String img_str = "data:image/png;base64,"+ Base64.encodeToString(image, 0);
                        //System.out.println("string:"+img_str);
                        String img_str = android.util.Base64.encodeToString(image, 0);*/

                        ///


                        //------------------//

                        /*BitmapFactory.Options options = new BitmapFactory.Options();

                        options.inSampleSize = 2;
                        options.inPurgeable=true;
                        String base64String = Base64.encodeToString(picData, Base64.NO_WRAP);
                        byte[] decoded64String = Base64.decode(base64String, Base64.NO_WRAP);
                        Bitmap bm = BitmapFactory.decodeByteArray(decoded64String,0,decoded64String.length);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.JPEG, 60, baos); //bm is the bitmap object

                        byte[] b = baos.toByteArray();
                        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);*/
                        //-----------------//

                        // TODO: 05/07/2016 save the data to database
                        ContentValues contentValue = new ContentValues();
                        String base64String = Base64.encodeToString(picData, Base64.NO_WRAP);
                        Log.e("BYTELENGTH","The size in byte is " + picData.length);

                        Log.e("BASE64LENGTH","The size in base64 is " + base64String.length());
                        contentValue.put("img", base64String);
                        contentValue.put("building_id",app.getreg_building_building_id());
                        contentValue.put("registered_by", app.getUserid());
                        //String getDateTime = dataDB.getDateTime(getApplicationContext());
//                    contentValue.put("registered_on",String.valueOf(timer.getTime()));
                        contentValue.put("registered_on",app.getreg_building_registered_on());
                        //String servid = dataDB.myConnection(getApplicationContext()).selectColumnFromTableWithLimit("service_id","client",1);
                        //contentValue.put("service_id", app.getServiceID() != null ? app.getServiceID() : servid);
                        contentValue.put("service_id", app.getServiceID());
//                    contentValues.put("service_id", si);
//                    contentValue.put("user_id", app.getUserid());

                        //contentValue.put("authorization_id", app.getAuthorizationID() !=null && !app.getAuthorizationID().isEmpty() ? app.getAuthorizationID() : app.getLogin() + Devices.getDeviceUUID(getApplicationContext()) + Devices.getDeviceIMEI(getApplicationContext()));
                        //Toast.makeText(getApplicationContext(), "trying to save",Toast.LENGTH_LONG).show();
                        long insert_emp = dataDB.myConnection(getApplicationContext()).onInsertOrUpdate(contentValue, "building_images");
                        if (insert_emp > 0) {
                            Log.e("saving +++++: " ,":}");

                            //app.setCameraByte(picData);
                            Toast.makeText(getApplicationContext(), "Picture saved successfully",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), BuildingRegComplete.class));

                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Error saving picture. Please try again",Toast.LENGTH_LONG).show();
                        }

                    }
                    else {
                        Toast.makeText(getApplicationContext(), "please capture first",Toast.LENGTH_LONG).show();
                    }


                }
                if(app.getActiveActivity().getClass() == AddBusiness.class) {

                    if(picData != null && picData.length > 0)
                    {
                        // TODO: 05/07/2016 save the data to database
                        ContentValues contentValue = new ContentValues();
                        String base64String = Base64.encodeToString(picData, Base64.NO_WRAP);
                        contentValue.put("img", base64String);
                        contentValue.put("business_id",app.getBID());
                        contentValue.put("registered_by", app.getUserid());
                        //String getDateTime = dataDB.getDateTime(getApplicationContext());
//                    contentValue.put("registered_on",String.valueOf(timer.getTime()));
                        contentValue.put("registered_on",app.getreg_building_registered_on());
                        //String servid = dataDB.myConnection(getApplicationContext()).selectColumnFromTableWithLimit("service_id","client",1);
                        //contentValue.put("service_id", app.getServiceID() != null ? app.getServiceID() : servid);
                        contentValue.put("service_id", app.getServiceID());
//                    contentValues.put("service_id", si);
//                    contentValue.put("user_id", app.getUserid());

                        //contentValue.put("authorization_id", app.getAuthorizationID() !=null && !app.getAuthorizationID().isEmpty() ? app.getAuthorizationID() : app.getLogin() + Devices.getDeviceUUID(getApplicationContext()) + Devices.getDeviceIMEI(getApplicationContext()));
                        //Toast.makeText(getApplicationContext(), "trying to save",Toast.LENGTH_LONG).show();
                        long insert_emp = dataDB.myConnection(getApplicationContext()).onInsertOrUpdate(contentValue, "business_images");
                        if (insert_emp > 0) {
                            //app.setCameraByte(picData);
                            Toast.makeText(getApplicationContext(), "Picture saved successfully",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), BusinessComplete.class));
                            app.setActiveActivity(null);

                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Error saving picture. Please try again",Toast.LENGTH_LONG).show();
                        }

                    }
                    else {
                        Toast.makeText(getApplicationContext(), "please capture first",Toast.LENGTH_LONG).show();
                    }


                    //app.setCameraByte(data);
                    //saveBusiness();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Class not set", Toast.LENGTH_SHORT).show();
                }


            }
        });

        cameraStartPreview(); //start previewing camera


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void cameraStartPreview()
    {
        mCamera = getCameraInstance();
        setCameraDisplayOrientation(mCamera); //set camera display orientation
        mCameraPreview = new CameraPreview(this,mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mCameraPreview);
    }

    public void onResume(){
        super.onResume();

        if(mCamera ==null){

            setContentView(R.layout.activity_camera_use);
            mCamera = getCameraInstance();
            setCameraDisplayOrientation(mCamera); //set camera display orientation
            mCameraPreview = new CameraPreview(this,mCamera);
            FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
            preview.addView(mCameraPreview);

        }

    }

    private Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();

            /******/
            Camera.Parameters parameters = camera.getParameters();
            List<Camera.Size> sizes = parameters.getSupportedPictureSizes();
            Camera.Size mSize = null;
            for (Camera.Size size : sizes) {
                //Log.i(TAG, "Available resolution: "+size.width+" "+size.height);
                mSize = size;
            }
            //Log.i(TAG, "Chosen resolution: "+ mSize.width+" "+ mSize.height);
            parameters.setPictureSize(320, 240); //320 240

            //Check if device support autoflash
            List<String> flashModes = parameters.getSupportedFlashModes();
            if(flashModes.contains(Camera.Parameters.FLASH_MODE_AUTO))
            {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
            }
            camera.setParameters(parameters);

            /**********/
        } catch (Exception e) {
            // cannot get camera or does not exist
        }
        return camera;
    }

    // Called when shutter is opened
    Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {
        }
    };

    // Handles data for jpeg picture
    /*Camera.PictureCallback jpegCallback = new PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {

            SQLiteDatabase db = myDBHelper.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put("image", data);
            db.insert("storedImages", "tag", values);
            preview.camera.startPreview();
        }
    };*/

    // Handles data for raw picture
    Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            //picData = data;

            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            if(bitmap==null){
                Toast.makeText(getApplicationContext(), "not taken", Toast.LENGTH_SHORT).show();
                app.setCameraByte(data);
            }
            else
            {
                Toast.makeText(getApplicationContext(), "taken", Toast.LENGTH_SHORT).show();
                picData = data;

                /*if(app.getActivity().getClass() == BuildingEnumerationActivity.class) {
                    //app.setCameraByte(data);
                    saveBuilding();
                }
                if(app.getActivity().getClass() == BusinessEnumerationActivity.class) {
                    //app.setCameraByte(data);
                    saveBusiness();
                }*/
               /* if(app.getActivity().getClass() == iReportActivity.class){
                    app.setSituationImage(data);
                }*/
            }



            //removed
            /*File pictureFile = getOutputMediaFile();
            if (pictureFile == null) {
                //Log.d("MyCameraApp", "Picture taken is null");
                Toast.makeText(getApplicationContext(),"PicData is null", Toast.LENGTH_LONG).show();
                return;
            }*/
            //SoundPool soundPool = new SoundPool(1, AudioManager.STREAM_NOTIFICATION, 0);
            //int shutterSound = soundPool.load(this, R.raw.camera_click, 0);
            //soundPool.play(shutterSound, 1f, 1f, 0, 0, 1);

            //removed
            /*System.out.println("Text [Byte Format] : " + data);
            Log.d(TAG, "Picture data:" + data.toString());
            Toast.makeText(getApplicationContext(),"Picture taken successfully", Toast.LENGTH_LONG).show();*/

            /*try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                Log.d("MyCameraApp", "Picture taken:" + data.toString());
                fos.close();
            } catch (FileNotFoundException e) {

            } catch (IOException e) {
            }*/
        }


    };

    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "MyCameraApp");
        Log.d("MyCameraApp", "success in create directory");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }

    private int getBackFacingCameraId() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {

                cameraId = i;
                break;
            }
        }
        return cameraId;
    }
    public void setCameraDisplayOrientation(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();

        Camera.CameraInfo camInfo =
                new Camera.CameraInfo();
        Camera.getCameraInfo(getBackFacingCameraId(), camInfo);


        Display display = ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (camInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (camInfo.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (camInfo.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }


/*
    void saveBuilding(byte[] data){

        // TODO: 23/09/2015 Process saving form data to database
        ContentValues contentValue = new ContentValues();
        //long numOfRec = dataDB.myConnection(getApplicationContext()).countRecords("_buildings") + 1;
        String buildin_id = app.getActiveStreet()+"/"+count_rand;
//                            contentValue.put("building_id",buildin_id);
        contentValue.put("building_id",app.getreg_building_building_id());
        app.setreg_building_building_id(buildin_id);

        contentValue.put("building_name", app.getreg_building_building_name());
        app.setreg_building_building_name(selected_buildingtypeName);
        contentValue.put("building_no", app.getreg_building_building_no());
        app.setreg_building_building_no(_buildingNo);
        Log.e("Byte Encode", "encode format: " + _buildingImage);
        //MessageDigest md = MessageDigest.getInstance()
        //String baseHex = GenericHelper.byteArrayToHexString(_buildingImage);
        //Log.e("Hex Encode","encode format: " + baseHex);
        //base64String = Base64.encodeToString(_buildingImage, Base64.DEFAULT);
        //Log.e("Base64 Encode","encode format: " + base64String.toString());
        String base64String = Base64.encodeToString(data, Base64.NO_WRAP);
        Log.e("BASE64LENGTH","The size in base64 is " + base64String.length());
        contentValue.put("building_image", base64String);
        contentValue.put("building_category_id", app.getreg_building_building_category_id());
        app.setreg_building_building_category_id(Integer.toString(mybid));
        contentValue.put("street_id", app.getreg_building_street_id());
        app.setreg_building_street_id(Integer.toString(app.getActiveStreet()));
        String s = "SELECT service_id,email FROM client_table LIMIT 1;";
        Cursor cu = dataDB.myConnection(getApplicationContext()).selectAllFromTable(s, true);
        String s_id=""; String myemail = "";
        if(cu.getCount() > 0)
        {
            if (cu.moveToFirst()) {
                CurrentUserDetail userD = new CurrentUserDetail();
                do {
                    s_id = cu.getString(0);
                    myemail = cu.getString(1);
                }while (cu.moveToNext());
            }

        }


        contentValue.put("service_id", app.getreg_building_service_id());
        app.setreg_building_service_id(app.getServiceID() != null ? String.valueOf(app.getServiceID()) : s_id);
        contentValue.put("authorization_id", app.getreg_building_authorization_id());
        app.setreg_building_authorization_id(app.getAuthorizationID()!=null && !app.getAuthorizationID().isEmpty() ? app.getAuthorizationID() : myemail + Devices.getDeviceUUID(getApplicationContext()) + Devices.getDeviceIMEI(getApplicationContext()));

        contentValue.put("latitude",app.getreg_building_latitude());
        app.setreg_building_latitude(_latitude);
        contentValue.put("longitude",app.getreg_building_longitude());
        app.setreg_building_longitude(_longitude);
        contentValue.put("registered_by",app.getUserid());
        app.setreg_building_registered_by(app.getUserid());
        contentValue.put("registered_on", app.getreg_building_registered_on());
        app.setreg_building_registered_on(String.valueOf(timer.getTime()));
        contentValue.put("building_owner_name",app.getreg_building_building_owner_name());
        app.setreg_building_building_owner_name(_buildingOwnerName);
        contentValue.put("building_owner_phone",_buildingOwnerPhone);
        app.setreg_building_building_owner_phone(_buildingOwnerPhone);
        contentValue.put("building_owner_email", _buildingOwnerEmail);
        app.setreg_building_building_owner_email(_buildingOwnerEmail);
        long enum_building = dataDB.myConnection(getApplicationContext()).onInsert(contentValue, "_buildings");
        if (enum_building > 0) {
            Log.e("building_id","::inserted::" + buildin_id);


            app.setActiveBuildingName(_houseName);
            app.setActiveBuildingNo(_buildingNo);
            app.setActiveBuilding(buildin_id);
            app.setActiveBuildingCategoryId(String.valueOf(selected_buildingCategoryId));
            Intent intent = new Intent(getApplicationContext(), BuildingEnumerationSuccessActivity.class);
            intent.putExtra("addedBuilding", String.valueOf(enum_building));
            //_buildingImage.notify();
            app.setCameraByte(null);
            BuildingEnumerationActivity.this.finish();
            startActivity(intent);
        }


    }
*/

    void saveBusiness(){}

}
