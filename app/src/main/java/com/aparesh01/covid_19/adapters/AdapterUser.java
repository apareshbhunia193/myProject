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
import com.aparesh01.covid_19.R;
import com.aparesh01.covid_19.doc.DoctorsList;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterUser extends  RecyclerView.Adapter<AdapterUser.NewHolder>{

    Context context;
    List<DoctorsList> doctorsLists;

    public AdapterUser(Context con,List<DoctorsList> dl) {
        context = con;
        doctorsLists =dl;

    }

        @NonNull
        @Override
        public NewHolder onCreateViewHolder (@NonNull ViewGroup parent,int viewType){
            View view = LayoutInflater.from(context).inflate(R.layout.doc_row, parent,false);

            return new NewHolder(view);
        }

        @Override
        public void onBindViewHolder (@NonNull NewHolder holder,int position){
            final String docUID = doctorsLists.get(position).getUid();
            String docImage = doctorsLists.get(position).getImage();
            String docName = doctorsLists.get(position).getName();
            final String docEmail = doctorsLists.get(position).getEmail();

//            String checkDoc = doctorsLists.get(position).getJob();

                holder.email.setText(docEmail);
                holder.nam.setText(docName);

                try {

                    Picasso.get().load(docImage).placeholder(R.drawable.ic_add_a_photo).into(holder.dp);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(context, "" + docEmail, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(view.getContext(), ChartboxActivity.class);
                    intent.putExtra("docUid",docUID);
                    view.getContext().startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount () {
            return doctorsLists.size();
        }
       static class NewHolder extends RecyclerView.ViewHolder {

            ImageView dp;
            TextView nam;
            TextView email;

            NewHolder(@NonNull View itemView) {
                super(itemView);
                dp = itemView.findViewById(R.id.docdp);
                nam = itemView.findViewById(R.id.docnam);
                email = itemView.findViewById(R.id.docemail);
            }
        }
}
