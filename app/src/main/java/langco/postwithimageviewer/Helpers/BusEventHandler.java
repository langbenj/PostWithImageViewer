package langco.postwithimageviewer.Helpers;

/**
 * Created by Langb_000 on 7/17/2016.
 */

import java.util.ArrayList;

public class BusEventHandler {
    private String mParameter;

    //These bus events can probably be refactored into one or two events TODO
    public BusEventHandler (String passedParameters) {
        mParameter=passedParameters;
    }

    public String getParameter() {

        return mParameter;
    }
}
