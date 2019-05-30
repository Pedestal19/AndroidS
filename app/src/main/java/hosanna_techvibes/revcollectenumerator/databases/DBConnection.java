package hosanna_techvibes.revcollectenumerator.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hosanna on 23/09/2016.
 */
public class DBConnection extends SQLiteOpenHelper {
    private static String DB_PATH = "/data/data/hosanna_techvibes.revcollectenumerator/databases/";
    private static String DB_NAME = "revcollect_new.db";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase myDataBase;
    private Context myContext = null;

    // TODO: 23/09/2016 class Instantiation
    public DBConnection(Context context)
    {
        super(context, DB_NAME, null, DATABASE_VERSION);
        this.myContext = context;
        Log.e("Path 1", DB_PATH);
    }

    // TODO: 23/09/2016 onCreate method 
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    // TODO: 23/09/2016 onUpgrade method 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        Log.w("DB", "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");

        if(newVersion>oldVersion){
            try {
                myContext.deleteDatabase(DB_NAME);
                createDataBase();
                openDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // TODO: 23/09/2016 to create database
    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if(dbExist){
            //do nothing - database already exist
            Log.w("DB", "do nothing - database already exist ");

        }else{
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }

    }

    // TODO: 23/09/2016 Check if database exist
    public boolean checkDataBase()
    {
        SQLiteDatabase checkDB = null;
        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
            Log.w("DB", "Checked and found database ");
        }catch(SQLiteException e){
            //database does't exist yet.
            Log.w("DB", "Database does not exist after checking ");
        }
        if(checkDB != null){
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    // TODO: 23/09/2016 Copy database
    private void copyDataBase() throws IOException
    {
        Log.w("DB", "Copying database... ");
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }
        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase() throws SQLException {
        Log.w("DB", "Opening database... ");
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

    }

    @Override
    public synchronized void close() {
        if(myDataBase != null)
            myDataBase.close();
        super.close();
    }

    public long onInsertOrUpdate(ContentValues values, String _tableName)
    {
        long id;
        Log.d("onInsertOrUpdate", "insertOrIgnore on " + values);
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            id=db.insertWithOnConflict(_tableName, null, values,
                    SQLiteDatabase.CONFLICT_REPLACE);
        } finally {
            db.close();
        }
        return id;
    }

    public long onUpdateOrIgnore(ContentValues values, String _tableName, String _fieldName, String _fieldValue)
    {
        long id;
        Log.d("onInsertOrUpdate", "insertOrIgnore on " + values);
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            //id=db.update(_tableName, values, _fieldName + "=?", new String[]{_fieldValue.toString()}); //new String[]{String.valueOf(_fieldValue)}
            id=db.update(_tableName, values, _fieldName + "='" + _fieldValue + "'", null);
        } finally {
            db.close();
        }
        return id;
    }

