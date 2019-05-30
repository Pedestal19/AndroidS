package hosanna_techvibes.revcollectenumerator.services;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;

import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import hosanna_techvibes.revcollectenumerator.Listener.LocationListener;
import hosanna_techvibes.revcollectenumerator.MainActivity;
import hosanna_techvibes.revcollectenumerator.MyLogin.LoginActivity;
import hosanna_techvibes.revcollectenumerator.R;
import hosanna_techvibes.revcollectenumerator.databases.DBCheckAuthorization;
import hosanna_techvibes.revcollectenumerator.databases.DataDB;
import hosanna_techvibes.revcollectenumerator.helpers.Devices;
import hosanna_techvibes.revcollectenumerator.helpers.GenericHelpers;
import hosanna_techvibes.revcollectenumerator.helpers.LocationMgr;
import hosanna_techvibes.revcollectenumerator.helpers.MyJSON;
import hosanna_techvibes.revcollectenumerator.helpers.ReInitializeDB;
import hosanna_techvibes.revcollectenumerator.helpers.ServiceHandler;
import hosanna_techvibes.revcollectenumerator.helpers.Push;
import hosanna_techvibes.revcollectenumerator.model.App;
import hosanna_techvibes.revcollectenumerator.model.AuthorizationHttpResponse;

/**
 * Created by Hosanna_TechVibes on 06-Feb-17.
 */

public class MyService extends Service {

  String latitude = "Unknown", longitude = "Unknown", email, appK, deviceimei, accessCode, service_code, errorMssg, client_name, client_userid, client_taxid, service_id, authorization_code, isFirstSync, add_request_url, request_date, urlParameters;
  App app;
  int authorized;
  AuthorizationHttpResponse authResp;
  private static final String TAG = "AuthorizationService";
  private LocationManager mLocationManager = null;
  private static final int LOCATION_INTERVAL = 1000;
  private static final float LOCATION_DISTANCE = 10f;
  // if GPS is enabled
  boolean isGPSEnabled = false;
  // if Network is enabled
  boolean isNetworkEnabled = false;
  DBCheckAuthorization checkAuthorization = new DBCheckAuthorization();
  DataDB dataDB = new DataDB();

  MyJSON pull = new MyJSON();
  Push Push = new Push();
    /*MyJSON myJSON = new MyJSON();
    Push push = new Push();*/

  String SyncEmpInfoEP, SyncEmpInfoVerifyEP, SyncPenEmpInfoEP, SyncPenEmpInfoVerifyEP, PushEmpInfo, PushEnroll, PushPenEmpInfo, PushPenEnroll, PushCheckin;

  public MyService() {
    super();
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }


