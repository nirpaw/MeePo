package il.ac.hit.meepo.Adapters;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;

import il.ac.hit.meepo.Helpers.ChatViewHolders;
import il.ac.hit.meepo.Models.NewChatObject;
import il.ac.hit.meepo.R;

 public class NewChatAdapter extends RecyclerView.Adapter<ChatViewHolders>{
    private List<NewChatObject> chatList;

    public static final int MSG_TYPE_OTHER = 0;
    public static final int MSG_TYPE_ME = 1;
    private Context context;


    public NewChatAdapter(List<NewChatObject> matchesList, Context context){
        this.chatList = matchesList;
        this.context = context;
    }

    @Override
    public ChatViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType == MSG_TYPE_ME) {
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
        }
        else{
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
        }
        ChatViewHolders chatViewHolders = new ChatViewHolders(view);
        return chatViewHolders;
    }

    @Override
    public void onBindViewHolder(@NonNull  ChatViewHolders holder, int position) {
            holder.mMessage.setText(chatList.get(position).getMessage());
            String imageUrl = chatList.get(position).getProfilePic();
            if(imageUrl != null) {
                if (imageUrl.equals("default")) {
                    holder.mProfileImage.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(context).load(imageUrl).into(holder.mProfileImage);
                }
            }
            if(holder.mTime != null){
                holder.mTime.setText(chatList.get(position).getTime());
            }
            else {
                holder.mTime.setText("No time");
            }
    }

    @Override
    public int getItemCount() {
        return this.chatList.size();
    }


     @Override
     public int getItemViewType(int position) {
         if(chatList.get(position).getCurrentUser()){
             return  MSG_TYPE_ME;
         }
         else {
             return MSG_TYPE_OTHER;
         }
    }
 }