package hosanna_techvibes.revcollectenumerator;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import hosanna_techvibes.revcollectenumerator.MyLogin.LoginActivity;
import hosanna_techvibes.revcollectenumerator.databases.DataDB;
import hosanna_techvibes.revcollectenumerator.helpers.Devices;
import hosanna_techvibes.revcollectenumerator.helpers.GenericHelpers;
import hosanna_techvibes.revcollectenumerator.helpers.LocationMgr;
import hosanna_techvibes.revcollectenumerator.helpers.PullGuestData;
import hosanna_techvibes.revcollectenumerator.helpers.ServiceHandler;
import hosanna_techvibes.revcollectenumerator.helpers.UniqueID;
import hosanna_techvibes.revcollectenumerator.model.App;
import hosanna_techvibes.revcollectenumerator.model.AuthorizationHttpResponse;

public class GuestSignUp extends AppCompatActivity implements View.OnClickListener {

    private String name, email;
    private EditText et_name, et_email, et_password, et_confirmPassword;
    private Button bt_signup;
    private RegisterUserTask mRegTask = null;
    private ProgressDialog pDialog;
    private String longitude, latitude, myCity;
    private String SeseErrorMssg, errorMssg;
    private PullGuestData pullGuestData;
    private String responseCode;

    App app;
    DataDB dataDB = new DataDB();
    private String TAG = "GuestSignUp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_sign_up);
        app = ((App) getApplicationContext());


        getIds();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        name = (String) bundle.getString("name");
        email = (String) bundle.getString("email");

        et_name.setText(name);
        et_email.setText(email);

