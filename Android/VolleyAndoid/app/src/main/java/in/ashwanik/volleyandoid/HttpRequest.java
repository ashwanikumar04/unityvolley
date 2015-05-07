package in.ashwanik.volleyandoid;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.unity3d.player.UnityPlayer;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    ProgressDialog progressDialog;
    int responseCode;
    IAsyncCallback callback;
    Response.Listener<JSONObject> jsonResponseListener;
    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            dismissProgressDialog();
            callback.onError(new ErrorDetail(error));
        }
    };
    private HashMap<String, String> params;
    private HashMap<String, String> headers;
    private String url;
    private Activity context;
    private String gameObject;

    public HttpRequest(Activity localActivity,
                       String url, String gameObjectName) {

        context = localActivity;
        this.url = url;
        gameObject = gameObjectName;
        headers = new HashMap<>();
        params = new HashMap<>();
        addJSONHeaders();
        setListeners();
    }


    public IAsyncCallback getCallback() {
        return callback;
    }

    public Context getContext() {
        return context;
    }

    private void addJSONHeaders() {
        AddHeader("Accept", "application/json");
        AddHeader("Content-type", "application/json");
    }

    private void setListeners() {
        jsonResponseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissProgressDialog();
                WebResponse backendResponse;
                try {
                    String responseString = response.toString();
                    backendResponse = new WebResponse(responseCode,
                            responseString);
                    if (responseString == null) {
                        callback.onError(new ErrorDetail(new Exception("Empty response returned")));
                    } else {
                        callback.onComplete(backendResponse);
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                    callback.onError(new ErrorDetail(exception));
                }
            }
        };
    }

    public void AddParam(String name, String value) {
        params.put(name, value);
    }

    public void AddHeader(String name, String value) {
        headers.put(name, value);
    }

    public void dismissProgressDialog() {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
    }

    public void execute() {
        this.callback = new IAsyncCallback() {
            @Override
            public void onComplete(WebResponse responseContent) {
                UnityPlayer.UnitySendMessage(gameObject, "OnComplete", responseContent.getResponse());
            }

            @Override
            public void onError(ErrorDetail errorData) {
                UnityPlayer.UnitySendMessage(gameObject, "OnError", errorData.toString());
            }
        };
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Please wait...");
                dismissProgressDialog();
                progressDialog.show();
            }
        });
        addToRequestQueue(VolleyManager.getInstance(context).getRequestQueue(), getJsonRequest());
    }

    public <X> void addToRequestQueue(RequestQueue requestQueue, Request<X> req) {
        requestQueue.add(req);
    }

    Request<JSONObject> getJsonRequest() {
        try {
            getUrl();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new JsonObjectRequest(Request.Method.GET, url, jsonResponseListener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };
    }

    void getUrl() throws UnsupportedEncodingException {
        String combinedParams = "";
        if (!params.isEmpty()) {
            combinedParams += "?";
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String paramString = entry.getKey() + "="
                        + URLEncoder.encode(entry.getValue(), "UTF-8");
                if (combinedParams.length() > 1) {
                    combinedParams += "&" + paramString;
                } else {
                    combinedParams += paramString;
                }
            }
        }
        url = url + combinedParams;
    }

}
