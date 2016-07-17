package langco.postwithimageviewer;

import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import langco.postwithimageviewer.Helpers.App;
import langco.postwithimageviewer.Helpers.BusEventHandler;

/**
 * Created by Langb_000 on 7/16/2016.
 */
public class FacebookFeedRead {

    public FacebookFeedRead (String api_key, String app_key,String user_key) {
        AccessToken accessToken = new AccessToken(api_key,app_key,user_key,null,null,null,null,null);

       // String url = "https://graph.facebook.com/v2.7/me/photos/uploaded?access_token=CAAAATT1YoUABAOT1GrQphXQx7RZBSpMjwUfwxTdkXFUsQZCILNqbKDIdPnDfXpvXAapK4P3GQpYj6hEooLEMbRLWZBWWN1rhmxEl0RLJochZBUQZA8mDiJsFpLEpVVXx1VdFDY3DuDqIDNlVG7YZA9lhvg6HmuqqmNPZARrHoQrr4ZCT8tkV6LJddcmTDzgnyD8X5djrihbQbgZDZD";
        App.bus.register(this);
        GraphRequest full_feed_request = GraphRequest.newGraphPathRequest(
                accessToken,
                "/me/photos/uploaded",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse full_feed_response) {

                        checkForEndOfFeed(full_feed_response.getRawResponse());
                        parseJson(full_feed_response.getRawResponse());
                        ArrayList<String[]> parsed_feed =App.getParsedFeed();

                        App.bus.post(new BusEventHandler("JSON Parse Complete"));
                    }
                    });
        Bundle parameters = new Bundle();
        parameters.putString("limit", "1000000");
        full_feed_request.setParameters(parameters);
       full_feed_request.executeAsync();



    }


    private String checkForEndOfFeed(String passed_data) {
        //Takes in the JSON data from the URL and if it has a node for next passes back the URL. If not it returns ""

        JSONObject full_json_object = null;
        JSONObject paging_object =null;
        String return_value="";
        try {
            full_json_object = new JSONObject(passed_data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            paging_object = full_json_object.getJSONObject("paging");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            return_value = paging_object.getString("next");

        } catch (JSONException e) {
            return_value = "";
        }
        return return_value;
    }

    public void parseJson (String passed_data) {
        ArrayList<String[]> parsed_feed = new ArrayList<>();
        String date;
        String copy;
        String id;
        String image_url;

       /* ArrayList<String> message_array= new ArrayList<>();
        ArrayList<String> date_array= new ArrayList<>();*/
       // JSONObject full_json_object = passed_data.getJSONObject();
        JSONObject full_json_object = null;
        JSONArray data_array=null;
        try {
             full_json_object = new JSONObject(passed_data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            data_array = full_json_object.getJSONArray("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (data_array!=null) {
            JSONObject post_object = null;
            for (int i = 0; i < data_array.length(); i++) {
                try {
                    post_object=data_array.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    date = post_object.getString("created_time");
                } catch (JSONException e) {
                    date = "";
                }
                try {
                    copy = post_object.getString("name");
                } catch (JSONException e) {
                    copy = "";
                }
                try {
                    id = post_object.getString("id");
                    image_url = "http://graph.facebook.com/"+post_object.getString("id")+"/picture";
                } catch (JSONException e) {
                    id="";
                    image_url = "";
                }
                App.addToParsedFeed(new String[]{date,copy,image_url,id});

            }
        }


    }
}
