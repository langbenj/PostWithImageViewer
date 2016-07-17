package langco.postwithimageviewer;

/**
 * Created by Langb_000 on 7/17/2016.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import langco.postwithimageviewer.Helpers.App;


public class PostListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater fragmentInflater, ViewGroup target, Bundle savedInstanceState) {

        View view= fragmentInflater.inflate(R.layout.feed_list_recycler_view, target, false);
        RecyclerView rvPosts = (RecyclerView) view.findViewById(R.id.feed_recycler);
        // Create adapter passing in the sample user data
        PostListAdapter adapter = new PostListAdapter();
        // Attach the adapter to the RecyclerView to populate items
        rvPosts.setAdapter(adapter);

        rvPosts.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }







}

