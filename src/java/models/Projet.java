package tp.javafx.models;

import java.util.ArrayList;

public class Projet {
    private String nom;
    private String description;
    private ArrayList<Tache> taches;


    public Projet(String nom, String description, ArrayList<Tache> taches) {
        super();
        this.nom = nom;
        this.description = description;
        this.taches = taches;
    }

    public Projet(String nom, String description) {
        super();
        this.nom = nom;
        this.description = description;
        this.taches = new ArrayList<Tache>();
    }

    public void ajouter_tache(Tache tache) {
        this.taches.add(tache);
    }


    public void afficher_projet() {
        System.out.println("Nom du projet: " + this.nom);
        System.out.println("Description: " + this.description);
        System.out.println("Liste des taches: ");

        for (Tache tache : taches) {
            tache.afficher_tache();
        }
    }

    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<Tache> getTaches() {
        return taches;
    }
}



