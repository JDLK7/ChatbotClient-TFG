package com.jdlk7.chatbottfg;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jdlk7.chatbottfg.entities.Action;
import com.jdlk7.chatbottfg.entities.ButtonAction;
import com.jdlk7.chatbottfg.entities.RatingAction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

public class ChatActivity extends AppCompatActivity {

    /**
     * La url base del servicio web.
     */
    private String baseUrl;

    /**
     * Id to identity ACCESS_FINE_LOCATION permission request.
     */
    private static final int REQUEST_LOCATION = 7;

    // Listado con scroll
    private RecyclerView recyclerView;
    private MessageListAdapter messageListAdapter;
    private List<Message> messageList;

    private VolleySingleton volleySingleton;
    private SharedPrefManager sharedPrefManager;

    /**
     * Instancia de LoginData.
     */
    private LoginData loginData;

    EditText editText;
    RelativeLayout addBtn;
    Boolean flagFab = true;

    private boolean mayRequestLocation() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        requestPermissions(new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        /**
         * Se guarda la url base.
         */
        baseUrl = getResources().getString(R.string.base_url);

        /**
         * Se instancia el listado de mensajes como un ArrayList
         * y se añade un mensaje de bienvenida.
         */
        messageList = new ArrayList<Message>();
        messageList.add(new Message("Bienvenid@", false));

        recyclerView = (RecyclerView) findViewById(R.id.reyclerview_message_list);
        recyclerView.setHasFixedSize(true);

        /**
         * Se configura la RecyclerView como LinearLayout vertical,
         * para que se muestre un mensaje debajo de otro.
         */
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        /**
         * Se configura el adapter para que la RecyclerView sepa cómo dibujar los mensajes.
         */
        messageListAdapter = new MessageListAdapter(this, messageList);
        recyclerView.setAdapter(messageListAdapter);

        editText = (EditText)findViewById(R.id.edittext_chatbox);
        addBtn = (RelativeLayout)findViewById(R.id.button_chatbox_send);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = editText.getText().toString().trim();

                if (!message.equals("")) {
                    sendMessage(new Message(message, true), true, null);
                }

                editText.setText("");

            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ImageView fab_img = findViewById(R.id.fab_img);

                if (s.toString().trim().length() != 0 && flagFab) {
                    ImageViewAnimatedChange(ChatActivity.this, fab_img, R.drawable.ic_send_white_24dp);
                    flagFab = false;

                }
                else if (s.toString().trim().length() == 0) {
                    ImageViewAnimatedChange(ChatActivity.this, fab_img, R.drawable.ic_mic_white_24dp);
                    flagFab = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /**
         * Se obtiene la instancia de los Singleton en el contexto actual.
         */
        sharedPrefManager = SharedPrefManager.getInstance(this);
        volleySingleton = VolleySingleton.getInstance(this);
        loginData = LoginData.getInstance(this);

        mayRequestLocation();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();

        if (intent.hasExtra("hiddenMessage")) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("type", intent.getStringExtra("pointType"));

            String hiddenMessage = intent.getStringExtra("hiddenMessage");

            sendMessage(
                    new Message(hiddenMessage, true),
                    false,
                    params
            );
        }
    }

    /**
     * Envía un mensaje al servicio web y recoge su respuesta
     *
     * @param message
     */
    protected void sendMessage(final Message message, boolean isVisible, Map<String, String> params) {
        if (isVisible) {
            addMessage(message);
        }

        HashMap<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("driver", "web");
        requestParams.put("userId", loginData.getUser().getId());
        requestParams.put("message", message.getMessage());
        requestParams.put("interactive", "true");   // Para que funcione como si fuesen botones

        if (params != null) {
            for (int i = 0; i < params.size(); i++) {
                requestParams.putAll(params);
            }
        }

        JsonObjectRequest request = new JsonObjectRequest(baseUrl + "/api/botman", new JSONObject(requestParams),
        new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray botMessages = response.getJSONArray("messages");
                        for(int i = 0; i < botMessages.length(); i++) {
                            JSONObject botMessage = botMessages.getJSONObject(i);
                            Message message = new Message(botMessage.getString("text"), false);

                            if (botMessage.has("actions")) {
                                JSONArray actions = botMessage.getJSONArray("actions");

                                for (int j = 0; j < actions.length(); j++) {
                                    JSONObject messageAction = actions.getJSONObject(j);

                                    String type = messageAction.getString("type");
                                    final Action action;

                                    if (type.equals("button")) {
                                        action = new ButtonAction(messageAction.getString("text"),
                                                messageAction.getString("value"));
                                        ((ButtonAction) action).setActionListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                sendMessage(new Message(action.getValue(), true),
                                                        false, null);
                                            }
                                        });
                                    }
                                    else if (type.equals("rating")) {
                                        action = new RatingAction(messageAction.getString("value"));
                                        ((RatingAction) action).setActionListener(new RatingBar.OnRatingBarChangeListener() {
                                            @Override
                                            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                                                sendMessage(new Message(Float.toString(ratingBar.getRating()), true),
                                                        false, null);
                                            }
                                        });
                                    }
                                    else {
                                        throw new IllegalArgumentException("Tipo de acción indefinido");
                                    }

                                    message.getActions().add(action);
                                }
                            }

                            addMessage(message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer "
                        + sharedPrefManager.getString(SharedPrefManager.Key.ACCESS_TOKEN));

                return headers;
            }
        };
        volleySingleton.addToRequestQueue(request);
    }

    /**
     * Añade un nuevo mensaje al listado del chat
     *
     * @param message
     */
    public void addMessage(final Message message) {
        messageListAdapter.add(message);
        recyclerView.scrollToPosition(messageListAdapter.getItemCount() - 1);
    }

    public void ImageViewAnimatedChange(Context c, final ImageView v, final int newImageId) {
        final Drawable newImageDrawable = getResources().getDrawable(newImageId);

        final Animation anim_out = AnimationUtils.loadAnimation(c, R.anim.zoom_out);
        final Animation anim_in  = AnimationUtils.loadAnimation(c, R.anim.zoom_in);
        anim_out.setAnimationListener(new Animation.AnimationListener()
        {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationRepeat(Animation animation) {}
            @Override public void onAnimationEnd(Animation animation)
            {
                v.setImageDrawable(newImageDrawable);
                anim_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override public void onAnimationStart(Animation animation) {}
                    @Override public void onAnimationRepeat(Animation animation) {}
                    @Override public void onAnimationEnd(Animation animation) {}
                });
                v.startAnimation(anim_in);
            }
        });
        v.startAnimation(anim_out);
    }
}
