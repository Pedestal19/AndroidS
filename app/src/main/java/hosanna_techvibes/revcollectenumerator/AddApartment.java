package hosanna_techvibes.revcollectenumerator;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import hosanna_techvibes.revcollectenumerator.databases.DBCheckAuthorization;
import hosanna_techvibes.revcollectenumerator.databases.DataDB;
import hosanna_techvibes.revcollectenumerator.helpers.GenericHelpers;
import hosanna_techvibes.revcollectenumerator.helpers.LocationMgr;
import hosanna_techvibes.revcollectenumerator.model.App;
import hosanna_techvibes.revcollectenumerator.model.CoordinateModel;
import hosanna_techvibes.revcollectenumerator.model.Taxpayer;

public class AddApartment extends AppCompatActivity {

    private RegisterUserTask mRegTask = null;
    private ProgressDialog pDialog;
    private View mProgressView;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
    Calendar timer = Calendar.getInstance();
    String ia, ib, iti;
    String latitude = "unknown";
    String longitude = "unknown";


    String successMssg;
    String errorMssg;

    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;


    EditText et_apartment, et_buildingid, et_apartment_occupants_no;
    Spinner spiApartmentType;
    Button btn_registerApartment;

    private static final String TAG = "Add Apartment";
    ArrayAdapter dataApartmentType;

    App app;
    private ProgressDialog myDialog;
    DataDB dataDB = new DataDB();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_apartment);
        app = ((App) getApplicationContext());

        getIds();
        populateSpinners();


        if (app.getReg_mode()) {
            et_buildingid.setText("NA");
            app.setA_buildingid("000");
        } else {
            et_buildingid.setText(app.getMyFirstName());

        }

        btn_registerApartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_SHORT).show();