    public long onInsert(ContentValues values, String _tableName)
    {
        long isSuccess = 0;
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            isSuccess = db.insert(_tableName, null, values);
            db.close();
        }catch(Exception e){
            e.printStackTrace();
            isSuccess = 0;
        }
        myDataBase.close();
        return isSuccess;
    }

    public boolean onInsert(String query)
    {
        boolean isSuccess = false;
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(query);
            db.close();
            isSuccess = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            isSuccess = false;
        }
        return isSuccess;
    }

    // count all records
    public long countRecords(String _tableName){

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT count(*) from " + _tableName, null);
        cursor.moveToFirst();

        long recCount = cursor.getInt(0);
        cursor.close();
        db.close();

        return recCount;
    }

    // count all records
    public long countRecordsWhere(String _tableName, String _columnName, String _param){

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT count(*) from " + _tableName + " Where " +_columnName+"='"+_param+"'", null);
        cursor.moveToFirst();

        long recCount = cursor.getInt(0);
        cursor.close();
        db.close();

        return recCount;
    }



    public long authorized(String email)
    {
        String[] args = {email.trim()};
        String usrid = null;
        Log.i("LoginCount", email);
        SQLiteDatabase db = this.getWritableDatabase();
        //Cursor cursor2 = db.rawQuery("SELECT id_users,first_name,surname,login,pwd,group_id,service_id FROM users WHERE login=? AND pwd=? AND authorized=1 LIMIT 1", args);

        //Cursor cursor = db.rawQuery("SELECT id_users,first_name,surname,login,pwd,group_id,service_id from users  where login='"+email+"' and pwd='"+passw+"' and authorized=1 limit 1",null);

        Cursor cursor = db.rawQuery("update users set authorized=1 where login='"+email+"'",null);

        cursor.moveToFirst();

        long recCount = cursor.getInt(0);
        cursor.close();
        db.close();

        return recCount;


    }


    public Cursor selectAllFromTable(String _tableName)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + _tableName + ";", null);
        cursor.moveToFirst();

        cursor.close();
        db.close();

        return cursor;
    }

    public Cursor ms(String _tableName)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + _tableName + " WHERE synch_status=0 LIMIT 50;", null);
        cursor.moveToFirst();

        cursor.close();
        db.close();

        return cursor;
    }

    public Cursor selectAllFromTable(String _from, String _fieldName, String _fieldValue)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + _from + " where " + _fieldName + " = '" +_fieldValue + "';";

        Cursor cursor = db.rawQuery(sql, null);
        if(cursor != null)
        {
            cursor.moveToFirst();
        }

        return cursor;
    }

    //&&&&&

    public Cursor selectAllFromTableAs()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT idtax_registration as _id, first_name, surname,temp_tax_id FROM tax_registration ORDER BY _id DESC;";

        Cursor cursor = db.rawQuery(sql, null);
        if(cursor != null)
        {
            cursor.moveToFirst();
        }

        return cursor;
    }

    //^^^^^^^^^^^

    public String getDimensionCode(String dimension)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT dimension_code from revenue_dimensions where dimension ='"+dimension+"';";

        String dimensionCode="";

        Cursor cursor = db.rawQuery(sql, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Log.i("Selected field for", cursor.getString(0));
                    dimensionCode = cursor.getString(0);
                    // myVals.add(_what);
                } while (cursor.moveToNext());

            }
        } finally {
            cursor.close();
        }

        return dimensionCode;

    }

    public String getap(String revenue_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT amount from revenue_amount where revenue_id ='"+revenue_id+"';";

        String amount="";

        Cursor cursor = db.rawQuery(sql, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Log.i("Selected field for", cursor.getString(0));
                    amount = cursor.getString(0);
                    // myVals.add(_what);
                } while (cursor.moveToNext());

            }
        } finally {
            cursor.close();
        }

        return amount;

    }

    //^^^^^^^^^^^\
    public List<String> selectAssetType()
    {
        List<String> revenueNameList=new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT asset_type from asset_types order by asset_type;", null);
        if(cursor.moveToFirst()) {
            do {

                String revenue_name = cursor.getString(0);
                revenueNameList.add(revenue_name);
            }
            while(cursor.moveToNext());

        }


        cursor.close();
        db.close();

        return revenueNameList;
    }

    public List<String> selectLandType()
    {
        List<String> revenueNameList=new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT land_type from land_types order by land_type;", null);
        if(cursor.moveToFirst()) {
            do {

                String revenue_name = cursor.getString(0);
                revenueNameList.add(revenue_name);
            }
            while(cursor.moveToNext());

        }


        cursor.close();
        db.close();

        return revenueNameList;
    }

    public List<String> selectLandPurpose()
    {
        List<String> revenueNameList=new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT land_purpose from land_purposes order by land_purpose;", null);
        if(cursor.moveToFirst()) {
            do {

                String revenue_name = cursor.getString(0);
                revenueNameList.add(revenue_name);
            }
            while(cursor.moveToNext());

        }


        cursor.close();
        db.close();

        return revenueNameList;
    }

    public List<String> selectLandFunction()
    {
        List<String> revenueNameList=new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT land_function from land_functions order by land_function;", null);
        if(cursor.moveToFirst()) {
            do {

                String revenue_name = cursor.getString(0);
                revenueNameList.add(revenue_name);
            }
            while(cursor.moveToNext());

        }


        cursor.close();
        db.close();

        return revenueNameList;
    }

    public List<String> selectLandOwnership()
    {
        List<String> revenueNameList=new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT land_ownership from land_ownerships order by land_ownership;", null);
        if(cursor.moveToFirst()) {
            do {

                String revenue_name = cursor.getString(0);
                revenueNameList.add(revenue_name);
            }
            while(cursor.moveToNext());

        }


        cursor.close();
        db.close();

        return revenueNameList;
    }

    public List<String> selectState()
    {
        List<String> revenueNameList=new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT name from states order by name;", null);
        if(cursor.moveToFirst()) {
            do {

                String revenue_name = cursor.getString(0);
                revenueNameList.add(revenue_name);
            }
            while(cursor.moveToNext());

        }


        cursor.close();
        db.close();

        return revenueNameList;
    }

    public List<String> selectLgaLimit()
    {
        List<String> revenueNameList=new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT lga from local_goverment_area order by lga LIMIT 1;", null);
        if(cursor.moveToFirst()) {
            do {

                String revenue_name = cursor.getString(0);
                revenueNameList.add(revenue_name);
            }
            while(cursor.moveToNext());

        }


        cursor.close();
        db.close();

        return revenueNameList;
    }
    public List<String> selectWardLimit()
    {
        List<String> revenueNameList=new ArrayList<String>();
        revenueNameList.add("--Select Ward--");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT ward from wards order by ward LIMIT 1;", null);
        if(cursor.moveToFirst()) {
            do {

                String revenue_name = cursor.getString(0);
                revenueNameList.add(revenue_name);
            }
            while(cursor.moveToNext());

        }


        cursor.close();
        db.close();

        return revenueNameList;
    }


    public List<String> universalSelect(String table_name, String column)
    {
        List<String> revenueNameList=new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT " +column+ " from "+table_name+" order by "+column+";", null);
        if(cursor.moveToFirst()) {
          do {

              String revenue_name = cursor.getString(0);
              revenueNameList.add(revenue_name);
          }
               while(cursor.moveToNext());

        }


        cursor.close();
        db.close();

        return revenueNameList;
    }

    public List<String> universalSelect3(String table_name, String column)
    {
        List<String> revenueNameList=new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT " +column+ " from "+table_name+ " order by "+column+" LIMIT 1;" , null);
        if(cursor.moveToFirst()) {
            do {

                String revenue_name = cursor.getString(0);
                revenueNameList.add(revenue_name);
            }
            while(cursor.moveToNext());

        }


        cursor.close();
        db.close();

        return revenueNameList;
    }


    public ArrayList<String> universalSelect2(String table_name, String column)
    {
        ArrayList<String> revenueNameList=new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT " +column+ " from "+table_name+ " order by "+column+";", null);
        if(cursor.moveToFirst()) {
            do {

                String revenue_name = cursor.getString(0);
                revenueNameList.add(revenue_name);
            }
            while(cursor.moveToNext());

        }


        cursor.close();
        db.close();

        return revenueNameList;
    }

    public List<String> selectLGA(String stateid)
    {
        List<String> revenueNameList=new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT name from _local_governments where state_id='"+stateid+"';", null);
        if(cursor.moveToFirst()) {
            do {

                String revenue_name = cursor.getString(0);
                revenueNameList.add(revenue_name);
            }
            while(cursor.moveToNext());

        }


        cursor.close();
        db.close();

        return revenueNameList;
    }

    public ArrayList<String> selectAssessmentRulesWhere(String stateid)
    {
        ArrayList<String> revenueNameList=new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT assessment_item from assessment_rules where profile_ref='"+stateid+"';", null);
        if(cursor.moveToFirst()) {
            do {

                String revenue_name = cursor.getString(0);
                revenueNameList.add(revenue_name);
            }
            while(cursor.moveToNext());

        }


        cursor.close();
        db.close();

        return revenueNameList;
    }

    public List<String> selectBuilding(String streetid)
    {
        List<String> revenueNameList=new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT building_name from _buildings where street_id='"+streetid+"';", null);
        if(cursor.moveToFirst()) {
            do {

                String revenue_name = cursor.getString(0);
                revenueNameList.add(revenue_name);
            }
            while(cursor.moveToNext());

        }


        cursor.close();
        db.close();

        return revenueNameList;
    }

    public List<String> selectLGA3(String streetid)
    {
        List<String> revenueNameList=new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT lga from local_goverment_area where state_id='"+streetid+"';", null);
        if(cursor.moveToFirst()) {
            do {

                String revenue_name = cursor.getString(0);
                revenueNameList.add(revenue_name);
            }
            while(cursor.moveToNext());

        }


        cursor.close();
        db.close();

        return revenueNameList;
    }

    public List<String> selectBusiness(String buildingid)
    {
        List<String> revenueNameList=new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT business_name from _businesses where building_id='"+buildingid+"';", null);
        if(cursor.moveToFirst()) {
            do {

                String revenue_name = cursor.getString(0);
                revenueNameList.add(revenue_name);
            }
            while(cursor.moveToNext());

        }


        cursor.close();
        db.close();

        return revenueNameList;
    }

    public List<String> selectWard(String buildingid)
    {
        List<String> revenueNameList=new ArrayList<String>();
        revenueNameList.add("--Select Ward--");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT ward from wards where lga_id='"+buildingid+"';", null);
        if(cursor.moveToFirst()) {
            do {

                String revenue_name = cursor.getString(0);
                revenueNameList.add(revenue_name);
            }
            while(cursor.moveToNext());

        }


        cursor.close();
        db.close();

        return revenueNameList;
    }



    //&&&&

    public Cursor selectAllFromTableAs2(String firstname, String lastname)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT idtax_registration as _id, first_name, surname,temp_tax_id FROM tax_registration where first_name LIKE '%"+firstname+"%' OR surname LIKE '%"+lastname+"%' ORDER BY _id DESC;";

        Cursor cursor = db.rawQuery(sql, null);
        if(cursor != null)
        {
            cursor.moveToFirst();
        }

        return cursor;
    }

    public Cursor selectAllFromTableAs2(String firstname, String lastname, String taxId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT idtax_registration as _id, first_name, surname,temp_tax_id FROM tax_registration where first_name LIKE '%"+firstname+"%' OR surname LIKE '%"+lastname+"%' OR temp_tax_id LIKE '%"+taxId+"%' ORDER BY _id DESC;";

        Cursor cursor = db.rawQuery(sql, null);
        if(cursor != null)
        {
            cursor.moveToFirst();
        }

        return cursor;
    }

    //&&&&&

    public Cursor selectAllFromTableTransactions2(String transactionId, String date)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT idtransaction_logs as _id, amount, description,payment_on FROM transaction_logs where transaction_id = '"+transactionId+"' OR payment_on = '"+date+"' ORDER BY _id DESC;";

        Cursor cursor = db.rawQuery(sql, null);
        if(cursor != null)
        {
            cursor.moveToFirst();
        }

        return cursor;
    }


    public Cursor selectAllFromTableTransactions()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT idtransaction_logs as _id, transaction_id, tax_id,amount,revenue_amount, description,payment_on FROM transaction_logs ORDER BY _id DESC;";

        Cursor cursor = db.rawQuery(sql, null);
        if(cursor != null)
        {
            cursor.moveToFirst();
        }

        return cursor;
    }

    public Cursor selectAllFromPayerBalance()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT id as _id, user_id, tax_id,wallet_balance from payer_balance ORDER BY _id DESC limit 1;";

        Cursor cursor = db.rawQuery(sql, null);
        if(cursor != null)
        {
            cursor.moveToFirst();
        }

        return cursor;
    }

    //&&&&&

    public Cursor selectAllFromTableWithLimitAndCondition(String _tableName, String _limit, String _whereKey, String _whereValue)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * from " + _tableName + " WHERE " + _whereKey + "='" + _whereValue +"' LIMIT " + _limit +";", null);
        cursor.moveToFirst();

        //cursor.close();
        db.close();

        return cursor;
    }

    public Cursor selectAllFromTable(String sql,boolean yes)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        //String sql = "SELECT * FROM " + _from + " where " + _fieldName + " = '" +_fieldValue + "';";

        Cursor cursor = db.rawQuery(sql, null);
        if(cursor != null)
        {
            cursor.moveToFirst();
        }

        return cursor;
    }
    // TODO: 23-Sep-15 selects columns from a table with limit using an order by without a where clause
