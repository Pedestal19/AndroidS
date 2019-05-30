package hosanna_techvibes.revcollectenumerator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hosanna_techvibes.revcollectenumerator.databases.DataDB;
import hosanna_techvibes.revcollectenumerator.model.App;

public class SelectItems extends Activity implements AdapterView.OnItemClickListener {

//    DBBuildingCategory dbBuildingCategory;

    ListView rkt_ListView;
    Button btn_show_me;

    private ArrayList<String> kaminey_dost_array_list = new ArrayList<String>();
    App app;
    DataDB dataDB = new DataDB();


    private void kaminey_dost() {
        Intent intent = getIntent();
        String profileref = intent.getExtras().getString("profile");

        kaminey_dost_array_list = dataDB.myConnection(this).selectAssessmentRulesWhere(profileref);

        kaminey_dost_array_list.add("None");
    }

    RktArrayAdapter rktArrayAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_items);
        app= ((App)getApplicationContext());



//        dbBuildingCategory = new DBBuildingCategory();

        kaminey_dost();
        rkt_ListView = (ListView) findViewById(R.id.rkt_listview);

        rktArrayAdapter = new RktArrayAdapter(
                this,
                R.layout.list_row,
                android.R.id.text1,
                kaminey_dost_array_list
        );

        rkt_ListView.setAdapter(rktArrayAdapter);
        rkt_ListView.setOnItemClickListener(this);

        btn_show_me = (Button) findViewById(R.id.btn_show_me);
        btn_show_me.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String result = "";

                List<String> resultList = rktArrayAdapter.getCheckedItems();
                for (int i = 0; i < resultList.size(); i++) {
//                    result += String.valueOf(resultList.get(i)) + "\n";
                    result += String.valueOf(resultList.get(i)) + ",";

                }

                rktArrayAdapter.getCheckedItemPositions().toString();

                if (result.matches("")) {
                    Toast.makeText(
                            getApplicationContext(),
                            "Please select some thing from list to show",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            result,
                            Toast.LENGTH_LONG).show();

                    Intent intent=new Intent();
                    intent.putExtra("taxpayeritems",result);
                    setResult(2,intent);

                    //app.setSI(result);
                    finish();
                    //startActivity(new Intent(getApplicationContext(), BusinessEnumerationActivity.class));

                }


            }
        });


    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        rktArrayAdapter.rkt_toggleChecked(i);
    }

    public class RktArrayAdapter extends ArrayAdapter<String> {

        private HashMap<Integer, Boolean> myChecked = new HashMap<Integer, Boolean>();

        public RktArrayAdapter(Context context, int resource,
                               int textViewResourceId, List<String> objects) {
            super(context, resource, textViewResourceId, objects);

            for (int i = 0; i < objects.size(); i++) {
                myChecked.put(i, false);
            }
        }

        public void rkt_toggleChecked(int position) {
            if (myChecked.get(position)) {
                myChecked.put(position, false);
            } else {
                myChecked.put(position, true);
            }

            notifyDataSetChanged();
        }

        public List<Integer> getCheckedItemPositions() {
            List<Integer> checkedItemPositions = new ArrayList<Integer>();

            for (int i = 0; i < myChecked.size(); i++) {
                if (myChecked.get(i)) {
                    (checkedItemPositions).add(i);
                }
            }

            return checkedItemPositions;
        }

        public List<String> getCheckedItems() {
            List<String> checkedItems = new ArrayList<String>();

            for (int i = 0; i < myChecked.size(); i++) {
                if (myChecked.get(i)) {
                    (checkedItems).add(kaminey_dost_array_list.get(i));
                }
            }

            return checkedItems;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;

            if (row == null) {
                LayoutInflater inflater = getLayoutInflater();
                row = inflater.inflate(R.layout.list_row, parent, false);
            }

            CheckedTextView checked_TextView = (CheckedTextView) row.findViewById(R.id.checked_textview);
            checked_TextView.setText(kaminey_dost_array_list.get(position));

            Boolean checked = myChecked.get(position);
            if (checked != null) {
                checked_TextView.setChecked(checked);
            }

            return row;
        }

    }
    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(SelectItems.this,
                    AddBusiness.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }*/

    @Override
    public void onBackPressed() {
        Toast.makeText(
                getApplicationContext(),
                "Press on the Done button to finish ",
                Toast.LENGTH_LONG).show();
    }
}