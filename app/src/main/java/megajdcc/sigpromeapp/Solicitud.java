package megajdcc.sigpromeapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Jnatn'h on 9/7/2018.
 */

public class Solicitud {
    private static Solicitud mInstance;
    private RequestQueue mR;
    private static Context mContext;

    private Solicitud(Context context) {
        mContext = context;
        mR = getRequestQueue();
    }

    public static synchronized  Solicitud getInstance(Context context){
        if(mInstance == null){
            mInstance = new Solicitud(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue(){
        if(mR == null){
            mR = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mR;
    }
    public <T> void addToRequestQueue(Request rq){
        getRequestQueue().add(rq);
    }
}
