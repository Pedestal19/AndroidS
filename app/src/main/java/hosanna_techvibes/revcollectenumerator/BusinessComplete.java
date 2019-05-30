package hosanna_techvibes.revcollectenumerator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import hosanna_techvibes.revcollectenumerator.model.App;

public class BusinessComplete extends AppCompatActivity {

    Button btn_take_picture, btn_finish;
    App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_complete);
        app = ((App)getApplicationContext());

        btn_take_picture = (Button)findViewById(R.id.btn_take_picture);
        btn_finish = (Button)findViewById(R.id.btn_finish);
        TextView landrin = (TextView) findViewById(R.id.txtRIN);



        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_take_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "@todo", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
