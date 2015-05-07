package in.ashwanik.volleyandoid;


import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

public class ErrorDetail {
    private Exception exception;

    public ErrorDetail(Exception error) {
        exception = error;
    }

    @Override
    public String toString() {

        JSONObject jsonObject = new JSONObject();
        try {
            if (exception instanceof VolleyError) {
                VolleyError error = (VolleyError) exception;
                jsonObject.put("ErrorCode", error.networkResponse.statusCode);

            } else {
                jsonObject.put("ErrorCode", -1);
            }
            jsonObject.put("Message", exception.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
