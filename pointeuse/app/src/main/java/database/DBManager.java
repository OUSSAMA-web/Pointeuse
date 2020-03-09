package database;

import android.content.Context;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.stmt.query.In;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.Absence;
import Model.Absence_details;
import Model.Classe;
import Model.Etudiant;
import Model.Prof;

public class DBManager {

    public static void Init(Context context) {
        if (ourInstance == null)
            ourInstance = new DBManager(context);
    }

    public static DBManager getInstance() {
        return ourInstance;
    }

    public DBManager(Context context) {
        helper = new DatabaseHelper(context);
    }

    private DatabaseHelper getHelper() {
        return helper;
    }


    public void ClearData(){
        helper.clearData();
    }
    public String createEtudiant(Etudiant etudiant) {
        try {
            getHelper().getEtudiants().create(etudiant);
            return etudiant.getCNE();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String createProfesseur(Prof prof) {
        try {
            getHelper().getProfs().create(prof);
            return prof.getIDprof();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public int createClasse(Classe classe) {
        try {
            getHelper().getClasses().create(classe);
            return classe.getIdClasse();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }



    public int creatAbsence(Absence absence) {
        try {
            getHelper().getAbsences().create(absence);
            return absence.getIdAbsence();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }



    public int createAbsence(Absence absence) {
        try {
            getHelper().getAbsences().create(absence);
            return absence.getIdAbsence();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }


    public int creatdetailsAbsence(Absence_details absence_details) {
        try {
            getHelper().getAbsencesDetails().create(absence_details);
            return absence_details.getIdAbsence();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }


    // ndepanik biha wsaf

    // khes requete li tatfetchi li deja tscanaw w li mzl ... // hh finahya makaynach
    // hanta


    // hadi katjib ga3 les etudiants li fdak classe idClasse
    // db 3ndna li hadrin khsna n7ydohom mn hadi
    // how ???????  Mm nariii lpc again khsni nsed mozila att n7awl nsd o ok

    public List<Etudiant> getEtudiantsByClasse(int idClasse) {
        try {
            return getHelper().getEtudiants().queryForEq("idClasse", idClasse);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    public List<Etudiant> getEtudiantByCNE(String CNE) {
        try {
            return getHelper().getEtudiants().queryForEq("CNE", CNE);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Classe> getClasseByID(int idclasse) {
        try {
            return getHelper().getClasses().queryForEq("idClasse", idclasse);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    public List<Etudiant> getEtudiantByCNEAndIDclasse(String CNE,int idClasse) {
        try {

            QueryBuilder<Etudiant, Integer> qb =  getHelper().getEtudiants().queryBuilder();
            Where where = qb.where();
            where.eq("CNE", CNE);
            where.and();
            where.eq("idClasse", idClasse);
            List<Etudiant> result = qb.query();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Prof> getProfByID(String IDprof) {
        try {
            return getHelper().getProfs().queryForEq("IDprof", IDprof);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    public List<Classe> getAllClasses() {
        try {
            return getHelper().getClasses().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }




    }





    public List<Classe> getClasseByQuery(Classe c){
        try {


            QueryBuilder<Classe, Integer> qb =  getHelper().getClasses().queryBuilder();
            Where where = qb.where();
            where.eq("cycle", c.getCycle());
            where.and();
            where.eq("annee", c.getAnnee());
            where.and();
            where.eq("fillier", c.getFillier());

            List<Classe> result = qb.query();
            return result;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Etudiant> getEtudiantsByidClass(int idClasse){
        try {


            QueryBuilder<Etudiant, Integer> qb =  getHelper().getEtudiants().queryBuilder();
            Where where = qb.where();
            where.eq("idClasse", idClasse);
            List<Etudiant> result = qb.query();
            return result;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private DatabaseHelper  helper;
    private static DBManager ourInstance;
}