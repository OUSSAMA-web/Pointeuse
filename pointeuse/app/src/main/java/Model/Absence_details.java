package Model;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "Absence_details")
public class Absence_details  implements Serializable {

    @DatabaseField(columnName = "id_absence_details" ,generatedId = true)
    int id_absence_details;

    @DatabaseField(columnName = "idAbsence")
    int idAbsence;

    @DatabaseField(columnName = "CNE")
    String CNE;

    @DatabaseField(columnName = "isAbsent")
    boolean isAbsent;

    public Absence_details() {
    }

    public Absence_details(int idAbsence, String CNE, boolean isAbsent) {
        this.idAbsence = idAbsence;
        this.CNE = CNE;
        this.isAbsent = isAbsent;
    }

    public int getId_absence_details() {
        return id_absence_details;
    }

    public void setId_absence_details(int id_absence_details) {
        this.id_absence_details = id_absence_details;
    }

    public int getIdAbsence() {
        return idAbsence;
    }

    public void setIdAbsence(int idAbsence) {
        this.idAbsence = idAbsence;
    }

    public String getCNE() {
        return CNE;
    }

    public void setCNE(String CNE) {
        this.CNE = CNE;
    }

    public boolean isAbsent() {
        return isAbsent;
    }

    public void setAbsent(boolean absent) {
        isAbsent = absent;
    }
}