//                alert3();
//                alert2();
                attemptUserRegistration();
            }
        });

    }


    private void attemptUserRegistration() {
        /*if (mRegTask != null) {
            return;
        }*/

        et_apartment.setError(null);


        boolean cancel = false;
        View focusView = null;


        String apartment = et_apartment.getText().toString();
        String apartment_occupants_no = et_apartment_occupants_no.getText().toString();
        String buildingid = et_buildingid.getText().toString();

        String apartment_type = spiApartmentType.getSelectedItem().toString();

        if (TextUtils.isEmpty(apartment)) {
            et_apartment.setError(getString(R.string.error_field_required));
            focusView = et_apartment;
            cancel = true;

        }
        else if (TextUtils.isEmpty(apartment_occupants_no)) {
            et_apartment_occupants_no.setError(getString(R.string.error_field_required));
            focusView = et_apartment_occupants_no;
            cancel = true;

        }



        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.

            focusView.requestFocus();
        } else {

            if(spiApartmentType.getSelectedItem().toString().equals("--Select Apartment Type--")) {

                Toast.makeText(getApplicationContext(), "Please Select All Drop Downs", Toast.LENGTH_SHORT).show();
            }
            else {
                pDialog = new ProgressDialog(this);
                // Showing progress dialog before making http request
                pDialog.setMessage("Apartment Registration in progress...");
                pDialog.show();


                mRegTask = new RegisterUserTask(buildingid, apartment, apartment_type, apartment_occupants_no);
                mRegTask.execute((Void) null);
            }

        }

    }

    public class RegisterUserTask extends AsyncTask<Void, Void, Boolean> {
        private final String mbuildingid,mapartment,mapartmenttype, mapartmentoccupantsno;

        RegisterUserTask(String buildingid, String apartment,String apartmenttype, String apartmentoccupantsno) {
            mbuildingid = buildingid;
            mapartment = apartment;
            mapartmenttype = apartmenttype;
            mapartmentoccupantsno = apartmentoccupantsno;
        }
        @Override
        protected Boolean doInBackground(Void... params) {
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

                long _rand1,tax_registration_id=0;
                   /* if(mexistingTAXID != null && !mexistingTAXID.isEmpty())
                    {
                        _rand1 = Long.parseLong(mexistingTAXID);
                    }
                    else {*/
                Random randi = new Random();
                _rand1 = (long) (randi.nextDouble() * 10000000000L);
                tax_registration_id = dataDB.myConnection(getApplicationContext()).countRecords("_apartments")+_rand1+1;

                String rin = "RIN"+tax_registration_id;
//                    }
                //app.setActiveTIN(String.valueOf(_rand1));
                String getDateTime = dataDB.getDateTime(getApplicationContext());
                Log.e("date time:::",getDateTime);
                /*DBCheckAuthorization checkAuthorization = new DBCheckAuthorization();
                Cursor cursor = checkAuthorization.checkForAuthorizationRequest(getApplicationContext());
                String si = "000000";
                String ui = "0";
                if (cursor != null && cursor.getCount() > 0)//&& cursor.getCount()>0
                {
                    Log.e("Log", "Running:authorization record found:" + cursor.getCount());
                    if (cursor.moveToFirst()) {
                        do {
                            si = cursor.getString(12);
                            Log.e("Log", "service id****:" + cursor.getString(12));
                            ui = cursor.getString(9);
                            Log.e("Log", "service id****:" + cursor.getString(9));

                        } while (cursor.moveToNext());
                    }
                }
*/


                // TODO: 11/10/2016 save user data
                ContentValues contentValues = new ContentValues();

                //contentValues.put("tax_registration_id",tax_registration_id);
                //Log.e("tax registration is**: ", Long.toString(tax_registration_id));

                //contentValues.put("tax_registration_id","100");
                contentValues.put("apartment_id", rin);
                contentValues.put("apartment", mapartment);
                contentValues.put("apartment_type_id", "1");

                String formattedDate = df.format(timer.getTime());
                contentValues.put("registered_on", getDateTime);
                contentValues.put("registered_by", app.getUserid());
                contentValues.put("building_id", app.getA_buildingid());
                contentValues.put("service_id", app.getServiceID());
                contentValues.put("active", "1");



                //db code


                long rowInserted = dataDB.myConnection(getApplicationContext()).onInsertOrUpdate(contentValues, "_apartments");
                if (rowInserted != -1) {
                    Log.e(TAG, " rin number" + rin);

//                        app.setActiveTax_registration_id(String.valueOf(tax_registration_id));
                        /*app.setActiveTax_registration_id(rin);
                        ;
                        app.setActiveTIN(String.valueOf(_rand1));*/
                    Log.e(TAG, "New row added, row id: " + " to lands table");
                    Log.e(TAG, "Adding Apartment Occupants: " + " ......");

                    ContentValues contentValues2 = new ContentValues();

                    contentValues2.put("apartment_id", rin);
                    contentValues2.put("building_id", app.getA_buildingid());
                    contentValues2.put("appartment_occupant_no", mapartmentoccupantsno);
                    contentValues2.put("registered_on", getDateTime);
                    contentValues2.put("registered_by", app.getUserid());
                    contentValues2.put("service_id", app.getServiceID());
                    long rowInserted2 = dataDB.myConnection(getApplicationContext()).onInsertOrUpdate(contentValues2, "_apartments_occupants");

                    Log.e(TAG, "Apartment Occupants Added: " + ":-)");

                    app.setReg_mode(false);
                    //Database insert successful, give permission for app to continue
                    return true;
                }

                else {
                    //errorMssg = httpResp.getResponseData();
                    // Toast.makeText(getApplicationContext(), "Something wrong", Toast.LENGTH_SHORT).show();
                }





            }
            catch (Exception e)
            {
                e.printStackTrace();
            }


            return false;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mRegTask = null;
            hidePDialog();

            if (success) {
                Log.i("ApartRegistrationLog", "Registration Successful");
                finish();
                Intent qRIntent = new Intent(getApplicationContext(), ApartmentRegComplete.class);

//                String dot=".";
                //String mfirstname2 = "r"+mfirstname;



                startActivity(qRIntent);
//                startActivity(new Intent(getApplicationContext(), RegistrationComplete.class));
//                Toast.makeText(getApplicationContext(), "Taxpayer has been registered", Toast.LENGTH_LONG).show();

            }

            else {
                Log.i("UserRegistrationLog", "Failed Registration");
                /*if(errorMssg!=null)
                {
                    edtLastname.setError(errorMssg);
                    et_taxid.requestFocus();
                }
                else {*/
                et_apartment.setError("Registration failed");
                //}
            }
        }


        @Override
        protected void onCancelled() {
            mRegTask = null;
            hidePDialog();
        }
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
    private void hidemyDialog() {
        if (myDialog != null) {
            myDialog.dismiss();
            myDialog = null;
        }
    }



    void getIds(){

        et_buildingid = (EditText) findViewById(R.id.et_buildingid);
        et_apartment = (EditText) findViewById(R.id.et_apartment);
        et_apartment_occupants_no = (EditText) findViewById(R.id.et_apartment_occupants_no);
        spiApartmentType = (Spinner)findViewById(R.id.spiApartmentType);
        btn_registerApartment = (Button) findViewById(R.id.btn_register);


    }

    void populateSpinners(){

        List<String> apartmentTypeCursor = dataDB.myConnection(this).universalSelect("_apartment_types","apartment_type");

        dataApartmentType = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, apartmentTypeCursor);
        dataApartmentType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiApartmentType.setAdapter(dataApartmentType);


    }









}