        findViewById(R.id.btn_signup).setOnClickListener(this);

    }

    private void getIds() {
        et_name = (EditText) findViewById(R.id.et_guestName);
        et_email = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);
        et_confirmPassword = (EditText) findViewById(R.id.et_confirmPassword);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_signup:
                attemptUserRegistration();
        }
    }

    private void attemptUserRegistration() {

        et_email.setError(null);
        et_name.setError(null);

        boolean cancel = false;
        View focusView = null;


        String name = et_name.getText().toString();
        String email = et_email.getText().toString();
        String password = et_password.getText().toString();
        String confirmPassword = et_confirmPassword.getText().toString();


        if (TextUtils.isEmpty(password)) {
            et_password.setError(getString(R.string.error_field_required));
            focusView = et_password;
            cancel = true;

        }
        if (password.length() < 6) {
            et_password.setError(getString(R.string.passwordcondition1));
            focusView = et_password;
            cancel = true;

        }
        if (TextUtils.isEmpty(confirmPassword)) {
            et_confirmPassword.setError(getString(R.string.error_field_required));
            focusView = et_confirmPassword;
            cancel = true;

        }

        if (!password.equals(confirmPassword)) {
            et_confirmPassword.setError(getString(R.string.password_err));
            focusView = et_confirmPassword;
            cancel = true;

        }



        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.

            focusView.requestFocus();
        } else {
            // TODO: 22/09/2015 check if gps is on
            // Check if GPS is enabled
            //String provider = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            //if(!provider.equals(""))

            /*String provider = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if (!provider.equals("")) {


*/


//            pDialog = new ProgressDialog(this);
//            // Showing progress dialog before making http request
//            pDialog.setMessage("Signup in progress...");
//            pDialog.setCanceledOnTouchOutside(false);
//            pDialog.show();


            mRegTask = new RegisterUserTask(name, email, password, confirmPassword);
            mRegTask.execute((Void) null);

        }

        //}
        //else{
        // GenericHelpers.askUserToOpenGPS(RegisterActivity.this);
        //}

    }

    private class RegisterUserTask extends AsyncTask<Void, Void, Boolean> {

        private final String async_name, async_email, async_password, async_confirmPassword;

        public RegisterUserTask(String async_name, String async_email, String async_password, String async_confirmPassword) {
            this.async_name = async_name;
            this.async_email = async_email;
            this.async_password = async_password;
            this.async_confirmPassword = async_confirmPassword;

            pDialog = new ProgressDialog(GuestSignUp.this);
            // Showing progress dialog before making http request
            pDialog.setMessage("Signup in progress...");
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            {
                // TODO: attempt register user via a network service.

                try {
                    // Simulate network access.
                    Thread.sleep(1000); //2000
                } catch (InterruptedException e) {
                    Log.e("doInBackground", e.toString() + ". Trying do in Background");
                    return false;
                }
                //if (GenericHelpers.isOnline(getApplicationContext())) {
                String httpparams;
                try {
                    String lat = null, lon = null;
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

                   /* if (latitude != null && longitude != null) {
                        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(longitude), Double.parseDouble(latitude), 1);
                            String address = addresses.get(0).getAddressLine(0);
                            myCity = addresses.get(0).getLocality();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }*/


//                    }
                    //app.setActiveTIN(String.valueOf(_rand1));
                    String[] usernames1 = async_name.split(" ");

                    JSONObject authJsonObject = new JSONObject();
                    authJsonObject.put("email", async_email.trim());
                    authJsonObject.put("firstname", usernames1[0]);
                    authJsonObject.put("surname", usernames1[1]);
                    authJsonObject.put("password", async_password);
                    authJsonObject.put("service_code", "2340751008");
                    authJsonObject.put("device_os", Devices.getDeviceModel());
                    authJsonObject.put("user_id", new UniqueID().getUniqueID());
                    ;
                    authJsonObject.put("device_uuid", Devices.getDeviceUUID(getApplicationContext().getApplicationContext()));
                    authJsonObject.put("device_imei", Devices.getDeviceIMEI(getApplicationContext().getApplicationContext()));
                    authJsonObject.put("device_name", Devices.getDeviceName());
                    authJsonObject.put("device_model", Devices.getDeviceModel());
                    authJsonObject.put("device_longitude", longitude);
                    authJsonObject.put("device_latitude", latitude);
                    authJsonObject.put("device_version", Devices.getDeviceVersion());
                    authJsonObject.put("token", "mytoken");
                    authJsonObject.put("info", "myinfo");


                    Log.e("GuestSignUp", "This is request " + authJsonObject.toString());

                    ServiceHandler serviceHandler = new ServiceHandler();

                    AuthorizationHttpResponse httpResp = serviceHandler.makeServiceCallJSON(getString(R.string.guest_auth_ep),2,authJsonObject);


                    if (httpResp != null && httpResp.getResponseData() != null) {
                        Log.e("GuestSignUp", "This is response " + httpResp.getResponseCode() + "::" + httpResp.getResponseData());


                        if (httpResp.getResponseCode() == 200) {


                            // TODO: 11/10/2016 get user data from json
                            JSONObject parentObject = new JSONObject(httpResp.getResponseData());

                            JSONObject childObject = parentObject.getJSONObject("response");

                            String SeseResponseCode = childObject.getString("responseCode");

                            String SeseResponseDescription = childObject.getString("responseDescription");




                            Log.e("GuestSignUp", "Parent Object: " + parentObject);
                            Log.e("GuestSignUp", "Child Object: " + childObject);
                            Log.e("GuestSignUp", "response code: " + SeseResponseCode);
                            Log.e("GuestSignUp", "response description: " + SeseResponseDescription);




                            if (SeseResponseCode.equals("00")) {

                                String my_userid = childObject.getString("UserId");

                                JSONObject JSONSyncObject = childObject.getJSONObject("responseDescription");


                                String getDateTime = dataDB.getDateTime(getApplicationContext());
                                Log.e("date time:::", getDateTime);
                                String g_userid = new UniqueID().getUniqueID();

                                // TODO: 11/10/2016 save user data
                                ContentValues contentValues = new ContentValues();

                                String[] usernames = async_name.split(" ");
                                contentValues.put("first_name", usernames[0]);
                                contentValues.put("surname", usernames[1]);
                                contentValues.put("login", async_email);
                                contentValues.put("pwd", async_password);
                                contentValues.put("user_id", my_userid);
                                contentValues.put("service_code", "DEMO");
                                contentValues.put("service_id", "2340751008");
                                contentValues.put("group_id", "9000");
                                contentValues.put("authorized", "1");



                                //db code

                                long rowInserted = dataDB.myConnection(getApplicationContext()).onInsertOrUpdate(contentValues, "users");
                                if (rowInserted != -1) {

                                    ;
                                    Log.e("GuestSignup", "New Guest added, row id: " + " to lands table");

                                    pullGuestData = new PullGuestData(getApplicationContext(),JSONSyncObject);
                                    JSONObject savedData = pullGuestData.pullData();
                                    //Database insert successful, give permission for app to continue
                                    if(callServiceMod(getString(R.string.verifysync), savedData,"2340751008", g_userid)){
                                        return true;
                                    }

                                } else {
                                    //errorMssg = httpResp.getResponseData();
                                    // Toast.makeText(getApplicationContext(), "Something wrong", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                responseCode = SeseResponseCode;
                                errorMssg=SeseResponseCode;
                                SeseErrorMssg=SeseResponseDescription;
                                Log.e(TAG, "Hey I am Here!!!");
                                return false;
                            }
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            mRegTask = null;
            hidePDialog();

            if (success) {
                Log.i("UserRegistrationLog", "Finished Authorization Successful");

                successAlert();
//
            } else{


                    Log.i("UserRegistrationLog", "Failed Registration");
                    if (SeseErrorMssg != null) {
                    /*et_password.setError(SeseErrorMssg);
                    et_password.requestFocus();*/

                        failureAlert();
                    } else {
/*
                    et_password.setError("Connection timeout::");
*/
                        try {
                            failureAlertC();
                        } catch (Exception e) {
                            failureAlertC();
                        }
                    }


            }
        }



    @Override
    protected void onCancelled() {
        mRegTask = null;
        hidePDialog();
    }


    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    public void successAlert() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                GuestSignUp.this, R.style.AppTheme_Dark_Dialog2);

        // Setting Dialog Title
        alertDialog.setTitle("Sign Up Success!");
        // Setting Dialog Message
        alertDialog.setMessage("You can now login with your credentials");
        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.dialog_tick2);
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

//                        Toast.makeText(getBaseContext(), myCity, Toast.LENGTH_LONG).show();
//                        finish();
//                        startActivity(new Intent(getActivity().getApplicationContext(), SetPassword.class));
                        finish();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    }
                });

        // Showing Alert Message
        alertDialog.show();


    }

    public void failureAlert() {


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                GuestSignUp.this,R.style.AppTheme_Dark_Dialog3);

        // Setting Dialog Title
        alertDialog.setTitle("Oops! An error occured");
        // Setting Dialog Message

        if (SeseErrorMssg!=null){
            alertDialog.setMessage("Error "+errorMssg+"\n"+SeseErrorMssg);
        }
        else {
            alertDialog.setMessage("Error 19 \nPls Contact Administrator");
        }
        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.dialog_fail2);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


