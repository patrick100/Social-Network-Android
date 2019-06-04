package com.crm.elmolino.tracker_crm;

import android.content.Context;
import android.content.Intent;
import android.os.Debug;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;


public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private List<Post> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;

    ViewHolder holder_actual;
    int actual_position;


    //Firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference imageref;
    DatabaseReference imgref;

    DatabaseReference commentref;

    String username;
    String actual_imageid;

    Historias hist;
    int pos_actual=0;



    void setUser(String user){

        username = user;
    }

    // data is passed into the constructor
    MyRecyclerViewAdapter(Context context, List<Post> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
        //this.username = data.get(0).getUsername();
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.post_item, parent, false);

        hist = new Historias();

        firebaseDatabase = FirebaseDatabase.getInstance();
        imageref = firebaseDatabase.getReference("USERS/"+username+"/DATA");

        imageref.keepSynced(true);


        view.findViewById(R.id.like).setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {


                String id  = (String)v.getTag(R.id.tag_like);


                //Toast.makeText(context, "Dando like image id: "+ mData.get(getAdapterPosition()).getId(), Toast.LENGTH_SHORT).show();


                imgref = imageref.child(id).child("likes");
                //imgref = imageref.child(username);
                //imgref.setValue(1);
                //imgref.setValue(username);
                //imgref.push().setValue(username,1);

                imgref.push().setValue(username);

                //.child(username);
                //imgref.setValue(1);

                //Toast.makeText(context, "like en: "+username+"  "+imgref.toString(), Toast.LENGTH_SHORT).show();

                //Log.d("Data ", "click image id: "+id);

            }
        });


        view.findViewById(R.id.comentar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(context, "Comentando", Toast.LENGTH_SHORT).show();
                String id  = (String)v.getTag(R.id.tag_comment);
                String img_url  = (String)v.getTag(R.id.tag_img_url);



                Intent i = new Intent(context,Comentarios.class);
                i.putExtra("id",id);
                i.putExtra("img_url",img_url);
                i.putExtra("username",username);
                context.startActivity(i);



                //commentref = imageref.child(id).child("comments");

                //commentref.child(username).setValue("jeje");
                //commentref.push().setValue(username);

            }
        });


        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //String animal = mData.get(position);
        //holder.myTextView.setText(animal);

        holder.description.setText(mData.get(position).getDescription());

        holder_actual = holder;
        actual_position = position;
        Picasso.with(context).load(mData.get(position).getImage()).into(holder.img_url);

        /*
        Picasso.with(context).load(mData.get(position).getImage()).networkPolicy(NetworkPolicy.OFFLINE).into(holder.img_url,
                new Callback() {
                    @Override
                    public void onSuccess() {
                        Picasso.with(context).load(mData.get(actual_position).getImage()).into(holder_actual.img_url);
                    }

                    @Override
                    public void onError() {
                        Picasso.with(context).load(mData.get(actual_position).getImage()).into(holder_actual.img_url);
                    }
                });
        */


        holder.like.setTag(R.id.tag_like,mData.get(position).getId());

        holder.num_likes.setText(String.valueOf(mData.get(position).getNum_likes()));

        holder.comentarios.setTag(R.id.tag_comment,mData.get(position).getId());
        holder.comentarios.setTag(R.id.tag_img_url,mData.get(position).getImage());


    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView description;
        ImageView img_url;
        ImageButton like;
        TextView num_likes;
        TextView comentarios;



        ViewHolder(View itemView) {
            super(itemView);

            description = itemView.findViewById(R.id.txt_content);
            img_url = itemView.findViewById(R.id.txt_image);
            like = itemView.findViewById(R.id.like);
            num_likes = itemView.findViewById(R.id.num_likes);

            comentarios = itemView.findViewById(R.id.comentar);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            //pos_actual = getAdapterPosition();
            //Toast.makeText(context, "IMAGE ID "+ mData.get(pos_actual).getId(), Toast.LENGTH_SHORT).show();

            //if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Post getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}