  @Override
  public void onCreate() {
    super.onCreate();
    app = ((App) getApplicationContext());


    // TODO: 02/10/2016 Location awareness to be instantiated
    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(getApplicationContext(), LocationManager.NETWORK_PROVIDER),
            new LocationListener(getApplicationContext(), LocationManager.GPS_PROVIDER)
    };
    initializeLocationManager();
    try {
      // Getting GPS status
      isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
      if (isNetworkEnabled) {
        mLocationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                mLocationListeners[1]);
      }


    } catch (SecurityException ex) {
      Log.i(TAG, "fail to request location update, ignore", ex);
    } catch (IllegalArgumentException ex) {
      Log.d(TAG, "network provider does not exist, " + ex.getMessage());
    }
    try {
      isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
      if (isGPSEnabled) {
        mLocationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                mLocationListeners[0]);
      }
      if (!isGPSEnabled) {
        // so asking user to open GPS
        //GenericHelper.askUserToOpenGPS(getApplicationContext());
      }

    } catch (SecurityException ex) {
      Log.i(TAG, "fail to request location update, ignore", ex);
    } catch (IllegalArgumentException ex) {
      Log.d(TAG, "gps provider does not exist " + ex.getMessage());
    }

    mTimer = new Timer();
    mTimer.schedule(timerTask, 5000, 20 * 1000); //2000, 5 * 1000 //60*1000

  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {

    try {
      Log.e("[-V-]", "My VeRiTeX iS RuNiNg:-)");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return super.onStartCommand(intent, flags, startId);
  }

  private Timer mTimer;

  TimerTask timerTask = new TimerTask() {

    @Override
    public void run() {
      if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED){

        if (GenericHelpers.toggleGPS(getApplicationContext())) {

          Log.e("Log", "My SeRvIcE iS RuNiNg:-)");
          //JSONObject buildJsonFromTable2 = pushAPI.JsonPush(getApplicationContext(), "mobile_synch_buildings");
          deviceimei = Devices.getDeviceIMEI(getApplicationContext());
          Cursor cursor = checkAuthorization.checkForAuthorizationRequest(getApplicationContext());
          if (cursor != null && cursor.getCount() > 0) {
            Log.e("Log", "Running:authorization record found:" + cursor.getCount());
            if (cursor.moveToFirst()) {
              do {
                client_name = "dummy";
                service_id = cursor.getString(12);
                appK = getString(R.string.auth);
                            /*authorization_code = cursor.getString(11);
                            if (cursor.getString(15) != null) {
                                authorized = cursor.getString(15);
                                Log.e("authorized found", cursor.getString(15));
                            }*/
                //if(cursor.getString(7) != null) {
                try {
                  email = cursor.getString(7);
                  authorized = cursor.getInt(15);
                  Log.e("email found", cursor.getString(1));
                } catch (Exception e) {
                  e.printStackTrace();
                }
                //}
                service_code = cursor.getString(11);
                add_request_url = cursor.getString(12);
                Log.e("add_request_url found", cursor.getString(12) + ";");
                //request_date = cursor.getString(8);
                Log.e("service code", cursor.getString(8) + ";");
                Log.e("service id", cursor.getString(3) + ";");

                client_userid = cursor.getString(9);
                Log.e("client user id", cursor.getString(7) + ";");
                client_taxid = cursor.getString(2);
                Log.e("client tax id", cursor.getString(2) + ";");


              } while (cursor.moveToNext());
            }

            if (authorized == 0) {
              verify(email, service_code);
            } else if (authorized == 1) {
              Sync(client_userid, service_id);
              Fire();
            }
                 /*   Cursor mycursor = dataDB.myConnection(getApplicationContext()).selectAllFromTable("users","EP_ID","1");

                    SyncEmpInfoEP = mycursor.getString(13);
                    SyncEmpInfoVerifyEP = mycursor.getString(21);
                    SyncPenEmpInfoEP = mycursor.getString(14);
                    SyncPenEmpInfoVerifyEP = mycursor.getString(22);
                    PushEmpInfo = mycursor.getString(15);
                    PushEnroll = mycursor.getString(17);
                    PushPenEmpInfo = mycursor.getString(16);
                    PushPenEnroll = mycursor.getString(18);
                    PushCheckin = mycursor.getString(19);
*/
                   /* Log.e("client tax id", SyncEmpInfoEP + ";");
                    Log.e("client tax id", SyncEmpInfoVerifyEP + ";");
                    Log.e("client tax id", SyncPenEmpInfoEP + ";");
                    Log.e("client tax id", SyncPenEmpInfoVerifyEP + ";");
                    Log.e("client tax id", PushEmpInfo + ";");
                    Log.e("client tax id", PushEnroll + ";");
                    Log.e("client tax id", PushPenEmpInfo + ";");
                    Log.e("client tax id", PushPenEnroll + ";");
                    Log.e("client tax id", PushCheckin + ";");*/

                   /* Sync(SyncEmpInfoEP,SyncEmpInfoVerifyEP,"-Pulling-", "synch_employee_info");
                    Sync(SyncPenEmpInfoEP,SyncPenEmpInfoVerifyEP,"-Pulling-", "synch_pensions_employee_info");

                    push.Sync("mobile_synch_employee_info","Employee_Info",PushEmpInfo,"mobile_synch_employee_info",service_id,client_userid,service_code,getApplicationContext());
                    push.Sync("mobile_synch_enroll","Enroll",PushEnroll,"mobile_synch_enroll",service_id,client_userid,service_code,getApplicationContext());

                    push.Sync("mobile_synch_pensions_employee_info","Pensions_Employee_Info",PushPenEmpInfo,"mobile_synch_pensions_employee_info",service_id,client_userid,service_code,getApplicationContext());
                    push.Sync("mobile_synch_pensions_enroll","Pensions_Enroll",PushPenEnroll,"mobile_synch_pensions_enroll",service_id,client_userid,service_code,getApplicationContext());

                    push.Checkin("mobile_synch_check_in","Check_In",PushCheckin,"mobile_synch_check_in",service_id,client_userid,service_code,getApplicationContext());
                    push.Checkinn("mobile_synch_check_in","Check_In",getString(R.string.PushCheckinnn),"mobile_synch_check_in",service_id,client_userid,service_code,getApplicationContext());
                   // push.nor("users","users",getString(R.string.PushCheckinn),"users",service_id,client_userid,service_code,getApplicationContext());
*/
          } else {
            Log.e("Log", "Running:No authorization matching record found");
          }
          cursor.close();
        }
        else {
          Log.e("Log", "Running No GPS");
        }
      }
    else {
        Log.e("service", " Permission not granted ");
//          ActivityCompat.requestPermissions(MyService.getApplicationContext(), new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN,Manifest.permission.BLUETOOTH_PRIVILEGED,Manifest.permission.READ_PHONE_STATE }, 0);

      }



    }
  };

  void Fire(){
    Push.Fire("mobile_synch_apartments", "mobile_synch_apartments", getString(R.string.mobilesync), "mobile_synch_apartments", service_id, client_userid, service_code, getApplicationContext());
    Push.Fire("mobile_synch_apartments_occupants", "mobile_synch_apartments_occupants", getString(R.string.mobilesync), "mobile_synch_apartments_occupants", service_id, client_userid, service_code, getApplicationContext());
    Push.Fire("mobile_synch_buildings", "mobile_synch_buildings", getString(R.string.mobilesync), "mobile_synch_buildings", service_id, client_userid, service_code, getApplicationContext());
    Push.Fire("mobile_synch_businesses", "mobile_synch_businesses", getString(R.string.mobilesync), "mobile_synch_businesses", service_id, client_userid, service_code, getApplicationContext());
    Push.Fire("mobile_synch_lands", "mobile_synch_lands", getString(R.string.mobilesync), "mobile_synch_lands", service_id, client_userid, service_code, getApplicationContext());
    Push.Fire("mobile_synch_tax_payers", "mobile_synch_tax_payers", getString(R.string.mobilesync), "mobile_synch_tax_payers", service_id, client_userid, service_code, getApplicationContext());
    Push.Fire("mobile_synch_taxpayer_items", "mobile_synch_taxpayer_items", getString(R.string.mobilesync), "mobile_synch_taxpayer_items", service_id, client_userid, service_code, getApplicationContext());


    SyncImages("building_images", "building_images", (getString(R.string.syncbuildingimages)),"building_images", "building_id");
    SyncImages("business_images", "business_images", (getString(R.string.syncbusinessimages)),"business_images", "business_id");

        /*Push.Fire("building_images", "building_images", getString(R.string.mobilesync), "building_images", service_id, client_userid, service_code, getApplicationContext());
        Push.Fire("business_images", "business_images", getString(R.string.mobilesync), "business_images", service_id, client_userid, service_code, getApplicationContext());
*/
  }
  private void SyncImages(String whatToSync, String jsonResultName, String url, String mobileSynchTableToUpdate, String imageid){

    Log.e("Pushing....}}} ", "->"+whatToSync);


    String myPath = "/data/data/hosanna_techvibes.revcollectenumerator/databases/" + "revcollect_new.db";// Set path to your database
    SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);


