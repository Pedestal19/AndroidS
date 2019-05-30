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
import android.widget.AdapterView;
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

import hosanna_techvibes.revcollectenumerator.MyLogin.LoginActivity;
import hosanna_techvibes.revcollectenumerator.databases.DBCheckAuthorization;
import hosanna_techvibes.revcollectenumerator.databases.DataDB;
import hosanna_techvibes.revcollectenumerator.helpers.GenericHelpers;
import hosanna_techvibes.revcollectenumerator.helpers.LocationMgr;
import hosanna_techvibes.revcollectenumerator.model.App;
import hosanna_techvibes.revcollectenumerator.model.CoordinateModel;
import hosanna_techvibes.revcollectenumerator.model.Taxpayer;

public class AddProperty extends AppCompatActivity {

    private String tpr;
    private RegisterUserTask mRegTask = null;
    private ProgressDialog pDialog;
    private View mProgressView;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
    Calendar timer = Calendar.getInstance();
    private Bitmap QRBit;
    String ia, ib, iti;
    String latitude = "unknown";
    String longitude = "unknown";

    String currentLat="0.0";
    String currentLon="0.0";

    String a1, b1,c1,d1,e1,f1,a2,b2,c2,d2,e2,f2;
    boolean cod1, cod2, cod3, cod4, cod5, cod6;

    CoordinateModel coordinateModel = new CoordinateModel();

    String successMssg;
    String errorMssg;

    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;
    // if GPS is enabled
    boolean isGPSEnabled = false;
    // if Network is enabled
    boolean isNetworkEnabled = false;

    EditText et_coordinate, et_street, et_landtagno, et_landname, et_landno,et_landaddress, et_town, et_lga, et_ward, et_taxid;
    Spinner spiAssetType, spiLandType, spiLandPurpose, spiLandFunction, spiLandOwnership, SpiState, SpiLGA, SpiWard;
    Button btn_takeCoordinates;

    private static final String TAG = "Add Property";
    ArrayAdapter dataAssetType, dataLandType, dataLandPurpose,dataLandFunction,dataLandOwnership, dataState, dataLGA, dataWard;
    App app;
    private ProgressDialog myDialog;
    DataDB dataDB = new DataDB();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property);
        app = ((App) getApplicationContext());

        getIds();
        populateSpinners();
        initializeLocationManager();

        if(app.getReg_mode()){
            et_street.setText("NA");

        }
        else {
            et_street.setText(app.getMySurname());
        }

        try {
            // Getting GPS status
            isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (isNetworkEnabled) {
                mLocationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                        mLocationListeners[1]);
                Log.e("GPS", "Network is enabled");
            } else {
                Log.e("GPS", "Network is disabled");
            }


        } catch (SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (isGPSEnabled) {
                Log.e("GPS", "GPS is enabled on GPS_Provider");
                mLocationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                        mLocationListeners[0]);
            }
            if (!isGPSEnabled) {
                Log.e("GPS", "GPS is disabled on GPS_Provider");
                // so asking user to open GPS
                //GenericHelper.askUserToOpenGPS(getApplicationContext());
                //GenericHelper.checkIfGPSIsON(getApplicationContext());
            }

        } catch (SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }



        btn_takeCoordinates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_SHORT).show();
//                alert3();
//                alert2();
                attemptUserRegistration();
            }
        });


        et_coordinate.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                String provider = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
                if(!provider.equals(""))
                {
                    //GPS is enabled
                    //Toast.makeText(getApplicationContext(), "GPS is enabled!", Toast.LENGTH_SHORT).show();
                    //call coordinate form
                    coordinateAlert();
                }
                else{
                    GenericHelpers.askUserToOpenGPS(AddProperty.this);
                }

            }
        });

        SpiState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
