package Model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Etudiant")
public  class Etudiant {

    @DatabaseField(columnName = "id",generatedId = true)
    private int id;

    @DatabaseField(columnName = "CNE")
    String CNE;
    @DatabaseField(columnName = "nom")
    String nom;
    @DatabaseField(columnName = "prenom")
    String prenom;
    @DatabaseField(columnName = "idClasse")
    int idClasse;

    public Etudiant() {
    }

    public Etudiant(String CNE, String nom, String prenom, int idClasse) {
        this.CNE = CNE;
        this.nom = nom;
        this.prenom = prenom;
        this.idClasse = idClasse;
    }

    public String getCNE() {
        return CNE;
    }

    public void setCNE(String CNE) {
        this.CNE = CNE;
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

    public int getIdClasse() {
        return idClasse;
    }

    public void setIdClasse(int idClasse) {
        this.idClasse = idClasse;
    }
}
