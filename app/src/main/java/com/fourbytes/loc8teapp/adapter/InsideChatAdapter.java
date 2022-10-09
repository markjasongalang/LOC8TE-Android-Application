package com.fourbytes.loc8teapp.adapter;

import static com.fourbytes.loc8teapp.InsideChatItems.layout_left;
import static com.fourbytes.loc8teapp.InsideChatItems.layout_right;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fourbytes.loc8teapp.InsideChatItems;
import com.fourbytes.loc8teapp.R;


import java.util.List;

public class InsideChatAdapter extends RecyclerView.Adapter {

    Context inside_chat_context;
    List<InsideChatItems> inside_chat_list;

    public InsideChatAdapter(Context inside_chat_context, List<InsideChatItems> inside_chat_list) {
        this.inside_chat_context = inside_chat_context;
        this.inside_chat_list = inside_chat_list;
    }

    @Override
    public int getItemViewType(int position) {
        switch(inside_chat_list.get(position).getViewType()){
            case 1:
                return layout_left;
            case 2:
                return layout_right;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case layout_left:
                View layoutLeft = LayoutInflater.from(inside_chat_context).inflate(R.layout.fragment_chat_inside_chat_items_left,parent,false);
                return new LeftChatViewHolder(layoutLeft);
            case layout_right:
                View layoutRight = LayoutInflater.from(inside_chat_context).inflate(R.layout.fragment_chat_inside_chat_items_right,parent,false);
                return new RightChatViewHolder(layoutRight);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch(inside_chat_list.get(position).getViewType()){
            case layout_left:
                String lmsg = inside_chat_list.get(position).getInside_chat_message();
                ((LeftChatViewHolder) holder).setTextLeft(lmsg);
                int limg = inside_chat_list.get(position).getInside_chat_image();
                ((LeftChatViewHolder) holder).setImgLeft(limg);
                break;
            case layout_right:
                String rmsg = inside_chat_list.get(position).getInside_chat_message();
                ((RightChatViewHolder) holder).setTextRight(rmsg);
                int rimg = inside_chat_list.get(position).getInside_chat_image();
                ((RightChatViewHolder) holder).setImgRight(rimg);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return inside_chat_list.size();
    }

    class LeftChatViewHolder extends RecyclerView.ViewHolder{

        public ImageView inchat_img_left;
        public TextView inchat_msg_left;

        public LeftChatViewHolder(@NonNull View itemView) {
            super(itemView);
            inchat_img_left = itemView.findViewById(R.id.insidechat_img_left);
            inchat_msg_left = itemView.findViewById(R.id.insidechat_msg_left);
        }

        private void setTextLeft(String text){
            inchat_msg_left.setText(text);
        }

        private void setImgLeft(int image){
            inchat_img_left.setImageResource(image);
        }
    }

    class RightChatViewHolder extends RecyclerView.ViewHolder{

        public ImageView inchat_img_right;
        public TextView inchat_msg_right;

        public RightChatViewHolder(@NonNull View itemView) {
            super(itemView);
            inchat_img_right = itemView.findViewById(R.id.insidechat_img_right);
            inchat_msg_right = itemView.findViewById(R.id.insidechat_msg_right);
        }

        private void setTextRight(String text){
            inchat_msg_right.setText(text);
        }

        private void setImgRight(int image){
            inchat_img_right.setImageResource(image);
        }
    }
}
