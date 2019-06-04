package com.crm.elmolino.tracker_crm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.design.widget.TabLayout;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;


import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity  {

    private static final String TAG = "MainActivity";

    private SectionsPageAdapter mSectionsPageAdapter;

    private ViewPager mViewPager;

    //private Mapa map;


    //private Menu menu;


    private ShareImage share;
    private Historias historia;
    private MenuOpciones  opciones;
    private Friends  friends;
    private Historias2 historias2;


    //SEARCH VIEW


    Dialog dialogo_m;

    //private MaterialSearchView searchView;


    private SearchView searchView;
    private TabLayout tabLayout;


    String mCurrentPhotoPath;





/*
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case 1:
                if ( grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(getContext(),"GPS permission granted",Toast.LENGTH_LONG).show();

                    map.iniciar_Mapa();
                    //getLoaderManager().initLoader(1, null,this);
                    //getActivity().getSupportLoaderManager().initLoader(0, null, );

                } else {
                    //Toast.makeText(getContext(),"GPS permission denied",Toast.LENGTH_LONG).show();
                    // show user that permission was denied. inactive the location based feature or force user to close the app
                }
                return;
        }
    }

*/





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //searchviewcode();



        Log.d(TAG, "onCreate: Starting.");
        share = new ShareImage();
        historia = new Historias();
        opciones = new MenuOpciones();
        friends = new Friends();
        historias2 = new Historias2();



        /*
        map = new Mapa();

        menu = new Menu(MainActivity.this);

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},1);
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},1);

            //googleMap.getUiSettings().setZoomControlsEnabled(true);
        }*/










        //getSupportActionBar().setCustomView(new CustomView(this));
        //getSupportActionBar().setDisplayShowCustomEnabled(true);

        //icons = new ArrayList();

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);

        mViewPager.setOffscreenPageLimit(5);


        setupViewPager(mViewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);




        //tabLayout.getTabAt(1).setIcon(R.drawable.ic_account_circle_black_48dp);

        //icons = new ArrayList();




        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setImageResource(R.drawable.upload_img);
        tabLayout.getTabAt(0).setCustomView(imageView);


        imageView = new ImageView(getApplicationContext());
        imageView.setImageResource(R.drawable.perfil);
        tabLayout.getTabAt(1).setCustomView(imageView);






         /*
        imageView = new ImageView(getApplicationContext());
        imageView.setImageResource(R.drawable.map_icon);
        tabLayout.getTabAt(2).setCustomView(imageView);

        imageView = new ImageView(getApplicationContext());
        imageView.setImageResource(R.drawable.bell_icon);
        tabLayout.getTabAt(3).setCustomView(imageView);
        */


        imageView = new ImageView(getApplicationContext());
        imageView.setImageResource(R.drawable.muro);
        tabLayout.getTabAt(2).setCustomView(imageView);



        imageView = new ImageView(getApplicationContext());
        imageView.setImageResource(R.drawable.friends);
        tabLayout.getTabAt(3).setCustomView(imageView);

        imageView = new ImageView(getApplicationContext());
        imageView.setImageResource(R.drawable.tools);
        tabLayout.getTabAt(4).setCustomView(imageView);




        //for (int i = 0; i < tabLayout.getTabCount(); i++) {
        //    ImageView imageView = new ImageView(getApplicationContext());
        //    imageView.setImageResource(icons[i]);
        //    tabLayout.getTabAt(i).setCustomView(imageView);
        //}


        //tabLayout.getTabAt(2).setIcon(R.drawable.mapa_icon98);

        //for (int i = 0; i < tabLayout.getTabCount(); i++) {
        //    tabLayout.getTabAt(i).setIcon(R.drawable.ic_launcher_background);
        //}


        mViewPager.setCurrentItem(0);


        dialogo_m = new Dialog(MainActivity.this);
        //share.img;

        share.AddActivity(this);

        historia.AddActivity(this);
        opciones.AddActivity(this);
        friends.AddActivity(this);
        historias2.AddActivity(this);

        share.AddHistorias(historia.view);

        //image_upload = share.img;

        //image_upload = (ImageView) share.view.findViewById(R.id.upload_image);


    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = getApplicationContext().getExternalFilesDir(null);


        //File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();

        //share.AddImagePath(mCurrentPhotoPath);
        return image;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:


                final AlertDialog.Builder dialogo = new AlertDialog.Builder(MainActivity.this);
                dialogo.setTitle("Seleccionar foto");

                final ListView opciones = new ListView(MainActivity.this);
                String[] stringArray = new String[]{"Tomar foto", "Escoger fotografía del álbum"};
                ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, stringArray);
                opciones.setAdapter(modeAdapter);

                dialogo.setView(opciones);


                dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(MainActivity.this, "Disminuir", Toast.LENGTH_SHORT).show();
                        dialogo_m.dismiss();
                    }

                });

                dialogo_m = dialogo.create();
                dialogo_m.show();
                opciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        if (position == 0) {

                            File photoFile = null;


                            try {
                                photoFile = createImageFile();
                            } catch (IOException e) {
                                Toast.makeText(MainActivity.this, "ERROR1", Toast.LENGTH_SHORT).show();
                                //e.printStackTrace();
                            }
                            //photoFile = new File(Environment.getExternalStorageDirectory(), "POST_IMAGE.jpg");

                            //mCurrentPhotoPath =  photoFile.getAbsolutePath();

                            //Toast.makeText(AddCliente2.this, mCurrentPhotoPath, Toast.LENGTH_SHORT).show();

                            // Continue only if the File was successfully created
                            if (photoFile != null) {

                                //takePicture.putExtra(View, view);

                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                                    takePicture.putExtra(MediaStore.EXTRA_OUTPUT,  Uri.fromFile(photoFile));
                                } else {

                                    Uri photoUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", photoFile);
                                    takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

                                }

                                //takePicture.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                                if (takePicture.resolveActivity(getApplicationContext().getPackageManager()) != null) {
                                    startActivityForResult(takePicture, 0);//zero can be replaced with any action code
                                }


                            }


                            //Toast.makeText(AddCliente2.this, "Abriendo la Camara", Toast.LENGTH_SHORT).show();
                        }
                        if (position == 1) {

                            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto , 1);//one can be replaced with any action code

                            //Toast.makeText(AddCliente2.this, "Abriendo el album", Toast.LENGTH_SHORT).show();





                        }

                        dialogo_m.dismiss();
                        }

                });



                //Toast.makeText(this, "Abriendo la galeria", Toast.LENGTH_SHORT).show();

                //menu.dialog.show();


                return true;



            default:
                return super.onOptionsItemSelected(item);
        }
    }




    private void setupViewPager(ViewPager viewPager) {




        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(share, "ShareImage");
        adapter.addFragment(historia, "Historias");
        adapter.addFragment(historias2, "Historias2");

        //adapter.addFragment(map, "Map");


        //adapter.addFragment(opciones, "Friends");

        adapter.addFragment(friends, "Friends");
        adapter.addFragment(opciones, "MenuOpciones");
        //adapter.addFragment(map, "MenuOpciones");
        viewPager.setAdapter(adapter);
    }





    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate( R.menu.menu_main, menu);


        //getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);

        //searchView.setMenuItem(item);


        ActionBar actionbar = getSupportActionBar ();
        actionbar.setDisplayHomeAsUpEnabled ( true );
        actionbar.setHomeAsUpIndicator ( R.drawable.subir_img );


        //BUSQUEDA
        /*
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        */
        //BUSQUEDA CON SUGERENCIAS


        //mViewPager.setCurrentItem(0);

        searchView = (SearchView) item.getActionView();






        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {

                mViewPager.setCurrentItem(3);
                friends.modeAdapter.getFilter().filter(s);
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
    }



    private void searchviewcode() {
        searchView = (MaterialSearchView) findViewById(R.id.search_view);

        searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
        searchView.setEllipsize(true);
        searchView.setVoiceSearch(true);

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                //Toast.makeText(MainActivity.this, "Mostrando "+ query, Toast.LENGTH_SHORT).show();



                Intent intent = new Intent(getApplicationContext(), Cliente_info.class);
                intent.putExtra("name", query);
                startActivity(intent);

                searchView.closeSearch();


                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Toast.makeText(MainActivity.this, "MOSTRANDO", Toast.LENGTH_SHORT).show();
                //tabLayout.setVisibility(View.GONE);
                searchView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onSearchViewClosed() {
                //Toast.makeText(MainActivity.this, "CERRANDO", Toast.LENGTH_SHORT).show();
                //tabLayout.setVisibility(View.VISIBLE);
                searchView.setVisibility(View.GONE);

            }
        });
    }

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }*/



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){

                    //Bitmap photo = (Bitmap) data.getExtras().get("data");

                    //ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    //photo.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                    //foto_actual.setImageBitmap(photo);

                    Bitmap photo = null;

                    try {



                        share.AddImagePath(Uri.parse(mCurrentPhotoPath));


                        photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));


                        //ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        //photo.compress(Bitmap.CompressFormat.JPEG, 10, bytes);


                        //photo = Bitmap.createBitmap(photo, 0, 0, 400, 400);
                        //foto_actual.setImageBitmap(photo);
                        //image_upload.setImageBitmap(Bitmap.createScaledBitmap(photo, 300, 400, false));

                        share.addImage(photo);


                    } catch (IOException e) {
                        //e.printStackTrace();
                    }


                }

                break;
            case 1:
                if(resultCode == RESULT_OK){

                    Uri imageUri = data.getData();

                    share.AddImagePath(imageUri);

                    //share.AddImagePath(imageUri.getPath().toString());

                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        //image_upload.setImageBitmap(bitmap);

                        share.addImage(bitmap);


                    } catch (IOException e) {

                        //e.printStackTrace();
                    }


                }
                break;
        }
    }




}

