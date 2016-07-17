package langco.postwithimageviewer.Helpers;

import android.app.Application;
import android.content.Context;

import com.squareup.otto.Bus;

import java.util.ArrayList;

/**
 * Created by Langb_000 on 7/17/2016.
 */
public class App extends Application {
    private static ArrayList<String[]> parsed_feed = new ArrayList<>();
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

    public static void addToParsedFeed(String[] passed_array) {
        /* 0: Post date
        *  1: Post copy
        *  2: Image URL
        *  3: Post id */
        parsed_feed.add(passed_array);
    }



}