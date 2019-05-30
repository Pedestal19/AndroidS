package hosanna_techvibes.revcollectenumerator.helpers;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.apache.http.Header;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import hosanna_techvibes.revcollectenumerator.model.AuthorizationHttpResponse;

/**
 * Created by EbukaProf on 29/09/2016.
 */
public class HttpURLConnectionHelper {
    private final String USER_AGENT = "Mozilla/5.0";
    private static final String TAG = "HttpURL";

    public StringBuffer sendGet(String url) throws Exception
    {
        //example : http://www.google.com/search?k=johndo
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        //adding header
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while((inputLine = in.readLine()) != null)
        {
            response.append(inputLine);

        }
        in.close();
        Log.w(TAG, "response from Get; " + response.toString());
        return response;

    }

    public StringBuffer sendPOST(String url, String urlparams) throws Exception
    {
        //urlparams example: firstname=ebuka&company=techvibes&num=1234567
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

        //send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlparams);
        wr.flush();
        wr.close();

        //get response code
        int responseCode = con.getResponseCode();

        //fetch response string
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while((inputLine = in.readLine()) != null)
        {
            response.append(inputLine);
        }
        in.close();
        Log.w(TAG,"Response from POST request;" + response);
        return response;
    }

    public AuthorizationHttpResponse sendPOST(String url, String urlparams, boolean yes) throws Exception
    {
        //urlparams example: firstname=ebuka&company=techvibes&num=1234567
        AuthorizationHttpResponse httpResponse = new AuthorizationHttpResponse();
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

        //send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlparams);
        wr.flush();
        wr.close();

        //get response code
        //int responseCode = con.getResponseCode();
        httpResponse.setResponseCode(con.getResponseCode());

        //fetch response string
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while((inputLine = in.readLine()) != null)
        {
            //response.append(inputLine);
            httpResponse.setResponseData(inputLine);
        }
        in.close();
        Log.w(TAG,"Response from POST request;" + response);
        return httpResponse; //response;
    }

