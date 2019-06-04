package com.crm.elmolino.tracker_crm;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by User on 2/28/2017.
 */

public class ShareImage extends Fragment {

    //private static final String TAG = "Perfil";

    //private Button btnTEST;


    public ImageView img;
    public TextView description;

    public ImageButton firebase_button;
    private Activity myactivity;

    private View HistoriasView;


    Uri filePath;
    String img_path;


    ProgressDialog pd;
    //creating reference to firebase storage
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://uploadimage-3046c.appspot.com");    //change the url according to your firebase app

    StorageReference childRef;



    //creating reference to firebase database
    //Firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    //FirebaseRecyclerOptions<Post> options;
    //FirebaseRecyclerAdapter<Post,MyRecyclerViewHolder> adapter;

    RecyclerView recyclerView;

    String username="";


    public void AddActivity(final Activity activity) {
        myactivity = activity;
    }


    public void AddHistorias(final View v) {
        HistoriasView = v;
    }

    public void AddImagePath(Uri path){
            filePath = path;
    }


    private void cargar_user(){
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        BufferedReader in = null;

        try {
            in = new BufferedReader(new FileReader(new File(myactivity.getExternalFilesDir(null), "config.txt")));
            while ((line = in.readLine()) != null) stringBuilder.append(line);

            username = stringBuilder.toString();
            //String[] separated = stringBuilder.toString().split(";");

            //if(separated.length==4){

                /*

                id_vendedor = Integer.valueOf(separated[0]);
                distancia_min = Float.valueOf(separated[1]);
                minutos = Long.valueOf(separated[2]);
                url = separated[3];

                //Toast.makeText(getApplicationContext(), "Archivos Parseados correctamente", Toast.LENGTH_SHORT).show();

                //sendNotification("Archivos Parseados correctamente");

                Intent i =new Intent(getApplicationContext(),GPS_Service.class);


                i.putExtra("minutos", minutos);
                i.putExtra("distancia", distancia_min);
                i.putExtra("id_vendedor", id_vendedor);
                i.putExtra("url", url);

                //sendNotification("SERVICIO REINICIADO CORRECTAMENTE");
                startService(i);
                */


            //}
            //else{
            //    Toast.makeText(getApplicationContext(), "Problem archivo con menos de 4 datos", Toast.LENGTH_SHORT).show();
                //sendNotification("Problem archivo con menos de 4 datos");

            //}


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
        View view = inflater.inflate(R.layout.share_image_fragment,container,false);


        //btnTEST = (Button) view.findViewById(R.id.btnTEST2);

        //btnTEST.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        Toast.makeText(getActivity(), "TESTING BUTTON CLICK 2", Toast.LENGTH_SHORT).show();
        //    }
        //});

        img = (ImageView)view.findViewById(R.id.upload_image);
        description = (TextView)view.findViewById(R.id.description);


        //EditText editText = (EditText) view.findViewById(R.id.description);

        // Request focus and show soft keyboard automatically
        //editText.requestFocus();
        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        pd = new ProgressDialog(myactivity);
        pd.setMessage("Uploading....");

        //FirebaseOptions opts = FirebaseApp.getInstance().getOptions();
        //Log.i("ERROR", "Bucket = " + opts.getStorageBucket());


        //FIREBASE DATABASE
        firebaseDatabase = FirebaseDatabase.getInstance();
        cargar_user();
        databaseReference = firebaseDatabase.getReference("USERS/"+username+"/DATA");
        databaseReference.keepSynced(true);

        //recyclerView = (RecyclerView)HistoriasView.findViewById(R.id.recycler_view);
        //recyclerView.setLayoutManager(new LinearLayoutManager(myactivity));


        /*
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //displayComment();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/


        //final FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference ref = database.getReference("server/saving-data/fireblog/posts");

        // Attach a listener to read the data at our posts reference







        firebase_button = (ImageButton) view.findViewById(R.id.firebase_upload);

        firebase_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(myactivity, "UPLOADING TO FIREBASE", Toast.LENGTH_SHORT).show();

               // Log.d("myPATH",img_path);

                //filePath = Uri.fromFile(new File(img_path));




                if(filePath != null) {
                    //Log.d("myPATH",filePath.toString());

                    pd.show();


                    String filename=  filePath.toString().substring(filePath.toString().lastIndexOf("/")+1);


                    childRef = storageRef.child(filename);

                    //StorageReference childRef = storageRef.child("imagengg.jpg");

                    //uploading the image
                    UploadTask uploadTask = childRef.putFile(filePath);

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            pd.dismiss();
                            Toast.makeText(myactivity, "Upload successful", Toast.LENGTH_SHORT).show();

                            //StorageReference load = getImage(id);
                            childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Got the download URL for 'users/me/profile.png'
                                    // Pass it to Picasso to download, show in ImageView and caching


                                    //Picasso.with(myactivity).load(uri.toString()).into(show_image);
                                    //Log.d("myPATH",uri.toString());
                                    //Log.d("myPATH", description.getText().toString());


                                    Toast.makeText(myactivity, "Guardando info", Toast.LENGTH_SHORT).show();

                                    postComment(uri.toString(),description.getText().toString());

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                }
                            });



                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(myactivity, "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();

                            Log.d("myPATH",e.toString());
                        }
                    });


                    //String myUrl = storageRef.child(filename).getDownloadUrl().getResult().toString();

                    //Toast.makeText(myactivity, myUrl, Toast.LENGTH_SHORT).show();
                    //Log.d("myPATH",myUrl);



                }
                else {
                    Toast.makeText(myactivity, "Select an image", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }

    private void postComment(String image,String description) {


        Post post = new Post(image,description);
        Toast.makeText(getContext(), "image", Toast.LENGTH_SHORT).show();
        databaseReference.push().setValue(post);
        //adapter.notifyDataSetChanged();
    }


    /*
    private void displayComment() {

        options = new FirebaseRecyclerOptions.Builder<Post>().setQuery(databaseReference,Post.class).build();


        adapter = new FirebaseRecyclerAdapter<Post, MyRecyclerViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyRecyclerViewHolder holder, int position, @NonNull Post model) {

                //holder.txt_title.setText(model.getTitle());

                Picasso.with(myactivity).load(model.getTitle().toString()).into(holder.img);

                holder.txt_comment.setText(model.getContent());


            }

            @NonNull
            @Override
            public MyRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

                View itemView = LayoutInflater.from(myactivity).inflate(R.layout.post_item,parent,false);


                return new MyRecyclerViewHolder(itemView);


            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
    */

    public void addImage(Bitmap imagen){

        //img.setImageBitmap(imagen);
        img.setImageBitmap(Bitmap.createScaledBitmap(imagen, 700, 600, false));
    }





}
