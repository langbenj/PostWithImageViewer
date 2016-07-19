package langco.postwithimageviewer.FacebookClasses;

import android.os.Bundle;
import android.util.Log;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import langco.postwithimageviewer.Helpers.App;
import langco.postwithimageviewer.Helpers.BusEventHandler;

public class FacebookFeedRead {
    static String feed_status = "started";
    static String image_feed_status = "started";

    public FacebookFeedRead (String api_key, String app_key,String user_key) {
        AccessToken accessToken = new AccessToken(api_key,app_key,user_key,null,null,null,null,null);
        //Register this with Otto for bus communication
        App.bus.register(this);

        //Read in the post information using the GraphAPI
        GraphRequest full_feed_request = GraphRequest.newGraphPathRequest(
                accessToken,
                "/me/photos/uploaded",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse full_feed_response) {
                        //Parses the returned JSON feed and loads it into shared storage in App
                        parseFeedJson(full_feed_response.getRawResponse(), "data");
                        /* Checks to see if the image feed read has been completed. If both are
                         * complete then calls the MainActivity using Otto and triggers the list
                         * generation.
                         */
                        if (image_feed_status.equals("finished")) {
                            App.bus.post(new BusEventHandler(new String[]{"JSON Parse Complete",""}));
                        }
                        feed_status="finished";
                    }
                    });
        Bundle parameters = new Bundle();
        //Loads all of the posts. Only processes the posts that are actually there. TODO handle this better
        parameters.putString("limit", "1000000");
        full_feed_request.setParameters(parameters);
        full_feed_request.executeAsync();

        //Read in the image information using the GraphAPI
        GraphRequest image_request = GraphRequest.newGraphPathRequest(
                accessToken,
                "/me/photos/uploaded",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse image_response) {
                        //Parses the returned JSON feed and loads it into shared storage in App
                        parseFeedJson(image_response.getRawResponse(), "images");
                        /* Checks to see if the image feed read has been completed. If both are
                         * complete then calls the MainActivity using Otto and triggers the list
                         * generation.
                         */
                        if (feed_status.equals("finished")) {
                            App.bus.post(new BusEventHandler(new String[]{"JSON Parse Complete",""}));
                        }
                        image_feed_status="finished";
                    }
                });

        Bundle image_parameters = new Bundle();
        //Tells the GraphAPI to return the image information not the post information
        image_parameters.putString("fields", "images");
        //Loads all of the posts. Only processes the posts that are actually there. TODO handle this better
        image_parameters.putString("limit", "1000000");
        image_request.setParameters(image_parameters);
        image_request.executeAsync();



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
            if (full_json_object != null) {
                paging_object = full_json_object.getJSONObject("paging");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if (paging_object != null) {
                return_value = paging_object.getString("next");
            }

        } catch (JSONException e) {
            return_value = "";
        }
        return return_value;
    }

    public void parseFeedJson (String passed_data, String type) {
        JSONObject full_json_object = null;
        JSONArray data_array=null;
        //Read in the full JSON object and parse out the data array. This is common to both cases

        try {
             full_json_object = new JSONObject(passed_data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if (full_json_object != null) {
                data_array = full_json_object.getJSONArray("data");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (data_array!=null) {

            //If we are reading in the post information execute this code
           if (type.equals("data")) {
               String date="";
               String copy="";
               String id="";
               JSONObject post_object = null;

            for (int i = 0; i < data_array.length(); i++) {
                try {
                    post_object=data_array.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (post_object != null) {
                        date = post_object.getString("created_time");
                    }
                } catch (JSONException e) {
                    date = "";
                }
                try {
                    if (post_object != null) {
                        copy = post_object.getString("name");
                    }
                } catch (JSONException e) {
                    copy = "";
                }

                try {
                    if (post_object != null) {
                        id = post_object.getString("id");
                    }
                } catch (JSONException e) {
                    id = "";
                }

                //Add the date, copy and id to the ParsedImageFeed ArrayList in App
                App.addToParsedFeed(new String[]{date,copy,id});
            }
           }

           //If we are reading in the image information execute this code
            else if (type.equals("images")) {
               JSONArray image_array=null;
               String thumbnail_url="";
               String full_image_url="";
               JSONObject thumbnail_object = null;
               JSONObject full_image_object = null;
               for (int i = 0; i < data_array.length(); i++) {
                   try {
                       image_array = data_array.getJSONObject(i).getJSONArray("images");

                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
                   try {
                       //Object 4 is the image size that best fits the thumbnail
                       if (image_array != null) {
                           thumbnail_object=image_array.getJSONObject(4);
                       }
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }

                   try {
                       if (thumbnail_object != null) {
                           thumbnail_url = thumbnail_object.getString("source");
                       }
                   } catch (JSONException e) {
                       thumbnail_url = "";
                   }
                   try {
                       //Object 0 is the image size that is full size
                       if (image_array != null) {
                           full_image_object=image_array.getJSONObject(0);
                       }
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }

                   try {
                       if (full_image_object != null) {
                           full_image_url = full_image_object.getString("source");
                       }
                   } catch (JSONException e) {
                       full_image_url = "";
                   }
                   //Add the thumbnail and full image urls to the ParsedImageFeed ArrayList in App

                   App.addToParsedImageFeed(new String[]{thumbnail_url,full_image_url});

               }
           }
        }
        else {
            Log.d("FacebookFeedRead", "The data returned is null");
        }


    }

}
