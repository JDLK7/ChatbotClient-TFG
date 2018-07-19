package com.jdlk7.chatbottfg;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
import android.widget.RelativeLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    /**
     * La url base del servicio web.
     */
    private String baseUrl;

    // Listado con scroll
    private RecyclerView recyclerView;
    private MessageListAdapter messageListAdapter;
    private List<Message> messageList;

    /**
     * Instancia de VolleySingleton.
     */
    private VolleySingleton volleySingleton;

    /**
     * Instancia de LoginData.
     */
    private LoginData loginData;

    EditText editText;
    RelativeLayout addBtn;
    Boolean flagFab = true;

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
                    sendMessage(new Message(message, true));
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
        volleySingleton = VolleySingleton.getInstance(this);
        loginData = LoginData.getInstance(this);
    }

    /**
     * Envía un mensaje al servicio web y recoge su respuesta
     *
     * @param message
     */
    protected void sendMessage(final Message message) {
        addMessage(message);

        HashMap<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("driver", "web");
        requestParams.put("userId", loginData.getUser().getId());
        requestParams.put("message", message.getMessage());

        JsonObjectRequest request = new JsonObjectRequest(baseUrl + "/botman", new JSONObject(requestParams),
        new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray botMessages = response.getJSONArray("messages");
                        for(int i = 0; i < botMessages.length(); i++) {
                            JSONObject botMessage = botMessages.getJSONObject(i);
                            addMessage(new Message(botMessage.getString("text"), false));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });
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
