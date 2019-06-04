package com.crm.elmolino.tracker_crm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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

public class ListFriends extends AppCompatActivity{



    //SEARCH VIEW
    private SearchView searchView;
    ListView lista;
    public ArrayList<String> friends;
    String username="";
    FirebaseDatabase firebaseDatabase;
    DatabaseReference friend_ref;

    ArrayAdapter<String> modeAdapter;
    Dialog dialogo_m;


    private void cargar_user(){
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        BufferedReader in = null;

        try {
            in = new BufferedReader(new FileReader(new File(getApplicationContext().getExternalFilesDir(null), "config.txt")));
            while ((line = in.readLine()) != null) stringBuilder.append(line);

            username = stringBuilder.toString();


        }catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), "Problema al leer config.txt", Toast.LENGTH_SHORT).show();
            //sendNotification("Problema al leer los datos");

        } catch (FileNotFoundException e) {
            Toast.makeText(getApplicationContext(), "CONFIGURACION NO ENCONTRADA", Toast.LENGTH_SHORT).show();
            //sendNotification("CONFIGURACION NO ENCONTRADA");

        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "NO SE PUEDE LEER EL ARCHIVO", Toast.LENGTH_SHORT).show();
            //sendNotification("NO SE PUEDE LEER EL ARCHIVO");
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);



        lista =(ListView) findViewById(R.id.lista);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //String[] stringArray = new String[] { "Victor", "Juan" ,"Christian","Raul","aa","bbc","abcsa" };
        //modeAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, android.R.id.text1, stringArray);
        //lista.setAdapter(modeAdapter);

        firebaseDatabase = FirebaseDatabase.getInstance();
        cargar_user();
        friend_ref = firebaseDatabase.getReference("USERS/"+username+"/friends/");


        friends = new ArrayList<String>();
        modeAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1, android.R.id.text1, friends);

        // Assign adapter to ListView
        lista.setAdapter(modeAdapter);
        lista.setTextFilterEnabled(true);


        friend_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //Log.d("Data2 " ,""+snapshot.getChildrenCount());
                friends.clear();
                //adapter.notifyDataSetChanged();

                for (DataSnapshot postSnapshot: snapshot.getChildren()) {

                    //if(username.equals(postSnapshot.getKey())==false){


                        friends.add(postSnapshot.getValue().toString());
                    //}
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
                final String  namefriend    = (String) lista.getItemAtPosition(position);
                //myfriend = namefriend;
                final AlertDialog.Builder dialogo = new AlertDialog.Builder(ListFriends.this);
                dialogo.setTitle("Deseas eliminar a "+namefriend+" de tus amigos");


                dialogo.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(getApplicationContext(), "Eliminando Amigo", Toast.LENGTH_SHORT).show();

                        friend_ref.removeValue();


                        //friend_ref.push().setValue(myfriend);
                        //friend_ref.push().setValue(myfriend,1);
                        //dialogo_m.dismiss();
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

                //Toast.makeText(ListFriends.this, "HIIII", Toast.LENGTH_SHORT).show();


                // Show Alert
                //Toast.makeText(getActivity().getApplicationContext(),"Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG).show();

            }

        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(Cliente.this, "ADDING USER", Toast.LENGTH_SHORT).show();

                //Intent intent = new Intent(getApplicationContext(), AddCliente2.class);
                //intent.putExtra("name", query);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                //getApplicationContext().startActivity(intent);



                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate( R.menu.menu_main, menu);


        //getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        //searchView = (SearchView) item.getActionView();

        //searchView.setMenuItem(item);


        ActionBar actionbar = getSupportActionBar ();
        actionbar.setDisplayHomeAsUpEnabled ( true );
        actionbar.setHomeAsUpIndicator ( R.drawable.back );

        //searchView.setIconifiedByDefault(false);
        //searchView.setOnQueryTextListener(this);
        //searchView.setSubmitButtonEnabled(true);
        //searchView.setQueryHint("Search Here");




        searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {

                modeAdapter.getFilter().filter(s);
                return true;
            }
        });


        return true;




    }



    /*
    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();

        } else {
            super.onBackPressed();
        }
    }*/


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
