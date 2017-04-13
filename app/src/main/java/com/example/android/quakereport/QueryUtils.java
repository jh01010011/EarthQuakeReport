package com.example.android.quakereport;

/**
 * Created by blueb on 3/31/2017.
 */

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

//import static com.example.android.quakereport.EarthquakeActivity.LOG_TAG;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    private static final String LOG_TAG=QueryUtils.class.getName();


    private QueryUtils() {
    }

    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<Earthquake> extractEarthquakes(String param_url) {

        Log.i(LOG_TAG,"______Extract______");



        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Earthquake> earthquakes = new ArrayList<>();

        if(param_url==null){
            return null;
        }


         String SAMPLE_JSON_RESPONSE=extract_json(param_url);

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.
            JSONObject root=new JSONObject(SAMPLE_JSON_RESPONSE);
            JSONArray feature=root.optJSONArray("features");
            for(int i=0; i<feature.length();i++){
                JSONObject temp=feature.getJSONObject(i);
                JSONObject properties=temp.getJSONObject("properties");
                float mag= (float) properties.getDouble("mag");
                String loc= properties.getString("place");
                long time=properties.getLong("time");
                String url = properties.getString("url");


                earthquakes.add(new Earthquake(mag,loc,time,url));

            }

        }
        catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

    private static String extract_json(String url_param){
        String json_res=null;

        URL url=createURL(url_param);
        try {
            json_res=makeHTTPRequest(url);
        }

        catch (IOException e){

            Log.v("MAIN_ACTIVITY","HTTP ERROR "+e);
        }



        return json_res;
    }

    private static URL createURL(String url_param){
        URL url=null;
        try {
            url=new URL(url_param);
        }
        catch (MalformedURLException e){
            Log.e("MAIN_ACTIVITY","EROR URL"+e);
        }
        return url;
    }


    private static String makeHTTPRequest(URL url_param) throws IOException{
        String json_res=null;
        if(url_param==null){
            return json_res;
        }

        HttpURLConnection httpURLConnection=null;
        InputStream inputStream=null;

        try {
            httpURLConnection=(HttpURLConnection) url_param.openConnection();
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode()==200){
                inputStream=httpURLConnection.getInputStream();
                json_res=readFromStream(inputStream);

            }

            else{
                Log.v("MAIN_ACTIVITY","Error Response"+httpURLConnection.getResponseCode());
            }
        }
        catch (IOException e){
            Log.v("MAIN_ACTIVITY","Prob Retrieving JSON"+e);
        }

        finally {
            if(httpURLConnection!=null){
                httpURLConnection.disconnect();
            }
            if(inputStream!=null){
                inputStream.close();
            }
        }


        return json_res;
    }


    private static String readFromStream(InputStream inputStream) throws IOException{

        StringBuilder stringBuilder=new StringBuilder();
        if (inputStream!=null){

            InputStreamReader inputStreamReader=new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
            String line=bufferedReader.readLine();

            while (line!=null){
                stringBuilder.append(line);
                line=bufferedReader.readLine();
            }
        }

        return stringBuilder.toString();

    }

}