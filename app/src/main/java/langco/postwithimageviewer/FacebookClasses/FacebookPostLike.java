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

package langco.postwithimageviewer.FacebookClasses;

import langco.postwithimageviewer.Helpers.App;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

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
