package com.crm.elmolino.tracker_crm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by User on 2/28/2017.
 */

public class MenuOpciones extends Fragment {
    private static final String TAG = "Tab1Fragment";

    //private Button btnTEST;

    ListView listView;

    private Activity myactivity;


    public void AddActivity(final Activity activity) {
        myactivity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_fragment,container,false);

        // Get ListView object from xml
        listView = (ListView) view.findViewById(R.id.opciones);

        // Defined Array values to show in ListView
        String[] values = new String[] { "Lista de Amigos","Ayuda y soporte técnico","Cerrar sesión"
        };

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_list_item_2, android.R.id.text1, values);


        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index

                //int itemPosition     = position;

                // ListView Clicked item value
                //String  itemValue    = (String) listView.getItemAtPosition(position);

                // Show Alert
                //Toast.makeText(getActivity().getApplicationContext(),
                //        "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
                //        .show();


                if(position==0){

                    //Toast.makeText(getActivity(), "Cerrando sesion", Toast.LENGTH_SHORT).show();


                    Toast.makeText( getApplicationContext(),"Mostrando Lista de Amigos", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(myactivity,ListFriends.class);
                    startActivity(i);

                }



                if(position==2){

                    //Toast.makeText(getActivity(), "Cerrando sesion", Toast.LENGTH_SHORT).show();

                    File path = getApplicationContext().getExternalFilesDir(null);
                    Toast.makeText( getApplicationContext(),"ELIMINANDO "+ path.toString(), Toast.LENGTH_SHORT).show();

                    File file = new File(path, "config.txt");
                    file.delete();


                    Intent i = new Intent(myactivity,Login.class);
                    startActivity(i);
                    //boolean deleted = file.delete();
                }

            }

        });



        return view;
    }
}
