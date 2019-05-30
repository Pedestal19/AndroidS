package hosanna_techvibes.revcollectenumerator.databases;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import java.io.IOException;

import hosanna_techvibes.revcollectenumerator.model.App;

/**
 * Created by Hosanna on 05/10/2016.
 */
public class DataDB {
    private static final String TAG = "DataDB";
    App appState;
    App app;
    DBConnection myDBconnection;
    public DBConnection myConnection(Context context) {
        myDBconnection = new DBConnection(context);
        try {
            myDBconnection.createDataBase();
        } catch (IOException e) {
        }
        if (myDBconnection.checkDataBase()) {
            myDBconnection.openDataBase();
        }
        return myDBconnection;
    }

    public double getMyWalletBalance(Context context, String _tax_id, String _userid)
    {
        String[] args = {_tax_id.trim(),_userid.trim()};
        appState = ((App)context);
        String __walletBal = "";
        SQLiteDatabase db = myConnection(context).getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT wallet_balance FROM payer_balance WHERE tax_id=? AND user_id=? LIMIT 1", args);
        int k = cursor.getCount();
        try{
            if (cursor.moveToFirst()) {
                do {
                    __walletBal = cursor.getString(0);
                    appState.setMyWalletBalance(__walletBal);
                    Log.i("WalletLog", cursor.getString(0));

                } while (cursor.moveToNext());
            }
        }finally{
            cursor.close();
            db.close();
        }
        myDBconnection.close();
        double walletBal = 0;
        if(__walletBal != null && !__walletBal.equalsIgnoreCase("null") && !__walletBal.isEmpty())
        {
            walletBal = Double.valueOf(__walletBal);
        }
        return walletBal;
    }

    public String getTaxPayerActualTIN(Context context, String _tax_id)
    {
        String[] args = {_tax_id.trim()};
        appState = ((App)context);
        String _real_taxid="";
        SQLiteDatabase db = myConnection(context).getWritableDatabase();
        String sq = "SELECT tax_id,temp_tax_id FROM tax_registration WHERE tax_id='" + _tax_id + "' OR temp_tax_id='" + _tax_id +"' LIMIT 1";
        Cursor cursor = db.rawQuery(sq, null);
        int k = cursor.getCount();
        try{
            String _taxid = "",_temp_taxid="";
            if (cursor.moveToFirst()) {
                do {
                    _taxid = cursor.getString(0);
                    _temp_taxid = cursor.getString(1);
                } while (cursor.moveToNext());
            }
            if(_temp_taxid != null && !_temp_taxid.isEmpty())
            {
                _real_taxid = _temp_taxid;
            }
            if(_taxid != null && !_taxid.isEmpty())
            {
                _real_taxid = _taxid;
            }
        }finally{
            cursor.close();
            db.close();
        }
        myDBconnection.close();

        return _real_taxid;
    }

    public String getTaxPayerTaxRegistrationIDByTIN(Context context, String _tax_id)
    {
        String[] args = {_tax_id.trim()};
        appState = ((App)context);
        String _tax_registration_id="";
        SQLiteDatabase db = myConnection(context).getWritableDatabase();
        String sq = "SELECT tax_registration_id FROM tax_registration WHERE tax_id='" + _tax_id + "' OR temp_tax_id='" + _tax_id +"' LIMIT 1";
        //Cursor cursor = db.rawQuery("SELECT tax_registration_id FROM tax_registration WHERE tax_id=? OR temp_tax_id=? LIMIT 1", args);
        Cursor cursor = db.rawQuery(sq,null);
        int k = cursor.getCount();
        try{
            String _taxid = "",_temp_taxid="";
            if (cursor.moveToFirst()) {
                do {
                    _tax_registration_id = cursor.getString(0);
                } while (cursor.moveToNext());
            }

        }finally{
            cursor.close();
            db.close();
        }
        myDBconnection.close();

        return _tax_registration_id;
    }

    //
    public String getTaxPayerTaxRegistrationIDByTIN2(Context context, String _tax_id)
    {
        String[] args = {_tax_id.trim()};
        appState = ((App)context);
        String _tax_registration_id="";
        SQLiteDatabase db = myConnection(context).getWritableDatabase();
        String sq = "SELECT tax_registration_id FROM tax_registration WHERE tax_id='" + _tax_id + "' OR temp_tax_id='" + _tax_id +"' LIMIT 1";
        //Cursor cursor = db.rawQuery("SELECT tax_registration_id FROM tax_registration WHERE tax_id=? OR temp_tax_id=? LIMIT 1", args);
        Cursor cursor = db.rawQuery(sq,null);
        int k = cursor.getCount();
        try{
            String _taxid = "",_temp_taxid="";
            if (cursor.moveToFirst()) {
                do {
                    _tax_registration_id = cursor.getString(0);
                } while (cursor.moveToNext());
            }

        }finally{
            cursor.close();
            db.close();
        }
        myDBconnection.close();

        return _tax_registration_id;
    }


    // TODO: database date time now

