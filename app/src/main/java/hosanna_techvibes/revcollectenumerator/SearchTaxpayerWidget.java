package hosanna_techvibes.revcollectenumerator;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

import hosanna_techvibes.revcollectenumerator.adapter.TaxpayerA;
import hosanna_techvibes.revcollectenumerator.adapter.TaxpayerSearch;
import hosanna_techvibes.revcollectenumerator.databases.DataDB;
import hosanna_techvibes.revcollectenumerator.model.Taxpayer;

public class SearchTaxpayerWidget extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private ListView list;
    private TaxpayerA adapter;
    private SearchView editsearch;
    public static ArrayList<TaxpayerSearch> taxpayerArrayList = new ArrayList<TaxpayerSearch>();
    DataDB dataDB = new DataDB();

    private int start = 0;
    private int limit = 7;

    private String name;
    private String rin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_search_taxpayer_widget);

        list = (ListView) findViewById(R.id.listview);

        taxpayerArrayList = new ArrayList<>();
        PopulateList(start += 7, limit);

        adapter = new TaxpayerA(this);
        list.setAdapter(adapter);

        editsearch = (SearchView) findViewById(R.id.search);
        editsearch.setOnQueryTextListener(this);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(SearchTaxpayerWidget.this, taxpayerArrayList.get(position).getName(), Toast.LENGTH_SHORT).show();
                name = taxpayerArrayList.get(position).getName();
                rin = taxpayerArrayList.get(position).getT_id();
                alert3();
            }
        });




        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
*/
    }

    void alert3(){

        final android.support.v7.app.AlertDialog.Builder adb = new android.support.v7.app.AlertDialog.Builder(
                this);
        adb.setTitle("Confirm");
        adb.setIcon(R.mipmap.ic_launcher);
        adb.setMessage("Are you sure you want to select "+name);
        adb.setNeutralButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
//                startActivityForResult(new Intent(getApplicationContext(), SearchTaxpayerWidget.class),1);
            }
        });
        adb.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //startActivity(new Intent(getApplicationContext(),PhotoRegistration.class));
                dialog.dismiss();
//            et_taxid.setFocusable(true);
                myFinish();
            }
        });


        adb.show();

    }


    void myFinish(){
//        Toast.makeText(this, name+"\n"+rin, Toast.LENGTH_SHORT).show();
        Intent intent=new Intent();
        intent.putExtra("name",name);
        intent.putExtra("rin",rin);
        setResult(1,intent);

        //app.setSI(result);
        finish();

    }


    void PopulateList(int start, int limit)
    {
        Log.e("inside pop list", " inside method ");

//        String sql = null;

//        String sql = "SELECT * FROM states ORDER BY id DESC LIMIT " + start + "," + limit + ";";
        String sql = "SELECT * FROM tax_payers ORDER BY taxpayer_id DESC ;";

        Cursor cursor = dataDB.myConnection(getApplicationContext()).selectAllFromTable(sql, true);
        if(cursor.moveToFirst()) {
            do {
                Taxpayer taxpayer = new Taxpayer();
                String tpID = cursor.getString(cursor.getColumnIndex("taxpayer_rin"));
                String fname = cursor.getString(cursor.getColumnIndex("taxpayer_name"));
                Log.e("test", "name "+fname+" id "+tpID);
                TaxpayerSearch i = new TaxpayerSearch(fname, tpID);
                taxpayerArrayList.add(i);
//                taxpayerArrayList.add(new TaxpayerSearch("dummy","123"));



                taxpayer.setTemp_tax_id(tpID);
                taxpayer.setSurname(fname);

//                myList.add(taxpayer);
            } while (cursor.moveToNext());
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        adapter.filter(text);
        return false;
    }

    @Override
    public void onBackPressed() {

       // Toast.makeText(getApplicationContext(), "on back pressed", Toast.LENGTH_SHORT).show();

        Intent intent=new Intent();
        intent.putExtra("name"," ");
        intent.putExtra("rin","");
        setResult(1,intent);
        finish();
    }
}
