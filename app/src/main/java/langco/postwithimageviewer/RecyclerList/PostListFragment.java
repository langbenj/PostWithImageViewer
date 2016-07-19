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


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import langco.postwithimageviewer.R;

public class PostListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater fragmentInflater, ViewGroup target, Bundle savedInstanceState) {
        View view= fragmentInflater.inflate(R.layout.feed_list_recycler_view, target, false);
        RecyclerView rvPosts = (RecyclerView) view.findViewById(R.id.feed_recycler);
        // Create adapter
        PostListAdapter adapter = new PostListAdapter();
        // Attach the adapter to the RecyclerView to populate items
        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }
}

