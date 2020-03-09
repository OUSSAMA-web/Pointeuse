package Model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "Prof")
public class Prof  implements Serializable {

    @DatabaseField(columnName = "IDprof")
    String IDprof;
    @DatabaseField(columnName = "nom")
    String nom;
    @DatabaseField(columnName = "prenom")
    String prenom;


    public Prof() {
    }

    public Prof(String IDprof, String nom, String prenom) {
        this.IDprof = IDprof;
        this.nom = nom;
        this.prenom = prenom;
    }

    public String getIDprof() {
        return IDprof;
    }

    public void setIDprof(String IDprof) {
        this.IDprof = IDprof;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
}
