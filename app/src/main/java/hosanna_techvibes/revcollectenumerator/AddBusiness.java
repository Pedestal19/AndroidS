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
import java.util.ArrayList;
import java.util.Arrays;
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

public class AddBusiness extends AppCompatActivity {

    private RegisterUserTask mRegTask = null;
    private ProgressDialog pDialog;
    private View mProgressView;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
    Calendar timer = Calendar.getInstance();
    String ia, ib, iti;
    String latitude = "unknown";
    String longitude = "unknown";
    private String profile = null;
    private String taxpayeritems = "";
    private String taxpayer_rin="null";

    String successMssg;
    String errorMssg;

    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;
    // if GPS is enabled
    boolean isGPSEnabled = false;
    // if Network is enabled
    boolean isNetworkEnabled = false;

    EditText et_apartmentno, et_business_name, et_business_size, et_business_email, et_business_number,
            et_contact_person, et_taxid;
    Spinner spiBusinessType, spiAssetType, spiBusinessCategory, spiBusinessSector, spiBusinessSubSector;
    Spinner spiBusinessOperation, spiBusinessStructure, spiProfilePref, SpiState, SpiLGA;
    Button btn_registerBusiness, btn_assessment_rules;

    private static final String TAG = "Add Business";
    ArrayAdapter dataBusinessType, dataAssetType, dataBusinessCategory, dataBusinessSector, dataBusinessSubSector,
            dataBusinessOperation, dataBusinessStructure, dataProfile,  dataState, dataLGA;

    App app;
    private ProgressDialog myDialog;
    DataDB dataDB = new DataDB();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_business);
        app = ((App) getApplicationContext());

        getIds();
        populateSpinners();


        if (app.getReg_mode()) {
            et_apartmentno.setText("NA");
        } else {
            et_apartmentno.setText(app.getMySurname());
            et_taxid.setText(getTaxID(app.getAid()));


        }

        SpiState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), " si ::"+selectedItem, Toast.LENGTH_SHORT).show();
                populateLGATypeWithState(selectedItem);
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btn_assessment_rules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_SHORT).show();
//                alert3();
//                alert2();
                assessment_rules();
            }
        });

        btn_registerBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_SHORT).show();
