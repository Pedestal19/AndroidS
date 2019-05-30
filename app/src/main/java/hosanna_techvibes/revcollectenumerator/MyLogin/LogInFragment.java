package hosanna_techvibes.revcollectenumerator.MyLogin;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import hosanna_techvibes.revcollectenumerator.MainActivity;
import hosanna_techvibes.revcollectenumerator.R;
import hosanna_techvibes.revcollectenumerator.databases.DataDB;
import hosanna_techvibes.revcollectenumerator.model.App;

public class LogInFragment extends AuthFragment{

    private static final String TAG = "LOGIN";
    private ProgressDialog myDialog;
    DataDB dataDB = new DataDB();
    App app;
    public int identifier=0;
    private ProgressDialog pDialog;


    @BindViews(value = {R.id.email_input_edit, R.id.password_input_edit})
    protected List<TextInputEditText> views;

    EditText et_email, et_password;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        app = ((App)getActivity().getApplicationContext());

        getIds(view);
        if(view!=null){
            caption.setText(getString(R.string.log_in_label));

            caption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                onLogin();
                }
            });

            view.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.color_log_in));
            for(TextInputEditText editText:views){
                if(editText.getId()==R.id.password_input_edit){
                    final TextInputLayout inputLayout=ButterKnife.findById(view,R.id.password_input);
                    Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
                    inputLayout.setTypeface(boldTypeface);
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
        }
    }

    @Override
    public int authLayout() {
        return R.layout.login_fragment;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
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
        final float padding=getResources().getDimension(R.dimen.folded_label_padding)/2;
        set.addListener(new Transition.TransitionListenerAdapter(){
            @Override
            public void onTransitionEnd(Transition transition) {
                super.onTransitionEnd(transition);
                caption.setTranslationX(-padding);
                caption.setRotation(0);
                caption.setVerticalText(true);
                caption.requestLayout();

            }
        });
        TransitionManager.beginDelayedTransition(parent,set);
        caption.setTextSize(TypedValue.COMPLEX_UNIT_PX,caption.getTextSize()/2);
        caption.setTextColor(Color.WHITE);
        ConstraintLayout.LayoutParams params=getParams();
        params.leftToLeft= ConstraintLayout.LayoutParams.UNSET;
        params.verticalBias=0.5f;
        caption.setLayoutParams(params);
        caption.setTranslationX(caption.getWidth()/8-padding);
    }

    @Override
    public void clearFocus() {
        for(View view:views) view.clearFocus();
    }

    public boolean onValidate() {
        boolean valid = true;

        String email = et_email.getText().toString();
        String password = et_password.getText().toString();

        if (password.isEmpty() || password.length() < 4) {
            et_password.setError("password should be above 6 alphanumeric characters");
            et_password.requestFocus();
            valid = false;
        } else {
            et_password.setError(null);
        }

        if (email.isEmpty()) {
            et_email.setError("enter a username");
            et_email.requestFocus();
            valid = false;
        } else {
            et_email.setError(null);
        }



        return valid;
    }

    public void onLogin() {
        Log.d(TAG, "Login");

        if (!onValidate()) {
            onLoginFailed();
            return;
        }

        caption.setEnabled(false);

        myDialog = new ProgressDialog(getActivity());
        myDialog.setMessage("Authenticating user...");
        myDialog.setTitle(getString(R.string.app_name));
        myDialog.setIcon(R.mipmap.ic_launcher);
        myDialog.setIcon(R.mipmap.ic_launcher);
        myDialog.show();

        final String email = et_email.getText().toString();
        final String password = et_password.getText().toString();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        int myLoginCount = dataDB.login(getActivity().getApplicationContext(), email.trim().toLowerCase(), password.trim());
                        Log.i("Login count ::::::", Integer.toString(myLoginCount));
                        if(myLoginCount >=1) {
                            onLoginSuccess();
                        }
                        else {
                            onLoginFailed();
                        }
                        myDialog.dismiss();
                    }
                }, 2000);
    }

    public void onLoginSuccess() {
        caption.setEnabled(true);
        getActivity().finish();
        startActivity(new Intent(getActivity(),MainActivity.class));
    }

    public void onLoginFailed() {
        Toast.makeText(getActivity(), "Login failed", Toast.LENGTH_SHORT).show();
        caption.setEnabled(true);
    }

    void getIds(View view){
        et_email = (EditText) view.findViewById(R.id.email_input_edit);
        et_password = (EditText) view.findViewById(R.id.password_input_edit);
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

/*

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
        alertDialog.setIcon(R.drawable.ic_launcher);
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
*/

}
