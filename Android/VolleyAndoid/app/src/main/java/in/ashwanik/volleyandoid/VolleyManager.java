package in.ashwanik.volleyandoid;


import android.app.Activity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyManager {

    private static VolleyManager instance;
    private RequestQueue mRequestQueue;

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }


    public synchronized static VolleyManager getInstance(Activity activity) {
        if (instance == null) {
            instance = new VolleyManager();
            instance.mRequestQueue = Volley.newRequestQueue(activity);
        }
        return instance;
    }
}