//                alert3();
//                alert2();
                attemptUserRegistration();
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

        et_business_name.setError(null);
        et_business_size.setError(null);
        et_business_email.setError(null);
        et_business_number.setError(null);
        et_contact_person.setError(null);


        boolean cancel = false;
        View focusView = null;


        String business_name = et_business_name.getText().toString();
        String business_size = et_business_size.getText().toString();
        String business_email = et_business_email.getText().toString();
        String business_number = et_business_number.getText().toString();
        String contact_person = et_contact_person.getText().toString();


        String apartment_no = et_apartmentno.getText().toString();
        String lga = SpiLGA.getSelectedItem().toString();

        String business_type = spiBusinessType.getSelectedItem().toString();
        String asset_type = spiAssetType.getSelectedItem().toString();
        String business_category = spiBusinessCategory.getSelectedItem().toString();
        String business_sector = spiBusinessSector.getSelectedItem().toString();
        String business_sub_sector = spiBusinessSubSector.getSelectedItem().toString();
        String business_operation = spiBusinessOperation.getSelectedItem().toString();
        String business_structure = spiBusinessStructure.getSelectedItem().toString();
        String taxid = et_taxid.getText().toString();
        profile = spiProfilePref.getSelectedItem().toString();


        if (TextUtils.isEmpty(business_name)) {
            et_business_name.setError(getString(R.string.error_field_required));
            focusView = et_business_name;
            cancel = true;

        }
        if (TextUtils.isEmpty(business_size)) {
            et_business_size.setError(getString(R.string.error_field_required));
            focusView = et_business_size;
            cancel = true;

        } else if (business_name.toString().length() < 3) {
            et_business_name.setError("Please enter a valid name");
            focusView = et_business_name;
            cancel = true;
        } else if (TextUtils.isEmpty(business_number)) {
            et_business_number.setError(getString(R.string.error_field_required));
            focusView = et_business_number;
            cancel = true;
        } else if (TextUtils.isEmpty(business_number)) {
            et_business_number.setError(getString(R.string.error_field_required));
            focusView = et_business_number;
            cancel = true;
        } else if (TextUtils.isEmpty(taxid)) {
            et_taxid.setError(getString(R.string.error_field_required));
            focusView = et_taxid;
            cancel = true;
        }
        else if(spiBusinessType.getSelectedItem().toString().equals("--Select Business Type--")||
                spiAssetType.getSelectedItem().toString().equals("--Select Asset Type--")||
//                    spiBusinessCategory.getSelectedItem().toString().equals("--Select Business Category--")||
                spiBusinessSector.getSelectedItem().toString().equals("--Select Business Sector--")||
                spiBusinessSubSector.getSelectedItem().toString().equals("--Select Business Sub Sector--")||
                spiBusinessOperation.getSelectedItem().toString().equals("--Select Business Operation--")||
                spiBusinessStructure.getSelectedItem().toString().equals("--Select Business Structure--"))
        {
            cancel = true;
            focusView = et_taxid;
            Toast.makeText(getApplicationContext(), "Please Select All Drop Downs", Toast.LENGTH_SHORT).show();
        }
        else if(taxpayeritems.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please Set Assessment Rules", Toast.LENGTH_SHORT).show();
            focusView = et_taxid;
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

            //else {
            pDialog = new ProgressDialog(this);
            // Showing progress dialog before making http request
            pDialog.setMessage("Business Registration in progress...");
            pDialog.show();


            mRegTask = new RegisterUserTask(business_name, business_size, business_email, business_number, contact_person,
                    apartment_no, business_type, asset_type, business_category, business_sector,
                    business_sub_sector, business_operation, business_structure, profile, taxid,lga);
            mRegTask.execute((Void) null);
            //}

        }

    }

    public class RegisterUserTask extends AsyncTask<Void, Void, Boolean> {
        private final String mbusiness_name, mbusiness_size, mbusiness_email, mbusiness_number, mcontact_person,
                mapartment_no, mbusiness_type, masset_type, mbusiness_category, mbusiness_sector,
                mbusiness_sub_sector, mbusiness_operation, mbusiness_structure, mprofile, mtaxid, mlga;

        RegisterUserTask(String business_name, String business_size, String business_email, String business_number, String contact_person,
                         String apartment_no, String business_type, String asset_type, String business_category, String business_sector,
                         String business_sub_sector, String business_operation, String business_structure, String profile, String taxid, String lga) {
            mbusiness_name = business_name;
            mbusiness_size = business_size;
            mbusiness_email = business_email;
            mbusiness_number = business_number;
            mcontact_person = contact_person;
            mapartment_no = apartment_no;
            mbusiness_type = business_type;
            masset_type = asset_type;
            mbusiness_category = business_category;
            mbusiness_sector = business_sector;
            mbusiness_sub_sector = business_sub_sector;
            mbusiness_operation = business_operation;
            mbusiness_structure = business_structure;
            mprofile = profile;
            mtaxid = taxid;
            mlga = lga;
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


                long _rand1, tax_registration_id = 0;
                   /* if(mexistingTAXID != null && !mexistingTAXID.isEmpty())
                    {
                        _rand1 = Long.parseLong(mexistingTAXID);
                    }
                    else {*/
                Random randi = new Random();
                _rand1 = (long) (randi.nextDouble() * 10000000000L);
                tax_registration_id = dataDB.myConnection(getApplicationContext()).countRecords("businesses") + _rand1 + 1;

                String rin = "RIN" + tax_registration_id;
//                    }
                //app.setActiveTIN(String.valueOf(_rand1));
                String getDateTime = dataDB.getDateTime(getApplicationContext());
                Log.e("date time:::", getDateTime);


                // TODO: 11/10/2016 save user data
                ContentValues contentValues = new ContentValues();

                contentValues.put("business_rin", rin);
                contentValues.put("business_type", mbusiness_type);
                contentValues.put("asset_type", masset_type);
                contentValues.put("business_name", mbusiness_name);
                contentValues.put("business_category", mbusiness_category);
                contentValues.put("business_sector", mbusiness_sector);
                contentValues.put("business_sub_sector", mbusiness_sub_sector);
                contentValues.put("business_structure", mbusiness_structure);
                contentValues.put("business_operation", mbusiness_operation);
                contentValues.put("business_size", mbusiness_size);
                contentValues.put("taxpayer_rin", taxpayer_rin);
                contentValues.put("contact_person", mcontact_person);
                contentValues.put("businessnumber", mbusiness_number);
                contentValues.put("business_email", mbusiness_email);
                if (app.getReg_mode()) {
                    contentValues.put("apartment_id", "000");
                } else {
//                    contentValues.put("apartment_id", mapartment_no);
                    contentValues.put("apartment_id", app.getAid());

                }
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
                long taxidcount = dataDB.myConnection(getApplicationContext()).countRecordsWhere("tax_payers", "taxpayer_rin", mtaxid);

                String str = "...";
//                List<String> taxpayeritemsList = Arrays.asList(taxpayeritems.split(","));
                ArrayList<String> taxpayeritemsList = new ArrayList<String>(Arrays.asList(taxpayeritems.split(",")));
                if (taxidcount > 0) {
                    long rowInserted = dataDB.myConnection(getApplicationContext()).onInsertOrUpdate(contentValues, "businesses");
                    if (rowInserted != -1) {
                        Log.e(TAG, " rin number" + rin);

//                        app.setActiveTax_registration_id(String.valueOf(tax_registration_id));
                        /*app.setActiveTax_registration_id(rin);
                        ;
                        app.setActiveTIN(String.valueOf(_rand1));*/
                        Log.e(TAG, "New row added, row id: " + " to lands table");
                        app.setBID(rin);
                        app.setReg_mode(false);

                        for (int i = 0; i < taxpayeritemsList.size(); i++) {
                            String current_item = taxpayeritemsList.get(i);

                            ContentValues taxpayeritemsdata = new ContentValues();
                            taxpayeritemsdata.put("taxpayer_rin", rin);
                            taxpayeritemsdata.put("profile_ref", profile);
                            taxpayeritemsdata.put("assessment_item_name", current_item);
                            taxpayeritemsdata.put("service_id", app.getServiceID());
//                        taxpayeritemsdata.put("back_up", "0");


                            long rowInserted2 = dataDB.myConnection(getApplicationContext()).onInsertOrUpdate(taxpayeritemsdata, "taxpayer_items");

                            if (rowInserted2 != -1) {

                            }
                        }
                        //Database insert successful, give permission for app to continue
                        return true;
                    }
                } else if (taxidcount < 1) {
                    errorMssg = "3";
//                    alert3();
                } else {
                    //errorMssg = httpResp.getResponseData();
                    // Toast.makeText(getApplicationContext(), "Something wrong", Toast.LENGTH_SHORT).show();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


            return false;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mRegTask = null;
            hidePDialog();

            Log.i("bizRegistrationLog", "error mssg:"+errorMssg);

            if (success) {
                Log.i("bizRegistrationLog", "Registration Successful");
                finish();
                app.setActiveActivity(AddBusiness.this);
                Intent qRIntent = new Intent(getApplicationContext(), CameraUseActivity.class);

//                String dot=".";
                //String mfirstname2 = "r"+mfirstname;


                startActivity(qRIntent);
//                startActivity(new Intent(getApplicationContext(), RegistrationComplete.class));
//                Toast.makeText(getApplicationContext(), "Taxpayer has been registered", Toast.LENGTH_LONG).show();

            } else if (errorMssg != null && errorMssg.equals("3")) {
                alert3();
            } else {
                Log.i("bizRegistrationLog", "Failed Registration");

                et_contact_person.setError("Registration failed");
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


    void getIds() {

        et_apartmentno = (EditText) findViewById(R.id.et_apartmentno);
        et_business_name = (EditText) findViewById(R.id.et_business_name);

        et_business_size = (EditText) findViewById(R.id.et_business_size);
        et_business_email = (EditText) findViewById(R.id.et_business_email);
        et_business_number = (EditText) findViewById(R.id.et_business_number);
        et_contact_person = (EditText) findViewById(R.id.et_contact_person);

        spiBusinessType = (Spinner) findViewById(R.id.spiBusinessType);
        spiAssetType = (Spinner) findViewById(R.id.spiAssetType);
        spiBusinessCategory = (Spinner) findViewById(R.id.spiBusinessCategory);

        spiBusinessSector = (Spinner) findViewById(R.id.spiBusinessSector);
        spiBusinessSubSector = (Spinner) findViewById(R.id.spiBusinessSubSector);

        spiBusinessOperation = (Spinner) findViewById(R.id.spiBusinessOperation);
        spiProfilePref = (Spinner) findViewById(R.id.spiProfiles);
        SpiState = (Spinner) findViewById(R.id.spiState);
        SpiLGA = (Spinner) findViewById(R.id.spiLGA);
        btn_assessment_rules = (Button) findViewById(R.id.btn_assessment_rules);

        btn_registerBusiness = (Button) findViewById(R.id.btn_registerBusiness);

        spiBusinessStructure = (Spinner) findViewById(R.id.spiBusinessStructure);

        et_taxid = (EditText) findViewById(R.id.et_taxid);


    }

    void populateSpinners() {

        List<String> stateCursor = dataDB.myConnection(this).selectState();


        dataState = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, stateCursor);
        dataState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpiState.setAdapter(dataState);

        List<String> lgaCursor = dataDB.myConnection(this).selectLgaLimit();

        dataLGA = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, lgaCursor);
        dataLGA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpiLGA.setAdapter(dataLGA);


        List<String> businessTypeCursor = dataDB.myConnection(this).universalSelect("business_types", "business_type");

        dataBusinessType = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, businessTypeCursor);
        dataBusinessType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiBusinessType.setAdapter(dataBusinessType);

        List<String> assetTypeCursor = dataDB.myConnection(this).selectAssetType();

        dataAssetType = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, assetTypeCursor);
        dataAssetType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiAssetType.setAdapter(dataAssetType);

        List<String> businessCategoryCursor = dataDB.myConnection(this).universalSelect("business_categories", "business_category");

        dataBusinessCategory = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, businessCategoryCursor);
        dataBusinessCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiBusinessCategory.setAdapter(dataBusinessCategory);

        List<String> businessSectorCursor = dataDB.myConnection(this).universalSelect("business_sectors", "business_sector");

        dataBusinessSector = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, businessSectorCursor);
        dataBusinessSector.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiBusinessSector.setAdapter(dataBusinessSector);

        List<String> businessSubSectorCursor = dataDB.myConnection(this).universalSelect("business_sub_sectors", "business_sub_sector");

        dataBusinessSubSector = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, businessSubSectorCursor);
        dataBusinessSubSector.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiBusinessSubSector.setAdapter(dataBusinessSubSector);

        List<String> businessOperationsCursor = dataDB.myConnection(this).universalSelect("business_operations", "business_operation");

        dataBusinessOperation = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, businessOperationsCursor);
        dataBusinessOperation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiBusinessOperation.setAdapter(dataBusinessOperation);

        List<String> businessStructureCursor = dataDB.myConnection(this).universalSelect("business_structures", "business_structure");

        dataBusinessStructure = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, businessStructureCursor);
        dataBusinessStructure.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiBusinessStructure.setAdapter(dataBusinessStructure);

        List<String> profileCursor = dataDB.myConnection(this).universalSelect("profiles", "profile_ref");

        dataProfile = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, profileCursor);
        dataProfile.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiProfilePref.setAdapter(dataProfile);


    }


    private void assessment_rules() {
        profile = spiProfilePref.getSelectedItem().toString();

        if (!(profile.equals("--Select Profile--")) && profile != null) {
            Intent intent = new Intent(getApplicationContext(), SelectItems.class);
            intent.putExtra("profile", profile);
            startActivityForResult(intent, 2);

        } else {
            Toast.makeText(getApplicationContext(), "Please Select a Profile", Toast.LENGTH_SHORT).show();

        }
    }

    void alert3() {

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

    void alert2() {
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
                RadioButton EA = (RadioButton) layout.findViewById(selectedId);
                RadioButton TT = (RadioButton) layout.findViewById(selectedId2);


                ContentValues tp = new ContentValues();

                tp.put("taxpayer_name", taxpayername.getText().toString());
                tp.put("mobile_number", mobile.getText().toString());
                tp.put("tax_payer_type", TT.getText().toString());
                tp.put("economic_activity", EA.getText().toString());
                tp.put("service_id", app.getServiceID());

                long _rand1, tax_registration_id = 0;
                   /* if(mexistingTAXID != null && !mexistingTAXID.isEmpty())
                    {
                        _rand1 = Long.parseLong(mexistingTAXID);
                    }
                    else {*/
                Random randi = new Random();
                _rand1 = (long) (randi.nextDouble() * 10000000000L);
//                    }
                tax_registration_id = dataDB.myConnection(getApplicationContext()).countRecords("tax_payers") + _rand1 + 1;
                taxpayer_rin = "RIN" + tax_registration_id;

                tp.put("taxpayer_rin", taxpayer_rin);


                long rowInserted = dataDB.myConnection(getApplicationContext()).onInsertOrUpdate(tp, "tax_payers");
                if (rowInserted != -1) {
//                        app.setActiveTax_registration_id(String.valueOf(tax_registration_id));
                    taxpayer_reg_success_alert(taxpayername.getText().toString(), taxpayer_rin);
                    Toast.makeText(AddBusiness.this, "Taxpayer Saved!!!", Toast.LENGTH_SHORT).show();
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

    void taxpayer_reg_success_alert(String name, String taxid) {

        final android.support.v7.app.AlertDialog.Builder adb = new android.support.v7.app.AlertDialog.Builder(
                this);
        adb.setTitle("Taxpayer Registration Success");
        adb.setIcon(R.mipmap.ic_launcher);
        adb.setMessage("Taxpayer Registration is complete.\nName: " + name + "\nID: " + taxid);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 2) {
            String tax_payer_items = data.getStringExtra("taxpayeritems");
            taxpayeritems = tax_payer_items;
        }
        else if (requestCode == 1) {
            //String tax_payer_name = data.getStringExtra("name");
            String tax_payer_rin = data.getStringExtra("rin");
            et_taxid.setText(tax_payer_rin);
//            Toast.makeText(this, tax_payer_rin, Toast.LENGTH_SHORT).show();

        }
    }

    private String getTaxID(String apt_no){
        Log.e(TAG,"get tax id::apartment id " + apt_no);

        String buildingid = dataDB.myConnection(getApplicationContext()).selectFromTable("building_id","_apartments","apartment_type_id",apt_no);
        Log.e(TAG,"get tax id::building id " + buildingid);

        String landrin = dataDB.myConnection(getApplicationContext()).selectFromTable("land_rin","buildings","building_id",buildingid);
        Log.e(TAG,"get tax id::land rin " + landrin);

        String taxid = dataDB.myConnection(getApplicationContext()).selectFromTable("tax_id","lands","land_rin",landrin);
        Log.e(TAG,"get tax id::tax id " + taxid);

//        Toast.makeText(getApplicationContext(), "tax id : "+taxid, Toast.LENGTH_SHORT).show();

        return taxid;
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

            List<String> BuildingTypeCursor = dataDB.myConnection(AddBusiness.this).selectLGA3(y);

            ArrayAdapter dataBuildingType = new ArrayAdapter<String>(AddBusiness.this, R.layout.support_simple_spinner_dropdown_item, BuildingTypeCursor);
            dataBuildingType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            SpiLGA.setAdapter(dataBuildingType);
        }
    }


}
