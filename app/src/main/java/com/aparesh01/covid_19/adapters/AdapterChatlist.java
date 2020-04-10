package com.aparesh01.covid_19.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aparesh01.covid_19.ChartboxActivity;
import com.aparesh01.covid_19.Chatlist;
import com.aparesh01.covid_19.R;
import com.aparesh01.covid_19.doc.DoctorsList;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class AdapterChatlist extends RecyclerView.Adapter<AdapterChatlist.MyHolder>{

    Context context;
    List<DoctorsList> userList;

    public AdapterChatlist(Context context, List<DoctorsList> userList){

        this.context = context;
        this.userList = userList;

    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_chatlist,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        final String docUid = userList.get(position).getUid();
        String docImage = userList.get(position).getImage();
        String docName = userList.get(position).getName();
        String docEmail = userList.get(position).getEmail();

        holder.name.setText(docName);
        holder.email.setText(docEmail);
        try{

            Picasso.get().load(docImage).into(holder.profiledp);


        }catch(Exception e){}
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChartboxActivity.class);
                intent.putExtra("docUid",docUid);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        ImageView profiledp;
        TextView name,email;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            profiledp = itemView.findViewById(R.id.profilepic_chatlist);
            name = itemView.findViewById(R.id.name_chatlist);
            email = itemView.findViewById(R.id.email_chatlist);

        }
    }
}
