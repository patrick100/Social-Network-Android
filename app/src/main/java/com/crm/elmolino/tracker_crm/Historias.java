package com.crm.elmolino.tracker_crm;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by User on 2/28/2017.
 */

public class Historias extends Fragment {
    private static final String TAG = "Tab1Fragment";

    //private Button btnTEST;

    public View view;
    private Activity myactivity;


    public void AddActivity(final Activity activity) {
        myactivity = activity;
    }


    ArrayList<Post> posts;
    RecyclerView recyclerView;

    MyRecyclerViewAdapter adapter;

    public String username="";


    private void cargar_user(){
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        BufferedReader in = null;

        try {
            in = new BufferedReader(new FileReader(new File(myactivity.getExternalFilesDir(null), "config.txt")));
            while ((line = in.readLine()) != null) stringBuilder.append(line);

            username = stringBuilder.toString();


        }catch (NumberFormatException e) {
            Toast.makeText(myactivity, "Problema al leer config.txt", Toast.LENGTH_SHORT).show();
            //sendNotification("Problema al leer los datos");

        } catch (FileNotFoundException e) {
            Toast.makeText(myactivity, "CONFIGURACION NO ENCONTRADA", Toast.LENGTH_SHORT).show();
            //sendNotification("CONFIGURACION NO ENCONTRADA");

        } catch (IOException e) {
            Toast.makeText(myactivity, "NO SE PUEDE LEER EL ARCHIVO", Toast.LENGTH_SHORT).show();
            //sendNotification("NO SE PUEDE LEER EL ARCHIVO");
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.historias_fragment,container,false);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        cargar_user();
        DatabaseReference databaseReference = firebaseDatabase.getReference("USERS/"+username+"/DATA");

        databaseReference.keepSynced(true);

        // data to populate the RecyclerView with
        posts = new ArrayList<>();
        //animalNames.add("Horse");
        //animalNames.add("Cow");
        //animalNames.add("Camel");
        //animalNames.add("Sheep");
        //animalNames.add("Goat");

        // set up the RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(myactivity));
        adapter = new MyRecyclerViewAdapter(myactivity, posts);

        adapter.setUser(username);


        //adapter.setClickListener(myactivity);
        recyclerView.setAdapter(adapter);





        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {



                Log.d("Data2 " ,""+snapshot.getChildrenCount());
                posts.clear();
                //adapter.notifyDataSetChanged();


                for (DataSnapshot postSnapshot: snapshot.getChildren()) {


                    String id,image,description;
                    Long num_likes;
                    image = postSnapshot.child("image").getValue().toString();
                    description = postSnapshot.child("description").getValue().toString();


                    id = postSnapshot.getKey();
                    num_likes = Long.valueOf(postSnapshot.child("likes").getChildrenCount());

                    Post post = new Post(image,description);
                    post.setId(id);
                    post.setNum_likes(num_likes);

                    //Post post = postSnapshot.getValue(Post.class);

                    Log.d("Data ", image+" "+description);


                    Log.d("Data2", id);
                    posts.add(post);





                }

                Collections.reverse(posts);

                adapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: " ,firebaseError.getMessage());
            }
        });





        return view;
    }
}
