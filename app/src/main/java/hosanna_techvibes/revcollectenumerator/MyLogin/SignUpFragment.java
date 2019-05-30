package hosanna_techvibes.revcollectenumerator.MyLogin;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import hosanna_techvibes.revcollectenumerator.GuestSignUp;
import hosanna_techvibes.revcollectenumerator.R;
import hosanna_techvibes.revcollectenumerator.SearchTaxpayerWidget;
import hosanna_techvibes.revcollectenumerator.databases.DataDB;
import hosanna_techvibes.revcollectenumerator.helpers.Devices;
import hosanna_techvibes.revcollectenumerator.helpers.GenericHelpers;
import hosanna_techvibes.revcollectenumerator.helpers.LocationMgr;
import hosanna_techvibes.revcollectenumerator.helpers.ServiceHandler;
import hosanna_techvibes.revcollectenumerator.model.App;
import hosanna_techvibes.revcollectenumerator.model.AuthorizationHttpResponse;


public class SignUpFragment extends AuthFragment implements View.OnClickListener{

    private static final String TAG = "Signup";
    private ProgressDialog myDialog;
    DataDB dataDB = new DataDB();
    App app;
    EditText et_email, et_password, et_confirm_password, et_service_code;
    private RegisterUserTask mRegTask = null;
    private ProgressDialog pDialog;
    String latitude = "unknown";
    String longitude = "unknown";
    String successMssg;
    String errorMssg, SeseErrorMssg,abc;

    static final int RC_SIGN_IN = 1;
//    static final String TAG = "Google Login";
    GoogleSignInClient mGoogleSignInClient;

