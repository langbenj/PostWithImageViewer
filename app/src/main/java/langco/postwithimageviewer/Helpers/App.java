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

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.squareup.otto.Bus;

import java.util.ArrayList;

public class App extends Application {
    private static ArrayList<String[]> parsed_feed = new ArrayList<>();
    private static ArrayList<String[]> parsed_image_feed = new ArrayList<>();
    public static final Bus bus = new Bus();
    private static Application sApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }

    public static Application getApplication() {
        return sApplication;
    }

    public static ArrayList<String[]> getParsedFeed() {
        return parsed_feed;
    }

    public static void setParsedFeed(ArrayList<String[]> parsed_feed) {
        App.parsed_feed = parsed_feed;
    }

    public static void addToParsedFeed(String[] passed_array) {
        /* 0: Post date
        *  1: Post copy
        *  2: Post id */

        parsed_feed.add(passed_array);
    }

    public static ArrayList<String[]> getParsedImageFeed() {
        return parsed_image_feed;
    }

    public static void setParsedImageFeed(ArrayList<String[]> parsed_image_feed) {
        App.parsed_image_feed = parsed_image_feed;
    }

    public static void addToParsedImageFeed(String[] passed_array) {
        /* 0: Thumbnail URL
        *  1: Full Image URL */
        parsed_image_feed.add(passed_array);
    }

}