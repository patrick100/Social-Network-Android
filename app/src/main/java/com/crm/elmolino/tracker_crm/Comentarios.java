package com.crm.elmolino.tracker_crm;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

public class Comentarios extends AppCompatActivity {


    String img_url;
    String img_id;
    String username;

    ImageView img_comment;
    TextView comment;
    ImageButton upload_comment;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference commentref;

    ListView lista;

    public ArrayList<String> comentarios;
    ArrayAdapter<String> modeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentarios);

        lista = (ListView) findViewById(R.id.comments);


        Bundle extras = getIntent().getExtras();
        if(extras !=null)
        {
            img_id = extras.getString("id");
            img_url = extras.getString("img_url");
            username = extras.getString("username");
        }


        firebaseDatabase = FirebaseDatabase.getInstance();
        commentref = firebaseDatabase.getReference("USERS/"+username+"/DATA/"+img_id).child("comments");

        commentref.keepSynced(true);


        //commentref = imageref.child(id).child("comments");


        //commentref.push().setValue(username);




        //Toast.makeText(this, img_url, Toast.LENGTH_SHORT).show();
        img_comment=(ImageView)findViewById(R.id.img_comment);

        comment=(TextView)findViewById(R.id.enter_comment);

        upload_comment = (ImageButton) findViewById(R.id.upload_comment);


        Picasso.with(getApplicationContext()).load(img_url).into(img_comment);



        upload_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //subiendo comentario
                String comentario = comment.getText().toString();

                commentref.push().child(username).setValue(comentario);
                //commentref.child(username).setValue(comentario);

            }
        });


        comentarios = new ArrayList<String>();
        modeAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1, android.R.id.text1, comentarios);

        // Assign adapter to ListView
        lista.setAdapter(modeAdapter);



        commentref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                comentarios.clear();

                for (DataSnapshot postSnapshot: snapshot.getChildren()) {


                    //String name = postSnapshot.getKey().toString();

                    String comment = postSnapshot.getValue().toString();

                    //Log.d("CommentN",name);
                    //Log.d("CommentC",comment);
                    comentarios.add(comment);


                }

                Collections.reverse(comentarios);
                modeAdapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: " ,firebaseError.getMessage());
            }
        });


    }
}
