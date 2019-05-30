package hosanna_techvibes.revcollectenumerator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;

import hosanna_techvibes.revcollectenumerator.MyLogin.LoginActivity;
import hosanna_techvibes.revcollectenumerator.model.App;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    CardView property, building, apartment, business,logout;
    App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        app = ((App)getApplicationContext());
        app.setCommand(null);
        getIds();

        property.setOnClickListener(this);
        building.setOnClickListener(this);
        apartment.setOnClickListener(this);
        business.setOnClickListener(this);
        logout.setOnClickListener(this);




    }

    void getIds(){
        property = (CardView)findViewById(R.id.property);
        building = (CardView)findViewById(R.id.building);
        apartment = (CardView)findViewById(R.id.apartment);
        business = (CardView)findViewById(R.id.business);
        logout = (CardView)findViewById(R.id.logout);


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.property:
                startActivity(new Intent(getApplicationContext(),PropertyList.class));
                break;
            case R.id.building:
                startActivity(new Intent(getApplicationContext(),BuildingList.class));
                break;
            case R.id.apartment:
                startActivity(new Intent(getApplicationContext(),ApartmentList.class));
                break;
            case R.id.business:
                startActivity(new Intent(getApplicationContext(),BusinessList.class));
                break;
            case R.id.logout:
                backButtonHandler();
                break;
        }
    }

    //back button overide method
    public void backButtonHandler() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                this);
        // Setting Dialog Title
        alertDialog.setTitle("Log Out?");

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want to log out of this application?");
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

    @Override
    public void onBackPressed() {

            //Display alert message when back button has been pressed
            backButtonHandler();


    }

}
