package com.crm.elmolino.tracker_crm;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Register extends AppCompatActivity {



    CallbackManager callbackManager;
    TextView txtEmail;
    TextView txtUsername;
    TextView txtName;
    TextView password;
    ProgressDialog mDialog;

    ImageView imgAvatar;

    Button registrar;
    Button ingresar;


    //creating reference to firebase database
    //Firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    //String username="jeff";


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode,resultCode,data);

        callbackManager.onActivityResult(requestCode,resultCode,data);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        callbackManager = CallbackManager.Factory.create();

        txtUsername = (TextView)findViewById(R.id.txtUsername);
        txtEmail = (TextView)findViewById(R.id.txtEmail);
        txtName = (TextView)findViewById(R.id.txtName);

        password = (TextView)findViewById(R.id.txtPassword);


        //txtFriends = (TextView)findViewById(R.id.txtFriends);

        imgAvatar = (ImageView)findViewById(R.id.avatar);


        registrar = (Button)findViewById(R.id.registrarse);
        ingresar = (Button)findViewById(R.id.login_b);


        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Register.this, "INGRESANDO", Toast.LENGTH_SHORT).show();

                Intent myIntent = new Intent(Register.this, Login.class);
                //myIntent.putExtra("key", value); //Optional parameters
                startActivity(myIntent);

            }
        });



        //FIREBASE DATABASE
        firebaseDatabase = FirebaseDatabase.getInstance();





        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Toast.makeText(Register.this, "REGISTRANDO", Toast.LENGTH_SHORT).show();

                databaseReference = firebaseDatabase.getReference("USERS/"+txtUsername.getText().toString()+"/");

                databaseReference.child("name").setValue(txtName.getText().toString());
                databaseReference.child("password").setValue(password.getText().toString());
                databaseReference.child("email").setValue(txtEmail.getText().toString());

                //databaseReference.child("username").setValue(txtUsername.getText().toString());



                save(txtUsername.getText().toString());

                //writeToFile(txtUsername.getText().toString(),Register.this);

                Intent myIntent = new Intent(Register.this, MainActivity.class);
                //myIntent.putExtra("key", value); //Optional parameters
                startActivity(myIntent);

            }
        });



        LoginButton loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile","email","user_birthday","user_friends"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mDialog = new ProgressDialog(Register.this);
                mDialog.setMessage("Retrieving data...");
                mDialog.show();

                String accesstoken = loginResult.getAccessToken().getToken();
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        mDialog.dismiss();
                        Toast.makeText(Register.this, response.toString(), Toast.LENGTH_SHORT).show();
                        Log.i("response",response.toString());
                        getData(object);
                        //Bundle facebookData = getFacebookData(object);
                    }
                });
                //Request Graph API
                Bundle parameters = new Bundle();
                parameters.putString("fields","id,name,email,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }
            @Override
            public void onCancel() { }
            @Override
            public void onError(FacebookException error) { }
        });

        //if already login
        if(AccessToken.getCurrentAccessToken() != null ){
            txtEmail.setText(AccessToken.getCurrentAccessToken().getUserId());
        }


        //printKeyHash();
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





    private void getData(JSONObject object) {

        try{

            Log.d("JSON", object.toString());


            URL profile_picture = new URL("https://graph.facebook.com/"+object.getString("id")+"/picture?width=250&height=250");

            Picasso.with(this).load(profile_picture.toString()).into(imgAvatar);

            txtEmail.setText(object.getString("email"));
            //txtBirthday.setText(object.getString("birthday"));
            txtName.setText(object.getString("name"));

            //txtFriends.setText("Friends: "+object.getJSONObject("friends").getJSONObject("summary").getString("total_count"));


        } catch (MalformedURLException e) {
            Log.d("error","MAL PARSE");

            //e.printStackTrace();
        } catch (JSONException e) {
            Log.d("error","MAL PARSE2");
            //e.printStackTrace();
        }
    }


    private void printKeyHash() {
        try{
            PackageInfo info = getPackageManager().getPackageInfo("com.crm.elmolino.tracker_crm", PackageManager.GET_SIGNATURES);
            for(Signature signature:info.signatures)
            {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));

            }


        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

}
