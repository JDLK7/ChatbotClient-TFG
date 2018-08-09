package com.jdlk7.chatbottfg;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.jdlk7.chatbottfg.services.TrackingService;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MainActivity extends MyActivity {

    /**
     * Keep track of the token refresh task to ensure we can cancel it if requested.
     */
    private TokenRefreshTask mTokenRefreshTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        attemptTokenRefresh();
    }

    /**
     * Intenta refrescar el token de autenticación para
     * poder entrar sin iniciar sesión de nuevo.
     */
    private void attemptTokenRefresh() {
        if (mTokenRefreshTask != null) {
            return;
        }

        mTokenRefreshTask = new TokenRefreshTask(this);
        mTokenRefreshTask.execute((Void) null);
    }

    /**
     * Representa una tarea asincrona para comprobar si
     * el token es válido y refrescarlo en dicho caso
     */
    public class TokenRefreshTask extends AsyncTask<Void, Void, Boolean> {

        private final Context mContext;

        TokenRefreshTask(Context context) {
            mContext = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            final String accessToken = sharedPrefManager.getString(SharedPrefManager.Key.ACCESS_TOKEN);
            boolean isAuthenticated = false;

            if (accessToken != null) {

                RequestFuture<JSONObject> future = RequestFuture.newFuture();
                JsonObjectRequest request = new JsonObjectRequest(JsonObjectRequest.Method.POST,
                        baseUrl + "/api/auth/refresh", null, future, future)
                {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Authorization", "Bearer " + accessToken);
                        headers.put("Accept", "application/json");

                        return headers;
                    }
                };
                volleySingleton.addToRequestQueue(request);

                try {
                    JSONObject response = future.get(3, TimeUnit.SECONDS);

                    isAuthenticated = response.has("access_token");
                    if (!isAuthenticated) {
                        sharedPrefManager.remove(SharedPrefManager.Key.ACCESS_TOKEN);
                        return false;
                    }

                    sharedPrefManager.put(SharedPrefManager.Key.ACCESS_TOKEN, response.getString("access_token"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return isAuthenticated;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mTokenRefreshTask = null;

            if (success) {
                startActivity(new Intent(mContext, MapActivity.class));
                startService(new Intent(mContext, TrackingService.class));
            } else {
                startActivity(new Intent(mContext, LoginActivity.class));
            }

            finish();
        }

        @Override
        protected void onCancelled() {
            mTokenRefreshTask = null;

            startActivity(new Intent(mContext, LoginActivity.class));
            finish();
        }

    }
}