//        String _tableName = "mobile_synch_tax_registration";
    String _tableName = whatToSync;

//        Cursor cursor = dataDB.myConnection(getApplicationContext()).ms(_tableName);
    Cursor cursor = myDataBase.rawQuery("SELECT * FROM " + _tableName + " WHERE back_up=0 AND service_id='"+service_id+"' LIMIT 3;", null);

    JSONArray resultSetArray 	= new JSONArray();
    JSONObject returnJSON 	= new JSONObject();

    cursor.moveToFirst();
    while (cursor.isAfterLast() == false) {

      int totalColumn = cursor.getColumnCount();
      JSONObject rowObject = new JSONObject();

      for( int i=0 ;  i< totalColumn ; i++ )
      {
        if( cursor.getColumnName(i) != null )
        {

          try
          {

            if( cursor.getString(i) != null )
            {
              Log.d(TAG, cursor.getString(i) );
              rowObject.put(cursor.getColumnName(i) ,  cursor.getString(i) );
            }
            else
            {
              rowObject.put( cursor.getColumnName(i) ,  "null" );
            }
          }
          catch( Exception e )
          {
            Log.d(TAG, e.getMessage()  );
          }
        }

      }
      cursor.moveToNext();

      resultSetArray.put(rowObject);

    }

    try
    {
      returnJSON.put("Email", email);
      returnJSON.put("ServiceCode", service_code);
      returnJSON.put("UserID", client_userid);
      returnJSON.put(jsonResultName, resultSetArray);
    }
    catch( Exception e )
    {
      Log.d(TAG, e.getMessage()  );
    }
    //Log.e("TAG_NAME", resultSetArray.toString() );
    Log.e(TAG, returnJSON.toString() );


    cursor.close();

    //return resultSet;


    ServiceHandler serviceHandler = new ServiceHandler();
    AuthorizationHttpResponse httpResp = serviceHandler.makeServiceCallJSON(url, 2, returnJSON);


    if (httpResp != null && httpResp.getResponseData() != null) {


      Log.e(TAG, "trying to sync "+whatToSync+ ":::this is the response " + httpResp.getResponseCode() + "::" + httpResp.getResponseData());
      errorMssg = httpResp.getResponseData();
      //Toast.makeText(getApplicationContext(), "Something wrong", Toast.LENGTH_SHORT).show();
      String respcode = String.valueOf(httpResp.getResponseCode());
      errorMssg = respcode;
      authResp = httpResp;

      try {

        JSONObject parentObject = new JSONObject(httpResp.getResponseData());
        JSONObject childObject = parentObject.getJSONObject("response");
        String SeseResponseCode = childObject.getString("responseCode");

        String SeseResponseDescription = childObject.getString("responseDescription");
        JSONArray mobileSynchArray = parentObject.getJSONArray("mobile_synch");


        for (int i = 0; i < mobileSynchArray.length(); i++) {
          JSONObject presentObject = mobileSynchArray.getJSONObject(i);


          int status = presentObject.getInt("status");
          String authorizationID = presentObject.getString(imageid);

          if(status==1) {
            //Toast.makeText(getApplicationContext(), i, Toast.LENGTH_SHORT).show();

            Log.e(TAG, " response::status: " + status);
            Log.e(TAG, " response::image id : " + authorizationID);

            ContentValues contentValues = new ContentValues();

            contentValues.put("back_up", 1);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
            Calendar timer = Calendar.getInstance();
            String formattedDate = df.format(timer.getTime());
            //contentValues.put("last_synched_date", formattedDate);
            //contentValues.put("authorization_id", authorizationID);

            Log.i("Updating ", "mobile synch table");

            long rowInserted = dataDB.myConnection(getApplicationContext()).onUpdateOrIgnore(contentValues, mobileSynchTableToUpdate,imageid, authorizationID);
            if (rowInserted != -1) {

              Log.i("MOBILE SYNCH "+mobileSynchTableToUpdate, "UPDATED ");
              //revenueDimensionsBufferedData.append(authorizationID+",");

            } else {
              Log.i("ERROR-Saving to db"+mobileSynchTableToUpdate, ":-( ");
              //errorMssg = httpResp.getResponseData();
              // Toast.makeText(getApplicationContext(), "Something wrong", Toast.LENGTH_SHORT).show();
            }
          }

        }




      } catch (JSONException e) {
        e.printStackTrace();
      }

    } else {
      Log.d("Connecting", " to service failed...to push data");
    }


  }

  void Sync(String userid, String service_id) {

    String abc;
    String SeseErrorMssg;

    try {
//      String url="http://veritextsa.com/api/v1/pushToMobile/"+userid+"/"+service_id;

//        String url="http://192.168.16.156/enumerationwebservice/public/index.php/api/v1/pushToMobile/"+userid+"/"+service_id;
        String url="http://192.168.16.156/api/v1/pushToMobile/"+userid+"/"+service_id;



//            String url = "http://google.com";

      Log.e(TAG, "This is request " + url);

      ServiceHandler serviceHandler = new ServiceHandler();


      abc = url.toString();
                    /*Cursor mycursor = dataDB.myConnection(getActivity().getApplicationContext()).selectAllFromTableAs222();
                    String EPdata="";
                    if (mycursor != null  && mycursor.moveToFirst() ) { EPdata = mycursor.getString(20);}
*/

      List<NameValuePair> params = new ArrayList<NameValuePair>();

      AuthorizationHttpResponse httpResp = serviceHandler.makeServiceCall(url, 1, params);


      if (httpResp != null && httpResp.getResponseData() != null) {
        Log.e(TAG, "This is response " + httpResp.getResponseCode() + "::" + httpResp.getResponseData());
        if (httpResp.getResponseCode() == 200) {

          // TODO: 11/10/2016 get user data from json
          JSONObject parentObject = new JSONObject(httpResp.getResponseData());
//                    JSONObject parentObject = new JSONObject("{\"response\":{\"responseCode\":\"00\",\"responseDescription\":{\"synch_apartment_types\":[{\"synch_apartment_type_id\":22,\"apartment_type_id\":1,\"apartment_type\":\"flat\",\"registered_by\":\"admin\",\"registered_on\":\"2018-08-09 11:45:33\",\"active\":1,\"synch_status\":0,\"user_id\":22,\"last_synched_date\":\"2018-08-09 15:26:00\",\"back_up\":0}],\"synch_areacodes\":[{\"idsynch_area_codes\":52,\"area_name\":\"Central 2\",\"area_code\":\"8339\",\"service_id\":\"234120010005\",\"session_id\":\"005128894937843340778577969635165268\",\"area_code_id\":30,\"synch_status\":0,\"user_id\":22,\"last_synched_date\":\"2018-08-09 15:26:00\",\"back_up\":0,\"mark_delete\":0}],\"synch_building_completions\":[{\"synch_building_completion_id\":14,\"building_completion_id\":1,\"building_completion\":\"completed\",\"synch_status\":0,\"last_synched_date\":\"2018-08-09 15:26:00\",\"authorization_id\":null,\"back_up\":0,\"user_id\":22}],\"synch_building_functions\":[{\"synch_building_function_id\":14,\"building_function_id\":1,\"building_function\":\"hotel\",\"building_purpose\":\"commercial\",\"synch_status\":0,\"last_synched_date\":\"2018-08-09 15:26:00\",\"authorization_id\":null,\"back_up\":0,\"user_id\":22}],\"synch_building_occupancies\":[{\"synch_building_occupancy_id\":14,\"building_occupancy_id\":1,\"building_occupancy\":\"rented\",\"synch_status\":0,\"last_synched_date\":\"2018-08-09 15:26:00\",\"authorization_id\":null,\"back_up\":0,\"user_id\":22}],\"synch_building_occupancy_types\":[{\"synch_building_occupancy_type_id\":14,\"building_occupancy_type_id\":1,\"building_occupancy_type\":\"family\",\"building_occupancy\":\"rented\",\"synch_status\":0,\"last_synched_date\":\"2018-08-09 15:26:00\",\"authorization_id\":null,\"back_up\":0,\"user_id\":22}],\"synch_building_ownerships\":[{\"synch_building_ownership_id\":14,\"building_ownership_id\":1,\"building_ownership\":\"private\",\"synch_status\":0,\"last_synched_date\":\"2018-08-09 15:26:00\",\"authorization_id\":null,\"back_up\":0,\"user_id\":22}],\"synch_building_purposes\":[{\"synch_building_purpose_id\":14,\"building_purpose_id\":1,\"building_purpose\":\"commercial\",\"synch_status\":0,\"last_synched_date\":\"2018-08-09 15:26:00\",\"authorization_id\":null,\"back_up\":0,\"user_id\":22}],\"synch_business_categories\":[{\"id_synch_business_category\":9,\"business_category_id\":1,\"business_category\":\"PRIVATE\",\"business_type\":\"formal\",\"service_id\":\"234120010005\",\"created_by\":\"admin\",\"created_at\":\"2018-08-09 12:55:18\",\"updated_by\":null,\"updated_at\":\"2018-11-08 13:26:21\",\"synch_status\":0,\"last_synched_date\":\"2018-08-09 15:26:00\",\"authorization_id\":null,\"back_up\":0,\"user_id\":22}],\"synch_business_operations\":[{\"id_synch_business_operation\":9,\"business_operation_id\":1,\"business_type\":\"formal\",\"business_operation\":\"financial operation\",\"service_id\":\"234120010005\",\"created_by\":\"admin\",\"created_at\":\"2018-08-09 12:56:13\",\"updated_by\":null,\"updated_at\":\"2018-11-08 13:26:09\",\"synch_status\":0,\"last_synched_date\":\"2018-08-09 15:26:00\",\"authorization_id\":null,\"back_up\":0,\"user_id\":22}],\"synch_business_sectors\":[{\"synch_business_sector_id\":9,\"business_sector_id\":1,\"business_sector\":\"LIMITED LIABILITY\",\"business_type\":\"formal\",\"business_category\":\"private\",\"service_id\":\"234120010005\",\"created_by\":\"admin\",\"created_at\":\"2018-08-09 12:57:01\",\"updated_by\":null,\"updated_at\":\"2018-11-08 13:26:29\",\"synch_status\":0,\"last_synched_date\":\"2018-08-09 15:26:00\",\"authorization_id\":null,\"back_up\":0,\"user_id\":22}],\"synch_business_sizes\":[{\"synch_business_size_id\":9,\"business_size_id\":1,\"business_size\":\"large\",\"created_by\":\"admin\",\"created_at\":\"2018-08-09 12:57:30\",\"updated_by\":null,\"updated_at\":\"2018-11-08 13:26:37\",\"synch_status\":0,\"last_synched_date\":\"2018-08-09 15:26:00\",\"authorization_id\":null,\"back_up\":0,\"user_id\":22}],\"synch_business_structures\":[{\"synch_business_structure_id\":9,\"business_structure_id\":1,\"business_structure\":\"MULTIPLE EMPLOYEES\",\"business_type\":\"private\",\"service_id\":\"234120010005\",\"created_by\":\"admin\",\"created_at\":\"2018-08-09 12:59:19\",\"updated_by\":null,\"updated_at\":\"2018-11-08 13:26:45\",\"synch_status\":0,\"last_synched_date\":\"2018-08-09 15:26:00\",\"authorization_id\":null,\"back_up\":0,\"user_id\":22}],\"synch_business_sub_sectors\":[{\"synch_business_sub_sector_id\":9,\"business_sub_sector_id\":1,\"business_sub_sector\":\"General Merchants\",\"business_type\":\"private\",\"business_category\":\"Fixed Location\",\"business_sector\":\"LIMITED LIABILITY\",\"service_id\":\"234120010005\",\"created_by\":\"admin\",\"created_at\":\"2018-08-09 13:01:09\",\"updated_by\":null,\"updated_at\":\"2018-11-08 13:26:54\",\"synch_status\":0,\"last_synched_date\":\"2018-08-09 15:26:00\",\"authorization_id\":null,\"back_up\":0,\"user_id\":22}],\"synch_business_types\":[{\"synch_business_type_id\":30,\"business_type_id\":4,\"business_type\":\"BUSINESS TESTING\",\"service_id\":\"234120023846\",\"created_by\":null,\"created_at\":\"2018-08-09 14:44:34\",\"updated_by\":null,\"updated_at\":\"2018-11-08 13:27:02\",\"synch_status\":0,\"last_synched_date\":\"2018-08-09 15:26:00\",\"authorization_id\":null,\"back_up\":0,\"user_id\":22}],\"synch_cities\":[{\"cities_id\":494,\"city\":\"Utako\",\"local_government_id\":38,\"registered_on\":\"2018-08-09 15:26:00\",\"authorized_on\":null,\"service_id\":\"234120010005\",\"session_id\":null,\"authorized_by\":null,\"synch_status\":0,\"last_synched_date\":\"2018-08-09 15:26:00\",\"authorization_id\":null,\"back_up\":0,\"user_id\":22,\"city_id\":494,\"lga_id\":38}],\"synch_land_functions\":[{\"synch_land_function_id\":3,\"land_function_id\":1,\"land_function\":\"multi purpose\",\"land_purpose\":\"events\",\"service_id\":\"234120010005\",\"created_by\":\"admin\",\"created_at\":\"2018-08-09 14:43:11\",\"updated_by\":null,\"updated_at\":\"2018-11-08 13:27:25\",\"synch_status\":0,\"last_synched_date\":\"2018-08-09 15:26:00\",\"authorization_id\":null,\"back_up\":0,\"user_id\":22}],\"synch_land_ownerships\":[{\"synch_land_ownership_id\":2,\"land_ownership_id\":1,\"land_ownership\":\"private\",\"service_id\":\"234120010005\",\"created_by\":\"admin\",\"created_at\":\"2018-08-09 14:43:46\",\"updated_by\":null,\"updated_at\":\"2018-11-08 13:27:37\",\"synch_status\":0,\"last_synched_date\":\"2018-08-09 15:26:00\",\"authorization_id\":null,\"back_up\":0,\"user_id\":22}],\"synch_land_purposes\":[{\"synch_land_purpose_id\":2,\"land_purpose_id\":1,\"land_purpose\":\"events\",\"service_id\":\"234120010005\",\"created_by\":\"admin\",\"created_at\":\"2018-08-09 14:44:40\",\"updated_by\":null,\"updated_at\":\"2018-11-08 13:27:45\",\"synch_status\":0,\"last_synched_date\":\"2018-08-09 15:26:00\",\"authorization_id\":null,\"back_up\":0,\"user_id\":22}],\"synch_land_types\":[{\"synch_land_type_id\":2,\"land_type_id\":1,\"land_type\":\"4 plots\",\"service_id\":\"234120010005\",\"created_by\":\"admin\",\"created_at\":\"2018-08-09 14:46:11\",\"updated_by\":null,\"updated_at\":\"2018-11-08 13:27:53\",\"synch_status\":0,\"last_synched_date\":\"2018-08-09 15:26:00\",\"authorization_id\":null,\"back_up\":0,\"user_id\":22}],\"synch_streets\":[],\"synch_tax_payer_types\":[]}}}");


          JSONObject childObject = parentObject.getJSONObject("response");

          String SeseResponseCode = childObject.getString("responseCode");

          JSONObject JSONSyncObject = childObject.getJSONObject("responseDescription");


          Log.e(TAG, "Parent Object: " + parentObject);
          Log.e(TAG, "Child Object: " + childObject);
          Log.e(TAG, "sese response code: " + SeseResponseCode);
          Log.e(TAG, "Seses response description: " + JSONSyncObject);

          if (JSONSyncObject.length() > 0) {
            //success
            String apartmenttypeids = pull.AnalyzeApartmentType(getApplicationContext(), JSONSyncObject.getJSONArray("synch_apartment_types"));
            String areacodeids = pull.AnalyzeAreaCodes(getApplicationContext(), JSONSyncObject.getJSONArray("synch_areacodes"));
            String buildingcompletionids = pull.AnalyzeBuildingCompletions(getApplicationContext(), JSONSyncObject.getJSONArray("synch_building_completions"));
            String buildingfunctionids = pull.AnalyzeBuildingFunctions(getApplicationContext(), JSONSyncObject.getJSONArray("synch_building_functions"));
            String buldingoccupancyids = pull.AnalyzeBuildingOccupancies(getApplicationContext(), JSONSyncObject.getJSONArray("synch_building_occupancies"));
            String occupancytypeids = pull.AnalyzeBuildingOccupancyType(getApplicationContext(), JSONSyncObject.getJSONArray("synch_building_occupancy_types"));
            String buildingownershipids = pull.AnalyzeBuildingOwnership(getApplicationContext(), JSONSyncObject.getJSONArray("synch_building_ownerships"));
            String buildingpurposeids = pull.AnalyzeBuildingPurpose(getApplicationContext(), JSONSyncObject.getJSONArray("synch_building_purposes"));
            String businesscategoryids = pull.AnalyzeBusinessCategory(getApplicationContext(), JSONSyncObject.getJSONArray("synch_business_categories"));
            String businessoperationids = pull.AnalyzeBusinessOperations(getApplicationContext(), JSONSyncObject.getJSONArray("synch_business_operations"));
            String businesssectorids = pull.AnalyzeBusinessSectors(getApplicationContext(), JSONSyncObject.getJSONArray("synch_business_sectors"));
            String businesssizeids = pull.AnalyzeBusinessSize(getApplicationContext(), JSONSyncObject.getJSONArray("synch_business_sizes"));
            String businessstructureids = pull.AnalyzeBusinessStructure(getApplicationContext(), JSONSyncObject.getJSONArray("synch_business_structures"));
            String businesssubsectorids = pull.AnalyzeBusinessSubStructure(getApplicationContext(), JSONSyncObject.getJSONArray("synch_business_sub_sectors"));
            String businesstypesids = pull.AnalyzeBusinessTypes(getApplicationContext(), JSONSyncObject.getJSONArray("synch_business_types"));
            String landfunctionids = pull.AnalyzeLandFunctions(getApplicationContext(), JSONSyncObject.getJSONArray("synch_land_functions"));
            String landownershipids = pull.AnalyzeLandOwnership(getApplicationContext(), JSONSyncObject.getJSONArray("synch_land_ownerships"));
            String landpurposeids = pull.AnalyzeLandPurposes(getApplicationContext(), JSONSyncObject.getJSONArray("synch_land_purposes"));
            String landtypeids = pull.AnalyzeLandTypes(getApplicationContext(), JSONSyncObject.getJSONArray("synch_land_types"));
            String streetids = pull.AnalyzeStreet(getApplicationContext(), JSONSyncObject.getJSONArray("synch_streets"));
            String buildingtypeids = pull.AnalyzeBuildingTypes(getApplicationContext(), JSONSyncObject.getJSONArray("synch_building_types"));
            String profileids = pull.AnalyzeProfiles(getApplicationContext(), JSONSyncObject.getJSONArray("synch_profiles"));
            String assessmentruleids = pull.AnalyzeAssessmentRule(getApplicationContext(), JSONSyncObject.getJSONArray("synch_assessment_rules"));
            String wardids = pull.AnalyzeWards(getApplicationContext(), JSONSyncObject.getJSONArray("synch_wards"));


            JSONObject savedData = new JSONObject();

            savedData.put("synch_apartment_types", apartmenttypeids);
            savedData.put("synch_areacodes", areacodeids);
            savedData.put("synch_building_completions", buildingcompletionids);
            savedData.put("synch_building_functions", buildingfunctionids);
            savedData.put("synch_building_occupancies", buldingoccupancyids);
            savedData.put("synch_building_occupancy_types", occupancytypeids);
            savedData.put("synch_building_ownerships", buildingownershipids);
            savedData.put("synch_building_purposes", buildingpurposeids);
            savedData.put("synch_business_categories", businesscategoryids);
            savedData.put("synch_business_operations", businessoperationids);
            savedData.put("synch_business_sectors", businesssectorids);
            savedData.put("synch_business_sizes", businesssizeids);
            savedData.put("synch_business_structures", businessstructureids);
            savedData.put("synch_business_sub_sectors", businesssubsectorids);
            savedData.put("synch_business_types", businesstypesids);
            savedData.put("synch_land_functions", landfunctionids);
            savedData.put("synch_land_ownerships", landownershipids);
            savedData.put("synch_land_purposes", landpurposeids);
            savedData.put("synch_land_types", landtypeids);
            savedData.put("synch_streets", streetids);
            savedData.put("synch_building_types", buildingtypeids);
            savedData.put("synch_profiles", profileids);
            savedData.put("synch_assessment_rules", assessmentruleids);
            savedData.put("synch_wards", wardids);



            Log.e(TAG, "saveddata ::: " + savedData.toString());

            callServiceMod(getString(R.string.verifysync), savedData);

            //db code


            SeseErrorMssg = "null";

            //return true;
          } else if (SeseResponseCode.equals("500")) {
            //success
            //                                successAlert();
            SeseErrorMssg = "Internal Server Error";
            errorMssg = SeseResponseCode;

          } else {
    /*
                                    failureAlert();
    */
            errorMssg = SeseResponseCode;
//                        SeseErrorMssg=JSONSyncObject;
          }
        } else {
          errorMssg = httpResp.getResponseData();
          String respcode = String.valueOf(httpResp.getResponseCode());
          errorMssg = respcode;

        }
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

  }

  public AuthorizationHttpResponse callServiceMod(String endPoint, JSONObject enteredDataIds) {

    if (GenericHelpers.checkInternetConenction(getApplicationContext())) {

      if (app.getLatitude() == null || app.getLongitude() == null) {
        LocationMgr LocMgr = new LocationMgr();
        String locMan = LocMgr.getMyLoc(getApplicationContext());
        if (!locMan.equalsIgnoreCase("err")) {
          String[] splitedCoordinate = GenericHelpers.splitString(locMan, ":");
          Log.e("My LocMann", " latitude:" + splitedCoordinate[0] + " longitude:" + splitedCoordinate[1]);
          latitude = splitedCoordinate[0];
          longitude = splitedCoordinate[1];
        }
      } else {
        latitude = app.getLatitude();
        longitude = app.getLongitude();
      }

      if (enteredDataIds.equals(null)) {

      }

      if (client_userid != null && service_code != null) {
        Log.d("UserId And Servicecode", "Got User Id and service code");
        try {

          JSONObject myJsonObject = new JSONObject();
          myJsonObject.put("service_id", service_id.trim());
          myJsonObject.put("user_id", client_userid.trim());
//                    myJsonObject.put("appKeyParameter", appK.trim());
//                    myJsonObject.put("AuthorizationCode", enteredDataIds);


          Log.e(TAG, "This is request " + enteredDataIds.toString());
          ServiceHandler serviceHandler = new ServiceHandler();
          AuthorizationHttpResponse httpResp = serviceHandler.makeServiceCallJSON(endPoint, 2, enteredDataIds);


          if (httpResp != null && httpResp.getResponseData() != null) {


            Log.e(TAG, "This is response " + httpResp.getResponseCode() + "::" + httpResp.getResponseData());
            errorMssg = httpResp.getResponseData();
            //Toast.makeText(getApplicationContext(), "Something wrong", Toast.LENGTH_SHORT).show();
            String respcode = String.valueOf(httpResp.getResponseCode());
            errorMssg = respcode;
            authResp = httpResp;

          } else {
            Log.d("EmailAndServicecode", "Could not get email and service code");
          }


        } catch (Exception e) {
          e.printStackTrace();
        }


      }
    } else {
      //Toast.makeText(getApplicationContext(), "Device is offline at the moment", Toast.LENGTH_SHORT).show();
      Log.e("[-V-]", "Device is offline at the moment");
    }
    return authResp;
  }

  public AuthorizationHttpResponse callService(String endPoint, String enteredDataIds) {

    if (GenericHelpers.checkInternetConenction(getApplicationContext())) {

      if (app.getLatitude() == null || app.getLongitude() == null) {
        LocationMgr LocMgr = new LocationMgr();
        String locMan = LocMgr.getMyLoc(getApplicationContext());
        if (!locMan.equalsIgnoreCase("err")) {
          String[] splitedCoordinate = GenericHelpers.splitString(locMan, ":");
          Log.e("My LocMann", " latitude:" + splitedCoordinate[0] + " longitude:" + splitedCoordinate[1]);
          latitude = splitedCoordinate[0];
          longitude = splitedCoordinate[1];
        }
      } else {
        latitude = app.getLatitude();
        longitude = app.getLongitude();
      }

      if (enteredDataIds.equals(null)) {

      }

      if (client_userid != null && service_code != null) {
        Log.d("UserId And Servicecode", "Got User Id and service code");
        try {

          JSONObject myJsonObject = new JSONObject();
          myJsonObject.put("service_id", service_id.trim());
          myJsonObject.put("user_id", client_userid.trim());
//                    myJsonObject.put("appKeyParameter", appK.trim());
//                    myJsonObject.put("AuthorizationCode", enteredDataIds);


          Log.e(TAG, "This is request " + myJsonObject.toString());
          ServiceHandler serviceHandler = new ServiceHandler();
          AuthorizationHttpResponse httpResp = serviceHandler.makeServiceCallJSON(endPoint, 2, myJsonObject);


          if (httpResp != null && httpResp.getResponseData() != null) {


            Log.e(TAG, "This is response " + httpResp.getResponseCode() + "::" + httpResp.getResponseData());
            errorMssg = httpResp.getResponseData();
            //Toast.makeText(getApplicationContext(), "Something wrong", Toast.LENGTH_SHORT).show();
            String respcode = String.valueOf(httpResp.getResponseCode());
            errorMssg = respcode;
            authResp = httpResp;

          } else {
            Log.d("EmailAndServicecode", "Could not get email and service code");
          }


        } catch (Exception e) {
          e.printStackTrace();
        }


      }
    } else {
      //Toast.makeText(getApplicationContext(), "Device is offline at the moment", Toast.LENGTH_SHORT).show();
      Log.e("[-V-]", "Device is offline at the moment");
    }
    return authResp;
  }


/*
    public void Sync(String myEndPoint, String myEndPointVerify, String callType, String whatIsDone){

        Log.e(callType+" ", whatIsDone +" :-) ");
        AuthorizationHttpResponse r2 = callService(myEndPoint, " ");

        if (r2 != null && r2.getResponseData() != null)
        {
            Log.e(callType, "Initial:::Something was returned from service call" + r2.getResponseData() + ". status;" + r2.getResponseCode());
            if (r2.getResponseCode() == 200) {
                Log.e(callType, "Initial:::service call response is good!");


                JSONObject jsonReader = null;
                try {


                    // TODO: 11/10/2016 get user data from json
                    JSONObject parentObject = new JSONObject(r2.getResponseData());
                    JSONObject childObject = parentObject.getJSONObject("response");
                    String SeseResponseCode = childObject.getString("responseCode");


                    if (SeseResponseCode.equals("00")) {

                        String SeseResponseDescription = childObject.getString("responseDescription");

                        JSONArray myArray = null;
                        String enteredDataIds=null;

                        if(whatIsDone.equals("synch_employee_info")) {
                            myArray = parentObject.getJSONArray("Employee_Info");
                             enteredDataIds = myJSON.AnalyzeEmployeeInfo(getApplicationContext(),myArray);

                        }
                        else if(whatIsDone.equals("synch_pensions_employee_info")) {
                            myArray = parentObject.getJSONArray("Pension_Employee_Info");
                             enteredDataIds = myJSON.AnalyzePensionEmployeeInfo(getApplicationContext(),myArray);

                        }
                        else {
                            myArray = null;
                            enteredDataIds = null;
                        }


                        if(!(enteredDataIds.equals(null))) {

                            AuthorizationHttpResponse r22 = callService(myEndPointVerify, enteredDataIds);

                            if (r22 != null && r22.getResponseData() != null) {
                                Log.e(callType, "Verifying:::Something was returned from service call" + r22.getResponseData() + ". status;" + r22.getResponseCode());
                                if (r22.getResponseCode() == 200) {

                                    Log.e(callType, "Verifying:::service call response is good!");


                                }
                            }
                        }
                        else {
                            Log.e("No Need to respond", "-Nothing was saved");
                        }

                    }
                    else{

                        Log.e(callType+ " " +whatIsDone, " FAILED ...JSON is Empty");

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //AuthorizeUser AUTH_USER = new AuthorizeUser();
                //AUTH_USER.initialRequest(getApplicationContext(), mEmail, mServiceID, url);
            }

        }


    }
*/

  private void initializeLocationManager() {
    Log.e(TAG, "initializeLocationManager");
    if (mLocationManager == null) {
      mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
    }
  }


  @Override
  public void onDestroy() {
    super.onDestroy();
  }


  void verify(String username, String servicecode) {

    String abc;
    String SeseErrorMssg;

    try {
      JSONObject authJsonObject = new JSONObject();
      authJsonObject.put("email", username.trim());
//                    authJsonObject.put("password", mPassword.trim());
      authJsonObject.put("service_code", servicecode.trim());
      authJsonObject.put("device_os", Devices.getDeviceModel());
//                    authJsonObject.put("appKeyParameter", getString(R.string.appId));
      authJsonObject.put("device_uuid", Devices.getDeviceUUID(getApplicationContext().getApplicationContext()));
      authJsonObject.put("device_imei", Devices.getDeviceIMEI(getApplicationContext().getApplicationContext()));
      authJsonObject.put("device_name", Devices.getDeviceName());
      authJsonObject.put("device_model", Devices.getDeviceModel());
      authJsonObject.put("device_longitude", longitude);
      authJsonObject.put("device_latitude", latitude);
      authJsonObject.put("device_version", Devices.getDeviceVersion());

      Log.e(TAG, "This is request " + authJsonObject.toString());

      ServiceHandler serviceHandler = new ServiceHandler();


      abc = authJsonObject.toString();
                    /*Cursor mycursor = dataDB.myConnection(getActivity().getApplicationContext()).selectAllFromTableAs222();
                    String EPdata="";
                    if (mycursor != null  && mycursor.moveToFirst() ) { EPdata = mycursor.getString(20);}
*/
      AuthorizationHttpResponse httpResp = serviceHandler.makeServiceCallJSON(getString(R.string.auth), 2, authJsonObject);


      if (httpResp != null && httpResp.getResponseData() != null) {
        Log.e(TAG, "This is response " + httpResp.getResponseCode() + "::" + httpResp.getResponseData());
        if (httpResp.getResponseCode() == 200) {

          // TODO: 11/10/2016 get user data from json
          JSONObject parentObject = new JSONObject(httpResp.getResponseData());

          JSONObject childObject = parentObject.getJSONObject("response");

          String SeseResponseCode = childObject.getString("responseCode");

          String SeseResponseDescription = childObject.getString("responseDescription");


          Log.e(TAG, "Parent Object: " + parentObject);
          Log.e(TAG, "Child Object: " + childObject);
          Log.e(TAG, "sese response code: " + SeseResponseCode);
          Log.e(TAG, "Seses response description: " + SeseResponseDescription);

          if (SeseResponseDescription.equals("Success")) {
            //success
            //                                successAlert();


            Log.d(TAG, " user authorized: ");


            // TODO: 11/10/2016 save user data
            ContentValues contentValues = new ContentValues();

            //contentValues.put("tax_registration_id",tax_registration_id);
            //Log.e("tax registration is**: ", Long.toString(tax_registration_id));

            contentValues.put("authorized", "1");
            contentValues.put("user_type", "3");


            //db code


            long rowInserted = dataDB.myConnection(getApplicationContext()).onUpdateOrIgnore(contentValues, "users", "login", username);
            if (rowInserted != -1) {

              Log.e(TAG, "user details saved: " + ":-)");
                ReInitializeDB reInitializeDB = new ReInitializeDB(getApplicationContext());

                if (reInitializeDB.clearDB()) {
                    sendAuthorizationNotification("Authorization Granted");
                }

                //Database insert successful, give permission for app to continue
//                            return true;
            }


            SeseErrorMssg = "null";

            //return true;
          } else if (SeseResponseCode.equals("500")) {
            //success
            //                                successAlert();
            SeseErrorMssg = "Internal Server Error";
            errorMssg = SeseResponseCode;

          } else {
    /*
                                    failureAlert();
    */
            errorMssg = SeseResponseCode;
            SeseErrorMssg = SeseResponseDescription;
          }
        } else {
          errorMssg = httpResp.getResponseData();
          String respcode = String.valueOf(httpResp.getResponseCode());
          errorMssg = respcode;

        }
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

  }

  //@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
  //@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
  public void sendAuthorizationNotification(String message) {


    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
    PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

    int defaults = 0;
    defaults = defaults | Notification.DEFAULT_LIGHTS;
    defaults = defaults | Notification.DEFAULT_VIBRATE;
    defaults = defaults | Notification.DEFAULT_SOUND;
    Notification n = new Notification.Builder(getApplicationContext())
            .setContentTitle("Revcollect Enumerator")
            .setContentText(message)
            .setTicker("User Authorized!")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pIntent)
            .setDefaults(defaults)
            .setAutoCancel(true).build();

    NotificationManager notificationManager =
            (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

    notificationManager.notify(0, n);


    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationManager notificationManager2 = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
      String id = "id_product";
      // The user-visible name of the channel.
      CharSequence name = "Product";
      // The user-visible description of the channel.
      String description = "Notifications regarding our products";
      int importance = NotificationManager.IMPORTANCE_HIGH;
      NotificationChannel mChannel = new NotificationChannel(id, name, importance);
      // Configure the notification channel.
      mChannel.setDescription(description);
      mChannel.enableLights(true);
      // Sets the notification light color for notifications posted to this
      // channel, if the device supports this feature.
      mChannel.setLightColor(Color.RED);
      notificationManager2.createNotificationChannel(mChannel);
      notificationManager2.notify();
    }


  }




}

