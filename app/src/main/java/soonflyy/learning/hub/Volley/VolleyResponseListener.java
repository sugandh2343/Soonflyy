package soonflyy.learning.hub.Volley;

import org.json.JSONException;

public interface VolleyResponseListener {
    void onResponse(int requestType, String response) throws JSONException;
}
