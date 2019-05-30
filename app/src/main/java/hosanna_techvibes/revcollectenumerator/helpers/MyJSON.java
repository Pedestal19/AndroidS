package hosanna_techvibes.revcollectenumerator.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import hosanna_techvibes.revcollectenumerator.databases.DataDB;


/**
 * Created by Hosanna_TechVibes on 04-Dec-17.
 */

public class MyJSON {


    //9999999999


    Context _context;
    String _email;
    private static final String TAG = "SyncDataAnalyzer";
    DataDB dataDB = new DataDB();

    Calendar timer = Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");

    public String AnalyzeApartmentType(Context context, JSONArray jsonArray) {

        JSONArray responseJson = new JSONArray();
        StringBuffer myBufferedData = new StringBuffer();

        //check if we have data in jsonArray_ASSIGN
        if (jsonArray.length() > 0) {
            //repeat to get data from jsonarray
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject presentJSONObject = jsonArray.getJSONObject(i);
                    ContentValues table_builder = new ContentValues();


                    table_builder.put("synch_apartment_type_id", presentJSONObject.getString("synch_apartment_type_id"));
                    table_builder.put("apartment_type_id", presentJSONObject.getString("apartment_type_id"));
                    table_builder.put("apartment_type", presentJSONObject.getString("apartment_type"));
                    table_builder.put("registered_by", presentJSONObject.getString("registered_by"));
                    table_builder.put("registered_on", presentJSONObject.getString("registered_on"));//date_log
                    String authorizationID = presentJSONObject.getString("synch_apartment_type_id");
//                        table_builder.put("service_id", presentJSONObject.getString("service_id"));//date_log
//                        table_builder.put("registered_on", presentJSONObject.getString("date_of_birth"));//date_log



                         /*   //// TODO: 19/09/2015 Delete all records first
                            dataDB.myConnection(context).deleteRecords("admin_users");*/
                    // TODO: 19/09/2015 Call insert
                    long lastInsertId = dataDB.myConnection(context).onInsertOrUpdate(table_builder, "synch_apartment_types");
//                            Log.e(TAG, i+ " -Employee Saved...");
                    if (lastInsertId > 0) {

                        Log.i("Apendin auth id list ", "for response  ");
                        myBufferedData.append(authorizationID + ",");
                        Log.e(TAG, i + " -synch_apartment_types Saved...");

                    } else {
                        Log.i("Apendin auth id list", "FAILED");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        String revenuesBufferedDataToString = myBufferedData.toString();
        Log.e("buffer to string", "AFTER to string** " + revenuesBufferedDataToString);
        String replacedRevenueBufferedData = revenuesBufferedDataToString.replaceAll(",$", "");
        Log.e("Buffer reader", "AFTER string replace::" + replacedRevenueBufferedData);
        Log.e("Return to class", "Send employee info string back");

        return replacedRevenueBufferedData;
    }

    public String AnalyzeAreaCodes(Context context, JSONArray jsonArray) {

        JSONArray responseJson = new JSONArray();
        StringBuffer myBufferedData = new StringBuffer();

        //check if we have data in jsonArray_ASSIGN
        if (jsonArray.length() > 0) {
            //repeat to get data from jsonarray
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject presentJSONObject = jsonArray.getJSONObject(i);
                    ContentValues employee_info_table = new ContentValues();


                    employee_info_table.put("idsynch_area_codes", presentJSONObject.getString("idsynch_area_codes"));
                    employee_info_table.put("area_name", presentJSONObject.getString("area_name"));
                    employee_info_table.put("area_code", presentJSONObject.getString("area_code"));
                    employee_info_table.put("service_id", presentJSONObject.getString("service_id"));
                    employee_info_table.put("area_code_id", presentJSONObject.getString("area_code_id"));//date_log
//                    employee_info_table.put("service_id", presentJSONObject.getString("service_id"));//date_log

                    String authorizationID = presentJSONObject.getString("idsynch_area_codes");



                         /*   //// TODO: 19/09/2015 Delete all records first
                            dataDB.myConnection(context).deleteRecords("admin_users");*/
                    // TODO: 19/09/2015 Call insert
                    long lastInsertId = dataDB.myConnection(context).onInsertOrUpdate(employee_info_table, "synch_areacodes");
                    Log.e(TAG, i + " -Pension Employee Saved...");
                    if (lastInsertId > 0) {

                        Log.i("Apendin auth id list ", "for response  ");
                        myBufferedData.append(authorizationID + ",");

                    } else {
                        Log.i("Apendin auth id list", "FAILED");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        String revenuesBufferedDataToString = myBufferedData.toString();
        Log.e("buffer to string", "AFTER to string** " + revenuesBufferedDataToString);
        String replacedRevenueBufferedData = revenuesBufferedDataToString.replaceAll(",$", "");
        Log.e("Buffer reader", "AFTER string replace::" + replacedRevenueBufferedData);
        Log.e("Return to class", "Send employee info string back");

        return replacedRevenueBufferedData;
    }

    public String AnalyzeBuildingCompletions(Context context, JSONArray jsonArray) {

        JSONArray responseJson = new JSONArray();
        StringBuffer myBufferedData = new StringBuffer();

        //check if we have data in jsonArray_ASSIGN
        if (jsonArray.length() > 0) {
            //repeat to get data from jsonarray
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject presentJSONObject = jsonArray.getJSONObject(i);
                    ContentValues employee_info_table = new ContentValues();


                    employee_info_table.put("synch_building_completion_id", presentJSONObject.getString("synch_building_completion_id"));
                    employee_info_table.put("building_completion_id", presentJSONObject.getString("building_completion_id"));
                    employee_info_table.put("building_completion", presentJSONObject.getString("building_completion"));
//                    employee_info_table.put("service_id", presentJSONObject.getString("service_id"));
//                    employee_info_table.put("area_code_id", presentJSONObject.getString("area_code_id"));//date_log
//                    employee_info_table.put("service_id", presentJSONObject.getString("service_id"));//date_log

                    String authorizationID = presentJSONObject.getString("synch_building_completion_id");



                         /*   //// TODO: 19/09/2015 Delete all records first
                            dataDB.myConnection(context).deleteRecords("admin_users");*/
                    // TODO: 19/09/2015 Call insert
                    long lastInsertId = dataDB.myConnection(context).onInsertOrUpdate(employee_info_table, "synch_building_completions");
                    Log.e(TAG, i + " -Pension Employee Saved...");
                    if (lastInsertId > 0) {

                        Log.i("Apendin auth id list ", "for response  ");
                        myBufferedData.append(authorizationID + ",");

                    } else {
                        Log.i("Apendin auth id list", "FAILED");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        String revenuesBufferedDataToString = myBufferedData.toString();
        Log.e("buffer to string", "AFTER to string** " + revenuesBufferedDataToString);
        String replacedRevenueBufferedData = revenuesBufferedDataToString.replaceAll(",$", "");
        Log.e("Buffer reader", "AFTER string replace::" + replacedRevenueBufferedData);
        Log.e("Return to class", "Send employee info string back");

        return replacedRevenueBufferedData;
    }

    public String AnalyzeBuildingFunctions(Context context, JSONArray jsonArray) {

        JSONArray responseJson = new JSONArray();
        StringBuffer myBufferedData = new StringBuffer();

        //check if we have data in jsonArray_ASSIGN
        if (jsonArray.length() > 0) {
            //repeat to get data from jsonarray
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject presentJSONObject = jsonArray.getJSONObject(i);
                    ContentValues table_builder = new ContentValues();


                    table_builder.put("synch_building_function_id", presentJSONObject.getString("synch_building_function_id"));
                    table_builder.put("building_function_id", presentJSONObject.getString("building_function_id"));
                    table_builder.put("building_function", presentJSONObject.getString("building_function"));
                    table_builder.put("building_purpose", presentJSONObject.getString("building_purpose"));
//                    table_builder.put("registered_on", presentJSONObject.getString("registered_on"));//date_log
                    String authorizationID = presentJSONObject.getString("synch_building_function_id");
//                        table_builder.put("service_id", presentJSONObject.getString("service_id"));//date_log
//                        table_builder.put("registered_on", presentJSONObject.getString("date_of_birth"));//date_log



                         /*   //// TODO: 19/09/2015 Delete all records first
                            dataDB.myConnection(context).deleteRecords("admin_users");*/
                    // TODO: 19/09/2015 Call insert
                    long lastInsertId = dataDB.myConnection(context).onInsertOrUpdate(table_builder, "synch_building_functions");
//                            Log.e(TAG, i+ " -Employee Saved...");
                    if (lastInsertId > 0) {

                        Log.i("Apendin auth id list ", "for response  ");
                        myBufferedData.append(authorizationID + ",");
                        Log.e(TAG, i + " -synch_apartment_types Saved...");

                    } else {
                        Log.i("Apendin auth id list", "FAILED");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        String revenuesBufferedDataToString = myBufferedData.toString();
        Log.e("buffer to string", "AFTER to string** " + revenuesBufferedDataToString);
        String replacedRevenueBufferedData = revenuesBufferedDataToString.replaceAll(",$", "");
        Log.e("Buffer reader", "AFTER string replace::" + replacedRevenueBufferedData);
        Log.e("Return to class", "Send employee info string back");

        return replacedRevenueBufferedData;
    }

    public String AnalyzeBuildingOccupancies(Context context, JSONArray jsonArray) {

        JSONArray responseJson = new JSONArray();
        StringBuffer myBufferedData = new StringBuffer();

        //check if we have data in jsonArray_ASSIGN
        if (jsonArray.length() > 0) {
            //repeat to get data from jsonarray
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject presentJSONObject = jsonArray.getJSONObject(i);
                    ContentValues table_builder = new ContentValues();


                    table_builder.put("synch_building_occupancy_id", presentJSONObject.getString("synch_building_occupancy_id"));
                    table_builder.put("building_occupancy_id", presentJSONObject.getString("building_occupancy_id"));
                    table_builder.put("building_occupancy", presentJSONObject.getString("building_occupancy"));
                    /*table_builder.put("building_occupancy", presentJSONObject.getString("building_occupancy"));
                    table_builder.put("registered_on", presentJSONObject.getString("registered_on"));//date_log*/
                    String authorizationID = presentJSONObject.getString("synch_building_occupancy_id");
//                        table_builder.put("service_id", presentJSONObject.getString("service_id"));//date_log
//                        table_builder.put("registered_on", presentJSONObject.getString("date_of_birth"));//date_log



                         /*   //// TODO: 19/09/2015 Delete all records first
                            dataDB.myConnection(context).deleteRecords("admin_users");*/
                    // TODO: 19/09/2015 Call insert
                    long lastInsertId = dataDB.myConnection(context).onInsertOrUpdate(table_builder, "synch_building_occupancies");
//                            Log.e(TAG, i+ " -Employee Saved...");
                    if (lastInsertId > 0) {

                        Log.i("Apendin auth id list ", "for response  ");
                        myBufferedData.append(authorizationID + ",");
                        Log.e(TAG, i + " -synch_apartment_types Saved...");

                    } else {
                        Log.i("Apendin auth id list", "FAILED");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        String revenuesBufferedDataToString = myBufferedData.toString();
        Log.e("buffer to string", "AFTER to string** " + revenuesBufferedDataToString);
        String replacedRevenueBufferedData = revenuesBufferedDataToString.replaceAll(",$", "");
        Log.e("Buffer reader", "AFTER string replace::" + replacedRevenueBufferedData);
        Log.e("Return to class", "Send employee info string back");

        return replacedRevenueBufferedData;
    }

    public String AnalyzeBuildingOccupancyType(Context context, JSONArray jsonArray) {

        JSONArray responseJson = new JSONArray();
        StringBuffer myBufferedData = new StringBuffer();

        //check if we have data in jsonArray_ASSIGN
        if (jsonArray.length() > 0) {
            //repeat to get data from jsonarray
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject presentJSONObject = jsonArray.getJSONObject(i);
                    ContentValues table_builder = new ContentValues();

                    table_builder.put("synch_building_occupancy_type_id", presentJSONObject.getString("synch_building_occupancy_type_id"));
                    table_builder.put("building_occupancy_type_id", presentJSONObject.getString("building_occupancy_type_id"));
                    table_builder.put("building_occupancy_type", presentJSONObject.getString("building_occupancy_type"));
                    table_builder.put("building_occupancy", presentJSONObject.getString("building_occupancy"));
//                    table_builder.put("registered_on", presentJSONObject.getString("registered_on"));//date_log
                    String authorizationID = presentJSONObject.getString("synch_building_occupancy_type_id");
//                        table_builder.put("service_id", presentJSONObject.getString("service_id"));//date_log
//                        table_builder.put("registered_on", presentJSONObject.getString("date_of_birth"));//date_log



                         /*   //// TODO: 19/09/2015 Delete all records first
                            dataDB.myConnection(context).deleteRecords("admin_users");*/
                    // TODO: 19/09/2015 Call insert
                    long lastInsertId = dataDB.myConnection(context).onInsertOrUpdate(table_builder, "synch_building_occupancy_types");
//                            Log.e(TAG, i+ " -Employee Saved...");
                    if (lastInsertId > 0) {

                        Log.i("Apendin auth id list ", "for response  ");
                        myBufferedData.append(authorizationID + ",");
                        Log.e(TAG, i + " -synch_apartment_types Saved...");

                    } else {
                        Log.i("Apendin auth id list", "FAILED");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        String revenuesBufferedDataToString = myBufferedData.toString();
        Log.e("buffer to string", "AFTER to string** " + revenuesBufferedDataToString);
        String replacedRevenueBufferedData = revenuesBufferedDataToString.replaceAll(",$", "");
        Log.e("Buffer reader", "AFTER string replace::" + replacedRevenueBufferedData);
        Log.e("Return to class", "Send employee info string back");

        return replacedRevenueBufferedData;
    }

    public String AnalyzeBuildingOwnership(Context context, JSONArray jsonArray) {

        JSONArray responseJson = new JSONArray();
        StringBuffer myBufferedData = new StringBuffer();

        //check if we have data in jsonArray_ASSIGN
        if (jsonArray.length() > 0) {
            //repeat to get data from jsonarray
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject presentJSONObject = jsonArray.getJSONObject(i);
                    ContentValues table_builder = new ContentValues();

                    table_builder.put("synch_building_ownership_id", presentJSONObject.getString("synch_building_ownership_id"));
                    table_builder.put("building_ownership_id", presentJSONObject.getString("building_ownership_id"));
                    table_builder.put("building_ownership", presentJSONObject.getString("building_ownership"));
                    /*table_builder.put("building_occupancy", presentJSONObject.getString("building_occupancy"));
                    table_builder.put("registered_on", presentJSONObject.getString("registered_on"));//date_log
*/                    String authorizationID = presentJSONObject.getString("synch_building_ownership_id");
//                        table_builder.put("service_id", presentJSONObject.getString("service_id"));//date_log
//                        table_builder.put("registered_on", presentJSONObject.getString("date_of_birth"));//date_log



                         /*   //// TODO: 19/09/2015 Delete all records first
                            dataDB.myConnection(context).deleteRecords("admin_users");*/
                    // TODO: 19/09/2015 Call insert
                    long lastInsertId = dataDB.myConnection(context).onInsertOrUpdate(table_builder, "synch_building_ownerships");
//                            Log.e(TAG, i+ " -Employee Saved...");
                    if (lastInsertId > 0) {

                        Log.i("Apendin auth id list ", "for response  ");
                        myBufferedData.append(authorizationID + ",");
                        Log.e(TAG, i + " -synch_apartment_types Saved...");

                    } else {
                        Log.i("Apendin auth id list", "FAILED");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        String revenuesBufferedDataToString = myBufferedData.toString();
        Log.e("buffer to string", "AFTER to string** " + revenuesBufferedDataToString);
        String replacedRevenueBufferedData = revenuesBufferedDataToString.replaceAll(",$", "");
        Log.e("Buffer reader", "AFTER string replace::" + replacedRevenueBufferedData);
        Log.e("Return to class", "Send employee info string back");

        return replacedRevenueBufferedData;
    }

    public String AnalyzeBuildingPurpose(Context context, JSONArray jsonArray) {

        JSONArray responseJson = new JSONArray();
        StringBuffer myBufferedData = new StringBuffer();

        //check if we have data in jsonArray_ASSIGN
        if (jsonArray.length() > 0) {
            //repeat to get data from jsonarray
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject presentJSONObject = jsonArray.getJSONObject(i);
                    ContentValues table_builder = new ContentValues();

                    table_builder.put("synch_building_purpose_id", presentJSONObject.getString("synch_building_purpose_id"));
                    table_builder.put("building_purpose_id", presentJSONObject.getString("building_purpose_id"));
                    table_builder.put("building_purpose", presentJSONObject.getString("building_purpose"));
                    /*table_builder.put("building_occupancy", presentJSONObject.getString("building_occupancy"));
                    table_builder.put("registered_on", presentJSONObject.getString("registered_on"));//date_log
*/                    String authorizationID = presentJSONObject.getString("synch_building_purpose_id");
//                        table_builder.put("service_id", presentJSONObject.getString("service_id"));//date_log
//                        table_builder.put("registered_on", presentJSONObject.getString("date_of_birth"));//date_log



                         /*   //// TODO: 19/09/2015 Delete all records first
                            dataDB.myConnection(context).deleteRecords("admin_users");*/
                    // TODO: 19/09/2015 Call insert
                    long lastInsertId = dataDB.myConnection(context).onInsertOrUpdate(table_builder, "synch_building_purposes");
//                            Log.e(TAG, i+ " -Employee Saved...");
                    if (lastInsertId > 0) {

                        Log.i("Apendin auth id list ", "for response  ");
                        myBufferedData.append(authorizationID + ",");
                        Log.e(TAG, i + " -synch_apartment_types Saved...");

                    } else {
                        Log.i("Apendin auth id list", "FAILED");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        String revenuesBufferedDataToString = myBufferedData.toString();
        Log.e("buffer to string", "AFTER to string** " + revenuesBufferedDataToString);
        String replacedRevenueBufferedData = revenuesBufferedDataToString.replaceAll(",$", "");
        Log.e("Buffer reader", "AFTER string replace::" + replacedRevenueBufferedData);
        Log.e("Return to class", "Send employee info string back");

        return replacedRevenueBufferedData;
    }

    public String AnalyzeBusinessCategory(Context context, JSONArray jsonArray) {

        JSONArray responseJson = new JSONArray();
        StringBuffer myBufferedData = new StringBuffer();

        //check if we have data in jsonArray_ASSIGN
        if (jsonArray.length() > 0) {
            //repeat to get data from jsonarray
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject presentJSONObject = jsonArray.getJSONObject(i);
                    ContentValues table_builder = new ContentValues();


                    table_builder.put("id_synch_business_category", presentJSONObject.getString("id_synch_business_category"));
                    table_builder.put("business_category_id", presentJSONObject.getInt("business_category_id"));
                    table_builder.put("business_category", presentJSONObject.getString("business_category"));
                    /*table_builder.put("building_occupancy", presentJSONObject.getString("building_occupancy"));
                    table_builder.put("registered_on", presentJSONObject.getString("registered_on"));//date_log
*/                    String authorizationID = presentJSONObject.getString("id_synch_business_category");
                        table_builder.put("service_id", presentJSONObject.getString("service_id"));//date_log
                        table_builder.put("business_type", presentJSONObject.getString("business_type"));//date_log



                         /*   //// TODO: 19/09/2015 Delete all records first
                            dataDB.myConnection(context).deleteRecords("admin_users");*/
                    // TODO: 19/09/2015 Call insert
                    long lastInsertId = dataDB.myConnection(context).onInsertOrUpdate(table_builder, "synch_business_categories");
//                            Log.e(TAG, i+ " -Employee Saved...");
                    if (lastInsertId > 0) {

                        Log.i("Apendin auth id list ", "for response  ");
                        myBufferedData.append(authorizationID + ",");
                        Log.e(TAG, i + " -synch_apartment_types Saved...");

                    } else {
                        Log.i("Apendin auth id list", "FAILED");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        String revenuesBufferedDataToString = myBufferedData.toString();
        Log.e("buffer to string", "AFTER to string** " + revenuesBufferedDataToString);
        String replacedRevenueBufferedData = revenuesBufferedDataToString.replaceAll(",$", "");
        Log.e("Buffer reader", "AFTER string replace::" + replacedRevenueBufferedData);
        Log.e("Return to class", "Send employee info string back");

        return replacedRevenueBufferedData;
    }

    public String AnalyzeBusinessOperations(Context context, JSONArray jsonArray) {

        JSONArray responseJson = new JSONArray();
        StringBuffer myBufferedData = new StringBuffer();

        //check if we have data in jsonArray_ASSIGN
        if (jsonArray.length() > 0) {
            //repeat to get data from jsonarray
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject presentJSONObject = jsonArray.getJSONObject(i);
                    ContentValues table_builder = new ContentValues();


                    table_builder.put("id_synch_business_operation", presentJSONObject.getString("id_synch_business_operation"));
                    table_builder.put("business_operation_id", presentJSONObject.getInt("business_operation_id"));
                    table_builder.put("business_type", presentJSONObject.getString("business_type"));
                    /*table_builder.put("building_occupancy", presentJSONObject.getString("building_occupancy"));
                    table_builder.put("registered_on", presentJSONObject.getString("registered_on"));//date_log
*/                    String authorizationID = presentJSONObject.getString("id_synch_business_operation");
                    table_builder.put("service_id", presentJSONObject.getString("service_id"));//date_log
                    table_builder.put("business_operation", presentJSONObject.getString("business_operation"));//date_log



                         /*   //// TODO: 19/09/2015 Delete all records first
                            dataDB.myConnection(context).deleteRecords("admin_users");*/
                    // TODO: 19/09/2015 Call insert
                    long lastInsertId = dataDB.myConnection(context).onInsertOrUpdate(table_builder, "synch_business_operations");
//                            Log.e(TAG, i+ " -Employee Saved...");
                    if (lastInsertId > 0) {

                        Log.i("Apendin auth id list ", "for response  ");
                        myBufferedData.append(authorizationID + ",");
                        Log.e(TAG, i + " -synch_apartment_types Saved...");

                    } else {
                        Log.i("Apendin auth id list", "FAILED");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        String revenuesBufferedDataToString = myBufferedData.toString();
        Log.e("buffer to string", "AFTER to string** " + revenuesBufferedDataToString);
        String replacedRevenueBufferedData = revenuesBufferedDataToString.replaceAll(",$", "");
        Log.e("Buffer reader", "AFTER string replace::" + replacedRevenueBufferedData);
        Log.e("Return to class", "Send employee info string back");

        return replacedRevenueBufferedData;
    }

    public String AnalyzeBusinessSectors(Context context, JSONArray jsonArray) {

        JSONArray responseJson = new JSONArray();
        StringBuffer myBufferedData = new StringBuffer();

        //check if we have data in jsonArray_ASSIGN
        if (jsonArray.length() > 0) {
            //repeat to get data from jsonarray
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject presentJSONObject = jsonArray.getJSONObject(i);
                    ContentValues table_builder = new ContentValues();


                    table_builder.put("synch_business_sector_id", presentJSONObject.getString("synch_business_sector_id"));
                    table_builder.put("business_sector_id", presentJSONObject.getInt("business_sector_id"));
                    table_builder.put("business_sector", presentJSONObject.getString("business_sector"));
                    /*table_builder.put("building_occupancy", presentJSONObject.getString("building_occupancy"));
                    table_builder.put("registered_on", presentJSONObject.getString("registered_on"));//date_log
*/                    String authorizationID = presentJSONObject.getString("synch_business_sector_id");
                    table_builder.put("service_id", presentJSONObject.getString("service_id"));//date_log
                    table_builder.put("business_type", presentJSONObject.getString("business_type"));//date_log
                    table_builder.put("business_category", presentJSONObject.getString("business_category"));//date_log



                         /*   //// TODO: 19/09/2015 Delete all records first
                            dataDB.myConnection(context).deleteRecords("admin_users");*/
                    // TODO: 19/09/2015 Call insert
                    long lastInsertId = dataDB.myConnection(context).onInsertOrUpdate(table_builder, "synch_business_sectors");
//                            Log.e(TAG, i+ " -Employee Saved...");
                    if (lastInsertId > 0) {

                        Log.i("Apendin auth id list ", "for response  ");
                        myBufferedData.append(authorizationID + ",");
                        Log.e(TAG, i + " -synch_apartment_types Saved...");

                    } else {
                        Log.i("Apendin auth id list", "FAILED");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        String revenuesBufferedDataToString = myBufferedData.toString();
        Log.e("buffer to string", "AFTER to string** " + revenuesBufferedDataToString);
        String replacedRevenueBufferedData = revenuesBufferedDataToString.replaceAll(",$", "");
        Log.e("Buffer reader", "AFTER string replace::" + replacedRevenueBufferedData);
        Log.e("Return to class", "Send employee info string back");

        return replacedRevenueBufferedData;
    }

    public String AnalyzeBusinessSize(Context context, JSONArray jsonArray) {

        JSONArray responseJson = new JSONArray();
        StringBuffer myBufferedData = new StringBuffer();

        //check if we have data in jsonArray_ASSIGN
        if (jsonArray.length() > 0) {
            //repeat to get data from jsonarray
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject presentJSONObject = jsonArray.getJSONObject(i);
                    ContentValues table_builder = new ContentValues();


                    table_builder.put("synch_business_size_id", presentJSONObject.getString("synch_business_size_id"));
                    table_builder.put("business_size_id", presentJSONObject.getInt("business_size_id"));
                    table_builder.put("business_size", presentJSONObject.getString("business_size"));
                    /*table_builder.put("building_occupancy", presentJSONObject.getString("building_occupancy"));
                    table_builder.put("registered_on", presentJSONObject.getString("registered_on"));//date_log
*/                    String authorizationID = presentJSONObject.getString("synch_business_size_id");
//                    table_builder.put("service_id", presentJSONObject.getString("service_id"));//date_log
//                    table_builder.put("business_type", presentJSONObject.getString("business_type"));//date_log
//                    table_builder.put("business_category", presentJSONObject.getString("business_category"));//date_log



                         /*   //// TODO: 19/09/2015 Delete all records first
                            dataDB.myConnection(context).deleteRecords("admin_users");*/
                    // TODO: 19/09/2015 Call insert
                    long lastInsertId = dataDB.myConnection(context).onInsertOrUpdate(table_builder, "synch_business_sizes");
//                            Log.e(TAG, i+ " -Employee Saved...");
                    if (lastInsertId > 0) {

                        Log.i("Apendin auth id list ", "for response  ");
                        myBufferedData.append(authorizationID + ",");
                        Log.e(TAG, i + " -synch_apartment_types Saved...");

                    } else {
                        Log.i("Apendin auth id list", "FAILED");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        String revenuesBufferedDataToString = myBufferedData.toString();
        Log.e("buffer to string", "AFTER to string** " + revenuesBufferedDataToString);
        String replacedRevenueBufferedData = revenuesBufferedDataToString.replaceAll(",$", "");
        Log.e("Buffer reader", "AFTER string replace::" + replacedRevenueBufferedData);
        Log.e("Return to class", "Send employee info string back");

        return replacedRevenueBufferedData;
    }

    public String AnalyzeBusinessStructure(Context context, JSONArray jsonArray) {

        JSONArray responseJson = new JSONArray();
        StringBuffer myBufferedData = new StringBuffer();

        //check if we have data in jsonArray_ASSIGN
        if (jsonArray.length() > 0) {
            //repeat to get data from jsonarray
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject presentJSONObject = jsonArray.getJSONObject(i);
                    ContentValues table_builder = new ContentValues();

                    table_builder.put("synch_business_structure_id", presentJSONObject.getString("synch_business_structure_id"));
                    table_builder.put("business_structure_id", presentJSONObject.getString("business_structure_id"));
                    table_builder.put("business_structure", presentJSONObject.getString("business_structure"));
                    /*table_builder.put("building_occupancy", presentJSONObject.getString("building_occupancy"));
                    table_builder.put("registered_on", presentJSONObject.getString("registered_on"));//date_log
*/                    String authorizationID = presentJSONObject.getString("synch_business_structure_id");
                        table_builder.put("service_id", presentJSONObject.getString("service_id"));//date_log
                        table_builder.put("business_type", presentJSONObject.getString("business_type"));//date_log



                         /*   //// TODO: 19/09/2015 Delete all records first
                            dataDB.myConnection(context).deleteRecords("admin_users");*/
                    // TODO: 19/09/2015 Call insert
                    long lastInsertId = dataDB.myConnection(context).onInsertOrUpdate(table_builder, "synch_business_structures");
//                            Log.e(TAG, i+ " -Employee Saved...");
                    if (lastInsertId > 0) {

                        Log.i("Apendin auth id list ", "for response  ");
                        myBufferedData.append(authorizationID + ",");
                        Log.e(TAG, i + " -synch_apartment_types Saved...");

                    } else {
                        Log.i("Apendin auth id list", "FAILED");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        String revenuesBufferedDataToString = myBufferedData.toString();
        Log.e("buffer to string", "AFTER to string** " + revenuesBufferedDataToString);
        String replacedRevenueBufferedData = revenuesBufferedDataToString.replaceAll(",$", "");
        Log.e("Buffer reader", "AFTER string replace::" + replacedRevenueBufferedData);
        Log.e("Return to class", "Send employee info string back");

        return replacedRevenueBufferedData;
    }

    public String AnalyzeBusinessSubStructure(Context context, JSONArray jsonArray) {

        JSONArray responseJson = new JSONArray();
        StringBuffer myBufferedData = new StringBuffer();

        //check if we have data in jsonArray_ASSIGN
        if (jsonArray.length() > 0) {
            //repeat to get data from jsonarray
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject presentJSONObject = jsonArray.getJSONObject(i);
                    ContentValues table_builder = new ContentValues();

                    table_builder.put("synch_business_sub_sector_id", presentJSONObject.getString("synch_business_sub_sector_id"));
                    table_builder.put("business_sub_sector_id", presentJSONObject.getString("business_sub_sector_id"));
                    table_builder.put("business_sub_sector", presentJSONObject.getString("business_sub_sector"));
                    table_builder.put("business_category", presentJSONObject.getString("business_category"));
                    table_builder.put("business_sector", presentJSONObject.getString("business_sector"));//date_log
                    String authorizationID = presentJSONObject.getString("synch_business_sub_sector_id");
                    table_builder.put("service_id", presentJSONObject.getString("service_id"));//date_log
                    table_builder.put("business_type", presentJSONObject.getString("business_type"));//date_log



                         /*   //// TODO: 19/09/2015 Delete all records first
                            dataDB.myConnection(context).deleteRecords("admin_users");*/
                    // TODO: 19/09/2015 Call insert
                    long lastInsertId = dataDB.myConnection(context).onInsertOrUpdate(table_builder, "synch_business_sub_sectors");
//                            Log.e(TAG, i+ " -Employee Saved...");
                    if (lastInsertId > 0) {

                        Log.i("Apendin auth id list ", "for response  ");
                        myBufferedData.append(authorizationID + ",");
                        Log.e(TAG, i + " -synch_apartment_types Saved...");

                    } else {
                        Log.i("Apendin auth id list", "FAILED");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        String revenuesBufferedDataToString = myBufferedData.toString();
        Log.e("buffer to string", "AFTER to string** " + revenuesBufferedDataToString);
        String replacedRevenueBufferedData = revenuesBufferedDataToString.replaceAll(",$", "");
        Log.e("Buffer reader", "AFTER string replace::" + replacedRevenueBufferedData);
        Log.e("Return to class", "Send employee info string back");

        return replacedRevenueBufferedData;
    }

    public String AnalyzeBusinessTypes(Context context, JSONArray jsonArray) {

        JSONArray responseJson = new JSONArray();
        StringBuffer myBufferedData = new StringBuffer();

        //check if we have data in jsonArray_ASSIGN
        if (jsonArray.length() > 0) {
            //repeat to get data from jsonarray
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject presentJSONObject = jsonArray.getJSONObject(i);
                    ContentValues table_builder = new ContentValues();


                    table_builder.put("synch_business_type_id", presentJSONObject.getString("synch_business_type_id"));
                    table_builder.put("business_type_id", presentJSONObject.getString("business_type_id"));
//                    table_builder.put("business_sub_sector", presentJSONObject.getString("business_sub_sector"));
//                    table_builder.put("business_category", presentJSONObject.getString("business_category"));
//                    table_builder.put("business_sector", presentJSONObject.getString("business_sector"));//date_log
                    String authorizationID = presentJSONObject.getString("synch_business_type_id");
                    table_builder.put("service_id", presentJSONObject.getString("service_id"));//date_log
                    table_builder.put("business_type", presentJSONObject.getString("business_type"));//date_log



                         /*   //// TODO: 19/09/2015 Delete all records first
                            dataDB.myConnection(context).deleteRecords("admin_users");*/
                    // TODO: 19/09/2015 Call insert
                    long lastInsertId = dataDB.myConnection(context).onInsertOrUpdate(table_builder, "synch_business_types");
//                            Log.e(TAG, i+ " -Employee Saved...");
                    if (lastInsertId > 0) {

                        Log.i("Apendin auth id list ", "for response  ");
                        myBufferedData.append(authorizationID + ",");
                        Log.e(TAG, i + " -synch_apartment_types Saved...");

                    } else {
                        Log.i("Apendin auth id list", "FAILED");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        String revenuesBufferedDataToString = myBufferedData.toString();
        Log.e("buffer to string", "AFTER to string** " + revenuesBufferedDataToString);
        String replacedRevenueBufferedData = revenuesBufferedDataToString.replaceAll(",$", "");
        Log.e("Buffer reader", "AFTER string replace::" + replacedRevenueBufferedData);
        Log.e("Return to class", "Send employee info string back");

        return replacedRevenueBufferedData;
    }

    public String AnalyzeLandFunctions(Context context, JSONArray jsonArray) {

        JSONArray responseJson = new JSONArray();
        StringBuffer myBufferedData = new StringBuffer();

        //check if we have data in jsonArray_ASSIGN
        if (jsonArray.length() > 0) {
            //repeat to get data from jsonarray
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject presentJSONObject = jsonArray.getJSONObject(i);
                    ContentValues table_builder = new ContentValues();

                    table_builder.put("synch_land_function_id", presentJSONObject.getString("synch_land_function_id"));
                    table_builder.put("land_function_id", presentJSONObject.getString("land_function_id"));
                    table_builder.put("land_function", presentJSONObject.getString("land_function"));
                    table_builder.put("land_purpose", presentJSONObject.getString("land_purpose"));
//                    table_builder.put("business_sector", presentJSONObject.getString("business_sector"));//date_log
                    String authorizationID = presentJSONObject.getString("synch_land_function_id");
                    table_builder.put("service_id", presentJSONObject.getString("service_id"));//date_log
//                    table_builder.put("business_type", presentJSONObject.getString("business_type"));//date_log



                         /*   //// TODO: 19/09/2015 Delete all records first
                            dataDB.myConnection(context).deleteRecords("admin_users");*/
                    // TODO: 19/09/2015 Call insert
                    long lastInsertId = dataDB.myConnection(context).onInsertOrUpdate(table_builder, "synch_land_functions");
//                            Log.e(TAG, i+ " -Employee Saved...");
                    if (lastInsertId > 0) {

                        Log.i("Apendin auth id list ", "for response  ");
                        myBufferedData.append(authorizationID + ",");
                        Log.e(TAG, i + " -synch_apartment_types Saved...");

                    } else {
                        Log.i("Apendin auth id list", "FAILED");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        String revenuesBufferedDataToString = myBufferedData.toString();
        Log.e("buffer to string", "AFTER to string** " + revenuesBufferedDataToString);
        String replacedRevenueBufferedData = revenuesBufferedDataToString.replaceAll(",$", "");
        Log.e("Buffer reader", "AFTER string replace::" + replacedRevenueBufferedData);
        Log.e("Return to class", "Send employee info string back");

        return replacedRevenueBufferedData;
    }

    public String AnalyzeLandOwnership(Context context, JSONArray jsonArray) {

        JSONArray responseJson = new JSONArray();
        StringBuffer myBufferedData = new StringBuffer();

        //check if we have data in jsonArray_ASSIGN
        if (jsonArray.length() > 0) {
            //repeat to get data from jsonarray
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject presentJSONObject = jsonArray.getJSONObject(i);
                    ContentValues table_builder = new ContentValues();

                    table_builder.put("synch_land_ownership_id", presentJSONObject.getString("synch_land_ownership_id"));
                    table_builder.put("land_ownership_id", presentJSONObject.getString("land_ownership_id"));
                    table_builder.put("land_ownership", presentJSONObject.getString("land_ownership"));
                    /*table_builder.put("building_occupancy", presentJSONObject.getString("building_occupancy"));
                    table_builder.put("registered_on", presentJSONObject.getString("registered_on"));//date_log
*/                    String authorizationID = presentJSONObject.getString("synch_land_ownership_id");
                    table_builder.put("service_id", presentJSONObject.getString("service_id"));//date_log
//                    table_builder.put("business_type", presentJSONObject.getString("business_type"));//date_log



                         /*   //// TODO: 19/09/2015 Delete all records first
                            dataDB.myConnection(context).deleteRecords("admin_users");*/
                    // TODO: 19/09/2015 Call insert
                    long lastInsertId = dataDB.myConnection(context).onInsertOrUpdate(table_builder, "synch_land_ownerships");
//                            Log.e(TAG, i+ " -Employee Saved...");
                    if (lastInsertId > 0) {

                        Log.i("Apendin auth id list ", "for response  ");
                        myBufferedData.append(authorizationID + ",");
                        Log.e(TAG, i + " -synch_apartment_types Saved...");

                    } else {
                        Log.i("Apendin auth id list", "FAILED");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        String revenuesBufferedDataToString = myBufferedData.toString();
        Log.e("buffer to string", "AFTER to string** " + revenuesBufferedDataToString);
        String replacedRevenueBufferedData = revenuesBufferedDataToString.replaceAll(",$", "");
        Log.e("Buffer reader", "AFTER string replace::" + replacedRevenueBufferedData);
        Log.e("Return to class", "Send employee info string back");

        return replacedRevenueBufferedData;
    }

    public String AnalyzeLandPurposes(Context context, JSONArray jsonArray) {

        JSONArray responseJson = new JSONArray();
        StringBuffer myBufferedData = new StringBuffer();

        //check if we have data in jsonArray_ASSIGN
        if (jsonArray.length() > 0) {
            //repeat to get data from jsonarray
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject presentJSONObject = jsonArray.getJSONObject(i);
                    ContentValues table_builder = new ContentValues();


                    table_builder.put("synch_land_purpose_id", presentJSONObject.getString("synch_land_purpose_id"));
                    table_builder.put("land_purpose_id", presentJSONObject.getString("land_purpose_id"));
                    table_builder.put("land_purpose", presentJSONObject.getString("land_purpose"));
                    /*table_builder.put("building_occupancy", presentJSONObject.getString("building_occupancy"));
                    table_builder.put("registered_on", presentJSONObject.getString("registered_on"));//date_log
*/                    String authorizationID = presentJSONObject.getString("synch_land_purpose_id");
                    table_builder.put("service_id", presentJSONObject.getString("service_id"));//date_log
//                    table_builder.put("business_type", presentJSONObject.getString("business_type"));//date_log



                         /*   //// TODO: 19/09/2015 Delete all records first
                            dataDB.myConnection(context).deleteRecords("admin_users");*/
                    // TODO: 19/09/2015 Call insert
                    long lastInsertId = dataDB.myConnection(context).onInsertOrUpdate(table_builder, "synch_land_purposes");
//                            Log.e(TAG, i+ " -Employee Saved...");
                    if (lastInsertId > 0) {

                        Log.i("Apendin auth id list ", "for response  ");
                        myBufferedData.append(authorizationID + ",");
                        Log.e(TAG, i + " -synch_apartment_types Saved...");

                    } else {
                        Log.i("Apendin auth id list", "FAILED");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        String revenuesBufferedDataToString = myBufferedData.toString();
        Log.e("buffer to string", "AFTER to string** " + revenuesBufferedDataToString);
        String replacedRevenueBufferedData = revenuesBufferedDataToString.replaceAll(",$", "");
        Log.e("Buffer reader", "AFTER string replace::" + replacedRevenueBufferedData);
        Log.e("Return to class", "Send employee info string back");

        return replacedRevenueBufferedData;
    }

    public String AnalyzeLandTypes(Context context, JSONArray jsonArray) {

        JSONArray responseJson = new JSONArray();
        StringBuffer myBufferedData = new StringBuffer();

        //check if we have data in jsonArray_ASSIGN
        if (jsonArray.length() > 0) {
            //repeat to get data from jsonarray
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject presentJSONObject = jsonArray.getJSONObject(i);
                    ContentValues table_builder = new ContentValues();

                    table_builder.put("synch_land_type_id", presentJSONObject.getString("synch_land_type_id"));
                    table_builder.put("land_type_id", presentJSONObject.getString("land_type_id"));
                    table_builder.put("land_type", presentJSONObject.getString("land_type"));
                    /*table_builder.put("building_occupancy", presentJSONObject.getString("building_occupancy"));
                    table_builder.put("registered_on", presentJSONObject.getString("registered_on"));//date_log
*/                    String authorizationID = presentJSONObject.getString("synch_land_type_id");
                    table_builder.put("service_id", presentJSONObject.getString("service_id"));//date_log
//                    table_builder.put("business_type", presentJSONObject.getString("business_type"));//date_log



                         /*   //// TODO: 19/09/2015 Delete all records first
                            dataDB.myConnection(context).deleteRecords("admin_users");*/
                    // TODO: 19/09/2015 Call insert
                    long lastInsertId = dataDB.myConnection(context).onInsertOrUpdate(table_builder, "synch_land_types");
//                            Log.e(TAG, i+ " -Employee Saved...");
                    if (lastInsertId > 0) {

                        Log.i("Apendin auth id list ", "for response  ");
                        myBufferedData.append(authorizationID + ",");
                        Log.e(TAG, i + " -synch_apartment_types Saved...");

                    } else {
                        Log.i("Apendin auth id list", "FAILED");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        String revenuesBufferedDataToString = myBufferedData.toString();
        Log.e("buffer to string", "AFTER to string** " + revenuesBufferedDataToString);
        String replacedRevenueBufferedData = revenuesBufferedDataToString.replaceAll(",$", "");
        Log.e("Buffer reader", "AFTER string replace::" + replacedRevenueBufferedData);
        Log.e("Return to class", "Send employee info string back");

        return replacedRevenueBufferedData;
    }

    public String AnalyzeStreet(Context context, JSONArray jsonArray) {

        JSONArray responseJson = new JSONArray();
        StringBuffer myBufferedData = new StringBuffer();

        //check if we have data in jsonArray_ASSIGN
        if (jsonArray.length() > 0) {
            //repeat to get data from jsonarray
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject presentJSONObject = jsonArray.getJSONObject(i);
                    ContentValues table_builder = new ContentValues();

                    table_builder.put("idstreet", presentJSONObject.getString("idstreet"));
                    table_builder.put("street", presentJSONObject.getString("street"));
                    table_builder.put("city_id", presentJSONObject.getString("city_id"));
                    /*table_builder.put("building_occupancy", presentJSONObject.getString("building_occupancy"));
                    table_builder.put("registered_on", presentJSONObject.getString("registered_on"));//date_log
*/                    String authorizationID = presentJSONObject.getString("idstreet");
                    table_builder.put("service_id", presentJSONObject.getString("service_id"));//date_log
                    table_builder.put("street_id", presentJSONObject.getString("street_id"));//date_log
                    table_builder.put("area_code_id", presentJSONObject.getString("area_code_id"));//date_log
                    table_builder.put("area_name", presentJSONObject.getString("area_name"));//date_log
                    table_builder.put("city", presentJSONObject.getString("city"));//date_log
                    table_builder.put("zone", presentJSONObject.getString("zone"));//date_log



                         /*   //// TODO: 19/09/2015 Delete all records first
                            dataDB.myConnection(context).deleteRecords("admin_users");*/
                    // TODO: 19/09/2015 Call insert
                    long lastInsertId = dataDB.myConnection(context).onInsertOrUpdate(table_builder, "synch_streets");
//                            Log.e(TAG, i+ " -Employee Saved...");
                    if (lastInsertId > 0) {

                        Log.i("Apendin auth id list ", "for response  ");
                        myBufferedData.append(authorizationID + ",");
                        Log.e(TAG, i + " -synch_apartment_types Saved...");

                    } else {
                        Log.i("Apendin auth id list", "FAILED");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        String revenuesBufferedDataToString = myBufferedData.toString();
        Log.e("buffer to string", "AFTER to string** " + revenuesBufferedDataToString);
        String replacedRevenueBufferedData = revenuesBufferedDataToString.replaceAll(",$", "");
        Log.e("Buffer reader", "AFTER string replace::" + replacedRevenueBufferedData);
        Log.e("Return to class", "Send employee info string back");

        return replacedRevenueBufferedData;
    }

    public String AnalyzeBuildingTypes(Context context, JSONArray jsonArray) {

        JSONArray responseJson = new JSONArray();
        StringBuffer myBufferedData = new StringBuffer();

        //check if we have data in jsonArray_ASSIGN
        if (jsonArray.length() > 0) {
            //repeat to get data from jsonarray
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject presentJSONObject = jsonArray.getJSONObject(i);
                    ContentValues table_builder = new ContentValues();

                    table_builder.put("synch_building_type_id", presentJSONObject.getString("synch_building_type_id"));
                    table_builder.put("building_type_id", presentJSONObject.getString("building_type_id"));
                    table_builder.put("building_type", presentJSONObject.getString("building_type"));
                    /*table_builder.put("building_occupancy", presentJSONObject.getString("building_occupancy"));
                    table_builder.put("registered_on", presentJSONObject.getString("registered_on"));//date_log
*/                    String authorizationID = presentJSONObject.getString("synch_building_type_id");
//                    table_builder.put("service_id", presentJSONObject.getString("service_id"));//date_log
//                    table_builder.put("street_id", presentJSONObject.getString("street_id"));//date_log
//                    table_builder.put("area_code_id", presentJSONObject.getString("area_code_id"));//date_log
//                    table_builder.put("area_name", presentJSONObject.getString("area_name"));//date_log
//                    table_builder.put("city", presentJSONObject.getString("city"));//date_log
//                    table_builder.put("zone", presentJSONObject.getString("zone"));//date_log



                         /*   //// TODO: 19/09/2015 Delete all records first
                            dataDB.myConnection(context).deleteRecords("admin_users");*/
                    // TODO: 19/09/2015 Call insert
                    long lastInsertId = dataDB.myConnection(context).onInsertOrUpdate(table_builder, "synch_building_types");
//                            Log.e(TAG, i+ " -Employee Saved...");
                    if (lastInsertId > 0) {

                        Log.i("Apendin auth id list ", "for response  ");
                        myBufferedData.append(authorizationID + ",");
                        Log.e(TAG, i + " -synch_apartment_types Saved...");

                    } else {
                        Log.i("Apendin auth id list", "FAILED");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        String revenuesBufferedDataToString = myBufferedData.toString();
        Log.e("buffer to string", "AFTER to string** " + revenuesBufferedDataToString);
        String replacedRevenueBufferedData = revenuesBufferedDataToString.replaceAll(",$", "");
        Log.e("Buffer reader", "AFTER string replace::" + replacedRevenueBufferedData);
        Log.e("Return to class", "Send employee info string back");

        return replacedRevenueBufferedData;
    }

    public String AnalyzeProfiles(Context context, JSONArray jsonArray) {

        JSONArray responseJson = new JSONArray();
        StringBuffer myBufferedData = new StringBuffer();

        //check if we have data in jsonArray_ASSIGN
        if (jsonArray.length() > 0) {
            //repeat to get data from jsonarray
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject presentJSONObject = jsonArray.getJSONObject(i);
                    ContentValues table_builder = new ContentValues();


                    table_builder.put("id_synch_profile", presentJSONObject.getString("id_synch_profile"));
                    table_builder.put("profile_id", presentJSONObject.getString("profile_id"));
                    table_builder.put("profile_ref", presentJSONObject.getString("profile_ref"));
                    table_builder.put("profile_description", presentJSONObject.getString("profile_description"));
                    table_builder.put("asset_type", presentJSONObject.getString("asset_type"));//date_log
                    String authorizationID = presentJSONObject.getString("id_synch_profile");
                    table_builder.put("service_id", presentJSONObject.getString("service_id"));//date_log
                    table_builder.put("created_by", presentJSONObject.getString("created_by"));//date_log
                    table_builder.put("created_at", presentJSONObject.getString("created_at"));//date_log
//                    table_builder.put("area_name", presentJSONObject.getString("area_name"));//date_log
//                    table_builder.put("city", presentJSONObject.getString("city"));//date_log
//                    table_builder.put("zone", presentJSONObject.getString("zone"));//date_log



                         /*   //// TODO: 19/09/2015 Delete all records first
                            dataDB.myConnection(context).deleteRecords("admin_users");*/
                    // TODO: 19/09/2015 Call insert
                    long lastInsertId = dataDB.myConnection(context).onInsertOrUpdate(table_builder, "synch_profiles");
//                            Log.e(TAG, i+ " -Employee Saved...");
                    if (lastInsertId > 0) {

                        Log.i("Apendin auth id list ", "for response  ");
                        myBufferedData.append(authorizationID + ",");
                        Log.e(TAG, i + " -synch_apartment_types Saved...");

                    } else {
                        Log.i("Apendin auth id list", "FAILED");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        String revenuesBufferedDataToString = myBufferedData.toString();
        Log.e("buffer to string", "AFTER to string** " + revenuesBufferedDataToString);
        String replacedRevenueBufferedData = revenuesBufferedDataToString.replaceAll(",$", "");
        Log.e("Buffer reader", "AFTER string replace::" + replacedRevenueBufferedData);
        Log.e("Return to class", "Send employee info string back");

        return replacedRevenueBufferedData;
    }

    public String AnalyzeAssessmentRule(Context context, JSONArray jsonArray) {

        JSONArray responseJson = new JSONArray();
        StringBuffer myBufferedData = new StringBuffer();

        //check if we have data in jsonArray_ASSIGN
        if (jsonArray.length() > 0) {
            //repeat to get data from jsonarray
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject presentJSONObject = jsonArray.getJSONObject(i);
                    ContentValues table_builder = new ContentValues();

                   /* "assessment_rule_id": 8,
                            "rule_code": "AR13",
                            "profile_ref": "PRIVATE VEHICLE PROFILE",
                            "assessment_rule_name": "Motor Vehicle Licenses",
                            "assessment_item": "Motor Vehicle Licences",
                            "item_ref": null,
                            "assessment_amount": null,
                            "service_id": "234120010005",
                            "created_by": "admin@paye.com",
                            "created_at": "2018-10-29 11:10:03",
                            "updated_by": null,
                            "updated_at": "2018-12-18 13:54:13",
                            "synch_status": 0,
                            "last_synched_date": "2019-01-08 16:04:55",
                            "authorization_id": "937595934546",
                            "back_up": 0,
                            "user_id": 446
*/
                    table_builder.put("assessment_rule_id", presentJSONObject.getString("assessment_rule_id"));
                    table_builder.put("rule_code", presentJSONObject.getString("rule_code"));
                    table_builder.put("profile_ref", presentJSONObject.getString("profile_ref"));
                    table_builder.put("assessment_rule_name", presentJSONObject.getString("assessment_rule_name"));
                    table_builder.put("assessment_item", presentJSONObject.getString("assessment_item"));//date_log
                    String authorizationID = presentJSONObject.getString("assessment_rule_id");
                    table_builder.put("service_id", presentJSONObject.getString("service_id"));//date_log
                    table_builder.put("created_by", presentJSONObject.getString("created_by"));//date_log
                    table_builder.put("created_at", presentJSONObject.getString("created_at"));//date_log
                    table_builder.put("item_ref", presentJSONObject.getString("item_ref"));//date_log
                    table_builder.put("assessment_amount", presentJSONObject.getString("assessment_amount"));//date_log
                    table_builder.put("user_id", presentJSONObject.getString("user_id"));//date_log



                         /*   //// TODO: 19/09/2015 Delete all records first
                            dataDB.myConnection(context).deleteRecords("admin_users");*/
                    // TODO: 19/09/2015 Call insert
                    long lastInsertId = dataDB.myConnection(context).onInsertOrUpdate(table_builder, "synch_assessment_rules");
//                            Log.e(TAG, i+ " -Employee Saved...");
                    if (lastInsertId > 0) {

                        Log.i("Apendin auth id list ", "for response  ");
                        myBufferedData.append(authorizationID + ",");
                        Log.e(TAG, i + " -synch_apartment_types Saved...");

                    } else {
                        Log.i("Apendin auth id list", "FAILED");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        String revenuesBufferedDataToString = myBufferedData.toString();
        Log.e("buffer to string", "AFTER to string** " + revenuesBufferedDataToString);
        String replacedRevenueBufferedData = revenuesBufferedDataToString.replaceAll(",$", "");
        Log.e("Buffer reader", "AFTER string replace::" + replacedRevenueBufferedData);
        Log.e("Return to class", "Send employee info string back");

        return replacedRevenueBufferedData;
    }


    public String AnalyzeWards(Context context, JSONArray jsonArray) {

        JSONArray responseJson = new JSONArray();
        StringBuffer myBufferedData = new StringBuffer();

        //check if we have data in jsonArray_ASSIGN
        if (jsonArray.length() > 0) {
            //repeat to get data from jsonarray
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject presentJSONObject = jsonArray.getJSONObject(i);
                    ContentValues table_builder = new ContentValues();



                    table_builder.put("ward_id", presentJSONObject.getString("ward_id"));
                    table_builder.put("ward", presentJSONObject.getString("ward"));
                    table_builder.put("lga", presentJSONObject.getString("lga"));
                    table_builder.put("lga_id", presentJSONObject.getString("lga_id"));
                    table_builder.put("service_id", presentJSONObject.getString("service_id"));//date_log
                    String authorizationID = presentJSONObject.getString("ward_id");
                    table_builder.put("service_id", presentJSONObject.getString("service_id"));//date_log
                    table_builder.put("created_by", presentJSONObject.getString("created_by"));//date_log
                    table_builder.put("created_at", presentJSONObject.getString("created_at"));//date_log
//                    table_builder.put("item_ref", presentJSONObject.getString("item_ref"));//date_log
//                    table_builder.put("assessment_amount", presentJSONObject.getString("assessment_amount"));//date_log
                    table_builder.put("user_id", presentJSONObject.getString("user_id"));//date_log



                         /*   //// TODO: 19/09/2015 Delete all records first
                            dataDB.myConnection(context).deleteRecords("admin_users");*/
                    // TODO: 19/09/2015 Call insert
                    long lastInsertId = dataDB.myConnection(context).onInsertOrUpdate(table_builder, "synch_wards");
//                            Log.e(TAG, i+ " -Employee Saved...");
                    if (lastInsertId > 0) {

                        Log.i("Apendin auth id list ", "for response  ");
//                        myBufferedData.append(authorizationID + ",");
                        myBufferedData.append(presentJSONObject.getString("id_ward") + ",");

                        Log.e(TAG, i + " -synch_apartment_types Saved...");

                    } else {
                        Log.i("Apendin auth id list", "FAILED");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        String revenuesBufferedDataToString = myBufferedData.toString();
        Log.e("buffer to string", "AFTER to string** " + revenuesBufferedDataToString);
        String replacedRevenueBufferedData = revenuesBufferedDataToString.replaceAll(",$", "");
        Log.e("Buffer reader", "AFTER string replace::" + replacedRevenueBufferedData);
        Log.e("Return to class", "Send employee info string back");

        return replacedRevenueBufferedData;
    }



    //9999999999

}