;    public String selectColumnFromTableWithLimit(String _columns, String _table, int limit)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT " + _columns +" FROM " + _table + " LIMIT " +limit +";";
        String _what = null;
        Cursor cursor = db.rawQuery(sql, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Log.i("Selected field record", cursor.getString(0));
                    _what = cursor.getString(0);
                } while (cursor.moveToNext());

            }
        } finally {
            cursor.close();
        }

        return _what;
    }

    public String selectFromTable(String what, String _from, String _whereColumn, String _whereValue)
    {
        //ArrayList<String>myVals=new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT " + what + " FROM " + _from + " where " + _whereColumn + " = '" +_whereValue + "';";
        String _what = null;
        Cursor cursor = db.rawQuery(sql, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    //Log.i("Selected field for", cursor.getString(0));
                    _what = cursor.getString(0);
                   // myVals.add(_what);
                } while (cursor.moveToNext());

            }
        } finally {
            cursor.close();
        }

        return _what;
    }


    public String selectFromTableb(String what, String _from, String _whereColumn, String _whereValue)
    {
        //ArrayList<String>myVals=new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT " + what + " FROM " + _from + " where " + _whereColumn + " = '" +_whereValue + "';";
        String _what = null;
        Cursor cursor = db.rawQuery(sql, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Log.i("Selected field for", cursor.getString(0));
                    _what = cursor.getString(0);
                    // myVals.add(_what);
                } while (cursor.moveToNext());

            }
        } finally {
            cursor.close();
        }

        return _what;
    }




    public List<String> selectFromTableArray(String what, String _from, String _whereColumn, String _whereValue)
    {
        List<String> valuesList=new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        //String sql = "SELECT " + what + " FROM " + _from + " where " + _whereColumn + " = '" +_whereValue + " ORDER BY DIMENSION';";
        //String _what = null;
        Cursor cursor = db.rawQuery("SELECT " + what +" FROM " + _from + " where " + _whereColumn + " = '" +_whereValue + " ORDER BY DIMENSION'", null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
            valuesList.add(cursor.getString(0));
            cursor.moveToNext();

        }


            cursor.close();


        return valuesList;
    }



    // TODO: 23-Sep-15 selects colums from a table using a where clause
    public Cursor selectColumnsFromTable(String _columns, String _table, String _whereColumn, String _whereValue)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT " + _columns +" FROM " + _table + " where " + _whereColumn + " = '" + _whereValue + "';";

        Cursor cursor = db.rawQuery(sql, null);
        if(cursor != null)
        {
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }

    // TODO: 23-Sep-15 selects columns from a table using an order by without a where clause
    public Cursor selectColumnsFromTableOrderBy(String _columns, String _table)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT " + _columns +" FROM " + _table + ";";

        Cursor cursor = db.rawQuery(sql, null);
        if(cursor != null)
        {
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }

    // TODO: 23-Sep-15 selects columns from a table using an order by without a where clause
    public Cursor selectColumnsFromTableOrderBy(String _columns, String _table, String ORDER_BY)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT " + _columns +" FROM " + _table + " "+ ORDER_BY +";";

        Cursor cursor = db.rawQuery(sql, null);
        if(cursor != null)
        {
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }



    public String selectFromTableWithLimitAndOrder(String what, String _from, String _whereColumn, String _whereValue, String _limit, String _order_by)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT " + what + " FROM " + _from + " where " + _whereColumn + " = '" +_whereValue + "' ORDER BY " + _order_by + " LIMIT " + _limit + ";";
        Log.e("selectFromTableWithLimitAndOrder", sql);
        String _what = null;
        Cursor cursor = db.rawQuery(sql, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Log.i("Selected field for record", cursor.getString(0));
                    _what = cursor.getString(0);
                } while (cursor.moveToNext());

            }
        } finally {
            cursor.close();
        }

        return _what;
    }

    // TODO: 23-Sep-15  selects columns from a table using a where clause and order by
    public Cursor selectColumnsFromTableBy(String _columns, String _table, String _fieldName, String _fieldValue, String Order_by)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT " + _columns +" FROM " + _table + " where " + _fieldName + " = '" +_fieldValue + "' ORDER BY " + Order_by +";";
        Log.i("Sql query ", sql);
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor != null)
        {
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }

    // deletes all records
    public boolean deleteRecords(String _tableName){

        boolean isSuccess = false;
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("delete from "+ _tableName);
            db.close();
            isSuccess = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            isSuccess = false;
        }
        return isSuccess;
    }

    // deletes records with id
    public boolean deleteRecords(String _tableName, String _fieldName, String _fieldValue)
    {
        boolean isSuccess = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("delete from " + _tableName + " where " + _fieldName + "=" + _fieldValue + ";");
            db.close();
            isSuccess = true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            isSuccess = false;
        }
        return isSuccess;
    }

    /****
    public int login(Context context, String email, String passw)
    {

        String[] args = {email,passw};
        Log.i("LoginCount", email + ":" + passw);
        Cursor cursor = null;
        int k;
        try{
            String sql = "SELECT user_id,email,first_name,middle_name,last_name,phone,group_id FROM users WHERE email=? AND pwd=? LIMIT 1";
            cursor = this.selectAllFromTable(sql,true);
            k = cursor.getCount();
            Log.i("LoginLog",Integer.toString(k));
            if (cursor.moveToFirst()) {
                CurrentUserDetail userD = new CurrentUserDetail();
                App appState;
                do {
                    userD.setUserid(Integer.toString(cursor.getInt(0)));
                    userD.setEmail(cursor.getString(1));
                    appState = ((MyApp)context);
                    appState.setUserid(Integer.toString(cursor.getInt(0)));
                    appState.setEmail(cursor.getString(1));
                    appState.setFirstName(cursor.getString(2));
                    appState.setMiddleName(cursor.getString(3));
                    appState.setLastName(cursor.getString(4));
                    appState.setPhoneNumber(cursor.getString(5));
                    appState.setUserGroup_id(Integer.toString(cursor.getInt(6)));
                    String service_id = this.selectFromTable("service_id", "client_table", "email", cursor.getString(1));
                    if(service_id != null) {
                        appState.setServiceID(service_id);
                    }
                    appState.setAuthorizationID(email + Devices.getDeviceUUID(context) + Devices.getDeviceIMEI(context));

                    Log.i("LoginLog", cursor.getString(2));

                } while (cursor.moveToNext());
            }
        }finally{
            cursor.close();
        }

        return k;

    }
    ****/
}