//                Toast.makeText(AddProperty.this, " si ::"+selectedItem, Toast.LENGTH_SHORT).show();
                populateLGATypeWithState(selectedItem);
                populateWardWithLGA("--Select Ward--");
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        SpiLGA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                populateWardWithLGA(selectedItem);
//                                 Toast.makeText(getApplicationContext(), "building wrong " +selectedItem, Toast.LENGTH_SHORT).show();

            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        et_taxid.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                alert3();
                return true;
            }
        });




    }

    private void attemptUserRegistration() {
        /*if (mRegTask != null) {
            return;
        }*/

        et_landaddress.setError(null);
        et_landname.setError(null);
        et_landno.setError(null);
//        et_lga.setError(null);
        et_town.setError(null);
        et_taxid.setError(null);


        boolean cancel = false;
        View focusView = null;


        String street = et_street.getText().toString();
        String landtagno = et_landtagno.getText().toString();
        String landname = et_landname.getText().toString();
        String landno = et_landno.getText().toString();
        String ward = SpiWard.getSelectedItem().toString();
        String taxid = et_taxid.getText().toString();
        String landaddress = et_landaddress.getText().toString();
        String town = et_town.getText().toString();
        String lga = SpiLGA.getSelectedItem().toString();
        String deviceLatitude=app.getLatitude();
        String deviceLongitude=app.getLongitude();
        String devicecod = et_coordinate.getText().toString();

        String assetType = spiAssetType.getSelectedItem().toString();
        String landType = spiLandType.getSelectedItem().toString();
        String landPurpose = spiLandPurpose.getSelectedItem().toString();
        String landFunction = spiLandFunction.getSelectedItem().toString();
        String landOwnership = spiLandOwnership.getSelectedItem().toString();

    /*    String x ="";
        String y ="";
        String z ="";

        if((spiStreet != null && spiStreet.getSelectedItem() !=null)&&(spiBuilding != null && spiBuilding.getSelectedItem() !=null)&&(spiBusiness != null && spiBusiness.getSelectedItem() !=null) ) {
            x =spiStreet.getSelectedItem().toString();
            y =spiBuilding.getSelectedItem().toString();
            z =spiBusiness.getSelectedItem().toString();
//            name = (String)spinnerName.getSelectedItem();
        }

*/


        if (TextUtils.isEmpty(landtagno)) {
            et_landtagno.setError(getString(R.string.error_field_required));
            focusView = et_landtagno;
            cancel = true;

        }
        if (TextUtils.isEmpty(landname)) {
            et_landname.setError(getString(R.string.error_field_required));
            focusView = et_landname;
            cancel = true;

        }
        else if(landname.toString().length() < 3){
            et_landname.setError("Please enter a valid Land name");
            focusView = et_landname;
            cancel = true;
        }



        else if (TextUtils.isEmpty(landaddress)) {
            et_landaddress.setError(getString(R.string.error_field_required));
            focusView = et_landaddress;
            cancel = true;
        }
        else if(landaddress.toString().length() < 3){
            et_landaddress.setError("Please enter a valid address");
            focusView = et_landaddress;
            cancel = true;
        }
        else if (TextUtils.isEmpty(town)) {
            et_town.setError(getString(R.string.error_field_required));
            focusView = et_town;
            cancel = true;
        }


        else if (TextUtils.isEmpty(taxid)) {
            et_taxid.setError(getString(R.string.error_field_required));
            focusView = et_taxid;
            cancel = true;
        }

        else if (TextUtils.isEmpty(taxid)) {
            et_taxid.setError(getString(R.string.error_field_required));
            focusView = et_taxid;
            cancel = true;
        }

        else if (TextUtils.isEmpty(devicecod)) {
            et_coordinate.setError(getString(R.string.error_field_required));
            focusView = et_coordinate;
            cancel = true;
            Toast.makeText(getApplicationContext(), "Please Take Property Coordinates", Toast.LENGTH_SHORT).show();
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
            if(spiLandOwnership.getSelectedItem().toString().equals("--Select Ownership--")||
                    spiLandFunction.getSelectedItem().toString().equals("--Select Function--")||
                    spiLandPurpose.getSelectedItem().toString().equals("--Select Land Purpose--")||
                    spiLandType.getSelectedItem().toString().equals("--Select Land Type--")||
                    spiAssetType.getSelectedItem().toString().equals("--Select Asset--")) {

                    Toast.makeText(getApplicationContext(), "Please Select All Drop Downs", Toast.LENGTH_SHORT).show();
            }
            else {
                pDialog = new ProgressDialog(this);
                // Showing progress dialog before making http request
                pDialog.setMessage("Land Registration in progress...");
                pDialog.show();


                mRegTask = new RegisterUserTask(street, landtagno, landname, landno, ward,
                        taxid, landaddress, town, lga, assetType, landType, landPurpose,
                        landFunction, landOwnership);
                mRegTask.execute((Void) null);
            }
            /*} else {
                GenericHelpers.askUserToOpenGPS(this);
            }*/


        }

        //}
        //else{
        // GenericHelpers.askUserToOpenGPS(RegisterActivity.this);
        //}

    }

    public class RegisterUserTask extends AsyncTask<Void, Void, Boolean> {
        private final String mstreet,mlandtagno,mlandname, mlandno,mward,
                mtaxid, mlandaddress, mtown, mlga, massettype,mlandtype,
                mlandpurpose,mlandfunction,mlandownership;
//        private final int mtitle, mgender, mnationality,mstate, mmaritalStatus;

        RegisterUserTask(String street, String landtagno,String landname, String landno,String ward,
                         String taxid, String landaddress, String town, String lga, String assettype,
                         String landtype, String landpurpose, String landfunction, String landownership
                         ) {
            mstreet = street;
            mlandtagno = landtagno;
            mlandname = landname;
            mlandno = landno;
            mward = ward;
            mtaxid = taxid;
            mlandaddress = landaddress;
            mtown=town;
            mlga=lga;
            massettype=assettype;
            mlandtype=landtype;
            mlandpurpose=landpurpose;
            mlandfunction = landfunction;
            mlandownership = landownership;


//            mexistingTAXID = existingTAXID;
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


                long _rand1,tax_registration_id=0;
                   /* if(mexistingTAXID != null && !mexistingTAXID.isEmpty())
                    {
                        _rand1 = Long.parseLong(mexistingTAXID);
                    }
                    else {*/
                Random randi = new Random();
                _rand1 = (long) (randi.nextDouble() * 10000000000L);
                tax_registration_id = dataDB.myConnection(getApplicationContext()).countRecords("lands")+_rand1+1;

                String rin = "LND"+tax_registration_id;
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
                contentValues.put("land_rin", rin);
                contentValues.put("land", "land");
                contentValues.put("land_tag_number", mlandtagno);
                contentValues.put("land_name", mlandname);
                contentValues.put("land_number", mlandno);
                contentValues.put("land_address", mlandaddress);
                contentValues.put("street", mstreet);
                contentValues.put("town", mtown);
                contentValues.put("lga", mlga);
                contentValues.put("ward", mward);
                contentValues.put("asset_type", massettype);
                contentValues.put("land_type", mlandtype);
                contentValues.put("land_purpose", mlandpurpose);
                contentValues.put("land_function", mlandfunction);
                contentValues.put("land_ownership", mlandownership);
                contentValues.put("tax_id", mtaxid);
                //contentValues.put("tax_id", _rand1);
                String formattedDate = df.format(timer.getTime());
                contentValues.put("created_at", getDateTime);
                contentValues.put("created_by", app.getUserid());
//                contentValues.put("group_id", "100");
                contentValues.put("service_id", app.getServiceID());
                contentValues.put("status", "1");

                String mlongitude = a1+","+b1+","+c1+","+d1+","+e1+","+f1;
                String mlatitude = a2+","+b2+","+c2+","+d2+","+e2+","+f2;

                contentValues.put("longitude", mlongitude);
                contentValues.put("latitude", mlatitude);


//                    contentValues.put("user_id", ui);
                /*contentValues.put("street_id", streetv);
                contentValues.put("business_id", businessv);
                contentValues.put("building_id", buildingv);
*/

                    /*if (app.getServiceID() != null) {
                        contentValues.put("service_id", app.getServiceID());
                    }*/




                //db code
                long taxidcount = dataDB.myConnection(getApplicationContext()).countRecordsWhere("tax_payers","taxpayer_rin",mtaxid);
                if (taxidcount > 0) {

                    long rowInserted = dataDB.myConnection(getApplicationContext()).onInsertOrUpdate(contentValues, "lands");
                    if (rowInserted != -1) {
                        Log.e(TAG, " rin number" + rin);

//                        app.setActiveTax_registration_id(String.valueOf(tax_registration_id));
                        app.setActiveTax_registration_id(rin);
                        ;
                        app.setActiveTIN(String.valueOf(_rand1));
                        Log.e(TAG, "New row added, row id: " + " to lands table");
                        app.setReg_mode(false);

                        //Database insert successful, give permission for app to continue
                        return true;
                    }
                }
                else if(taxidcount < 1){
                    errorMssg="3";
//                    alert3();
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
                Log.i("landRegistrationLog", "Registration Successful");
                finish();
                Intent qRIntent = new Intent(getApplicationContext(), LandRegComplete.class);

//                String dot=".";
                //String mfirstname2 = "r"+mfirstname;



                startActivity(qRIntent);
//                startActivity(new Intent(getApplicationContext(), RegistrationComplete.class));
//                Toast.makeText(getApplicationContext(), "Taxpayer has been registered", Toast.LENGTH_LONG).show();

            }
            else if(errorMssg.equals("3")){
                alert3();
            }
            else {
                Log.i("UserRegistrationLog", "Failed Registration");
                /*if(errorMssg!=null)
                {
                    edtLastname.setError(errorMssg);
                    et_taxid.requestFocus();
                }
                else {*/
                    et_taxid.setError("Registration failed");
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

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    void getIds(){
        et_street = (EditText) findViewById(R.id.et_street);
        et_landtagno = (EditText) findViewById(R.id.et_landtagno);
        et_landname = (EditText) findViewById(R.id.et_landname);
        et_landno = (EditText) findViewById(R.id.et_landno);
        et_landaddress = (EditText) findViewById(R.id.et_landaddress);
        et_town = (EditText) findViewById(R.id.et_town);
        et_lga = (EditText) findViewById(R.id.et_lga);
        et_ward = (EditText) findViewById(R.id.et_ward);
        et_taxid = (EditText) findViewById(R.id.et_taxid);
        spiAssetType = (Spinner) findViewById(R.id.spiAssetType);
        spiLandType = (Spinner) findViewById(R.id.spiLandType);
        spiLandPurpose = (Spinner) findViewById(R.id.spiLandPurpose);
        spiLandFunction = (Spinner) findViewById(R.id.spiLandFunction);
        spiLandOwnership = (Spinner) findViewById(R.id.spiLandOwnership);
        SpiState = (Spinner) findViewById(R.id.spiState);
        SpiLGA = (Spinner) findViewById(R.id.spiLGA);
        SpiWard = (Spinner) findViewById(R.id.spiWard);

        btn_takeCoordinates = (Button) findViewById(R.id.btn_takeCoordinates);
        et_coordinate = (EditText) findViewById(R.id.et_coordinates);
        et_coordinate.setHint("Take Coordinates");

    }

    void populateSpinners(){

        List<String> assetTypeCursor = dataDB.myConnection(this).selectAssetType();

        dataAssetType = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, assetTypeCursor);
        dataAssetType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiAssetType.setAdapter(dataAssetType);

        List<String> landTypeCursor = dataDB.myConnection(this).selectLandType();

        dataLandType = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, landTypeCursor);
        dataLandType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiLandType.setAdapter(dataLandType);

        List<String> landPurposeCursor = dataDB.myConnection(this).selectLandPurpose();

        dataLandPurpose = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, landPurposeCursor);
        dataLandPurpose.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiLandPurpose.setAdapter(dataLandPurpose);

        List<String> landFunctionCursor = dataDB.myConnection(this).selectLandFunction();

        dataLandFunction = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, landFunctionCursor);
        dataLandFunction.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiLandFunction.setAdapter(dataLandFunction);

        List<String> landOwnershipCursor = dataDB.myConnection(this).selectLandOwnership();

        dataLandOwnership = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, landOwnershipCursor);
        dataLandOwnership.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiLandOwnership.setAdapter(dataLandOwnership);

