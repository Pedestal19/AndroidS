package hosanna_techvibes.revcollectenumerator;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import hosanna_techvibes.revcollectenumerator.MyLogin.LoginActivity;
import hosanna_techvibes.revcollectenumerator.adapter.PropertyAdapter;
import hosanna_techvibes.revcollectenumerator.adapter.TaxPayersAdapter;
import hosanna_techvibes.revcollectenumerator.databases.DataDB;
import hosanna_techvibes.revcollectenumerator.model.App;
import hosanna_techvibes.revcollectenumerator.model.Taxpayer;

public class PropertyList extends AppCompatActivity {
    private static final String TAG = "PropertyList";
    ProgressDialog mProgressDialog;

    private int start = 0;
    private int limit = 7;
    App app;
    DataDB dataDB = new DataDB();
    TextView textViewNoRecord;
    ListView listView_taxpayers;
    private PropertyAdapter taxPayersAdapter;
    ArrayList<Taxpayer> myList = new ArrayList<Taxpayer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tax_payers_list);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        app = ((App)getApplicationContext());
        textViewNoRecord = (TextView)findViewById(R.id.textViewNoRecord);

       /* if(app.getUserid() == null)
        {
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        }*/

        /*if(app.getUt()=="bir")
        {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.yellow)));

        }*/
        SwitchCompat mode = (SwitchCompat) findViewById(R.id.mode);

        mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Toast.makeText(getApplicationContext(), "Independent Mode is Active", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Independent Mode is Disabled", Toast.LENGTH_SHORT).show();
                  }

            }

        });


        FloatingActionButton fabAddTaxPayer = (FloatingActionButton) findViewById(R.id.fabAddTaxPayer);
        FloatingActionButton fabAddTaxPayer2 = (FloatingActionButton) findViewById(R.id.fabAddTaxPayer2);
        FloatingActionButton fabSearchTaxPayer = (FloatingActionButton) findViewById(R.id.fabSearchTaxPayer);
        if(app.getCommand()==null) {


            fabAddTaxPayer.setVisibility(View.VISIBLE);
//            fabAddTaxPayer2.setVisibility(View.VISIBLE);
            fabSearchTaxPayer.setVisibility(View.VISIBLE);

            fabAddTaxPayer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   /* if (app.getReg_mode()) {
                        startActivity(new Intent(getApplicationContext(), AddBuilding.class));
                    } else {
                        startActivity(new Intent(getApplicationContext(), StreetList.class));
                    }*/

                   /* if(app.getActiveActivity().getClass()==BuildingList.class){
                        startActivity(new Intent(getApplicationContext(), AddBuilding.class));
                    }
                    else {
                        startActivity(new Intent(getApplicationContext(), StreetList.class));
                    }*/

                    if(mode.isChecked()){
                        app.setReg_mode(true);
                        startActivity(new Intent(getApplicationContext(), AddProperty.class));
                    }
                    else{
                        startActivity(new Intent(getApplicationContext(), StreetList.class));
                    }
                }
            });
            /*fabAddTaxPayer2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    app.setReg_mode(true);
                    startActivity(new Intent(getApplicationContext(), StreetList.class));
                }
            });*/
            fabSearchTaxPayer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), SearchTaxPayersActivity.class));
                }
            });
        }
        new RemoteDataTask().execute();

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(PropertyList.this);
            // Set progressdialog title
            mProgressDialog.setTitle(getString(R.string.app_name));
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            PopulateList(start, limit);

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {

            //forPostExecute();
            listView_taxpayers = (ListView) findViewById(R.id.listView_taxpayers);
            //Create a button for load More
            Button btnLoadMore = new Button(getApplicationContext());
            btnLoadMore.setText("Load More");
            //Adding loadmore to building listview at footer
            listView_taxpayers.addFooterView(btnLoadMore);

            if(myList.size() > 0) {
                listView_taxpayers.setAdapter(new PropertyAdapter(getApplicationContext(), myList));

                //listView action
                listView_taxpayers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        final Taxpayer clickedT = (Taxpayer) adapterView.getItemAtPosition(i);
                        app.setActiveActivity(PropertyList.this);
                        app.setMySurname(clickedT.getSurname());
                        app.setP_town(clickedT.getTown());
                        app.setP_lga(clickedT.getLga());
                        app.setP_ward(clickedT.getWard());
                        app.setP_assettype(clickedT.getAssettype());

                        /*app.setActiveTIN(clickedT.getTax_id());
                        app.setActiveTempTIN(clickedT.getTemp_tax_id());
//                        app.setActiveTax_registration_id(clickedT.getTax_registration_id());
                        app.setActiveTax_registration_id(clickedT.getTemp_tax_id());*/

                        if (app.getCommand()!=null) {
                            if (app.getCommand().equals("register")) {
                                final AlertDialog.Builder adb = new AlertDialog.Builder(
                                        PropertyList.this);
                                adb.setTitle("Options");
                                adb.setIcon(R.mipmap.ic_launcher);
                                adb.setMessage("Please confirm an action to perform");
                                adb.setPositiveButton("Add Building", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        app.setReg_mode(false);
                                        startActivity(new Intent(getApplicationContext(),AddBuilding.class));
                                    }
                                });


                                adb.show();
                            }
                        }


                    }
                });
                textViewNoRecord.setVisibility(View.INVISIBLE);
            }
            else{
                textViewNoRecord.setVisibility(View.VISIBLE);
                textViewNoRecord.setText("No matching record found.");
            }
            // Close the progressdialog
            mProgressDialog.dismiss();



            // TODO: 09/10/2015 listen to load more event
            btnLoadMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // Starting a new async task
                    new LoadMoreDataTask().execute();
                }
            });

            // Create an OnScrollListener
            listView_taxpayers.setOnScrollListener(new AbsListView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(AbsListView view,
                                                 int scrollState) { // TODO Auto-generated method stub
                    int threshold = 1;
                    int count = listView_taxpayers.getCount();

                    if (scrollState == SCROLL_STATE_IDLE) {
                        if (listView_taxpayers.getLastVisiblePosition() >= count
                                - threshold) {
                            // Execute LoadMoreDataTask AsyncTask
                            new LoadMoreDataTask().execute();
                        }
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {
                    // TODO Auto-generated method stub

                }

            });

        }
    }

    private class LoadMoreDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(PropertyList.this);
            // Set progressdialog title
            mProgressDialog.setTitle(getString(R.string.app_name));
            // Set progressdialog message
            mProgressDialog.setMessage("Loading more...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            PopulateList(start += 7, limit);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            // Locate listview last item
            int position = listView_taxpayers.getLastVisiblePosition();
            // Pass the results into ListViewAdapter.java
            if(myList.size() > 0) {
                listView_taxpayers.setAdapter(new PropertyAdapter(getApplicationContext(), myList));
            }
            // Show the latest retrived results on the top
            listView_taxpayers.setSelectionFromTop(position, 0);
            // Close the progressdialog
            mProgressDialog.dismiss();

        }

    }

    void PopulateList(int start, int limit)
    {
        String sql = null;
        if(app.getSqlQuery() != null && !app.getSqlQuery().isEmpty())
        {
            sql = app.getSqlQuery() + " ORDER BY idstreet DESC LIMIT " + start + "," + limit + ";";
        }
        else {
            sql = "SELECT * FROM lands ORDER BY land_id DESC LIMIT " + start + "," + limit + ";";
        }
        Cursor cursor = dataDB.myConnection(getApplicationContext()).selectAllFromTable(sql, true);
        if(cursor.moveToFirst()) {
            do {
                Taxpayer taxpayer = new Taxpayer();
//                String tpID = cursor.getString(cursor.getColumnIndex("tax_registration_id"));
                String tpID = cursor.getString(cursor.getColumnIndex("land_tag_number"));
                String fname = cursor.getString(cursor.getColumnIndex("town"));
                String sname = cursor.getString(cursor.getColumnIndex("land_name"));
                String mname = cursor.getString(cursor.getColumnIndex("street"));
                String mlandrin = cursor.getString(cursor.getColumnIndex("land_rin"));
                app.setLandrin(mlandrin);


                //Log.e(TAG,"tax_reg_id " + tpID);
                String photo = null;
                /*taxpayer.setPhone_number(cursor.getString(cursor.getColumnIndex("phone_number")));
                taxpayer.setRegistered_on(cursor.getString(cursor.getColumnIndex("registered_on")));
                taxpayer.setTax_registration_id(tpID);
                taxpayer.setTax_id(cursor.getString(cursor.getColumnIndex("tax_id")));
                taxpayer.setTemp_tax_id(cursor.getString(cursor.getColumnIndex("temp_tax_id")));
                if((cursor.getString(cursor.getColumnIndex("temp_tax_id"))).equalsIgnoreCase(null)||(cursor.getString(cursor.getColumnIndex("temp_tax_id"))).equalsIgnoreCase("")){
                    taxpayer.setTemp_tax_id(cursor.getString(cursor.getColumnIndex("tax_id")));
                }*/

                taxpayer.setTown(cursor.getString(cursor.getColumnIndex("town")));
                taxpayer.setLga(cursor.getString(cursor.getColumnIndex("lga")));
                taxpayer.setWard(cursor.getString(cursor.getColumnIndex("ward")));
                taxpayer.setAssettype(cursor.getString(cursor.getColumnIndex("asset_type")));


                taxpayer.setTemp_tax_id(tpID);
                taxpayer.setSurname(sname);
                taxpayer.setRegistered_on(mname);

                taxpayer.setFirst_name(fname);
                taxpayer.setMiddle_name(mname);
                if(photo != null && !photo.isEmpty() && photo != "null")
                {
                    taxpayer.setPhoto(photo);
                }


                myList.add(taxpayer);
            } while (cursor.moveToNext());
        }
    }

    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }

}
