package Activity.Pointeuse;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import Model.Absence;
import Model.Absence_details;
import Model.Classe;
import Model.Etudiant;
import Model.Prof;
import database.ConnectDB;
import database.DBManager;
import database.DatabaseHelper;
import wolfsoft1.pay2wallet.R;

public class HomePointeuse extends AppCompatActivity {
    LinearLayout Pointer;
    LinearLayout SynchroniserDonnees;
    LinearLayout logout;

    private DatabaseHelper databaseHelper = null;
    final Etudiant etudiant = new Etudiant();
    DBManager manager;
    ConnectDB connectDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_pointeuse);

        manager = new DBManager(this);
        initView();

        // synchroniser les donnée
            // si ls donnés sont déja synchornisé
      //      if(manager.getAllClasses().size()==0){
                manager.ClearData();
                Synchroniser();
      //      }

        try {
             Dao<Etudiant, Integer> etudiantsDao = getHelper().getEtudiants();
             Dao<Prof, Integer> profsDao = getHelper().getProfs();
             Dao<Classe, Integer> classesDao = getHelper().getClasses();
             Dao<Absence, Integer> AbsenceDao = getHelper().getAbsences();
             Dao<Absence_details, Integer> absence_detailsDao = getHelper().getAbsencesDetails();

         }
         catch (Exception e){}

        Pointer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(HomePointeuse.this,PointerDetails.class));
            }
        });



        SynchroniserDonnees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Synchroniser();
                // wrap thread around original code for progress button
                final ProgressDialog ringProgressDialog = ProgressDialog.show(HomePointeuse.this, "Sunchronisation !", "Attender un moment",
                        true, false);
                ringProgressDialog.setIndeterminate(true);
                new Thread(new Runnable() {
                    public void run() {
                        try {

                            Thread.sleep(4000);


                            if (ringProgressDialog != null && ringProgressDialog.isShowing()) {
                                ringProgressDialog.dismiss();
                            }


                        } catch (Exception e) {

                        }

                    }

                }).start();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomePointeuse.this.finish();
            }
        });

    }
    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this,DatabaseHelper.class);
        }
        return databaseHelper;
    }

    void initView(){
        SynchroniserDonnees = (LinearLayout) findViewById(R.id.synchoniser);
        Pointer = (LinearLayout) findViewById(R.id.Appele);
        logout = (LinearLayout) findViewById(R.id.logout);
    }


    void Synchroniser(){

        manager.ClearData();
        connectDb = new ConnectDB(HomePointeuse.this);
        connectDb.execute();


    }


}
