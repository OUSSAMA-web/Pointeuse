package Model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "Absence")
public class Absence  implements Serializable {

    @DatabaseField(columnName = "idAbsence",generatedId = true)
    int idAbsence;

    @DatabaseField(columnName = "Date_time")
    String Date_time;

    @DatabaseField(columnName = "Prof_id")
    String Prof_id;

    @DatabaseField(columnName = "IDClasse")
    int IDClasse;

    public Absence() {

    }

    public Absence(int idAbsence, String date_time, String prof_id, int IDClasse) {
        this.idAbsence = idAbsence;
        Date_time = date_time;
        Prof_id = prof_id;
        this.IDClasse = IDClasse;
    }

    public int getIdAbsence() {
        return idAbsence;
    }

    public void setIdAbsence(int idAbsence) {
        this.idAbsence = idAbsence;
    }

    public String getDate_time() {
        return Date_time;
    }

    public void setDate_time(String date_time) {
        Date_time = date_time;
    }

    public String getProf_id() {
        return Prof_id;
    }

    public void setProf_id(String prof_id) {
        Prof_id = prof_id;
    }

    public int getIDClasse() {
        return IDClasse;
    }

    public void setIDClasse(int IDClasse) {
        this.IDClasse = IDClasse;
    }
}