//                        caption.setEnabled(true);
                    }
                });

        // Showing Alert Message
        alertDialog.show();


    }
    public void failureAlertC() {


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                GuestSignUp.this,R.style.AppTheme_Dark_Dialog3);

        // Setting Dialog Title
        alertDialog.setTitle("Oops! An error occured");
        // Setting Dialog Message


        alertDialog.setMessage("Check Your Connection");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.dialog_fail2);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if(pDialog!=null){
                            pDialog.dismiss();
                        }

//                        caption.setEnabled(true);
                    }
                });

        // Showing Alert Message
        alertDialog.show();


    }
    }

    @Override
    public void onBackPressed() {

        //Display alert message when back button has been pressed
        backButtonHandler();


    }

    public void backButtonHandler() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                this);
        // Setting Dialog Title
        alertDialog.setTitle("End Signup?");

        // Setting Dialog Message
        alertDialog.setMessage("Are sure you want to end the signup process?\nYou would lose already filled data.\n do you still want to go back?");
        // Setting Icon to Dialog
        alertDialog.setIcon(R.mipmap.ic_launcher);
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();

                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
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


    public Boolean callServiceMod(String endPoint, JSONObject enteredDataIds, String service_id, String client_userid) {

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

            if (client_userid != null && service_id != null) {
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

                        return true;
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
        return false;
    }


}