//        List<String> stateCursor = dataDB.myConnection(this).universalSelect("states","name");
        List<String> stateCursor = dataDB.myConnection(this).selectState();


        dataState = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, stateCursor);
        dataState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpiState.setAdapter(dataState);

        List<String> lgaCursor = dataDB.myConnection(this).selectLgaLimit();

        dataLGA = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, lgaCursor);
        dataLGA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpiLGA.setAdapter(dataLGA);

        List<String> wardCursor = dataDB.myConnection(this).selectWardLimit();

        dataWard = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, wardCursor);
        dataWard.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpiWard.setAdapter(dataWard);

    }

    void alert(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title");

// Set up the input
        final EditText input = new EditText(this);
        final EditText input2 = new EditText(this);
        final EditText input3 = new EditText(this);
        final RadioButton input4 = new RadioButton(this);


// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        input2.setInputType(InputType.TYPE_CLASS_PHONE);
        builder.setView(input2);
        builder.setView(input4);
// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String m_Text = input.getText().toString();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    void alert2(){
        //Preparing views
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_layout, (ViewGroup) findViewById(R.id.layout_root));
//layout_root should be the name of the "top-level" layout node in the dialog_layout.xml file.










        //Building dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                final EditText taxpayername = (EditText) layout.findViewById(R.id.et_taxpayername);
                final EditText mobile = (EditText) layout.findViewById(R.id.et_mobile);
                final RadioGroup radioEA = (RadioGroup) layout.findViewById(R.id.radioEconomicActivity);
                final RadioGroup radioTT = (RadioGroup) layout.findViewById(R.id.radioTaxPayerType);


                // get selected radio button from radioGroup
                int selectedId = radioEA.getCheckedRadioButtonId();
                int selectedId2 = radioTT.getCheckedRadioButtonId();


                // find the radiobutton by returned id
                RadioButton EA = (RadioButton)layout.findViewById(selectedId);
                RadioButton TT = (RadioButton)layout.findViewById(selectedId2);
                


                ContentValues tp = new ContentValues();

                tp.put("taxpayer_name", taxpayername.getText().toString());
                tp.put("mobile_number", mobile.getText().toString());
                tp.put("tax_payer_type", TT.getText().toString());
                tp.put("economic_activity", EA.getText().toString());
                tp.put("service_id", app.getServiceID());

                long _rand1,tax_registration_id=0;
                   /* if(mexistingTAXID != null && !mexistingTAXID.isEmpty())
                    {
                        _rand1 = Long.parseLong(mexistingTAXID);
                    }
                    else {*/
                Random randi = new Random();
                _rand1 = (long) (randi.nextDouble() * 10000000000L);
