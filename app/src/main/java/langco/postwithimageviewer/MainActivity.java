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

package langco.postwithimageviewer;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.view.ViewGroup.LayoutParams;

import langco.postwithimageviewer.DetailView.DetailViewFragment;
import langco.postwithimageviewer.FacebookClasses.FacebookFeedRead;
import langco.postwithimageviewer.FacebookClasses.FacebookPostLike;
import langco.postwithimageviewer.Helpers.App;
import langco.postwithimageviewer.Helpers.BusEventHandler;
import langco.postwithimageviewer.RecyclerList.PostListFragment;

import com.facebook.FacebookSdk;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
//Facebook Hash: Yxcd3fAuo5i1cPSCAAuEImJtGIQ=
    //To get image http://graph.facebook.com/1083721161666716/picture where the middle is the ID
    public static final String API_KEY = "CAAAATT1YoUABAOT1GrQphXQx7RZBSpMjwUfwxTdkXFUsQZCILNqbKDIdPnDfXpvXAapK4P3GQpYj6hEooLEMbRLWZBWWN1rhmxEl0RLJochZBUQZA8mDiJsFpLEpVVXx1VdFDY3DuDqIDNlVG7YZA9lhvg6HmuqqmNPZARrHoQrr4ZCT8tkV6LJddcmTDzgnyD8X5djrihbQbgZDZD";
    public static final String APP_KEY = "331741700416";
    public static final String USER_KEY = "9339792";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Register the Otto bus library to this class
        App.bus.register(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        //Read in the full feed. The output will be stored in the class App
        FacebookFeedRead facebook_feed = new FacebookFeedRead(API_KEY,APP_KEY,USER_KEY);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Unregister the Otto bus library so that lingering traces don't stick in memory.
        App.bus.unregister(this);
        /*ArrayList<String[]> empty_parsed_feed = new ArrayList<>();
        ArrayList<String[]> empty_parsed_image_feed = new ArrayList<>();
        App.setParsedFeed(empty_parsed_feed);
        App.setParsedImageFeed(empty_parsed_image_feed);*/
    }

    //Using the Otto library to pass items across a bus. Implemented in App
    @Subscribe
    public void readBusCommunication(BusEventHandler event) {
        String [] passed_data=event.getParameter();
        String command = passed_data[0];
        String parameter = passed_data[1];
        switch (command) {
            case "JSON Parse Complete":
                //Create the new fragment
                PostListFragment post_list_fragment = new PostListFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, post_list_fragment);
                transaction.commitAllowingStateLoss();
                break;
            case "Launch Detail View":
                DetailViewFragment detail_fragment = new DetailViewFragment();
                Bundle args = new Bundle();
                args.putString("Index", parameter);
                detail_fragment.setArguments(args);
                FragmentTransaction detail_view_transaction = getSupportFragmentManager().beginTransaction();
                detail_view_transaction.replace(R.id.fragment_container, detail_fragment);
                detail_view_transaction.addToBackStack(null);
                detail_view_transaction.commitAllowingStateLoss();
                break;
            case "Launch Popup View":
                //Grab the index from the parameter and call the launchPopup function.
                int index = Integer.parseInt(parameter);
                launchPopup(index);
                break;
        }
    }

    private void launchPopup(final int index) {
        LayoutInflater layout_inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popup_layout =layout_inflater.inflate(R.layout.popup_window, null, false);
        final PopupWindow popup = new PopupWindow(popup_layout, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
        popup.showAtLocation(this.findViewById(R.id.fragment_container), Gravity.CENTER, 0  , 0);

        Button close_button = (Button) popup_layout.findViewById(R.id.popup_cancel);
        Button select_button = (Button) popup_layout.findViewById(R.id.popup_select);
       close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });

        select_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FacebookPostLike liked = new FacebookPostLike(index);
                liked.postLikeToFacebook(API_KEY,APP_KEY,USER_KEY);
                popup.dismiss();
            }
        });
    }

}
