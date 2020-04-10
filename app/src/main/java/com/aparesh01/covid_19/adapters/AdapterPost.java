package com.aparesh01.covid_19.adapters;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aparesh01.covid_19.ModelPost;
import com.aparesh01.covid_19.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterPost extends RecyclerView.Adapter<AdapterPost.MyHolder>{

    Context context;
    List<ModelPost> postList;

    public AdapterPost(){

    }
    public AdapterPost(Context context, List<ModelPost> postList){
        this.context = context;
        this.postList = postList;

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_posts,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        String uid = postList.get(position).getUid();
        String uEmail = postList.get(position).getuEmail();
        String uName = postList.get(position).getuName();
        String uDp = postList.get(position).getuDp();
        String pId = postList.get(position).getpId();
        String pTitle = postList.get(position).getpTitle();
        String pDesc = postList.get(position).getpDesc();
        String pImage = postList.get(position).getpImage();

        holder.uName.setText(uName);
        holder.pTitle.setText(pTitle);
        holder.pDesc.setText(pDesc);

        try{

            Picasso.get().load(uDp).into(holder.uPic);

        }catch(Exception e){}

        if (pImage.equals("NoImage")){

            holder.pImage.setVisibility(View.GONE);
        }
        else {
            try {

                Picasso.get().load(pImage).into(holder.pImage);

            } catch (Exception e) {
            }
        }

        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "It's working", Toast.LENGTH_SHORT).show();
            }
        });
        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "This Post Report as a Useful ", Toast.LENGTH_SHORT).show();
            }
        });
        holder.reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Tis Post Report as a Fake ", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return postList.size();
    }


    class MyHolder extends RecyclerView.ViewHolder{

        ImageView uPic, pImage;
        TextView uName,pTitle,pDesc;
        ImageButton moreBtn;
        Button likeBtn,reportBtn;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            uPic = itemView.findViewById(R.id.uPicture);
            pImage = itemView.findViewById(R.id.home_main_post);
            uName = itemView.findViewById(R.id.uName);
            pTitle = itemView.findViewById(R.id.home_post_title);
            pDesc = itemView.findViewById(R.id.home_post_desc);
            moreBtn = itemView.findViewById(R.id.moreBtn);
            likeBtn = itemView.findViewById(R.id.home_usebtn);
            reportBtn = itemView.findViewById(R.id.home_report_btn);

        }
    }
}
