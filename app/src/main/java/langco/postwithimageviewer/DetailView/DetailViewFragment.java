package langco.postwithimageviewer.DetailView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import langco.postwithimageviewer.R;


public class DetailViewFragment extends Fragment {


    public View onCreateView(LayoutInflater fragmentInflater, ViewGroup target, Bundle savedInstanceState) {
        View view = fragmentInflater.inflate(R.layout.detail_view, target, false);
        TextView date_view = (TextView) view.findViewById(R.id.date);
        TextView post_view = (TextView) view.findViewById(R.id.post);

        ImageView image_detail_view = (ImageView) view.findViewById(R.id.photo);

        Bundle bundle = this.getArguments();
        int detail_index = Integer.parseInt(bundle.getString("Index"));
        ArrayList<String []> data_feed = App.getParsedFeed();
        String [] current_post = data_feed.get(detail_index);
        ArrayList<String []> image_feed = App.getParsedImageFeed();
        String [] current_images = image_feed.get(detail_index);

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


        Picasso.with(App.getContext()).load(current_images[1])
                .error(R.drawable.box)
                .placeholder(R.drawable.box)
                .into(image_detail_view, new Callback() {

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