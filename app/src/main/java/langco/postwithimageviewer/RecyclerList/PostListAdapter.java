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

package langco.postwithimageviewer.RecyclerList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import langco.postwithimageviewer.Helpers.DateFormater;
import langco.postwithimageviewer.Helpers.App;
import langco.postwithimageviewer.Helpers.BusEventHandler;
import langco.postwithimageviewer.Helpers.ImagePreloader;
import langco.postwithimageviewer.R;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.ViewHolder>{

    ImagePreloader preloader = new ImagePreloader();
    int MAX_POST_LENGTH=100;

    public PostListAdapter() {
        //Initialize the adapter
        //Registers this class into the Otto Library's bus
        App.bus.register(this);

    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mDate;
        TextView mPost;
        Button mLikeButton;
        TextView mCurrentPost;
        ImageView mImageNameField;


        public ViewHolder(View itemView) {
            super(itemView);
            //Assign references to the TextViews in the XML layout. These will be used later to write info to the fields
            mDate = (TextView) itemView.findViewById(R.id.date);
            mPost = (TextView) itemView.findViewById(R.id.post);
            mCurrentPost = (TextView) itemView.findViewById(R.id.current_id);
            mImageNameField = (ImageView) itemView.findViewById(R.id.photo);
            mLikeButton = (Button) itemView.findViewById(R.id.like_button);

            //Setup the onClickListener to handle click events on each view (individual row)
            itemView.setOnClickListener(this);

            //Handle clicks on the like button by calling the main activity to launch the popup.
            mLikeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String current_position= (String) mCurrentPost.getText();
                    App.bus.post(new BusEventHandler(new String[]{"Launch Popup View",current_position}));
                }
            });

        }

        @Override
        public void onClick(View v) {
            //Clicking on the view will open the detail view. Otto calls the Main Activity
            String current_position= (String) mCurrentPost.getText();
            App.bus.post(new BusEventHandler(new String[]{"Launch Detail View",current_position}));
        }
    }



    @Override
    public PostListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Select a XML file for the fragment and inflate it
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View postView =inflater.inflate(R.layout.feed_list_item, parent, false);
        return new ViewHolder(postView);
    }


    @Override
    public void onBindViewHolder(PostListAdapter.ViewHolder viewHolder, int position) {

        //Initial setup for the Recycler View
        Context context = App.getContext();

        //Checks to see if the current line is divisible by 15 and if so kicks off the preloader
        preloader.checkForNextLoad(position);

        //Load the row's information based on the position
        ArrayList<String []> data_feed = App.getParsedFeed();
        String [] current_post = data_feed.get(position);
        ArrayList<String []> image_feed = App.getParsedImageFeed();
        String [] current_images = image_feed.get(position);

        TextView date_view = viewHolder.mDate;
        TextView post_view = viewHolder.mPost;
        TextView current_id_view = viewHolder.mCurrentPost;

        //Format the date and load it into the field
        String date=current_post[0];
        DateFormater date_formatter = new DateFormater();
        String final_date = date_formatter.formatDate(date);
        date_view.setText(final_date);

        //If the post is more than 100 characters long it is trimmed and ... is added at the end
        String post_string = current_post[1];
        if (post_string.length()>MAX_POST_LENGTH) {
            post_string=post_string.substring(0,MAX_POST_LENGTH)+"...";
        }

        //Load the image into the mImageNameField using Picasso. 15 images at a time should be preloaded.
        post_view.setText(post_string);
        current_id_view.setText(String.valueOf(position));

        Picasso.with(context).load(current_images[0]).placeholder(R.drawable.box).into(viewHolder.mImageNameField, new Callback() {
            @Override
                    public void onSuccess() {
                    }
                    @Override
                    public void onError() {
                        Toast.makeText(App.getContext(), "Image Loading Failed", Toast.LENGTH_LONG).show();
                    }
                });
         }

    @Override
    public int getItemCount() {
        //This required function determines how long the Recycler List is.
        return App.getParsedFeed().size();
    }
}
