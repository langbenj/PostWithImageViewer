package langco.postwithimageviewer;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.squareup.otto.Subscribe;

import langco.postwithimageviewer.Helpers.App;
import langco.postwithimageviewer.Helpers.BusEventHandler;

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

        //Initialize Facebook communication and read in the full feed. The output will be stored in the class App
        FacebookSdk.sdkInitialize(getApplicationContext());
        FacebookFeedRead facebook_feed = new FacebookFeedRead(API_KEY,APP_KEY,USER_KEY);
    }


    //Using the Otto library to pass items across a bus. Implemented in App
    @Subscribe
    public void readBusCommunication(BusEventHandler event) {
        String command=event.getParameter();

        switch (command) {
            case "JSON Parse Complete":
                Toast.makeText(App.getContext(), command, Toast.LENGTH_LONG).show();
                //Create the new fragment
                PostListFragment post_list_fragment = new PostListFragment();
                //Need to pass an int value of the clicked Team into the fragment. Need to find it based on the team name.

                //Create the fragment and launch it
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, post_list_fragment);
                transaction.addToBackStack(null);
                transaction.commitAllowingStateLoss();
                break;
        }
    }

}