//                    }
                tax_registration_id = dataDB.myConnection(getApplicationContext()).countRecords("tax_payers")+_rand1+1;
                String taxpayer_rin = "RIN"+tax_registration_id;

                tp.put("taxpayer_rin", taxpayer_rin);


                long rowInserted = dataDB.myConnection(getApplicationContext()).onInsertOrUpdate(tp, "tax_payers");
                if (rowInserted != -1) {
//                        app.setActiveTax_registration_id(String.valueOf(tax_registration_id));
                    taxpayer_reg_success_alert(taxpayername.getText().toString(), taxpayer_rin);
                    Toast.makeText(AddProperty.this, "Taxpayer Saved!!!", Toast.LENGTH_SHORT).show();
                }
                //save info where you want it
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void alert3(){

        final android.support.v7.app.AlertDialog.Builder adb = new android.support.v7.app.AlertDialog.Builder(
                this);
        adb.setTitle("Taxpayer Validation Failed");
        adb.setIcon(R.mipmap.ic_launcher);
        adb.setMessage("Application cannot find Taxpayer based on inputed ID");
        adb.setNeutralButton("Search", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startActivityForResult(new Intent(getApplicationContext(), SearchTaxpayerWidget.class),1);
            }
        });
        adb.setNegativeButton("Re-Input ID", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //startActivity(new Intent(getApplicationContext(),PhotoRegistration.class));
                dialog.dismiss();
//            et_taxid.setFocusable(true);
                et_taxid.requestFocus();
            }
        });

        adb.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //startActivity(new Intent(getApplicationContext(),PhotoRegistration.class));
                alert2();
            }
        });
        adb.show();

    }

    void taxpayer_reg_success_alert(String name,String taxid){

        final android.support.v7.app.AlertDialog.Builder adb = new android.support.v7.app.AlertDialog.Builder(
                this);
        adb.setTitle("Taxpayer Registration Success");
        adb.setIcon(R.mipmap.ic_launcher);
        adb.setMessage("Taxpayer Registration is complete.\nName: "+name+"\nID: "+taxid);
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                /*et_taxid.setText(taxid);
                et_taxid.requestFocus();*/
                dialog.dismiss();
            }

        });

        adb.show();

        et_taxid.setText(taxid);
        et_taxid.requestFocus();

    }

    void coordinateAlert(){
        //Preparing views
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout2 = inflater.inflate(R.layout.dialog_layout2, (ViewGroup) findViewById(R.id.layout_root));
//layout_root should be the name of the "top-level" layout node in the dialog_layout.xml file.


        //Building dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(AddProperty.this);
        builder.setView(layout2);
/*        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();*/

        AlertDialog dialog = builder.create();
        dialog.show();

                final Button button1 = (Button) layout2.findViewById(R.id.button1);
                final Button button2 = (Button) layout2.findViewById(R.id.button2);
                final Button button3 = (Button) layout2.findViewById(R.id.button3);
                final Button button4 = (Button) layout2.findViewById(R.id.button4);
                final Button button5 = (Button) layout2.findViewById(R.id.button5);
                final Button button6 = (Button) layout2.findViewById(R.id.button6);
                final Button btn_finish = (Button) layout2.findViewById(R.id.btn_finish);
                Tracker mylistner = new Tracker(getApplicationContext());


                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {/*
//                        getCoordinates();
//                        Tracker mylistner = new Tracker(getApplicationContext());
                        double lat = mylistner.latitude;
                        double lon = mylistner.longitude;
                        String geo = mylistner.getGeo();
//                        Toast.makeText(AddProperty.this, " is  lat:: "+lat+" and long "+lon , Toast.LENGTH_SHORT).show();
                        Toast.makeText(AddProperty.this, " geo:: "+geo , Toast.LENGTH_SHORT).show();*/
                        Toast.makeText(AddProperty.this, " :: "+ currentLat
                                +" :: "+currentLon , Toast.LENGTH_SHORT).show();
                        a1=currentLon;
                        a2=currentLat;
                        cod1=true;
                        button1.setText("1st Coordinate Taken");
                        button1.setEnabled(false);
                    }
                });
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {/*
                        double lat = mylistner.latitude;
                        double lon = mylistner.longitude;
                        String geo = mylistner.getGeo();
                        Toast.makeText(AddProperty.this, " is  lat:: "+lat+" and long "+lon , Toast.LENGTH_SHORT).show();
//                        Toast.makeText(AddProperty.this, " geo:: "+geo , Toast.LENGTH_SHORT).show();
*/                      if(cod1) {
                        Toast.makeText(AddProperty.this, " :: " + currentLat
                                + " :: " + currentLon, Toast.LENGTH_SHORT).show();

                        b1 = currentLon;
                        b2 = currentLat;
                        cod2=true;
                        button2.setText("2nd Coordinate Taken");
                        button2.setEnabled(false);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Please take coordinate for previous point(s)", Toast.LENGTH_SHORT).show();
                    }
                    }
                });
                button3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    if(cod1&&cod2) {
                        Toast.makeText(AddProperty.this, " :: " + currentLat
                                + " :: " + currentLon, Toast.LENGTH_SHORT).show();

                        c1 = currentLon;
                        c2 = currentLat;
                        cod3=true;
                        button3.setText("3rd Coordinate Taken");
                        button3.setEnabled(false);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Please take coordinate for previous point(s)", Toast.LENGTH_SHORT).show();

                    }
                    }
                });
                button4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (cod1 && cod2 && cod3) {
                            Toast.makeText(AddProperty.this, " :: " + currentLat
                                    + " :: " + currentLon, Toast.LENGTH_SHORT).show();

                            d1 = currentLon;
                            d2 = currentLat;
                            cod4=true;
                            button4.setText("4th Coordinate Taken");
                            button4.setEnabled(false);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Please take coordinate for previous point(s)", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                button5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(cod1 && cod2 && cod3 && cod4) {
                            Toast.makeText(AddProperty.this, " :: " + currentLat
                                    + " :: " + currentLon, Toast.LENGTH_SHORT).show();

                            e1 = currentLon;
                            e2 = currentLat;
                            cod5=true;
                            button5.setText("5th Coordinate Taken");
                            button5.setEnabled(false);

                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Please take coordinate for previous point(s)", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                button6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    if(cod1 && cod2 && cod3 && cod4 && cod5) {
                        Toast.makeText(AddProperty.this, " :: " + currentLat
                                + " :: " + currentLon, Toast.LENGTH_SHORT).show();
                        f1 = currentLon;
                        f2 = currentLat;
                        cod6=true;
                        button6.setText("6th Coordinate Taken");
                        button6.setEnabled(false);

                    }
                        else {
                        Toast.makeText(getApplicationContext(), "Please take coordinate for previous point(s)", Toast.LENGTH_SHORT).show();

                    }
                    }
                });
                btn_finish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    if(cod1 && cod2 && cod3 && cod4 && cod5 && cod6){

//                        et_coordinate.requestFocus();
                        et_coordinate.setHint(null);
                 /*       et_coordinate.setHint("Coordinates Taken");
                        et_coordinate.setHintTextColor(getResources().getColor(R.color.primary_dark));*/

                        et_coordinate.setText("Coordinates Taken");
                        et_coordinate.setTextColor(getResources().getColor(R.color.primary_dark));

                       /* et_coordinate.setText("Coordinates Taken");
                        et_coordinate.setTextColor(getResources().getColor(R.color.primary_dark));
*/                        /*et_coordinate.setOnTouchListener(new View.OnTouchListener() {

                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                et_coordinate.setHint("it worked");
                                return false;
                            }

                        });*/
                        et_coordinate.setError(null);
                        et_coordinate.setEnabled(false);
                        dialog.dismiss();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Please take all coordinates", Toast.LENGTH_SHORT).show();
                    }
