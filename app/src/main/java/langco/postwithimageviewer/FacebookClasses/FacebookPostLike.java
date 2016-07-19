package langco.postwithimageviewer.FacebookClasses;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import langco.postwithimageviewer.Helpers.App;

/**
 * Created by Langb_000 on 7/18/2016.
 */
public class FacebookPostLike {

    String id;

    public FacebookPostLike (int index) {
        String [] current_post = App.getParsedFeed().get(index);
        id = current_post[2];
        int i=0;

    }

    public void postLikeToFacebook (String api_key, String app_key,String user_key) {
        AccessToken accessToken = new AccessToken(api_key,app_key,user_key,null,null,null,null,null);
        new GraphRequest(
                accessToken,
                "/"+id+"/likes",
                null,
                HttpMethod.POST,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        int i=0;
                    }
                }
        ).executeAsync();
    }
}
