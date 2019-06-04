package com.crm.elmolino.tracker_crm;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

public class Friends extends Fragment {
    //private static final String TAG = "Tab1Fragment";

    //private Button btnTEST;



    //SEARCH VIEW
    //public SearchView searchView;
    public ListView lista;
    public ArrayList<String> friends;
    ArrayAdapter<String> modeAdapter;
    Dialog dialogo_m;
    String username="";

    String myfriend="";

    private Activity myactivity;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference friend_ref;

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



    public void AddActivity(final Activity activity) {
        myactivity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.friends_fragment,container,false);



        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);


        // Get ListView object from xml
        lista = (ListView) view.findViewById(R.id.friends);



        firebaseDatabase = FirebaseDatabase.getInstance();
        cargar_user();
        DatabaseReference databaseReference = firebaseDatabase.getReference("USERS");

        friend_ref = firebaseDatabase.getReference("USERS/"+username+"/friends/");


        /*
        // Defined Array values to show in ListView
        String[] values = new String[] { "juan",
                "pedro",
                "miguel",
                "jose123",
                "andres",
                "luz",
                "rosa",
                "noelia",
                "gimena",
                "joel",
                "luis",
                "brayan",
                "brandon",
                "sebastian",
                "margarita"
        };*/

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        friends = new ArrayList<String>();
        modeAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_list_item_1, android.R.id.text1, friends);

        // Assign adapter to ListView
        lista.setAdapter(modeAdapter);


        lista.setTextFilterEnabled(true);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {



                //Log.d("Data2 " ,""+snapshot.getChildrenCount());

                friends.clear();
                //adapter.notifyDataSetChanged();

                for (DataSnapshot postSnapshot: snapshot.getChildren()) {

                    if(username.equals(postSnapshot.getKey())==false){

                        friends.add(postSnapshot.getKey());
                    }

                    //Post post = postSnapshot.getValue(Post.class);
                    //Log.d("Data2", post.getImage()+"  "+post.getDescription());
                    //posts.add(post);
                }

                //Collections.reverse(posts);

                modeAdapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: " ,firebaseError.getMessage());
            }
        });



        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                String  namefriend    = (String) lista.getItemAtPosition(position);

                myfriend = namefriend;


                final AlertDialog.Builder dialogo = new AlertDialog.Builder(myactivity);
                dialogo.setTitle("Quieres agregar a: "+ namefriend);

                /*
                final ListView opciones = new ListView(getActivity().getApplicationContext());
                String[] stringArray = new String[]{"Tomar foto", "Escoger fotografía del álbum"};
                ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, android.R.id.text1, stringArray);
                opciones.setAdapter(modeAdapter);

                dialogo.setView(opciones);
                */



                dialogo.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(getActivity().getApplicationContext(), "Agregando Amigo", Toast.LENGTH_SHORT).show();


                        //ADD FRIEND

                        //DatabaseReference friend_ref = firebaseDatabase.getReference("USERS/"+username+"/friends");


                        //friend_ref.push().setValue(myfriend);
                        friend_ref.push().setValue(myfriend,1);



                        dialogo_m.dismiss();
                    }

                });

                dialogo.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Toast.makeText(getActivity().getApplicationContext(), "Cancelando", Toast.LENGTH_SHORT).show();
                        dialogo_m.dismiss();
                    }

                });

                dialogo_m = dialogo.create();
                dialogo_m.show();



                // Show Alert
                //Toast.makeText(getActivity().getApplicationContext(),"Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG).show();

            }

        });



        return view;
    }
}