//                        coordinatecheckalert();
                    }
                });



                //save info where you want it
           /* }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });*/

    }


    void getCoordinates(){

        Log.e("else", " latitude:" + latitude + " longitude:" + longitude);


        if (coordinateModel.getLatitude() == null || coordinateModel.getLongitude() == null) {
            LocationMgr man = new LocationMgr();
            String locMan = man.getMyLoc(getApplicationContext());
            if (!locMan.equalsIgnoreCase("err")) {
                String[] splitedCoordinate = GenericHelpers.splitString(locMan, ":");
                Log.e("My LcMan", locMan);
                Log.e("My LocMann", " latitude:" + splitedCoordinate[0] + " longitude:" + splitedCoordinate[1]);
                latitude = splitedCoordinate[0];
                longitude = splitedCoordinate[1];
                Toast.makeText(this, "is  lat::"+splitedCoordinate[0], Toast.LENGTH_SHORT).show();
            }
        } else {
            latitude = coordinateModel.getLatitude();
            longitude = coordinateModel.getLongitude();
            Log.e("else", latitude);
            Log.e("else", " latitude:" + latitude + " longitude:" + longitude);
            Toast.makeText(this, "es  lat::"+latitude, Toast.LENGTH_SHORT).show();


        }

    }

  /*  @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "resumed", Toast.LENGTH_SHORT).show();
    }

*/
    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged: " + location);

            Log.e("**Latitude: ", String.valueOf(location.getLatitude()));
            Log.e("**Longitude: ", String.valueOf(location.getLongitude()));
            coordinateModel.setLatitude(String.valueOf(location.getLatitude()));
            coordinateModel.setLongitude(String.valueOf(location.getLongitude()));
            currentLat=String.valueOf(location.getLatitude());
            currentLon=String.valueOf(location.getLongitude());
            mLastLocation.set(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.NETWORK_PROVIDER),
            new LocationListener(LocationManager.GPS_PROVIDER)
    };



    private void toggleGPS(Context context, boolean enable) {
        String provider = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(provider.contains("gps") == enable) {
            return; // the GPS is already in the requested state
        }

        final Intent poke = new Intent();
        poke.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
        poke.setData(Uri.parse("3"));
        context.sendBroadcast(poke);
    }

    void coordinatecheckalert(){

        final android.support.v7.app.AlertDialog.Builder adb = new android.support.v7.app.AlertDialog.Builder(
                this);
        adb.setTitle("List of Coordinates");
        adb.setIcon(R.mipmap.ic_launcher);
        adb.setMessage("--1.Lat."+a1+" Long : "+a2+"\n" +
                "--2.Lat "+b1+" Long : "+b2+"\n" +
                "--3.Lat "+c1+" Long : "+c2+"\n" +
                "--4.Lat "+d1+" Long : "+d2+"\n" +
                "--5.Lat "+e1+" Long : "+e2+"\n" +
                "--6.Lat "+f1+" Long : "+f2);
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                /*et_taxid.setText(taxid);
                et_taxid.requestFocus();*/
                dialog.dismiss();
            }

        });

        adb.show();



    }

    //back button overide method
    public void backButtonHandler() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                this);
        // Setting Dialog Title
        alertDialog.setTitle("End Reg?");

        // Setting Dialog Message
        alertDialog.setMessage("Going back would make you lose your already filled form data\n do you still want to go back?");
        // Setting Icon to Dialog
        alertDialog.setIcon(R.mipmap.ic_launcher);
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();

