package tp.javafx.models;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

public class Creneau implements Decomposable {
    private LocalTime debut;
    private LocalTime fin;
    private ArrayList<Tache> taches;
    private boolean libre;
    private Duration dureeMinimale;

    public Creneau(LocalTime debut, LocalTime fin, ArrayList<Tache> taches, boolean libre, Duration dureeMinimale) {
        this.debut = debut;
        this.fin = fin;
        this.taches = taches;
        this.libre = libre;
        this.dureeMinimale = dureeMinimale;
    }

    public Creneau(LocalTime debut, LocalTime fin, boolean libre) {
        this(debut, fin, new ArrayList<>(), libre, Duration.of(0, ChronoUnit.MINUTES));
    }

    // A utiliser

    public Creneau(LocalTime debut, LocalTime fin) {
        this(debut, fin, true);
    }

    public Creneau(LocalTime debut, LocalTime fin, Duration dureeMinimale) {
        this(debut, fin, new ArrayList<>(), true, dureeMinimale);
        if (debut.isAfter(fin)) {
            throw new ExceptionInInitializerError("Le début du créneau doit être avant la fin");
        } else if (debut.equals(fin)) {
            throw new ExceptionInInitializerError("Le début du créneau doit être différent de la fin");
        } else if (fin.minus(dureeMinimale).equals(debut.plus(dureeMinimale))) {
            throw new ExceptionInInitializerError("La durée minimale doit être inférieure à la durée du créneau et inférieure à la moitié de la durée du créneau");
        }
    }

    public Creneau(LocalTime debut, LocalTime fin, boolean libre, Duration dureeMinimale) {
        this(debut, fin, new ArrayList<>(), libre, dureeMinimale);
    }

    public LocalTime getDebut() {
        return debut;
    }

    public void setDebut(LocalTime debut) {
        this.debut = debut;
    }

    public LocalTime getFin() {
        return fin;
    }

    public List<Tache> getTaches() {
        return taches;
    }

    public void setTaches(ArrayList<Tache> taches) {
        this.taches = taches;
    }

    public void ajouterTache(Tache tache) {
        if (taches == null) {
            taches = new ArrayList<>();
        }
        taches.add(tache);
    }

    public void setLibre(boolean libre) {
        this.libre = libre;
    }

    public boolean chevauche(Creneau autreCreneau) {
        return (debut.isBefore(autreCreneau.getFin()) && fin.isAfter(autreCreneau.getFin())) ||
                (debut.isAfter(autreCreneau.getDebut()) && fin.isBefore(autreCreneau.getFin())) ||
                (debut.isBefore(autreCreneau.getDebut()) && fin.isAfter(autreCreneau.getDebut())) ||
                (debut.equals(autreCreneau.getDebut()) && fin.equals(autreCreneau.getFin()));
    }

    public int getDureeEnMinutes() {
        return (int) ChronoUnit.MINUTES.between(debut, fin);
    }

    public boolean isLibre() {
        return libre;
    }

    public Duration getDureeMinimale() {
        return this.dureeMinimale;
    }

    public void setDureeMinimale(Duration dureeMinimale) {
        this.dureeMinimale = dureeMinimale;
    }

    @Override
/*public boolean estDecomposable(Duration dureeCreneau, Duration dureeTache) {
    return dureeCreneau.compareTo(dureeTache.plus(dureeMinimale)) >= 0;
}*/

    public boolean estDecomposable(Duration dureeCreneau, Duration dureeTache) {
        if (dureeCreneau == null || dureeTache == null) {
            return false;
        }

        long dureeSeconds = dureeCreneau.toSeconds();
        long dureeTacheSeconds = dureeTache.toSeconds();
        long dureeInSeconds = this.dureeMinimale.toSeconds();

        long difference = dureeSeconds - dureeTacheSeconds;
        if (difference >= dureeInSeconds) {
            LocalTime nouveauFin = debut.plus(dureeTache);
            Creneau nouveauCreneau = new Creneau(nouveauFin, fin, false);
            fin = nouveauFin;
            libre = true;

            if (taches == null) {
                taches = new ArrayList<>();
            }

            taches.add(new Tache("Tâche décomposée", dureeTache));
            return true;
        } else {
            return false;
        }


    }

    public boolean decomposer(Duration dureeTache) {

        if (estDecomposable(getDureeRestante(), dureeTache)) {

            LocalTime nouveauFin = debut.plus(dureeTache);
            Creneau nouveauCreneau = new Creneau(nouveauFin, fin, false);
            fin = nouveauFin;
            libre = true;

            if (taches == null) {
                taches = new ArrayList<>();
            }

            taches.add(new Tache("Tâche décomposée", dureeTache));

            return true;
        } else {
            return false;

        }
    }


    public Duration getDureeRestante() {
        return Duration.between(LocalDateTime.now(), fin);
    }

    @Override
    public String toString() {
        return "Creneau [debut=" + debut + ", fin=" + fin + "]";
    }

    public Duration getDuree() {
        return Duration.between(debut, fin);
    }

   /*  public boolean estPeriodeLibre(int periodicite) {
        LocalTime debut = this.getDebut();
        LocalTime fin = this.getFin();
        LocalDate currentDate = debut.toLocalDate().plusDays(periodicite);

        while (currentDate.isBefore(fin.toLocalDate())) {
            if (!isCreneauLibre(currentDate)) {
                return false;
            }
            currentDate = currentDate.plusDays(periodicite);
        }

        return true;
    }

    private boolean isCreneauLibre(LocalDate date) {
        LocalDate creneauDate = this.getDebut().toLocalDate();

        while (creneauDate.isBefore(this.getFin().toLocalDate().plusDays(1))) {
            if (creneauDate.isEqual(date) && !libre) {
                return false;
            }
            creneauDate = creneauDate.plusDays(1);
        }

        return true;
    }

} */
}







