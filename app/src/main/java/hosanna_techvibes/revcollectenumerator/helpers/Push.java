package hosanna_techvibes.revcollectenumerator.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import hosanna_techvibes.revcollectenumerator.R;
import hosanna_techvibes.revcollectenumerator.databases.DataDB;
import hosanna_techvibes.revcollectenumerator.model.AuthorizationHttpResponse;


/**
 * Created by Hosanna_TechVibes on 04-Dec-17.
 */

public class Push {

    String errorMssg="";
    AuthorizationHttpResponse authResp;
    private static final String TAG = "Push";
    DataDB dataDB = new DataDB();

    public void Fire(String whatToSync, String jsonResultName, String url, String mobileSynchTableToUpdate, String service_id, String client_userid, String service_code, Context context){

       /* String myPath = "/data/data/com.mobile.techvibesltd.revcollectmobile/databases/" + "revcollect_mobile.db";// Set path to your database
        SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
*/

        SQLiteDatabase myDataBase =dataDB.myConnection(context).getWritableDatabase();

//        String _tableName = "mobile_synch_tax_registration";
        String _tableName = whatToSync;

//        Cursor cursor = dataDB.myConnection(getApplicationContext()).ms(_tableName);
//        Cursor cursor = myDataBase.rawQuery("SELECT * FROM " + _tableName + " WHERE synch_status=0 AND service_id='"+service_id+"' LIMIT 50;", null);

        Cursor cursor = myDataBase.rawQuery("SELECT * FROM " + _tableName + " WHERE synch_status=0 LIMIT 50;", null);


        JSONArray resultSetArray 	= new JSONArray();
        JSONObject returnJSON 	= new JSONObject();

        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {

            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();

            for( int i=0 ;  i< totalColumn ; i++ )
            {
                if( cursor.getColumnName(i) != null )
                {

                    try
                    {

                        if( cursor.getString(i) != null )
                        {
                            Log.d(TAG, cursor.getString(i) );
                            rowObject.put(cursor.getColumnName(i) ,  cursor.getString(i) );
                        }
                        else
                        {
                            rowObject.put( cursor.getColumnName(i) ,  "null" );
                        }
                    }
                    catch( Exception e )
                    {
                        Log.d(TAG, e.getMessage()  );
                    }
                }

            }
            cursor.moveToNext();

            resultSetArray.put(rowObject);

        }

        try
        {
            Log.e(TAG, "userid:"+client_userid.toString() );
            Log.e(TAG, "servicecode:"+service_code.toString() );
            Log.e(TAG, "serviceid"+service_id.toString() );

//            returnJSON.put("ServiceCode", service_code);
//            returnJSON.put("ServiceID", service_id);
//
            returnJSON.put("UserID", client_userid);
            returnJSON.put("ServiceCode", service_code);
//            returnJSON.put("ServiceID", service_id);
            returnJSON.put(jsonResultName, resultSetArray);
        }
        catch( Exception e )
        {
            Log.d(TAG, e.getMessage()  );
        }
        //Log.e("TAG_NAME", resultSetArray.toString() );
        Log.e(TAG, returnJSON.toString() );


        cursor.close();

        //return resultSet;


        ServiceHandler serviceHandler = new ServiceHandler();
        AuthorizationHttpResponse httpResp = serviceHandler.makeServiceCallJSON(url, 2, returnJSON);


        if (httpResp != null && httpResp.getResponseData() != null) {


            Log.e(TAG, "trying to sync "+whatToSync+ ":::this is the response " + httpResp.getResponseCode() + "::" + httpResp.getResponseData());
            errorMssg = httpResp.getResponseData();
            //Toast.makeText(getApplicationContext(), "Something wrong", Toast.LENGTH_SHORT).show();
            String respcode = String.valueOf(httpResp.getResponseCode());
            errorMssg = respcode;
            authResp = httpResp;

            try {

                JSONObject parentObject = new JSONObject(httpResp.getResponseData());
                String childObject = parentObject.getString("response");
          /*      String SeseResponseCode = childObject.getString("responseCode");

                String SeseResponseDescription = childObject.getString("responseDescription");
*/

                if (childObject.equals("00")) {

                    JSONArray mobileSynchArray = parentObject.getJSONArray("mobile_synch");

                    for (int i = 0; i < mobileSynchArray.length(); i++) {
                        JSONObject presentObject = mobileSynchArray.getJSONObject(i);


                        int status = presentObject.getInt("status");
                        String authorizationID = presentObject.getString("authorization_id");

                        if(status==1) {
                            //Toast.makeText(getApplicationContext(), i, Toast.LENGTH_SHORT).show();

                            Log.e(TAG, " response::status: " + status);
                            Log.e(TAG, " response::authorizationId: " + authorizationID);

                            ContentValues contentValues = new ContentValues();

                            contentValues.put("synch_status", 1);
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
                            Calendar timer = Calendar.getInstance();
                            String formattedDate = df.format(timer.getTime());
                            contentValues.put("last_synched_date", formattedDate);
                            contentValues.put("authorization_id", authorizationID);

                            Log.i("Updating ", "mobile synch table");

                            long rowInserted = dataDB.myConnection(context).onUpdateOrIgnore(contentValues, mobileSynchTableToUpdate,"authorization_id", authorizationID);
                            if (rowInserted != -1) {

                                Log.i("MOBILE SYNCH "+mobileSynchTableToUpdate, "UPDATED ");
                                //revenueDimensionsBufferedData.append(authorizationID+",");

                            } else {
                                Log.i("ERROR-Saving to db"+mobileSynchTableToUpdate, ":-( ");
                                //errorMssg = httpResp.getResponseData();
                                // Toast.makeText(getApplicationContext(), "Something wrong", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                }
                else {
                    Log.w(TAG,"User Validation Failed, Could not get mobile synch");
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Log.d("Connecting", " to service failed...to push data");
        }


    }

    public void Checkin(String whatToSync, String jsonResultName, String url, String mobileSynchTableToUpdate, String service_id, String client_userid, String service_code, Context context){

       /* String myPath = "/data/data/com.mobile.techvibesltd.revcollectmobile/databases/" + "revcollect_mobile.db";// Set path to your database
        SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
*/

        SQLiteDatabase myDataBase =dataDB.myConnection(context).getWritableDatabase();

//        String _tableName = "mobile_synch_tax_registration";
        String _tableName = whatToSync;

//        Cursor cursor = dataDB.myConnection(getApplicationContext()).ms(_tableName);
        Cursor cursor = myDataBase.rawQuery("SELECT * FROM " + _tableName + " WHERE synch_status=0 AND service_id='"+service_id+"' LIMIT 50;", null);

        JSONArray resultSetArray 	= new JSONArray();
        JSONObject returnJSON 	= new JSONObject();

        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {

            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();

            for( int i=0 ;  i< totalColumn ; i++ )
            {
                if( cursor.getColumnName(i) != null )
                {

                    try
                    {

                        if( cursor.getString(i) != null )
                        {
                            Log.d(TAG, cursor.getString(i) );
                            rowObject.put(cursor.getColumnName(i) ,  cursor.getString(i) );
                        }
                        else
                        {
                            rowObject.put( cursor.getColumnName(i) ,  "null" );
                        }
                    }
                    catch( Exception e )
                    {
                        Log.d(TAG, e.getMessage()  );
                    }
                }

            }
            cursor.moveToNext();

            resultSetArray.put(rowObject);

        }

        try
        {
            returnJSON.put("UserID", client_userid);
            returnJSON.put("ServiceCode", service_code);
//            returnJSON.put("appKeyParameter", context.getResources().getString(R.string.appId));
            returnJSON.put(jsonResultName, resultSetArray);
        }
        catch( Exception e )
        {
            Log.d(TAG, e.getMessage()  );
        }
        //Log.e("TAG_NAME", resultSetArray.toString() );
        Log.e(TAG, returnJSON.toString() );


        cursor.close();

        //return resultSet;


        ServiceHandler serviceHandler = new ServiceHandler();
        AuthorizationHttpResponse httpResp = serviceHandler.makeServiceCallJSON(url, 2, returnJSON);


        if (httpResp != null && httpResp.getResponseData() != null) {


            Log.e(TAG, "trying to sync "+whatToSync+ ":::this is the response " + httpResp.getResponseCode() + "::" + httpResp.getResponseData());
            errorMssg = httpResp.getResponseData();
            //Toast.makeText(getApplicationContext(), "Something wrong", Toast.LENGTH_SHORT).show();
            String respcode = String.valueOf(httpResp.getResponseCode());
            errorMssg = respcode;
            authResp = httpResp;

            try {

                JSONObject parentObject = new JSONObject(httpResp.getResponseData());
                JSONObject childObject = parentObject.getJSONObject("response");
                String SeseResponseCode = childObject.getString("responseCode");

                String SeseResponseDescription = childObject.getString("responseDescription");
                JSONArray mobileSynchArray = parentObject.getJSONArray("mobile_synch");


                for (int i = 0; i < mobileSynchArray.length(); i++) {
                    JSONObject presentObject = mobileSynchArray.getJSONObject(i);


                    int status = presentObject.getInt("status");
                    String authorizationID = presentObject.getString("authorization_id");

                    if(status==1) {
                        //Toast.makeText(getApplicationContext(), i, Toast.LENGTH_SHORT).show();

                        Log.e(TAG, " response::status: " + status);
                        Log.e(TAG, " response::authorizationId: " + authorizationID);

                        ContentValues contentValues = new ContentValues();

                        contentValues.put("synch_status", 1);
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
                        Calendar timer = Calendar.getInstance();
                        String formattedDate = df.format(timer.getTime());
                        //contentValues.put("last_synch_date", formattedDate);
                        contentValues.put("authorization_id", authorizationID);

                        Log.i("Updating ", "mobile synch table");

                        long rowInserted = dataDB.myConnection(context).onUpdateOrIgnore(contentValues, mobileSynchTableToUpdate,"authorization_id", authorizationID);
                        if (rowInserted != -1) {

                            Log.i("MOBILE SYNCH "+mobileSynchTableToUpdate, "UPDATED ");
                            //revenueDimensionsBufferedData.append(authorizationID+",");

                        } else {
                            Log.i("ERROR-Saving to db"+mobileSynchTableToUpdate, ":-( ");
                            //errorMssg = httpResp.getResponseData();
                            // Toast.makeText(getApplicationContext(), "Something wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                }




            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Log.d("Connecting", " to service failed...to push data");
        }


    }

    public void Checkinn(String whatToSync, String jsonResultName, String url, String mobileSynchTableToUpdate, String service_id, String client_userid, String service_code, Context context){

       /* String myPath = "/data/data/com.mobile.techvibesltd.revcollectmobile/databases/" + "revcollect_mobile.db";// Set path to your database
        SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
*/

        SQLiteDatabase myDataBase =dataDB.myConnection(context).getWritableDatabase();

//        String _tableName = "mobile_synch_tax_registration";
        String _tableName = whatToSync;

//        Cursor cursor = dataDB.myConnection(getApplicationContext()).ms(_tableName);
        Cursor cursor = myDataBase.rawQuery("SELECT * FROM " + _tableName + " WHERE number_of_retry=0 LIMIT 50;", null);

        JSONArray resultSetArray 	= new JSONArray();
        JSONObject returnJSON 	= new JSONObject();

        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {

            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();

            for( int i=0 ;  i< totalColumn ; i++ )
            {
                if( cursor.getColumnName(i) != null )
                {

                    try
                    {

                        if( cursor.getString(i) != null )
                        {
                            Log.d(TAG, cursor.getString(i) );
                            rowObject.put(cursor.getColumnName(i) ,  cursor.getString(i) );
                        }
                        else
                        {
                            rowObject.put( cursor.getColumnName(i) ,  "null" );
                        }
                    }
                    catch( Exception e )
                    {
                        Log.d(TAG, e.getMessage()  );
                    }
                }

            }
            cursor.moveToNext();

            resultSetArray.put(rowObject);

        }

        try
        {
            returnJSON.put("UserID", client_userid);
            returnJSON.put("ServiceCode", service_code);
//            returnJSON.put("appKeyParameter", context.contextgetResources().getString(R.string.appId));
            returnJSON.put(jsonResultName, resultSetArray);
        }
        catch( Exception e )
        {
            Log.d(TAG, e.getMessage()  );
        }
        //Log.e("TAG_NAME", resultSetArray.toString() );
        Log.e(TAG, returnJSON.toString() );


        cursor.close();

        //return resultSet;


        ServiceHandler serviceHandler = new ServiceHandler();
        AuthorizationHttpResponse httpResp = serviceHandler.makeServiceCallJSON(url, 2, returnJSON);


        if (httpResp != null && httpResp.getResponseData() != null) {


            Log.e(TAG, "trying to sync "+whatToSync+ ":::this is the response " + httpResp.getResponseCode() + "::" + httpResp.getResponseData());
            errorMssg = httpResp.getResponseData();
            //Toast.makeText(getApplicationContext(), "Something wrong", Toast.LENGTH_SHORT).show();
            String respcode = String.valueOf(httpResp.getResponseCode());
            errorMssg = respcode;
            authResp = httpResp;

            try {

                JSONObject parentObject = new JSONObject(httpResp.getResponseData());
                JSONObject childObject = parentObject.getJSONObject("response");
                String SeseResponseCode = childObject.getString("responseCode");

                String SeseResponseDescription = childObject.getString("responseDescription");
                JSONArray mobileSynchArray = parentObject.getJSONArray("mobile_synch");


                for (int i = 0; i < mobileSynchArray.length(); i++) {
                    JSONObject presentObject = mobileSynchArray.getJSONObject(i);


                    int status = presentObject.getInt("status");
                    String authorizationID = presentObject.getString("authorization_id");

                    if(status==1) {
                        //Toast.makeText(getApplicationContext(), i, Toast.LENGTH_SHORT).show();

                        Log.e(TAG, " response::status: " + status);
                        Log.e(TAG, " response::authorizationId: " + authorizationID);

                        ContentValues contentValues = new ContentValues();

                        contentValues.put("number_of_retry", 1);
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
                        Calendar timer = Calendar.getInstance();
                        String formattedDate = df.format(timer.getTime());
                        //contentValues.put("last_synch_date", formattedDate);
                        contentValues.put("authorization_id", authorizationID);

                        Log.i("Updating ", "mobile synch table");

                        long rowInserted = dataDB.myConnection(context).onUpdateOrIgnore(contentValues, mobileSynchTableToUpdate,"authorization_id", authorizationID);
                        if (rowInserted != -1) {

                            Log.i("MOBILE SYNCH "+mobileSynchTableToUpdate, "UPDATED ");
                            //revenueDimensionsBufferedData.append(authorizationID+",");

                        } else {
                            Log.i("ERROR-Saving to db"+mobileSynchTableToUpdate, ":-( ");
                            //errorMssg = httpResp.getResponseData();
                            // Toast.makeText(getApplicationContext(), "Something wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                }




            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Log.d("Connecting", " to service failed...to push data");
        }


    }
    public void nor(String whatToSync, String jsonResultName, String url, String mobileSynchTableToUpdate, String service_id, String client_userid, String service_code, Context context){

       /* String myPath = "/data/data/com.mobile.techvibesltd.revcollectmobile/databases/" + "revcollect_mobile.db";// Set path to your database
        SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
*/

        SQLiteDatabase myDataBase =dataDB.myConnection(context).getWritableDatabase();

//        String _tableName = "mobile_synch_tax_registration";
        String _tableName = whatToSync;

//        Cursor cursor = dataDB.myConnection(getApplicationContext()).ms(_tableName);
        Cursor cursor = myDataBase.rawQuery("SELECT * FROM " + _tableName + " WHERE number_of_retry=0 LIMIT 50;", null);

        JSONArray resultSetArray 	= new JSONArray();
        JSONObject returnJSON 	= new JSONObject();

        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {

            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();

            for( int i=0 ;  i< totalColumn ; i++ )
            {
                if( cursor.getColumnName(i) != null )
                {

                    try
                    {

                        if( cursor.getString(i) != null )
                        {
                            Log.d(TAG, cursor.getString(i) );
                            rowObject.put(cursor.getColumnName(i) ,  cursor.getString(i) );
                        }
                        else
                        {
                            rowObject.put( cursor.getColumnName(i) ,  "null" );
                        }
                    }
                    catch( Exception e )
                    {
                        Log.d(TAG, e.getMessage()  );
                    }
                }

            }
            cursor.moveToNext();

            resultSetArray.put(rowObject);

        }

        try
        {
            returnJSON.put("UserID", client_userid);
            returnJSON.put("ServiceCode", service_code);
//            returnJSON.put("appKeyParameter", context.getResources().getString(R.string.appId));
            returnJSON.put(jsonResultName, resultSetArray);
        }
        catch( Exception e )
        {
            Log.d(TAG, e.getMessage()  );
        }
        //Log.e("TAG_NAME", resultSetArray.toString() );
        Log.e(TAG, returnJSON.toString() );


        cursor.close();

        //return resultSet;


        ServiceHandler serviceHandler = new ServiceHandler();
        AuthorizationHttpResponse httpResp = serviceHandler.makeServiceCallJSON(url, 2, returnJSON);


        if (httpResp != null && httpResp.getResponseData() != null) {


            Log.e(TAG, "trying to sync "+whatToSync+ ":::this is the response " + httpResp.getResponseCode() + "::" + httpResp.getResponseData());
            errorMssg = httpResp.getResponseData();
            //Toast.makeText(getApplicationContext(), "Something wrong", Toast.LENGTH_SHORT).show();
            String respcode = String.valueOf(httpResp.getResponseCode());
            errorMssg = respcode;
            authResp = httpResp;

            try {

                JSONObject parentObject = new JSONObject(httpResp.getResponseData());
                JSONObject childObject = parentObject.getJSONObject("response");
                String SeseResponseCode = childObject.getString("responseCode");

                String SeseResponseDescription = childObject.getString("responseDescription");
                JSONArray mobileSynchArray = parentObject.getJSONArray("mobile_synch");


                for (int i = 0; i < mobileSynchArray.length(); i++) {
                    JSONObject presentObject = mobileSynchArray.getJSONObject(i);


                    int status = presentObject.getInt("status");
                    String authorizationID = presentObject.getString("authorization_id");

                    if(status==1) {
                        //Toast.makeText(getApplicationContext(), i, Toast.LENGTH_SHORT).show();

                        Log.e(TAG, " response::status: " + status);
                        Log.e(TAG, " response::authorizationId: " + authorizationID);

                        ContentValues contentValues = new ContentValues();

                        contentValues.put("number_of_retry", 1);
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
                        Calendar timer = Calendar.getInstance();
                        String formattedDate = df.format(timer.getTime());
                        //contentValues.put("last_synch_date", formattedDate);
                        contentValues.put("authorization_id", authorizationID);

                        Log.i("Updating ", "mobile synch table");

                        long rowInserted = dataDB.myConnection(context).onUpdateOrIgnore(contentValues, mobileSynchTableToUpdate,"authorization_id", authorizationID);
                        if (rowInserted != -1) {

                            Log.i("MOBILE SYNCH "+mobileSynchTableToUpdate, "UPDATED ");
                            //revenueDimensionsBufferedData.append(authorizationID+",");

                        } else {
                            Log.i("ERROR-Saving to db"+mobileSynchTableToUpdate, ":-( ");
                            //errorMssg = httpResp.getResponseData();
                            // Toast.makeText(getApplicationContext(), "Something wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                }




            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Log.d("Connecting", " to service failed...to push data");
        }


    }

}
