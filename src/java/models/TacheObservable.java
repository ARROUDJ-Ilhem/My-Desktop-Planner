package tp.javafx.models;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public class TacheObservable extends Tache {

    private String debutToFin;

    public TacheObservable(String nom, Duration duree, Priorite prio, LocalDate deadline, Categorie cat, Etat etat, String debutToFin) {
        super(nom, duree, prio, deadline, cat, etat);
        this.debutToFin = debutToFin;
    }

    public String getDebutToFin() {
        return debutToFin;
    }
}
