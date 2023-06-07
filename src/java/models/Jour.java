package tp.javafx.models;

import java.time.Duration;
import java.time.LocalTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class Jour {

    private LocalDate date;
    private ArrayList<Creneau> creneaux;
    private int nombreMinimumTaches;

    public Jour() {
        creneaux = new ArrayList<>();
    }

    public Jour(LocalDate date) {
        super();
        this.date = date;
        creneaux = new ArrayList<>();
    }

    public int getNombreMinimumTaches() {
        return nombreMinimumTaches;
    }

    public void setNombreMinimumTaches(int nombreMinimumTaches) {
        this.nombreMinimumTaches = nombreMinimumTaches;
    }

    public ArrayList<Creneau> getCreneaux() {
        return creneaux;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setCreneaux(ArrayList<Creneau> creneaux) {
        this.creneaux = creneaux;
    }


    public void ajouterCreneau(Creneau creneau) {
        for (Creneau c : creneaux) {
            if (c.chevauche(creneau)) {
                throw new IllegalArgumentException("Le créneau chevauche un autre créneau déjà existant");
            }
        }
        creneaux.add(creneau);
    }

    public ArrayList<Creneau> getCreneauxLibres(Duration dureeTache) {
        ArrayList<Creneau> creneauxLibres = new ArrayList<>();
        LocalTime debut = LocalTime.now();
        LocalTime fin = debut.plus(dureeTache);

        for (Creneau creneau : creneaux) {
            if (creneau.isLibre() && debut.isBefore(creneau.getFin()) && fin.isAfter(creneau.getDebut())) {
                debut = creneau.getFin();
            } else if (creneau.isLibre() && debut.isBefore(creneau.getFin()) && fin.isBefore(creneau.getFin())) {
                creneauxLibres.add(new Creneau(debut, fin));
                debut = creneau.getFin();
            } else if (debut.isAfter(creneau.getFin())) {
                // Do nothing and continue looping
            }
        }

        return creneauxLibres;
    }

    public void addCreneauLibre(Creneau creneauLibre) {
        this.creneaux.add(creneauLibre);
    }

    public Creneau getPremierCreneauLibre(Duration dureeTache) {
        LocalTime debut = LocalTime.now();
        LocalTime fin = debut.plus(dureeTache);

        for (Creneau creneau : creneaux) {
            if (debut.isBefore(creneau.getFin()) && fin.isBefore(creneau.getFin())) {
                return creneau;
            } else if (debut.isBefore(creneau.getFin()) && fin.isAfter(creneau.getDebut())) {
                debut = creneau.getFin();
            } else if (debut.isAfter(creneau.getFin())) {
                // Do nothing and continue looping
            }
        }

        // Aucun créneau disponible
        return null;
    }


}
