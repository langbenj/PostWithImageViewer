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

package langco.postwithimageviewer.DetailView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import langco.postwithimageviewer.Helpers.DateFormat;
import langco.postwithimageviewer.Helpers.App;
import langco.postwithimageviewer.R;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailViewFragment extends Fragment {

    public View onCreateView(LayoutInflater fragmentInflater, ViewGroup target, Bundle savedInstanceState) {
        View view = fragmentInflater.inflate(R.layout.detail_view, target, false);
        TextView date_view = (TextView) view.findViewById(R.id.date);
        TextView post_view = (TextView) view.findViewById(R.id.post);
        ImageView image_detail_view = (ImageView) view.findViewById(R.id.photo);

        //Grab the current item number from the Bundle
        Bundle bundle = this.getArguments();
        int detail_index = Integer.parseInt(bundle.getString("Index"));

        //Load the correct info based on the index
        ArrayList<String []> data_feed = App.getParsedFeed();
        String [] current_post = data_feed.get(detail_index);
        ArrayList<String []> image_feed = App.getParsedImageFeed();
        String [] current_images = image_feed.get(detail_index);

        //Format the date and load it into the field
        String date=current_post[0];
        DateFormat date_formatter = new DateFormat();
        String final_date = date_formatter.formatDate(date);
        date_view.setText(final_date);

        //Set the text of the post
        post_view.setText(current_post[1]);

        //Load the image using Picasso
        Picasso.with(App.getContext()).load(current_images[1]).into(image_detail_view, new Callback() {
                    @Override
                    public void onSuccess() {
                    }
                    @Override
                    public void onError() {
                        Toast.makeText(App.getContext(), "Image Loading Failed", Toast.LENGTH_LONG).show();
                    }
                });

        return view;


    }

}