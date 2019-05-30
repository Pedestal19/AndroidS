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

public class AddBuilding extends AppCompatActivity {

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
    // if GPS is enabled
    boolean isGPSEnabled = false;
    // if Network is enabled
    boolean isNetworkEnabled = false;

    EditText et_street, et_town, et_lga, et_ward, et_asset_type, et_building_tag_number, et_building_name, et_building_address;
    Spinner spiBuildingType, spiBuildingCompletion, spiBuildingPurpose, spiBuildingFunction, spiBuildingOwnership;
    Spinner spiBuildingOccupancy, spiBuildingOccupancyType;
    Button btn_registerBuilding;

    private static final String TAG = "Add Building";
    ArrayAdapter dataBuildingType, dataBuildingCompletion, dataBuildingPurpose,dataBuildingFunction,dataBuildingOwnership,
    dataBuildingOccupancy, dataBuildingOccupancyType;

    App app;
    private ProgressDialog myDialog;
    DataDB dataDB = new DataDB();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_building);
        app = ((App) getApplicationContext());

        getIds();
        populateSpinners();

    if(app.getReg_mode()){
        et_street.setText("NA");
        et_town.setText("NA");
        et_lga.setText("NA");
        et_ward.setText("NA");
        et_asset_type.setText("NA");
        app.setLandrin("NA");
    }
    else {
        try {
            et_street.setText(app.getMySurname());
            et_town.setText(app.getP_town());
            et_lga.setText(app.getP_lga());
            et_ward.setText(app.getP_ward());
            et_asset_type.setText(app.getP_assettype());

        } catch (Exception e) {
            Toast.makeText(this, "An error Occured", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


        btn_registerBuilding.setOnClickListener(new View.OnClickListener() {
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

        et_building_tag_number.setError(null);
        et_building_name.setError(null);
        et_building_address.setError(null);

        boolean cancel = false;
        View focusView = null;


        String street = et_street.getText().toString();
        String town = et_street.getText().toString();
        String lga = et_lga.getText().toString();
        String ward = et_ward.getText().toString();
        String asset_type = et_asset_type.getText().toString();

        String building_tag_number = et_building_tag_number.getText().toString();
        String building_name = et_building_name.getText().toString();
        String building_address = et_building_address.getText().toString();

        String building_type = spiBuildingType.getSelectedItem().toString();
        String building_completion = spiBuildingCompletion.getSelectedItem().toString();
        String building_purpose = spiBuildingPurpose.getSelectedItem().toString();
        String building_function = spiBuildingFunction.getSelectedItem().toString();
        String building_ownership = spiBuildingOwnership.getSelectedItem().toString();
        String building_occupancy = spiBuildingOccupancy.getSelectedItem().toString();
        String building_occupancy_type = spiBuildingOccupancyType.getSelectedItem().toString();

        if (TextUtils.isEmpty(building_tag_number)) {
            et_building_tag_number.setError(getString(R.string.error_field_required));
            focusView = et_building_tag_number;
            cancel = true;

        }
        if (TextUtils.isEmpty(building_name)) {
            et_building_name.setError(getString(R.string.error_field_required));
            focusView = et_building_name;
            cancel = true;

        }
        else if(building_name.toString().length() < 3){
            et_building_name.setError("Please enter a valid name");
            focusView = et_building_name;
            cancel = true;
        }



        else if (TextUtils.isEmpty(building_address)) {
            et_building_address.setError(getString(R.string.error_field_required));
            focusView = et_building_address;
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
            if(spiBuildingType.getSelectedItem().toString().equals("--Select Building Type--")||
                    spiBuildingCompletion.getSelectedItem().toString().equals("--Select Building Completion--")||
                    spiBuildingPurpose.getSelectedItem().toString().equals("--Select Building Purpose--")||
                    spiBuildingFunction.getSelectedItem().toString().equals("--Select Building Function--")||
                    spiBuildingOwnership.getSelectedItem().toString().equals("--Select Building Ownership--")||
                    spiBuildingOccupancy.getSelectedItem().toString().equals("--Select Building Occupancy--")||
                    spiBuildingOccupancyType.getSelectedItem().toString().equals("--Select Occupancy Type--")) {

                Toast.makeText(getApplicationContext(), "Please Select All Drop Downs", Toast.LENGTH_SHORT).show();
            }
            else {
                pDialog = new ProgressDialog(this);
                // Showing progress dialog before making http request
                pDialog.setMessage("Building Registration in progress...");
                pDialog.show();


                mRegTask = new RegisterUserTask(street, town, lga, ward, asset_type,
                        building_tag_number, building_name, building_address, building_type, building_completion,
                        building_purpose, building_function,
                        building_ownership, building_occupancy, building_occupancy_type);
                mRegTask.execute((Void) null);
            }

        }

    }

    public class RegisterUserTask extends AsyncTask<Void, Void, Boolean> {
        private final String mstreet,mtown,mlga, mward,massettype,
                mbuildingtagnumber, mbuildingname, mbuildingaddress, mbuildingtype, mbuildingcompletion,mbuildingpurpose,
                mbuildingfunction,mbuildingownership,mbuildingoccupancy, mbuildingoccupancytype;

        RegisterUserTask(String street, String town,String lga, String ward,String assettype,
                         String buildingtagnumber, String buildingname, String buildingaddress, String buildingtype, String buildingcompletion,
                         String buildingpurpose, String buildingfunction, String buildingownership, String buildingoccupancy, String buildingoccupancytype
        ) {
            mstreet = street;
            mtown = town;
            mlga = lga;
            mward = ward;
            massettype = assettype;
            mbuildingtagnumber = buildingtagnumber;
            mbuildingname = buildingname;
            mbuildingaddress=buildingaddress;
            mbuildingtype=buildingtype;
            mbuildingcompletion=buildingcompletion;
            mbuildingpurpose=buildingpurpose;
            mbuildingfunction=buildingfunction;
            mbuildingownership = buildingownership;
            mbuildingoccupancy = buildingoccupancy;
            mbuildingoccupancytype = buildingoccupancytype;


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
                /*String lat = null, lon = null;
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
                }*/


                long _rand1,tax_registration_id=0;
                   /* if(mexistingTAXID != null && !mexistingTAXID.isEmpty())
                    {
                        _rand1 = Long.parseLong(mexistingTAXID);
                    }
                    else {*/
                Random randi = new Random();
                _rand1 = (long) (randi.nextDouble() * 10000000000L);
                tax_registration_id = dataDB.myConnection(getApplicationContext()).countRecords("buildings")+_rand1+1;

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

                contentValues.put("land_rin",app.getLandrin());
                contentValues.put("building_rin", rin);
                contentValues.put("building", "building");
                contentValues.put("building_tag_number", mbuildingtagnumber);
                contentValues.put("building_name", mbuildingname);
                contentValues.put("building_number", mbuildingtagnumber);
                contentValues.put("building_address", mbuildingaddress);
                contentValues.put("street", mstreet);
                contentValues.put("town", mtown);
                contentValues.put("lga", mlga);
                contentValues.put("ward", mward);
                contentValues.put("asset_type", massettype);
                contentValues.put("building_type", mbuildingtype);
                contentValues.put("building_completion", mbuildingcompletion);
                contentValues.put("building_purpose", mbuildingpurpose);
                contentValues.put("building_function", mbuildingfunction);
                contentValues.put("building_ownership", mbuildingownership);
                contentValues.put("building_occupancy", mbuildingoccupancy);
                contentValues.put("Building_occupancy_type", mbuildingoccupancytype);
                /*contentValues.put("land_ownership", mlandownership);
                contentValues.put("tax_id", mtaxid);
                contentValues.put("land_ownership", mlandownership);
                contentValues.put("tax_id", mtaxid);*/
                //contentValues.put("tax_id", _rand1);
                String formattedDate = df.format(timer.getTime());
                contentValues.put("created_at", getDateTime);
                contentValues.put("created_by", app.getUserid());
//                contentValues.put("group_id", "100");
                contentValues.put("service_id", app.getServiceID());
                contentValues.put("status", "1");

               /* String mlongitude = a1+","+b1+","+c1+","+d1+","+e1+","+f1;
                String mlatitude = a2+","+b2+","+c2+","+d2+","+e2+","+f2;

                contentValues.put("longitude", mlongitude);
                contentValues.put("latitude", mlatitude);*/


//                    contentValues.put("user_id", ui);
                /*contentValues.put("street_id", streetv);
                contentValues.put("business_id", businessv);
                contentValues.put("building_id", buildingv);
*/

                    /*if (app.getServiceID() != null) {
                        contentValues.put("service_id", app.getServiceID());
                    }*/




                //db code


                    long rowInserted = dataDB.myConnection(getApplicationContext()).onInsertOrUpdate(contentValues, "buildings");
                    if (rowInserted != -1) {
                        Log.e(TAG, " rin number" + rin);

//                        app.setActiveTax_registration_id(String.valueOf(tax_registration_id));
                        /*app.setActiveTax_registration_id(rin);
                        ;
                        app.setActiveTIN(String.valueOf(_rand1));*/
                        Log.e(TAG, "New row added, row id: " + " to lands table");

                        app.setreg_building_building_id(rin);
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
                Log.i("buildingRegistrationLog", "Registration Successful");
                finish();

                app.setActiveActivity(AddBuilding.this);
                Intent qRIntent = new Intent(getApplicationContext(), CameraUseActivity.class);

//                Intent qRIntent = new Intent(getApplicationContext(), BuildingRegComplete.class);

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
                et_building_address.setError("Registration failed");
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

        et_street = (EditText) findViewById(R.id.et_street);
        et_asset_type = (EditText) findViewById(R.id.et_asset_type);

        et_town = (EditText) findViewById(R.id.et_town);
        et_lga = (EditText) findViewById(R.id.et_lga);
        et_ward = (EditText) findViewById(R.id.et_ward);

        et_building_tag_number = (EditText) findViewById(R.id.et_building_tag_number);
        et_building_name = (EditText) findViewById(R.id.et_building_name);
        et_building_address = (EditText) findViewById(R.id.et_building_address);

        spiBuildingType = (Spinner) findViewById(R.id.spiBuildingType);
        spiBuildingCompletion = (Spinner) findViewById(R.id.spiBuildingCompletion);
        spiBuildingPurpose = (Spinner) findViewById(R.id.spiBuildingPurpose);
        spiBuildingFunction = (Spinner) findViewById(R.id.spiBuildingFunction);
        spiBuildingOwnership = (Spinner) findViewById(R.id.spiBuildingOwnership);
        spiBuildingOccupancy = (Spinner) findViewById(R.id.spiBuildingOccupancy);
        spiBuildingOccupancyType = (Spinner) findViewById(R.id.spiBuildingOccupancyType);
        btn_registerBuilding = (Button) findViewById(R.id.btn_registerBuilding);

    }

    void populateSpinners(){

        List<String> buildingTypeCursor = dataDB.myConnection(this).universalSelect("building_types","building_type");

        dataBuildingType = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, buildingTypeCursor);
        dataBuildingType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiBuildingType.setAdapter(dataBuildingType);

        List<String> buildingCompletionCursor = dataDB.myConnection(this).universalSelect("building_completions","building_completion");

        dataBuildingCompletion = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, buildingCompletionCursor);
        dataBuildingCompletion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiBuildingCompletion.setAdapter(dataBuildingCompletion);

        List<String> buildingPurposeCursor = dataDB.myConnection(this).universalSelect("building_purposes","building_purpose");

        dataBuildingPurpose = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, buildingPurposeCursor);
        dataBuildingPurpose.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiBuildingPurpose.setAdapter(dataBuildingPurpose);

        List<String> buildingFunctionCursor = dataDB.myConnection(this).universalSelect("building_functions","building_function");

        dataBuildingFunction = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, buildingFunctionCursor);
        dataBuildingFunction.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiBuildingFunction.setAdapter(dataBuildingFunction);

        List<String> buildingOwnershipCursor = dataDB.myConnection(this).universalSelect("building_ownerships","building_ownership");

        dataBuildingOwnership = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, buildingOwnershipCursor);
        dataBuildingOwnership.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiBuildingOwnership.setAdapter(dataBuildingOwnership);

        List<String> buildingOccupancyCursor = dataDB.myConnection(this).universalSelect("building_occupancies","building_occupancy");

        dataBuildingOccupancy = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, buildingOccupancyCursor);
        dataBuildingOccupancy.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiBuildingOccupancy.setAdapter(dataBuildingOccupancy);

        List<String> buildingOccupancyTypeCursor = dataDB.myConnection(this).universalSelect("building_occupancy_types","building_occupancy_type");

        dataBuildingOccupancyType = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, buildingOccupancyTypeCursor);
        dataBuildingOccupancyType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiBuildingOccupancyType.setAdapter(dataBuildingOccupancyType);

    }









}
