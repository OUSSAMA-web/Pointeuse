package Model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "Classe")
public class Classe  implements Serializable {


    @DatabaseField(columnName = "id",generatedId = true)
    private int id;


    @DatabaseField(columnName = "idClasse")
    int idClasse;

    @DatabaseField(columnName = "cycle")
    String cycle;

    @DatabaseField(columnName = "fillier")
    String fillier;

    @DatabaseField(columnName = "annee")
    String annee;

    public Classe() {
    }

    public Classe(int idClasse, String cycle, String fillier, String annee) {
        this.idClasse = idClasse;
        this.cycle = cycle;
        this.fillier = fillier;
        this.annee = annee;
    }

    public int getIdClasse() {
        return idClasse;
    }

    public String getCycle() {
        return cycle;
    }

    public String getFillier() {
        return fillier;
    }

    public String getAnnee() {
        return annee;
    }


    public void setIdClasse(int idClasse) {
        this.idClasse = idClasse;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public void setFillier(String fillier) {
        this.fillier = fillier;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }
}