    public String getDateTime(Context context)
    {
        //String[] args = {_tax_id.trim()};
        appState = ((App)context);
        String getDateTime="2017-05-25";
        SQLiteDatabase db = myConnection(context).getWritableDatabase();
        String sq = "SELECT datetime('now')";
        //Cursor cursor = db.rawQuery("SELECT tax_registration_id FROM tax_registration WHERE tax_id=? OR temp_tax_id=? LIMIT 1", args);
        Cursor cursor = db.rawQuery(sq,null);
        int k = cursor.getCount();
        try{
            String _taxid = "",_temp_taxid="";
            if (cursor.moveToFirst()) {
                do {
                    getDateTime = cursor.getString(0);
                } while (cursor.moveToNext());
            }

        }finally{
            cursor.close();
            db.close();
        }
        myDBconnection.close();

        return getDateTime;
    }


// TODO: 23-May-17 bir login
public int login1(Context context, String email, String passw)
{
    String[] args = {email.trim(),passw.trim()};
    String usrid = null;

    appState = ((App)context);
    Log.i("LoginCount", email + ":" + passw);
    SQLiteDatabase db = myConnection(context).getWritableDatabase();
    //Cursor cursor2 = db.rawQuery("SELECT id_users,first_name,surname,login,pwd,group_id,service_id FROM users WHERE login=? AND pwd=? AND authorized=1 LIMIT 1", args);
    Cursor cursor = db.rawQuery("SELECT id_users,first_name,surname,login,pwd,group_id,service_id, user_id from users  where login='"+email+"' and pwd='"+passw+"' and user_type='1' and authorized=1 limit 1",null);
    //Cursor cursor = db.rawQuery("SELECT authorized from users  where login='"+email+"' and pwd='"+passw+"' limit 1",null);

    int k = cursor.getCount();
    //int k2 = cursor2.getCount();
    Log.i("LoginLog", Integer.toString(k));
    //Log.i("LoginLog****", Integer.toString(k2));
    try{
        if (cursor.moveToFirst()) {
            do {
                usrid = cursor.getString(0);
                appState.setUserid(usrid);
                Log.i("##User_id^^^", cursor.getString(0));

                String userid = cursor.getString(7);
                appState.setUserid(userid);


                Log.i("##User_id+++", cursor.getString(7));
                appState.setMyEmail(cursor.getString(3));
                Log.i("##Email^^^", cursor.getString(3));
                if(cursor.getString(6) != null) {
                    appState.setServiceID(cursor.getString(6));
                    Log.i("##Service Id^^^", cursor.getString(6));
                }
                appState.setMySurname(cursor.getString(2));
                Log.i("##Surname^^^", cursor.getString(2));
                appState.setMyFirstName(cursor.getString(1));
                Log.i("##Firstname^^^", cursor.getString(1));

            } while (cursor.moveToNext());
        }
    }finally{
        cursor.close();
        db.close();
    }

    myDBconnection.close();

    return k;
}

    // TODO: 23-May-17 agent login
    public int login2(Context context, String email, String passw)
    {
        String[] args = {email.trim(),passw.trim()};
        String usrid = null;

        appState = ((App)context);
        Log.i("LoginCount", email + ":" + passw);
        SQLiteDatabase db = myConnection(context).getWritableDatabase();
        //Cursor cursor2 = db.rawQuery("SELECT id_users,first_name,surname,login,pwd,group_id,service_id FROM users WHERE login=? AND pwd=? AND authorized=1 LIMIT 1", args);
        Cursor cursor = db.rawQuery("SELECT id_users,first_name,surname,login,pwd,group_id,service_id, user_id from users  where login='"+email+"' and pwd='"+passw+"' and authorized=1 limit 1",null);
        //Cursor cursor = db.rawQuery("SELECT authorized from users  where login='"+email+"' and pwd='"+passw+"' limit 1",null);

        int k = cursor.getCount();
        //int k2 = cursor2.getCount();
        Log.i("LoginLog", Integer.toString(k));
        //Log.i("LoginLog****", Integer.toString(k2));
        try{
            if (cursor.moveToFirst()) {
                do {
                    usrid = cursor.getString(0);
                    appState.setUserid(usrid);
                    Log.i("##User_id^^^", cursor.getString(0));

                    String userid = cursor.getString(7);
                    appState.setUserid(userid);


                    Log.i("##User_id+++", cursor.getString(7));
                    appState.setMyEmail(cursor.getString(3));
                    Log.i("##Email^^^", cursor.getString(3));
                    if(cursor.getString(6) != null) {
                        appState.setServiceID(cursor.getString(6));
                        Log.i("##Service Id^^^", cursor.getString(6));
                    }
                    appState.setMySurname(cursor.getString(2));
                    Log.i("##Surname^^^", cursor.getString(2));
                    appState.setMyFirstName(cursor.getString(1));
                    Log.i("##Firstname^^^", cursor.getString(1));

                } while (cursor.moveToNext());
            }
        }finally{
            cursor.close();
            db.close();
        }

        myDBconnection.close();

        return k;
    }

