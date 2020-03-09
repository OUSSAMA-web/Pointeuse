package Model;

import java.util.ArrayList;
import java.util.List;

public class ListPresenceMail {


    List<Etudiant> etudiantspresent = new ArrayList<>();
    Absence absence = new Absence();
    List<Absence_details> absence_details = new ArrayList<>();
    Prof prof = new Prof();

    public ListPresenceMail() {
    }

    public ListPresenceMail(List<Etudiant> etudiantspresent, Absence absence, List<Absence_details> absence_details, Prof prof) {
        this.etudiantspresent = etudiantspresent;
        this.absence = absence;
        this.absence_details = absence_details;
        this.prof = prof;
    }

    public Prof getProf() {
        return prof;
    }

    public void setProf(Prof prof) {
        this.prof = prof;
    }

    public List<Etudiant> getEtudiantspresent() {

        return etudiantspresent;
    }

    public void setEtudiantspresent(List<Etudiant> etudiantspresent) {
        this.etudiantspresent = etudiantspresent;
    }

    public Absence getAbsence() {
        return absence;
    }

    public void setAbsence(Absence absence) {
        this.absence = absence;
    }

    public List<Absence_details> getAbsence_details() {
        return absence_details;
    }

    public void setAbsence_details(List<Absence_details> absence_details) {
        this.absence_details = absence_details;
    }

}