    @BindViews(value = {R.id.email_input_edit,
            R.id.password_input_edit,
            R.id.confirm_password_edit})
    protected List<TextInputEditText> views;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        app = ((App)getActivity().getApplicationContext());
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity().getApplicationContext(), gso);

        // Set the dimensions of the sign-in button.
        SignInButton signInButton = view.findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        view.findViewById(R.id.sign_in_button).setOnClickListener(this);

        TextView textView = (TextView) signInButton.getChildAt(0);
        textView.setText("Guest Sign Up");


        getIds(view);
        if(view!=null){
            view.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.color_sign_up));


            caption.setText(getString(R.string.sign_up_label));


            caption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSignUp();
                }
            });
            for(TextInputEditText editText:views){
                if(editText.getId()==R.id.password_input_edit){
                    final TextInputLayout inputLayout= ButterKnife.findById(view,R.id.password_input);
                    final TextInputLayout confirmLayout=ButterKnife.findById(view,R.id.confirm_password);
                    Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
                    inputLayout.setTypeface(boldTypeface);
                    confirmLayout.setTypeface(boldTypeface);
                    editText.addTextChangedListener(new TextWatcherAdapter(){
                        @Override
                        public void afterTextChanged(Editable editable) {
                            inputLayout.setPasswordVisibilityToggleEnabled(editable.length()>0);
                        }
                    });
                }
                editText.setOnFocusChangeListener((temp,hasFocus)->{
                    if(!hasFocus){
                        boolean isEnabled=editText.getText().length()>0;
                        editText.setSelected(isEnabled);
                    }
                });
            }
            caption.setVerticalText(true);
            foldStuff();
            caption.setTranslationX(getTextPadding());
        }
    }

    void alert3(){

        final AlertDialog.Builder adb = new AlertDialog.Builder(
                getActivity());
        adb.setTitle("Guest SignUp");
        adb.setIcon(R.mipmap.ic_launcher);
        adb.setMessage("Please the following has to be done for a \n" +
                "successful signup: \n" +
                "1. A Google Account is setup on this device\n" +
                "2. Device is connected to the Internet\n\n" +
                "Do you want to proceed?");
        adb.setNeutralButton("No, Let me do the above first", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //startActivityForResult(new Intent(getActivity().getApplicationContext(), SearchTaxpayerWidget.class),1);
            }
        });
        /*adb.setNegativeButton("Re-Input ID", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //startActivity(new Intent(getApplicationContext(),PhotoRegistration.class));
                dialog.dismiss();
//            et_taxid.setFocusable(true);
                et_taxid.requestFocus();
            }
        });
*/
        adb.setPositiveButton("Yes. Please proceed.", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //startActivity(new Intent(getApplicationContext(),PhotoRegistration.class));
                //alert2();
                signIn();
            }
        });
        adb.show();

    }
    void alert4(){

        final AlertDialog.Builder adb = new AlertDialog.Builder(
                getActivity());
        adb.setTitle("Info");
        adb.setIcon(R.mipmap.ic_launcher);
        adb.setMessage("An Error Occured. Possible causes: \n" +
                "1. No Google Account on this device\n" +
                "2. Device not connected to the Internet\n");
        adb.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //startActivityForResult(new Intent(getActivity().getApplicationContext(), SearchTaxpayerWidget.class),1);
            }
        });


        adb.show();

    }
    void alert5(String name, String email){

        final AlertDialog.Builder adb = new AlertDialog.Builder(
                getActivity());
        adb.setTitle("Success");
        adb.setIcon(R.mipmap.ic_launcher);
        adb.setMessage("Hello "+name+", Revcollect Enumerator would use\n" +
                "the email address "+email+" for this sign up\n");
        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                getActivity().finish();
                Intent intent = new Intent(getActivity().getApplicationContext(), GuestSignUp.class);
                intent.putExtra("name", name);
                intent.putExtra("email", email);
                startActivity(intent);
                //startActivityForResult(intent(getActivity().getApplicationContext(), ), 1);
            }
        });


        adb.show();

    }
    void alert6(String email){

        final AlertDialog.Builder adb = new AlertDialog.Builder(
                getActivity());
        adb.setTitle("Info");
        adb.setIcon(R.mipmap.ic_launcher);
        adb.setMessage(" This device already has a Guest account signed up:\n" +
                email+" ");
        adb.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //startActivityForResult(new Intent(getActivity().getApplicationContext(), SearchTaxpayerWidget.class),1);
            }
        });


        adb.show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                alert3();
                break;
            // ...
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            //updateUI(account);
            //Toast.makeText(getApplicationContext(), " "+account, Toast.LENGTH_SHORT).show();


           /* Toast.makeText(getActivity().getApplicationContext(), " "+completedTask.getResult().getEmail()+"" +
                    "\n"+completedTask.getResult().getDisplayName(), Toast.LENGTH_SHORT).show();
*/
           if(checkGuestConditions()) {
               alert5(completedTask.getResult().getDisplayName(), completedTask.getResult().getEmail());
           }
           else{
                alert6(completedTask.getResult().getEmail());
           }
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Log.w(TAG, "signInResult:failed details=" + e.getMessage());

            //Toast.makeText(getActivity().getApplicationContext(), " "+null, Toast.LENGTH_SHORT).show();

            //updateUI(null);
            alert4();
        }
    }

    private boolean checkGuestConditions(){
        long c = dataDB.myConnection(getActivity().getApplicationContext()).countRecordsWhere("users", "service_code","GUEST");

        if(c>0){
            return false;
        }

        return true;
    }


    @Override
    public int authLayout() {
        return R.layout.sign_up_fragment;
    }

    @Override
    public void clearFocus() {
        for(View view:views) view.clearFocus();
    }

    @Override
    public void fold() {
        lock=false;
        Rotate transition = new Rotate();
        transition.setEndAngle(-90f);
        transition.addTarget(caption);
        TransitionSet set=new TransitionSet();
        set.setDuration(getResources().getInteger(R.integer.duration));
        ChangeBounds changeBounds=new ChangeBounds();
        set.addTransition(changeBounds);
        set.addTransition(transition);
        TextSizeTransition sizeTransition=new TextSizeTransition();
        sizeTransition.addTarget(caption);
        set.addTransition(sizeTransition);
        set.setOrdering(TransitionSet.ORDERING_TOGETHER);
        set.addListener(new Transition.TransitionListenerAdapter(){
            @Override
            public void onTransitionEnd(Transition transition) {
                super.onTransitionEnd(transition);
                caption.setTranslationX(getTextPadding());
                caption.setRotation(0);
                caption.setVerticalText(true);
                caption.requestLayout();

            }
        });

        TransitionManager.beginDelayedTransition(parent,set);
        foldStuff();
        caption.setTranslationX(-caption.getWidth()/8+getTextPadding());
    }

    private void foldStuff(){

        caption.setTextSize(TypedValue.COMPLEX_UNIT_PX,caption.getTextSize()/2f);
        caption.setTextColor(Color.WHITE);
        ConstraintLayout.LayoutParams params=getParams();
        params.rightToRight= ConstraintLayout.LayoutParams.UNSET;
        params.verticalBias=0.5f;
        caption.setLayoutParams(params);
    }

    private float getTextPadding(){
        return getResources().getDimension(R.dimen.folded_label_padding)/2.1f;
    }

    void getIds(View view){
        et_email = (EditText) view.findViewById(R.id.email_input_edit);
        et_password = (EditText) view.findViewById(R.id.password_input_edit);
        et_confirm_password = (EditText) view.findViewById(R.id.confirm_password_edit);
        et_service_code = (EditText) view.findViewById(R.id.confirm_password_edit2);

    }

    public boolean onValidate() {
        boolean valid = true;

        String email = et_email.getText().toString();
        String password = et_password.getText().toString();

        String confimpassword = et_confirm_password.getText().toString();
        String servicecode = et_service_code.getText().toString();

        if (servicecode.isEmpty() || (servicecode.length() < 4)) {
            et_service_code.setError("Enter a valid service code");
            et_service_code.requestFocus();
            valid = false;
        } else {
            et_service_code.setError(null);
        }

        if (confimpassword.isEmpty() || !(confimpassword.equals(password))) {
            et_confirm_password.setError("passwords should match");
            et_confirm_password.requestFocus();
            valid = false;
        } else {
            et_confirm_password.setError(null);
        }

        if (password.isEmpty() || password.length() < 4) {
            et_password.setError("password should be above 6 alphanumeric characters");
            et_password.requestFocus();
            valid = false;
        } else {
            et_password.setError(null);
        }


        if (email.isEmpty()) {
            et_email.setError("enter a valid email address");
            et_email.requestFocus();
            valid = false;
        } else {
            et_email.setError(null);
        }



        return valid;
    }


    public void onSignUp() {
        Log.d(TAG, "Login");

        if (!onValidate()) {
            onSignupFailed();
            return;
        }

        caption.setEnabled(false);



        final String email = et_email.getText().toString();
        final String password = et_password.getText().toString();

        mRegTask = new RegisterUserTask(email, et_service_code.getText().toString());
        mRegTask.execute((Void) null);
    }

    public void onSignupFailed() {
        Toast.makeText(getActivity(), "Signup failed", Toast.LENGTH_SHORT).show();
        caption.setEnabled(true);
    }

    public class RegisterUserTask extends AsyncTask<Void, Void, Boolean> {
        private final String mUsername,mServiceCode;

        RegisterUserTask(String email, String serviceCode) {
            mUsername = email;
//            mPassword = password;
            mServiceCode = serviceCode;

            pDialog = new ProgressDialog(getActivity(), R.style.AppCompatAlertDialogStyle);
            pDialog.setMessage("Signing Up...");
            pDialog.setTitle(getString(R.string.app_name));
            pDialog.setIcon(R.mipmap.ic_launcher);
            pDialog.setIcon(R.mipmap.ic_launcher);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

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
            if (GenericHelpers.isOnline(getActivity().getApplicationContext())) {
                String httpparams;
                try {
                    String lat = null, lon = null;
                    if (app.getLatitude() == null || app.getLongitude() == null) {
                        LocationMgr LocMgr = new LocationMgr();
                        String locMan = LocMgr.getMyLoc(getActivity().getApplicationContext());
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



/*
                            "appKeyParameter":"TLsR:DI,Etml7jJ&+>x?PH^*2EM_R(/>C85o)yt>}@wp9/,rZP3vJT:Q!=UO-`",
*/

                    JSONObject authJsonObject = new JSONObject();
                    authJsonObject.put("email", mUsername.trim());
//                    authJsonObject.put("password", mPassword.trim());
                    authJsonObject.put("service_code", mServiceCode.trim());
                    authJsonObject.put("device_os", Devices.getDeviceModel());
//                    authJsonObject.put("appKeyParameter", getString(R.string.appId));
                    authJsonObject.put("device_uuid", Devices.getDeviceUUID(getActivity().getApplicationContext()));
                    authJsonObject.put("device_imei", Devices.getDeviceIMEI(getActivity().getApplicationContext()));
                    authJsonObject.put("device_name", Devices.getDeviceName());
                    authJsonObject.put("device_model", Devices.getDeviceModel());
                    authJsonObject.put("device_longitude", longitude);
                    authJsonObject.put("device_latitude", latitude);
                    authJsonObject.put("device_version", Devices.getDeviceVersion());

                    Log.e(TAG, "This is request " + authJsonObject.toString());

                    ServiceHandler serviceHandler = new ServiceHandler();


                    abc=authJsonObject.toString();
                    /*Cursor mycursor = dataDB.myConnection(getActivity().getApplicationContext()).selectAllFromTableAs222();
                    String EPdata="";
                    if (mycursor != null  && mycursor.moveToFirst() ) { EPdata = mycursor.getString(20);}
*/
                    AuthorizationHttpResponse httpResp = serviceHandler.makeServiceCallJSON(getString(R.string.initial_auth_ep),2,authJsonObject);


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

                            if(SeseResponseCode.equals("200")) {
                                //success
//                                successAlert();
//                                JSONObject authorizedObject = parentObject.getJSONObject("authorized");

                                JSONArray authorizationArray = parentObject.getJSONArray("responseData");

                                //String first_name = authorizationArray.getString("firstname");

                                JSONObject onlyAvailableJsonObject = authorizationArray.getJSONObject(0);

                                String name = onlyAvailableJsonObject.getString("name");
                                String username = onlyAvailableJsonObject.getString("username");
                                String email = onlyAvailableJsonObject.getString("email");

                                String user_phone = onlyAvailableJsonObject.getString("user_phone");
                                String service_id = onlyAvailableJsonObject.getString("service_id");
                                String service_code = onlyAvailableJsonObject.getString("service_code");


                                String user_id = onlyAvailableJsonObject.getString("id");


                                //String serviceID = onlyAvailableJsonObject.getString("seviceID");
                                Log.d(TAG, " response::name: " + name);
                                Log.d(TAG, " response::username: " + username);
                                Log.d(TAG, " response::email: " + email);

                                Log.d(TAG, " response::userphone: " + user_phone);
                                Log.d(TAG, " response::service id: " + service_id);

                                Log.d(TAG, " response::service_code: " + service_code);
                                Log.d(TAG, " response::user_id: " + user_id);


                                // TODO: 11/10/2016 save user data
                                ContentValues contentValues = new ContentValues();

                                //contentValues.put("tax_registration_id",tax_registration_id);
                                //Log.e("tax registration is**: ", Long.toString(tax_registration_id));

                                contentValues.put("pwd",et_password.getText().toString());
                                contentValues.put("first_name", name);
                                contentValues.put("login", username.toLowerCase());
                                contentValues.put("middle_name", email);

                                contentValues.put("phone", user_phone);
                                contentValues.put("user_id", user_id);
                                contentValues.put("service_code", service_code);
                                contentValues.put("service_id", service_id);



                                //db code


//                                if (clearDB()) {
                                    long rowInserted = dataDB.myConnection(getActivity()).onInsertOrUpdate(contentValues, "users");
                                    if (rowInserted != -1) {

                                        Log.e(TAG, "user details saved: " + ":-)");


                                        //Database insert successful, give permission for app to continue
                                        return true;
                                    }
//                                }


                                SeseErrorMssg="null";

                                //return true;
                            }
                            else if(SeseResponseCode.equals("500")) {
                                //success
//                                successAlert();
                                SeseErrorMssg="Internal Server Error";
                                errorMssg=SeseResponseCode;

                            }
                            else{
/*
                                failureAlert();
*/
                                errorMssg=SeseResponseCode;
                                SeseErrorMssg=SeseResponseDescription;
                            }
                        }
                        else {
                            errorMssg = httpResp.getResponseData();
                            String respcode=String.valueOf(httpResp.getResponseCode());
                            errorMssg=respcode;

                        }
                    }


                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else{
                errorMssg = "Limited Internet connectivity";
            }
            return false;
        }



        @Override
        protected void onPostExecute(final Boolean success) {
            mRegTask = null;
            hidePDialog();

            if (success) {
                Log.i("UserRegistrationLog", "Finished Authorization Successful");
                et_email.setText("");
                et_password.setText("");
                et_confirm_password.setText("");
                et_service_code.setText("");

                successAlert();
//
            } else {
                Log.i("UserRegistrationLog", "Failed Registration");
                if(SeseErrorMssg!=null)
                {
                    /*et_password.setError(SeseErrorMssg);
                    et_password.requestFocus();*/

                    failureAlert();
                }
                else {
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
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    public void successAlert() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                getActivity(), R.style.AppTheme_Dark_Dialog2);

        // Setting Dialog Title
        alertDialog.setTitle("Authorization Sent!");
        // Setting Dialog Message
        alertDialog.setMessage("Please wait for authorization from Web Admin");
        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.dialog_tick2);
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

//                        Toast.makeText(getBaseContext(), "Password set success", Toast.LENGTH_LONG).show();
//                        finish();
//                        startActivity(new Intent(getActivity().getApplicationContext(), SetPassword.class));
                    }
                });

        // Showing Alert Message
        alertDialog.show();


    }

    public void failureAlert() {


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                getActivity(),R.style.AppTheme_Dark_Dialog3);

        // Setting Dialog Title
        alertDialog.setTitle("User Confimation Failed!!!");
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


                        caption.setEnabled(true);                    }
                });

        // Showing Alert Message
        alertDialog.show();


    }
    public void failureAlertC() {


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                getActivity(),R.style.AppTheme_Dark_Dialog3);

        // Setting Dialog Title
        alertDialog.setTitle("User Confimation Failed!!!");
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
                        caption.setEnabled(true);                    }
                });

        // Showing Alert Message
        alertDialog.show();


    }


    boolean clearDB(){
        boolean users = dataDB.myConnection(getActivity()).deleteRecords("users");
        boolean _apartment_types = dataDB.myConnection(getActivity()).deleteRecords("_apartment_types");
        boolean assessment_rules = dataDB.myConnection(getActivity()).deleteRecords("assessment_rules");
        //boolean asset_types = dataDB.myConnection(getActivity()).deleteRecords("asset_types");
        boolean building_completions = dataDB.myConnection(getActivity()).deleteRecords("building_completions");
        boolean building_functions = dataDB.myConnection(getActivity()).deleteRecords("building_functions");
        boolean building_images = dataDB.myConnection(getActivity()).deleteRecords("building_images");
        boolean building_occupancies = dataDB.myConnection(getActivity()).deleteRecords("building_occupancies");
        boolean building_occupancy_types = dataDB.myConnection(getActivity()).deleteRecords("building_occupancy_types");
        boolean building_ownerships = dataDB.myConnection(getActivity()).deleteRecords("building_ownerships");
        boolean building_purposes = dataDB.myConnection(getActivity()).deleteRecords("building_purposes");
        boolean building_types = dataDB.myConnection(getActivity()).deleteRecords("building_types");
        boolean business_categories = dataDB.myConnection(getActivity()).deleteRecords("business_categories");
        boolean business_images = dataDB.myConnection(getActivity()).deleteRecords("business_images");
        boolean business_operations = dataDB.myConnection(getActivity()).deleteRecords("business_operations");
        boolean business_sectors = dataDB.myConnection(getActivity()).deleteRecords("business_sectors");
        boolean business_sub_sectors = dataDB.myConnection(getActivity()).deleteRecords("business_sub_sectors");
        boolean business_types = dataDB.myConnection(getActivity()).deleteRecords("business_types");
        boolean land_functions = dataDB.myConnection(getActivity()).deleteRecords("land_functions");
        boolean land_ownership = dataDB.myConnection(getActivity()).deleteRecords("land_ownerships");
        boolean land_purposes = dataDB.myConnection(getActivity()).deleteRecords("land_purposes");
        boolean land_types = dataDB.myConnection(getActivity()).deleteRecords("land_types");
        boolean taxpayer_items = dataDB.myConnection(getActivity()).deleteRecords("taxpayer_items");
        boolean tax_payers = dataDB.myConnection(getActivity()).deleteRecords("tax_payers");
        boolean wards = dataDB.myConnection(getActivity()).deleteRecords("wards");
        boolean _apartments = dataDB.myConnection(getActivity()).deleteRecords("_apartments");
        boolean _apartment_occupants = dataDB.myConnection(getActivity()).deleteRecords("_apartments_occupants");
        boolean buildings = dataDB.myConnection(getActivity()).deleteRecords("buildings");
        boolean businesses = dataDB.myConnection(getActivity()).deleteRecords("businesses");
        boolean lands = dataDB.myConnection(getActivity()).deleteRecords("lands");
        boolean business_structures = dataDB.myConnection(getActivity()).deleteRecords("business_structures");
        boolean profiles = dataDB.myConnection(getActivity()).deleteRecords("profiles");
        boolean streets = dataDB.myConnection(getActivity()).deleteRecords("streets");


        if(users && _apartment_types && assessment_rules && building_completions && building_images &&
                building_functions && building_occupancies && building_occupancy_types && building_ownerships
                && building_purposes && building_types && business_categories && business_images
                && business_operations && business_sub_sectors && business_sectors && business_types
                && land_functions && land_ownership && land_purposes && land_types && taxpayer_items
                && tax_payers && wards && _apartments && _apartment_occupants && buildings && businesses
                && lands && business_structures && profiles && streets){

                ContentValues a = new ContentValues();
                a.put("apartment_type_id", "100000");
                a.put("apartment_type","--Select Apartment Type--");

            dataDB.myConnection(getActivity().getApplicationContext()).onInsertOrUpdate(a, "_apartment_types");

           /* ContentValues b = new ContentValues();
            b.put("asset_type_id", "100000");
            b.put("asset_type","--Select Asset Type--");
            b.put("service_id","080808");
            b.put("asset_status", "1");

            dataDB.myConnection(getActivity().getApplicationContext()).onInsertOrUpdate(b, "asset_types");
*/
            ContentValues c = new ContentValues();
            c.put("building_completion_id", "100000");
            c.put("building_completion","--Select Building Completion--");

            dataDB.myConnection(getActivity().getApplicationContext()).onInsertOrUpdate(c, "building_completions");

            ContentValues d = new ContentValues();
            d.put("building_function_id", "100000");
            d.put("building_function","--Select Building Function--");

            dataDB.myConnection(getActivity().getApplicationContext()).onInsertOrUpdate(d, "building_functions");

            ContentValues e = new ContentValues();
            e.put("building_occupancy_id", "100000");
            e.put("building_occupancy","--Select Building Occupancy--");

            dataDB.myConnection(getActivity().getApplicationContext()).onInsertOrUpdate(e, "building_occupancies");


            ContentValues f = new ContentValues();
            f.put("building_occupancy_type_id", "100000");
            f.put("building_occupancy_type","--Select Occupancy Type--");

            dataDB.myConnection(getActivity().getApplicationContext()).onInsertOrUpdate(f, "building_occupancy_types");


            ContentValues g = new ContentValues();
            g.put("building_ownership_id", "100000");
            g.put("building_ownership","--Select Building Ownership--");

            dataDB.myConnection(getActivity().getApplicationContext()).onInsertOrUpdate(g, "building_ownerships");


            ContentValues h = new ContentValues();
            h.put("building_purpose_id", "100000");
            h.put("building_purpose","--Select Building Purpose--");

            dataDB.myConnection(getActivity().getApplicationContext()).onInsertOrUpdate(h, "building_purposes");

            ContentValues i = new ContentValues();
            i.put("building_type_id", "100000");
            i.put("building_type","--Select Building Type--");
            //i.put("service_id", "234001");

            dataDB.myConnection(getActivity().getApplicationContext()).onInsertOrUpdate(i, "building_types");


            ContentValues j = new ContentValues();
            j.put("service_id", "234001");
            j.put("business_category","--Select Business Category--");

            dataDB.myConnection(getActivity().getApplicationContext()).onInsertOrUpdate(j, "business_categories");


            ContentValues k = new ContentValues();
            k.put("service_id", "234001");
            k.put("business_operation","--Select Business Operation--");


            dataDB.myConnection(getActivity().getApplicationContext()).onInsertOrUpdate(k, "business_operations");


            ContentValues l = new ContentValues();
            l.put("business_sector_id", "0");
            l.put("business_sector","--Select Business Sector--");
            l.put("service_id","2343345");


            dataDB.myConnection(getActivity().getApplicationContext()).onInsertOrUpdate(l, "business_sectors");

            ContentValues m = new ContentValues();
            m.put("business_size_id", "100000");
            m.put("business_size","--Select Business Size--");

            dataDB.myConnection(getActivity().getApplicationContext()).onInsertOrUpdate(m, "business_sizes");


            ContentValues n = new ContentValues();
            n.put("business_sub_sector_id", "0");
            n.put("business_sub_sector","--Select Business Sub Sector--");
            n.put("service_id","2343345");


            dataDB.myConnection(getActivity().getApplicationContext()).onInsertOrUpdate(n, "business_sub_sectors");


            ContentValues o = new ContentValues();
            o.put("business_type_id", "0");
            o.put("business_type","--Select Business Type--");
            o.put("service_id","2343345");


            dataDB.myConnection(getActivity().getApplicationContext()).onInsertOrUpdate(o, "business_types");


            ContentValues p = new ContentValues();
            p.put("land_function_id", "100000");
            p.put("land_function","--Select Function--");
            p.put("service_id","2343345");


            dataDB.myConnection(getActivity().getApplicationContext()).onInsertOrUpdate(p, "land_functions");

            ContentValues q = new ContentValues();
            q.put("land_ownership_id", "100000");
            q.put("land_ownership","--Select Ownership--");
            q.put("service_id","2343345");


            dataDB.myConnection(getActivity().getApplicationContext()).onInsertOrUpdate(q, "land_ownerships");


            ContentValues r = new ContentValues();
            //r.put("land_ownership_id", "100000");
            r.put("business_structure","--Select Business Structure--");
            r.put("service_id","0000000");


            dataDB.myConnection(getActivity().getApplicationContext()).onInsertOrUpdate(r, "business_structures");


            ContentValues s = new ContentValues();
            s.put("land_purpose_id", "100000");
            s.put("land_purpose","--Select Land Purpose--");
            s.put("service_id","0000000");


            dataDB.myConnection(getActivity().getApplicationContext()).onInsertOrUpdate(s, "land_purposes");


            ContentValues t = new ContentValues();
            t.put("land_type_id", "100000");
            t.put("land_type","--Select Land Type--");
            t.put("service_id","0000000");


            dataDB.myConnection(getActivity().getApplicationContext()).onInsertOrUpdate(t, "land_types");

            ContentValues u = new ContentValues();
            u.put("profile_id", "0");
            u.put("profile_ref","--Select Profile--");
            u.put("service_id","0000000");


            dataDB.myConnection(getActivity().getApplicationContext()).onInsertOrUpdate(u, "profiles");



            return true;

        }
        return false;
    }

}
