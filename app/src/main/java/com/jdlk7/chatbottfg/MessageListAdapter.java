package com.jdlk7.chatbottfg;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jdlk7.chatbottfg.entities.Action;
import com.jdlk7.chatbottfg.entities.ButtonAction;
import com.jdlk7.chatbottfg.entities.RatingAction;

import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private List<Message> mMessageList;
    private Context mContext;

    public MessageListAdapter(Context context, List<Message> messageList) {
        mContext = context;
        mMessageList = messageList;
    }

    public void add(Message message) {
        this.mMessageList.add(message);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        Message message = (Message) mMessageList.get(position);

        if (message.isUserMsg()) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.my_message, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.their_message, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = (Message) mMessageList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.message_body);
        }

        void bind(Message message) {
            messageText.setText(message.getMessage());
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, nameText;
        LinearLayout messageActions;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.message_body);
            nameText = (TextView) itemView.findViewById(R.id.message_name);
            messageActions = (LinearLayout) itemView.findViewById(R.id.message_actions);
        }

        void bind(Message message) {
            messageText.setText(message.getMessage());
            nameText.setText("Botman");
            messageActions.removeAllViews();

            for (Action action : message.getActions()) {
                String type = action.getType();

                if (type.equals("button")) {
                    Button actionButton = (Button) LayoutInflater.from(mContext).inflate(
                            R.layout.their_message_action, messageActions, false);
                    actionButton.setText(action.getText());
                    actionButton.setOnClickListener(((ButtonAction) action).getActionListener());
                    messageActions.addView(actionButton);
                }
                else if (type.equals("rating")) {
                    RatingBar ratingBar = (RatingBar) LayoutInflater.from(mContext).inflate(
                            R.layout.rating_action, messageActions, false);
                    ratingBar.setOnRatingBarChangeListener(((RatingAction) action).getActionListener());
                    messageActions.addView(ratingBar);
                }
            }
        }
    }
}