//                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
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

    @Override
    public void onBackPressed() {

        //Display alert message when back button has been pressed
        backButtonHandler();


    }

    private void populateLGATypeWithState(String _State)
    {
        if (!_State.equals(null)) {
            // do your stuff
            String what = "id";
            String from = "states";
            String where = "name";
            final String y = dataDB.myConnection(getApplicationContext()).selectFromTable(what, from, where, _State);
//            Toast.makeText(this, " id "+y, Toast.LENGTH_SHORT).show();

            List<String> BuildingTypeCursor = dataDB.myConnection(AddProperty.this).selectLGA3(y);

            ArrayAdapter dataBuildingType = new ArrayAdapter<String>(AddProperty.this, R.layout.support_simple_spinner_dropdown_item, BuildingTypeCursor);
            dataBuildingType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            SpiLGA.setAdapter(dataBuildingType);
        }
    }


    private void populateWardWithLGA(String _Building)
    {
        if (!_Building.equals(null)) {
            // do your stuff
            String what = "lga_id";
            String from = "local_goverment_area";
            String where = "lga";
            final String y = dataDB.myConnection(getApplicationContext()).selectFromTable(what, from, where, _Building);

//            Toast.makeText(getApplicationContext(), y, Toast.LENGTH_SHORT).show();
            List<String> BusinessTypeCursor = dataDB.myConnection(AddProperty.this).selectWard(y);

            ArrayAdapter dataBusinessType = new ArrayAdapter<String>(AddProperty.this, R.layout.support_simple_spinner_dropdown_item, BusinessTypeCursor);
            dataBusinessType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            SpiWard.setAdapter(dataBusinessType);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
//        Toast.makeText(this, "urfada", Toast.LENGTH_SHORT).show();

        if (requestCode == 1) {
            String tax_payer_name = data.getStringExtra("name");
            String tax_payer_rin = data.getStringExtra("rin");
            tpr = tax_payer_rin;
            et_taxid.setText(tax_payer_rin);
//            Toast.makeText(this, tax_payer_rin, Toast.LENGTH_SHORT).show();

        }
    }

   /* @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "onresume", Toast.LENGTH_SHORT).show();
        et_taxid.setText(tpr);
    }*/
}
