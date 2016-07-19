package langco.postwithimageviewer.RecyclerList;

/**
 * Created by Langb_000 on 7/17/2016.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import langco.postwithimageviewer.Helpers.App;
import langco.postwithimageviewer.Helpers.BusEventHandler;
import langco.postwithimageviewer.R;


public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.ViewHolder>{

    public PostListAdapter() {
        //Initialize the adapter
        //Registers this class into the Otto Library's bus
        App.bus.register(this);

    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mDate;
        TextView mPost;
        TextView mCurrentPost;
        ImageView mImageNameField;
        Button mLikeButton;

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
            String current_position= (String) mCurrentPost.getText();
            App.bus.post(new BusEventHandler(new String[]{"Launch Detail View",current_position}));
        }
    }



    @Override
    public PostListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //The purpose of this function is to select a XML file for the fragment and inflate it
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View postView =inflater.inflate(R.layout.feed_list_item, parent, false);
        return new ViewHolder(postView);
    }


    @Override
    public void onBindViewHolder(PostListAdapter.ViewHolder viewHolder, int position) {
        //Initial setup for the Recycler View
        Context context = App.getContext();
        ArrayList<String []> data_feed = App.getParsedFeed();
        String [] current_post = data_feed.get(position);
        ArrayList<String []> image_feed = App.getParsedImageFeed();
        String [] current_images = image_feed.get(position);
        TextView date_view = viewHolder.mDate;
        TextView post_view = viewHolder.mPost;
        TextView current_id_view = viewHolder.mCurrentPost;

        //TODO Convert date parsing to class
        String date=current_post[0];

        /*Switch the format of the date that's returned from Facebook in "yyyy-MM-dd'T'HH:mm:ssZ" to
         *"May 18 at 3:43 PM"
         *Parse the date using the Facebook format if there is no date or the parse fails
         *parsed_date will be null*/
        SimpleDateFormat facebook_date_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        Date parsed_date = null;
        try {
            parsed_date = facebook_date_format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        /* The date is converted to the new format. If the parsing failed for any reason then
         * the date that will be assigned to the field will be "" */
        SimpleDateFormat output_format = new SimpleDateFormat("MMMM d 'at' h:mm a");
        String final_date;
        if (parsed_date!=null) {
            final_date = output_format.format(parsed_date);
        } else {
            final_date = "";
        }

        date_view.setText(final_date);
        post_view.setText(current_post[1]);
        current_id_view.setText(String.valueOf(position));

        Picasso.with(context).load(current_images[0])
                .error(R.drawable.box)
                .placeholder(R.drawable.box)
                .into(viewHolder.mImageNameField, new Callback() {

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
