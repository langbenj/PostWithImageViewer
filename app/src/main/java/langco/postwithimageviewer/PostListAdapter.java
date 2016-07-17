package langco.postwithimageviewer;

/**
 * Created by Langb_000 on 7/17/2016.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import langco.postwithimageviewer.Helpers.App;
import langco.postwithimageviewer.Helpers.BusEventHandler;


public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.ViewHolder>{

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mDate;
        TextView mPost;
        ImageView mImageNameField;
        String mCurrentId;

        public ViewHolder(View itemView) {
            super(itemView);
            //Assign references to the TextViews in the XML layout. These will be used later to write info to the fields
            mDate = (TextView) itemView.findViewById(R.id.date);
            mPost = (TextView) itemView.findViewById(R.id.post);
            mImageNameField = (ImageView) itemView.findViewById(R.id.photo);


            //Setup the onClickListener to handle click events on each view (individual row)
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            App.bus.post(new BusEventHandler(mCurrentId));

        }
    }

    public PostListAdapter() {
        //Initialize the adapter
        //Registers this class into the Otto Library's bus
        App.bus.register(this);

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
        TextView date_view = viewHolder.mDate;
        TextView post_view = viewHolder.mPost;
        date_view.setText(current_post[0]);
        post_view.setText(current_post[1]);

    }

    @Override
    public int getItemCount() {
        //This required function determines how long the Recycler List is.
        return App.getParsedFeed().size();
    }
}
