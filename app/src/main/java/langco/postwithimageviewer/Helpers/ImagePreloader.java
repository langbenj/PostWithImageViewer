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

package langco.postwithimageviewer.Helpers;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;



public class ImagePreloader {
    ArrayList<Integer> old_triggers = new ArrayList<>();
    int LOAD_TRIGGER=15;
    public ImagePreloader() {
        //Add the first index to old triggers so images aren't loaded again if you scroll to the top
        old_triggers.add(0);
        //Load the first LOAD_TRIGGER images
        preloadNextBatch(0);
    }

    //Check to see if the current index is a multiple of LOAD_TRIGGER and if it has been loaded
    public void checkForNextLoad(int indexNumber) {
        if (indexNumber%LOAD_TRIGGER==0) {
            boolean need_to_load = true;
            /*Old Triggers holds a list of the indexes that have triggered a load. This is to check
             *if the images have already been cached and not load them again.
             */
            for (int i=0; i<old_triggers.size(); i++) {
                if (indexNumber==old_triggers.get(i)) {
                    need_to_load=false;
                }
            }
            //If the indexNumber is a multiple of LOAD_TRIGGER and it hasn't been used load the next images
            if (need_to_load) {
                preloadNextBatch(indexNumber);
            }
        }
    }

    //Loads the next LOAD_TRIGGER images
    public void preloadNextBatch(int indexNumber) {
        //Pull the parsed image list from the App
        ArrayList<String[]> parsed_images = App.getParsedImageFeed();
        String [] current_image_node;
        //Check to see if the indexNumber+LOAD_TRIGGER > the array size. This prevents array reference errors
        int upper_limit;
        if ((indexNumber+LOAD_TRIGGER)>parsed_images.size()) {
            upper_limit=parsed_images.size();
        }
        else {
            //Set the end point of the loop
            upper_limit = indexNumber + LOAD_TRIGGER;
        }
        //Loop through the images indexNumber -> upper_limit and fetch them using Picasso
        for (int i=indexNumber; i<upper_limit; i++) {
            current_image_node = parsed_images.get(i);
            Picasso.with(App.getContext()).load(current_image_node[0]).fetch();
        }
    }
}
