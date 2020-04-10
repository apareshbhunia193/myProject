package com.aparesh01.covid_19.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aparesh01.covid_19.ModelChat;
import com.aparesh01.covid_19.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.MyHolder>{

    private static final int MSG_TYPE_LEFT = 0 ;
    private static final int MSG_TYPE_RIGHT = 1;
    Context context;
    List<ModelChat> chatList;
    String imageUrl;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    public AdapterChat(Context context,List<ModelChat> chatList,String imageUrl){

        this.chatList = chatList;
        this.context = context;
        this.imageUrl = imageUrl;

    }
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(context).inflate(R.layout.roe_chat_right,parent,false);
            return new MyHolder(view);
        }
        else{
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_left,parent,false);
            return new MyHolder(view);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        String message = chatList.get(position).getMessage();
        String timeStemp = chatList.get(position).getTimestamp();

        Calendar c = Calendar.getInstance(Locale.ENGLISH);
        c.setTimeInMillis(Long.parseLong(timeStemp));
        String dateTime = DateFormat.format("dd MM, yyyy hh:mm:ss",c).toString();

        holder.messageTV.setText(message);
        holder.timeTv.setText(dateTime);
        try{
            Picasso.get().load(imageUrl).into(holder.profiledp);

        }catch(Exception e){


        }
        if(position == chatList.size() -1){
            if(chatList.get(position).isSeen()){

                holder.isSeenTv.setText("Seen");
            }
            else {

                holder.isSeenTv.setText("Send");
            }

        }
        else {

            holder.isSeenTv.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        firebaseAuth =FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if(chatList.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;

        }
        else{

            return MSG_TYPE_LEFT;
        }

    }

    class MyHolder extends RecyclerView.ViewHolder{

        ImageView profiledp;
        TextView messageTV,timeTv,isSeenTv;
        public MyHolder(@NonNull View itemView) {
            super(itemView);

            profiledp = itemView.findViewById(R.id.dpLeft);
            messageTV = itemView.findViewById(R.id.msgLeft);
            timeTv = itemView.findViewById(R.id.timeLeft);
            isSeenTv = itemView.findViewById(R.id.seenstatusL);

        }
    }
}