    // TODO: 23-May-17 field officer login
    public int login3(Context context, String email, String passw)
    {
        String[] args = {email.trim(),passw.trim()};
        String usrid = null;

        appState = ((App)context);
        Log.i("LoginCount", email + ":" + passw);
        SQLiteDatabase db = myConnection(context).getWritableDatabase();
        //Cursor cursor2 = db.rawQuery("SELECT id_users,first_name,surname,login,pwd,group_id,service_id FROM users WHERE login=? AND pwd=? AND authorized=1 LIMIT 1", args);
        Cursor cursor = db.rawQuery("SELECT id_users,first_name,surname,login,pwd,group_id,service_id, user_id from users  where login='"+email+"' and pwd='"+passw+"' and user_type='3' and authorized=1 ORDER BY id_users DESC limit 1",null);
        //Cursor cursor = db.rawQuery("SELECT authorized from users  where login='"+email+"' and pwd='"+passw+"' limit 1",null);

        int k = cursor.getCount();
        //int k2 = cursor2.getCount();
        Log.i("LoginLog", Integer.toString(k));
        //Log.i("LoginLog****", Integer.toString(k2));
        try{
            if (cursor.moveToFirst()) {
                do {
                    usrid = cursor.getString(0);
                    appState.setUserid(usrid);
                    Log.i("##User_id(kounter)^^^", cursor.getString(0));

                    String userid = cursor.getString(7);
                    appState.setUserid(userid);


                    Log.i("##User_id+++", cursor.getString(7));
                    appState.setMyEmail(cursor.getString(3));
                    Log.i("##Email^^^", cursor.getString(3));
                    if(cursor.getString(6) != null) {
                        appState.setServiceID(cursor.getString(6));
                        Log.i("##Service Id^^^", cursor.getString(6));
                    }
                    appState.setMySurname(cursor.getString(2));
                    Log.i("##Surname^^^", cursor.getString(2));
                    appState.setMyFirstName(cursor.getString(1));
                    Log.i("##Firstname^^^", cursor.getString(1));

                } while (cursor.moveToNext());
            }
        }finally{
            cursor.close();
            db.close();
        }

        myDBconnection.close();

        return k;
    }

    public int login(Context context, String email, String passw)
    {
        String[] args = {email.trim(),passw.trim()};
        String usrid = null;

        appState = ((App)context);
        Log.i("LoginCount", email + ":" + passw);
        SQLiteDatabase db = myConnection(context).getWritableDatabase();
        //Cursor cursor2 = db.rawQuery("SELECT id_users,first_name,surname,login,pwd,group_id,service_id FROM users WHERE login=? AND pwd=? AND authorized=1 LIMIT 1", args);
        Cursor cursor = db.rawQuery("SELECT id_users,first_name,surname,login,pwd,group_id,service_id, user_id from users  where login='"+email+"' and pwd='"+passw+"' and authorized=1 limit 1",null);
        //Cursor cursor = db.rawQuery("SELECT authorized from users  where login='"+email+"' and pwd='"+passw+"' limit 1",null);

        int k = cursor.getCount();
        //int k2 = cursor2.getCount();
        Log.i("LoginLog", Integer.toString(k));
        //Log.i("LoginLog****", Integer.toString(k2));
        try{
            if (cursor.moveToFirst()) {
                do {
                    usrid = cursor.getString(0);
                    appState.setUserid(usrid);
                    Log.i("##User_id^^^", cursor.getString(0));

                    String userid = cursor.getString(7);
                    appState.setUserid(userid);


                    Log.i("##User_id+++", cursor.getString(7));
                    appState.setMyEmail(cursor.getString(3));
                    Log.i("##Email^^^", cursor.getString(3));
                    if(cursor.getString(6) != null) {
                        appState.setServiceID(cursor.getString(6));
                        Log.i("##Service Id^^^", cursor.getString(6));
                    }
                    appState.setMySurname(cursor.getString(2));
                    //Log.i("##Surname^^^", cursor.getString(2));
                    appState.setMyFirstName(cursor.getString(1));
                    Log.i("##Firstname^^^", cursor.getString(1));

                } while (cursor.moveToNext());
            }
        }finally{
            cursor.close();
            db.close();
        }

        myDBconnection.close();

        return k;
    }


    public long authorized(Context context, String email, String passw)
    {
        String[] args = {email.trim(),passw.trim()};
        String usrid = null;
        appState = ((App)context);
        Log.i("LoginCount", email + ":" + passw);
        SQLiteDatabase db = myConnection(context).getWritableDatabase();
        //Cursor cursor2 = db.rawQuery("SELECT id_users,first_name,surname,login,pwd,group_id,service_id FROM users WHERE login=? AND pwd=? AND authorized=1 LIMIT 1", args);

         //Cursor cursor = db.rawQuery("SELECT id_users,first_name,surname,login,pwd,group_id,service_id from users  where login='"+email+"' and pwd='"+passw+"' and authorized=1 limit 1",null);

        Cursor cursor = db.rawQuery("update users set authorized=1 where login='"+email+"'",null);

        cursor.moveToFirst();

        long recCount = cursor.getInt(0);
        cursor.close();
        db.close();

        return recCount;


    }




}
