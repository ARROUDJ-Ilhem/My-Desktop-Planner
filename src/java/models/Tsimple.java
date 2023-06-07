package tp.javafx.models;

import java.time.*;

public class Tsimple extends Tache {

    private int periodicite;

    public Tsimple(String nom, Duration duree, Priorite prio, LocalDate deadline, Categorie cat) {
        super(nom, duree, prio, deadline, cat);
    }

    public Tsimple(String nom, Duration duree, Priorite prio, LocalDate deadline, Categorie cat, int periodicite) {
        super(nom, duree, prio, deadline, cat);
        this.periodicite = periodicite;
    }

    public int getPeriodicite() {
        return periodicite;
    }

    public void setPeriodicite(int periodicite) {
        this.periodicite = periodicite;
    }

} 