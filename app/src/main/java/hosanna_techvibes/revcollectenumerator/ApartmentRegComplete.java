package hosanna_techvibes.revcollectenumerator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ApartmentRegComplete extends AppCompatActivity {

    Button btn_addbusiness, btn_finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment_reg_complete);


        btn_addbusiness = (Button)findViewById(R.id.btn_addbuilding);
        btn_finish = (Button)findViewById(R.id.btn_finish);

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_addbusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Add Business", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
