package Activity.Pointeuse;

import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Model.Absence;
import Model.Classe;
import customfonts.Button_Roboto_Medium;
import database.ConnectDB;
import database.DBManager;
import wolfsoft1.pay2wallet.R;

public class PointerDetails extends AppCompatActivity {
    Button_Roboto_Medium Pointer;

    Spinner spinnerCycle;
    Spinner spinnerFillier;
    Spinner spinnerAnnee;

    Classe classeChoisi = new Classe();
    List<Classe> classes = new ArrayList<>();
    Absence absence ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pointer_details);

        WifiManager wifiMan = (WifiManager) getApplicationContext().getSystemService(getApplicationContext().WIFI_SERVICE);
        WifiInfo wifiInf = wifiMan.getConnectionInfo();
        int ipAddress = ((WifiInfo) wifiInf).getIpAddress();
        String ip = String.format("%d.%d.%d.%d", (ipAddress & 0xff),(ipAddress >> 8 & 0xff),(ipAddress >> 16 & 0xff),(ipAddress >> 24 & 0xff));
        Log.d("ipV4",""+ip);

        initView();
        final DBManager manager = new DBManager(this);

        manager.getAllClasses();

        classes = manager.getAllClasses();

        final ArrayList<String> annee = new ArrayList<String>();

        final ArrayList<String> cycle = new ArrayList<String>();

        final ArrayList<String> fillier = new ArrayList<String>();


        for(int i = 0 ;i<classes.size();i++){
            cycle.add(classes.get(i).getCycle());
            fillier.add(classes.get(i).getFillier());
        }

        for(int i = 2014 ; i<2020 ; i++){
            annee.add("Année : "+i);
        }

        ArrayAdapter<String> dataAdapterCycle = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cycle);
        dataAdapterCycle.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCycle.setAdapter(dataAdapterCycle);



        ArrayAdapter<String> dataAdapterFilier = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, fillier);
        dataAdapterFilier.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFillier.setAdapter(dataAdapterFilier);



        ArrayAdapter<String> dataAdapterAnnee = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, annee);
        dataAdapterAnnee.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAnnee.setAdapter(dataAdapterAnnee);




        spinnerCycle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                classeChoisi.setCycle(cycle.get(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerFillier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                classeChoisi.setFillier(fillier.get(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerAnnee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //position 0..n
                int anee = 2014 ;
                classeChoisi.setAnnee(""+(position+anee) );
                Log.d("LPosition" , ""+(position+anee)) ;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





        Pointer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                List<Classe> result  = manager.getClasseByQuery(classeChoisi);

                    if(result.size()>0){
                        Intent intent = new Intent(PointerDetails.this, MainActivityPointeuse.class);
                        intent.putExtra("classe", result.get(0));
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(PointerDetails.this, "Aucun Classe avec ces parametres !", Toast.LENGTH_SHORT).show();

                    }
            }
        });



    }

    void initView(){
        Pointer = (Button_Roboto_Medium) findViewById(R.id.Pointer);
        spinnerCycle = (Spinner) findViewById(R.id.spinnerCycle);
        spinnerFillier = (Spinner) findViewById(R.id.spinnerFillier);
        spinnerAnnee = (Spinner) findViewById(R.id.spinnerAnnee);

    }
}
