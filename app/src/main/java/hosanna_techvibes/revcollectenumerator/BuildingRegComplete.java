package hosanna_techvibes.revcollectenumerator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import hosanna_techvibes.revcollectenumerator.model.App;

public class BuildingRegComplete extends AppCompatActivity {

    Button btn_addbuilding, btn_finish;
    App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_reg_complete);
        app = ((App)getApplicationContext());

        btn_addbuilding = (Button)findViewById(R.id.btn_addbuilding);
        btn_finish = (Button)findViewById(R.id.btn_finish);
        TextView landrin = (TextView) findViewById(R.id.txtRIN);

/*
        if(!(app.getActiveTax_registration_id().equals(null)) && !(app.getActiveTax_registration_id().equals(""))){
            landrin.setText("Land RIN: "+app.getActiveTax_registration_id());
        }
*/

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 finish();
            }
        });

        btn_addbuilding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BuildingRegComplete.this, "Add Apartment", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
