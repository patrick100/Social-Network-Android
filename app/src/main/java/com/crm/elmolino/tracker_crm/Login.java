package com.crm.elmolino.tracker_crm;


import android.content.Intent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;


public class Login extends AppCompatActivity {




    TextView txtUsername;
    TextView txtPassword;
    Button register;

    Button login;


    //creating reference to firebase database
    //Firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    String username="";
    String password="";


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode,resultCode,data);

        //callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    void save(String contenido){
        File path = getApplicationContext().getExternalFilesDir(null);
        Toast.makeText(getApplicationContext(), path.toString(), Toast.LENGTH_SHORT).show();

        File file = new File(path, "config.txt");

        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(file);
            //Toast.makeText(getApplicationContext(), "SE GUARDO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            Toast.makeText(getApplicationContext(), "ERROR AL CREAR CONFIGURATION", Toast.LENGTH_SHORT).show();
        }
        try {

            stream.write(contenido.getBytes());

        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "ERROR AL GUARDAR CONFIGURATION", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        File path = getApplicationContext().getExternalFilesDir(null);
        //Toast.makeText( getApplicationContext(),"ELIMINANDO "+ path.toString(), Toast.LENGTH_SHORT).show();

        File file = new File(path, "config.txt");

        if(file.exists()){

            Toast.makeText(this, "CONFIGURACION ENCONTRADA", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(Login.this,MainActivity.class);
            startActivity(i);
        }




        //callbackManager = CallbackManager.Factory.create();

        //txtBirthday = (TextView)findViewById(R.id.txtBirthday);

        txtUsername = (TextView) findViewById(R.id.txtUsername);
        txtPassword = (TextView) findViewById(R.id.txtPassword);


        register = (Button) findViewById(R.id.register_b);

        login = (Button) findViewById(R.id.ingresar);



        //FIREBASE DATABASE
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("USERS");





        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Login.this, "INGRESANDO", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(Login.this, Register.class);
                //myIntent.putExtra("key", value); //Optional parameters
                startActivity(i);

            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = txtUsername.getText().toString();
                password = txtPassword.getText().toString();

                //verificar si existe el usuario en la database


                if(username.length()>=8 && password.length()>=8 ){
                    Toast.makeText(Login.this,R.string.verificacion, Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(Login.this,R.string.noverificacion, Toast.LENGTH_SHORT).show();
                }

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.hasChild(username)) {
                            //
                            String pass = dataSnapshot.child(username).child("password").getValue().toString();
                            //Toast.makeText(Login.this, "PASS1 "+pass+" PASS2 "+password  , Toast.LENGTH_SHORT).show();
                            if(pass.equals(password))
                            {
                                save(username);
                                //Toast.makeText(Login.this, "INGRESO CORRECTAMENTE ", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(Login.this,MainActivity.class);
                                startActivity(i);
                            }
                            else{

                                //Toast.makeText(Login.this, "CONTRASEÑA INCORRECTA", Toast.LENGTH_SHORT).show();
                            }





                        }else{
                            //It is new users
                            //write an entry to your user table
                            //writeUserEntryToDB();

                            //Toast.makeText(Login.this, "USUARIO NO EXISTENTE", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("The read failed: " ,databaseError.getMessage());
                    }


                    /*
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        for (DataSnapshot postSnapshot: snapshot.getChildren()) {


                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError firebaseError) {
                        Log.e("The read failed: " ,firebaseError.getMessage());
                    }*/




                });


            }
        });


    }
}
