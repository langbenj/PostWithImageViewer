/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package langco.postwithimageviewer.FacebookClasses;

import android.os.Bundle;
import android.util.Log;

import langco.postwithimageviewer.Helpers.App;
import langco.postwithimageviewer.Helpers.BusEventHandler;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public class FacebookFeedRead {
    static String feed_status = "started";
    static String image_feed_status = "started";
    AccessToken accessToken;
    public FacebookFeedRead (String api_key, String app_key,String user_key) {
        accessToken = new AccessToken(api_key,app_key,user_key,null,null,null,null,null);
        //Register this with Otto for bus communication
        App.bus.register(this);
        //Read the text information for the feed
        readFacebookFeed("");
        //Read the image information for the feed
        readFacebookImageFeed("");
    }

    //Read the copy from the Facebook feed and parse it
    private void readFacebookFeed(String after) {
        //Read in the post information using the GraphAPI
        final GraphRequest full_feed_request = GraphRequest.newGraphPathRequest(
                accessToken,
                "/me/photos/uploaded",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse full_feed_response) {

                        String after = getAfter(full_feed_response.getRawResponse());

                        //Parses the returned JSON feed and loads it into shared storage in App
                            parseFeedJson(full_feed_response.getRawResponse(), "data");

                        /*Checks for if a next node is in the current JSON data if so it loads the
                        next set by recursively calling the function.*/
                            if (checkForNext(full_feed_response.getRawResponse())) {
                                //Reads the next page in the cursor
                                readFacebookFeed(after);
                            }
                            else {
                                /*If all of the items are loaded and the data feed is also complete the
                                * list is displayed. If the data feed is not complete then the image
                                * feed is marked as complete. */
                                if (image_feed_status.equals("finished")) {
                                     App.bus.post(new BusEventHandler(new String[]{"JSON Parse Complete",""}));
                                }

                                feed_status="finished";
                            }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("limit", "25");
        parameters.putString("after",after);
        full_feed_request.setParameters(parameters);
        full_feed_request.executeAsync();
    }

    //Read the images from the Facebook feed and parse them
    private void readFacebookImageFeed(String after) {
        //Read in the post information using the GraphAPI
        final GraphRequest full_feed_request = GraphRequest.newGraphPathRequest(
                accessToken,
                "/me/photos/uploaded",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse full_feed_response) {

                        String after = getAfter(full_feed_response.getRawResponse());

                        //Parses the returned JSON feed and loads it into shared storage in App
                        parseFeedJson(full_feed_response.getRawResponse(), "images");

                         /*Checks for if a next node is in the current JSON data if so it loads the
                        next set by recursively calling the function.*/
                        if (checkForNext(full_feed_response.getRawResponse())) {
                            //Reads the next page in the cursor
                            readFacebookImageFeed(after);
                        }
                        else {
                            /*If all of the items are loaded and the data feed is also complete the
                            * list is displayed. If the data feed is not complete then the image
                            * feed is marked as complete. */
                            if (feed_status.equals("finished")) {
                                 App.bus.post(new BusEventHandler(new String[]{"JSON Parse Complete",""}));
                            }
                            image_feed_status="finished";
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("limit", "25");
        parameters.putString("fields", "images");
        parameters.putString("after",after);
        full_feed_request.setParameters(parameters);
        full_feed_request.executeAsync();
    }

    //Get the "after" string from the current Facebook feed
    private String getAfter(String passed_data) {
        //Takes in the JSON data from the string and passes back the token for "after"
        JSONObject full_json_object = null;
        JSONObject paging_object =null;
        JSONObject cursor_object =null;
        String return_value="";
        if (passed_data==null) {
            return "";
        }
        //Read the data into the JSON object
        try {
            full_json_object = new JSONObject(passed_data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Grab the object paging
        try {
            if (full_json_object != null) {
                paging_object = full_json_object.getJSONObject("paging");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Grab the object cursors from inside of paging
        try {
            if (paging_object != null) {
                cursor_object = paging_object.getJSONObject("cursors");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Grab the string for after. If the data is malformed set it to ""
        try {
            if (cursor_object != null) {
                return_value = cursor_object.getString("after");
            }

        } catch (JSONException e) {
            return_value = "";
        }
        return return_value;
    }

    //Check to see if the current cursor page has a next field or if it's at the end.
    private Boolean checkForNext(String passed_data) {
        /*Takes in the JSON data from the URL passes back a boolean with true or false based on
        *the node next being present*/
        JSONObject full_json_object = null;
        JSONObject paging_object =null;
        JSONObject cursor_object =null;
        String next_value="";
        Boolean return_value=false;

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
                next_value = paging_object.getString("next");
                return_value=true;
            }

        } catch (JSONException e) {
            return_value=false;
        }
        return return_value;
    }

    //Parse the JSON from Facebook
    private void parseFeedJson (String passed_data, String type) {
        JSONObject full_json_object = null;
        JSONArray data_array=null;

        //Read in the full JSON object and parse out the data array. This is common to both cases

        try {
             full_json_object = new JSONObject(passed_data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Extract the data object
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

                //Pull the strings for the creation time, name and id from the JSON
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
                       //Object 0 is the highest resolution image
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
