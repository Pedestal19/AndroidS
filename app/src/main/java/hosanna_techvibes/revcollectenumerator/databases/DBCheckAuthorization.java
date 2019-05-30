package hosanna_techvibes.revcollectenumerator.databases;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Hosanna_Techvibes on 07/02/2017.
 */
public class DBCheckAuthorization {
    DBConnection myDBconnection;
    private static final String TAG = "DBCheckAuthorization";

    public Cursor checkForAuthorizationRequest(Context context)
    {
        Cursor cursor;
//        String sql = "SELECT * FROM users WHERE authorized=0 ORDER BY id_users DESC LIMIT 1;";//WHERE authorized=0
        String sql = "SELECT * FROM users ORDER BY id_users DESC LIMIT 1;";//WHERE authorized=0

        myDBconnection = new DBConnection(context);
        try {
            myDBconnection.createDataBase();
        } catch (IOException e) {
        }
        if(myDBconnection.checkDataBase()){
            myDBconnection.openDataBase();
            SQLiteDatabase db = myDBconnection.getWritableDatabase();
            //Cursor cursor = db.query("street", new String[] {KEY_ID,KEY_AREACODE,KEY_NAME,KEY_CITY},null,null,null,null,null);

            cursor = db.rawQuery(sql, null);
            Log.i(TAG, "Result from DBCheckAuthorization query; " + cursor.toString());
            if(cursor != null)
            {
                cursor.moveToFirst();
            }

            db.close();

        }else{
            return null;
        }

        return  cursor;
    }
    public String fetchUserEmail(Context context)
    {
        Cursor cursor; String email=null;
        String sql = "SELECT * FROM users WHERE authorized=0 ORDER BY id_users DESC LIMIT 1;";
        myDBconnection = new DBConnection(context);
        try {
            myDBconnection.createDataBase();
        } catch (IOException e) {
        }

        if(myDBconnection.checkDataBase()){
            myDBconnection.openDataBase();
            SQLiteDatabase db = myDBconnection.getWritableDatabase();
            //Cursor cursor = db.query("street", new String[] {KEY_ID,KEY_AREACODE,KEY_NAME,KEY_CITY},null,null,null,null,null);

            cursor = db.rawQuery(sql, null);
            Log.i(TAG, "Result from DBCheckAuthorization query; " + cursor.toString());
            if(cursor != null && cursor.getCount()>0)
            {
                if (cursor.moveToFirst()) {
                    do {
                        if(cursor.getString(5) != null) {
                            Log.d("email found", cursor.getString(5));
                            email = cursor.getString(5);
                        }


                    } while (cursor.moveToNext());
                }
            }

        }else{
            return email;
        }

        return email;
    }
}