    public static AuthorizationHttpResponse executePost(String targetURL, String urlParameters)
    {
        AuthorizationHttpResponse httpResponse = new AuthorizationHttpResponse();
        Log.i("Post Started", "just started the post");
        URL url;
        HttpURLConnection connection = null;
        try {
            //Create connection
            Log.i("Response", "using the try root");
            url = new URL(targetURL);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("SendChunked", "True");
            connection.setRequestProperty("Content-Type",
                    "application/x-www- form-urlencoded");

            connection.setRequestProperty("Content-Length", "" +
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            //Send request
            DataOutputStream wr = new DataOutputStream (connection.getOutputStream ());
            wr.writeBytes (urlParameters);
            wr.flush();
            wr.close();

            //Get the statuscode
            httpResponse.setResponseCode(connection.getResponseCode());
            //Get Response

            InputStream errorStream = connection.getErrorStream();

            if(errorStream == null){
                InputStream is = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();

                while((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
                Log.i("First response", "value:" + response.toString());
                httpResponse.setResponseData(response.toString());
                //return response.toString();
                return httpResponse;
            }
            else {

                BufferedReader rd = new BufferedReader(new InputStreamReader(errorStream));
                String line;
                StringBuffer response = new StringBuffer();

                while((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
                Log.i("error stream: ", "the error " + errorStream);
                httpResponse.setResponseData(response.toString());
                return httpResponse;
            }

        } catch (Exception e) {
            Log.i("Response", "using the catch root");
            e.printStackTrace();
            return null;

        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
    }

    public AuthorizationHttpResponse postMadeEasy(Context context,String url,String method,String params)
    {
        final AuthorizationHttpResponse httpResponse = new AuthorizationHttpResponse();
        try{
            if(GenericHelpers.checkInternetConenction(context)){
                //  AsyncHttpClient client = new AsyncHttpClient();
                SyncHttpClient client = new SyncHttpClient();

                if(method == "GET"){
                    client.get(url, new AsyncHttpResponseHandler() {

                        @Override
                        public void onStart() {
                            // called before request is started
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                            // called when response HTTP status is "200 OK"
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                            // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        }

                        @Override
                        public void onRetry(int retryNo) {
                            // called when request is retried
                        }
                    });
                }
                if(method == "POST")
                {
                    //params = "{\"AUTH_DATA\":{\"action\":\"PUSH\",\"service_id\":\"234001\",\"authorization_id\":\"mimi@techvibesltd.com383748374801837383728373801\",\"authentication_code\":\"9915603a567f20556b752ff4ad0af28abf93c9de94994f0b6371531916b5db3dd0e8bf74012196024bbd46c151a405fa\",\"DATA\":{\"SYNC_DATA\":{\"mobile_synch_meter_readings\":[{\"date_previous_reading\":\"2015-01-23 25:03:01\",\"service_id\":\"2093348\",\"number_of_retry\":\"0\",\"idmeter_reading\":\"5\",\"session_id\":\"2039984840\",\"meter_no\":\"49039422\",\"back_up\":\"0\",\"building_id\":\"5\",\"last_synched_date\":\"2015-09-01 12:40:22\",\"synch_status\":\"0\",\"current_reading\":\"12209\",\"previous_reading\":\"33390\",\"user_id\":\"1\",\"meter_image\":\"iVBORw0KGgoAAAANS\",\"authorization_id\":\"1\",\"locked\":\"0\",\"date_current_reading\":\"2015-03-01 04:02:10\"},{\"date_previous_reading\":\"2015-02-03 12:01-22\",\"service_id\":\"2093894\",\"number_of_retry\":\"0\",\"idmeter_reading\":\"3\",\"session_id\":\"20124489\",\"meter_no\":\"2123090\",\"back_up\":\"0\",\"building_id\":\"3\",\"last_synched_date\":\"2015-09-01 12:40:22\",\"synch_status\":\"0\",\"current_reading\":\"700\",\"previous_reading\":\"600\",\"user_id\":\"1\",\"meter_image\":\"iVBORw0KGgoAAAANS\",\"authorization_id\":\"1\",\"locked\":\"0\",\"date_current_reading\":\"2015-03-03 12:12:20\"},{\"date_previous_reading\":\"2014-01-23 24:02:03\",\"service_id\":\"3012309\",\"number_of_retry\":\"0\",\"idmeter_reading\":\"4\",\"session_id\":\"32098012\",\"meter_no\":\"32409431\",\"back_up\":\"0\",\"building_id\":\"4\",\"last_synched_date\":\"2015-09-01 12:40:22\",\"synch_status\":\"0\",\"current_reading\":\"1208\",\"previous_reading\":\"800.4\",\"user_id\":\"1\",\"meter_image\":\"iVBORw0KGgoAAAANS\",\"authorization_id\":\"1\",\"locked\":\"0\",\"date_current_reading\":\"2014-04-02 11:12:10\"},{\"date_previous_reading\":\"2015-08-01 12:40:22\",\"service_id\":\"3212343\",\"number_of_retry\":\"0\",\"idmeter_reading\":\"1\",\"session_id\":\"20148902\",\"meter_no\":\"12309012\",\"back_up\":\"0\",\"building_id\":\"1\",\"last_synched_date\":\"2015-09-01 12:40:22\",\"synch_status\":\"0\",\"current_reading\":\"3006.5\",\"previous_reading\":\"3003.5\",\"user_id\":\"1\",\"meter_image\":\"iVBORw0KGgoAAAANS\",\"authorization_id\":\"1\",\"locked\":\"0\",\"date_current_reading\":\"2015-09-01 12:40:22\"},{\"date_previous_reading\":\"2015-06-02 13:20:23\",\"service_id\":\"3212343\",\"number_of_retry\":\"0\",\"idmeter_reading\":\"2\",\"session_id\":\"20148902\",\"meter_no\":\"8290123\",\"back_up\":\"0\",\"building_id\":\"2\",\"last_synched_date\":\"2015-09-01 12:40:22\",\"synch_status\":\"0\",\"current_reading\":\"4200\",\"previous_reading\":\"4000\",\"user_id\":\"1\",\"meter_image\":\"iVBORw0KGgoAAAANS\",\"authorization_id\":\"1\",\"locked\":\"0\",\"date_current_reading\":\"2015-07-02 13:20:23\"}]}}}}";
                    StringEntity se = null;
                    try {
                        se = new StringEntity(params.toString());
                    } catch (UnsupportedEncodingException e) {
                        // handle exceptions properly!
                    }
                    se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "text/html"));

                    ByteArrayEntity entity = new ByteArrayEntity(params.getBytes("UTF-8"));
                    client.post(context, url, se, "application/json", new AsyncHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            httpResponse.setResponseCode(statusCode);
                            httpResponse.setResponseByteData(responseBody);
                            System.out.println("OnSuccess: " + responseBody + "StatusCode: " + statusCode);

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            httpResponse.setResponseCode(statusCode);
                            httpResponse.setResponseByteData(responseBody);
                            System.out.println("OnFailure: " + responseBody + "StatusCode: " + statusCode);
                        }
                    });
                }
            }
            else{
                System.out.println("No Internet Connection ");
                httpResponse.setResponseCode(0);
                httpResponse.setResponseData("No Internet Connection");
            }



        }
        catch (Exception e){
            System.out.println("Exception gotten; " + e.toString());
            httpResponse.setResponseCode(0);
            httpResponse.setResponseData("Exception gotten; " + e.toString());
        }
        return httpResponse;
    }
}
