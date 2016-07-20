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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormater {
    public DateFormater() {

    }

    /*Switch the format of the date that's returned from Facebook in "yyyy-MM-dd'T'HH:mm:ssZ" to
     *"May 18 at 3:43 PM"
     */

    public String formatDate(String date) {
        /*Parse the date using the Facebook format if there is no date or the parse fails
         *parsed_date will be null
         */
        SimpleDateFormat facebook_date_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        Date parsed_date = null;
        try {
            parsed_date = facebook_date_format.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        /* Checks to see if the current locale is US,China,Koreas,Taiwan,Hungary,Iran,Japan,Lithuania
         * All of these have the month in front of the date. Otherwise sets the date in front of the month.
         */
        String current_locale=Locale.getDefault().toString();
        SimpleDateFormat output_format;
        if (current_locale.equals("en_US") || current_locale.equals("zh_CN") || current_locale.equals("ko_KR")
                || current_locale.equals("zh_TW") || current_locale.equals("hu_HU") || current_locale.equals("fa_IR")
                || current_locale.equals("ja_JP") || current_locale.equals("lt-LT")) {
            output_format = new SimpleDateFormat("MMMM d 'at' h:mm a");
        }
        else {
            output_format = new SimpleDateFormat("d MMMM 'at' h:mm a");
        }
        /* The date is converted to the new format. If the parsing failed for any reason then
         * the date that will be assigned to the field will be ""
         */
        String final_date;
        if (parsed_date!=null) {
            final_date = output_format.format(parsed_date);
        } else {
            final_date = "";
        }
        return final_date;
    }
}
