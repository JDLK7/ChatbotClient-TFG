package com.jdlk7.chatbottfg;

import android.Manifest;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    // Listado con scroll
    private RecyclerView recyclerView;
    private MessageListAdapter messageListAdapter;
    private List<Message> messageList;

    EditText editText;
    RelativeLayout addBtn;
    Boolean flagFab = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messageList = new ArrayList<Message>();
        messageList.add(new Message("Bienvenid@", false));

        // Pide permiso para utilizar el microfono
        // ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},1);

        recyclerView = (RecyclerView) findViewById(R.id.reyclerview_message_list);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        messageListAdapter = new MessageListAdapter(this, messageList);
        recyclerView.setAdapter(messageListAdapter);

        editText = (EditText)findViewById(R.id.edittext_chatbox);
        addBtn = (RelativeLayout)findViewById(R.id.button_chatbox_send);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = editText.getText().toString().trim();

                if (!message.equals("")) {
                    messageListAdapter.add(new Message(message, true));
                    recyclerView.scrollToPosition(messageListAdapter.getItemCount() - 1);
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
