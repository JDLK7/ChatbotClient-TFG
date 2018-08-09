package com.jdlk7.chatbottfg;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public abstract class MyActivity extends AppCompatActivity {

    /**
     * La url base del servicio web.
     */
    protected String baseUrl;

    /**
     * Instancia de SharedPreference.
     */
    protected SharedPrefManager sharedPrefManager;

    /**
     * Instancia de VolleySingleton.
     */
    protected VolleySingleton volleySingleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Se guarda la url base.
        baseUrl = getResources().getString(R.string.base_url);

        // Se obtiene la instancia de los Singleton en el contexto actual.
        volleySingleton = VolleySingleton.getInstance(this);
        sharedPrefManager = SharedPrefManager.getInstance(this);
    }

}
