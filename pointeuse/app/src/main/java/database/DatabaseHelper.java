package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

import Model.Absence;
import Model.Absence_details;
import Model.Classe;
import Model.Etudiant;
import Model.Prof;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME    = "pointeuse.db";
    private static final int    DATABASE_VERSION = 1;

    private Dao<Etudiant,Integer> etudiants = null;
    private Dao<Prof,Integer> profs  = null;
    private Dao<Classe,Integer> classes  = null;
    private Dao<Absence,Integer> absences  = null;
    private Dao<Absence_details,Integer> absence_details  = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Etudiant.class);
            TableUtils.createTable(connectionSource, Prof.class);
            TableUtils.createTable(connectionSource, Classe.class);
            TableUtils.createTable(connectionSource, Absence.class);
            TableUtils.createTable(connectionSource, Absence_details.class);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        try {

            TableUtils.dropTable(connectionSource, Etudiant.class,true);
            TableUtils.dropTable(connectionSource, Prof.class,true);
            TableUtils.dropTable(connectionSource, Classe.class,true);
            TableUtils.dropTable(connectionSource, Absence.class,true);
            TableUtils.dropTable(connectionSource, Absence_details.class,true);

            onCreate(db, connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void clearData(){

        try {

        TableUtils.clearTable(connectionSource, Etudiant.class);
        TableUtils.clearTable(connectionSource, Prof.class);
        TableUtils.clearTable(connectionSource, Classe.class);

        }
             catch (SQLException e){

        }
    }

    public Dao.CreateOrUpdateStatus createOrUpdateEtudiant(Etudiant obj) throws SQLException {
        Dao<Etudiant, ?> dao = (Dao<Etudiant, ?>) getDao(obj.getClass());
        return dao.createOrUpdate(obj);
    }


    public Dao.CreateOrUpdateStatus createOrUpdateClasse(Classe obj) throws SQLException {
        Dao<Classe, ?> dao = (Dao<Classe, ?>) getDao(obj.getClass());
        return dao.createOrUpdate(obj);
    }



    public Dao.CreateOrUpdateStatus createOrUpdateProfs(Prof obj) throws SQLException {
        Dao<Prof, ?> dao = (Dao<Prof, ?>) getDao(obj.getClass());
        return dao.createOrUpdate(obj);
    }


    public Dao.CreateOrUpdateStatus createOrUpdateAbsence(Absence obj) throws SQLException {
        Dao<Absence, ?> dao = (Dao<Absence, ?>) getDao(obj.getClass());
        return dao.createOrUpdate(obj);
    }

    public Dao.CreateOrUpdateStatus createOrUpdateAbsenceDetails(Absence_details obj) throws SQLException {
        Dao<Absence_details, ?> dao = (Dao<Absence_details, ?>) getDao(obj.getClass());
        return dao.createOrUpdate(obj);
    }

    public Dao<Etudiant, Integer> getEtudiants() throws SQLException {
        if (etudiants == null) {
            etudiants = getDao(Etudiant.class);

        }
        return etudiants;
    }


    public  List getAll(Class clazz) throws SQLException {
        Dao<Object, ?> dao = getDao(clazz);
        return dao.queryForAll();
    }

    public Dao<Prof, Integer> getProfs() throws SQLException {
        if (profs == null) {
            profs = getDao(Prof.class);
        }
        return profs;
    }


    public  Object getById(Class clazz, Object aId) throws SQLException {
        Dao<Object, Object> dao = getDao(clazz);
        return dao.queryForId(aId);
    }

    public String createEtudiant(Etudiant etudiant) {
        try {

             this.createOrUpdateEtudiant(etudiant);

            return etudiant.getCNE();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String createProfs(Prof prof) {
        try {
            this.createOrUpdateProfs(prof);
            return prof.getIDprof();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int createClasse(Classe classe) {
        try {
            this.createOrUpdateClasse(classe);
            return classe.getIdClasse();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int createAbsence(Absence absence) {
        try {
            this.createOrUpdateAbsence(absence);
            return absence.getIdAbsence();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int createAbsenceDetails(Absence_details absence_details) {
        try {
            this.createOrUpdateAbsenceDetails(absence_details);
            return absence_details.getId_absence_details();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public Dao<Classe, Integer> getClasses() throws SQLException {
        if (classes == null) {
            classes = getDao(Classe.class);
        }
        return classes;
    }


    public Dao<Absence, Integer> getAbsences() throws SQLException {
        if (absences == null) {
            absences = getDao(Absence.class);
        }
        return absences;
    }

    public Dao<Absence_details, Integer> getAbsencesDetails() throws SQLException {
        if (absence_details == null) {
            absence_details = getDao(Absence_details.class);
        }
        return absence_details;
    }
}