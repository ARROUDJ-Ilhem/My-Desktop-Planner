package tp.javafx.models;

import java.time.Duration;
import java.time.LocalDate;

public class Tache {
    private String nom;
    private Duration duree;
    private Priorite prio;
    private LocalDate deadline;
    private Etat etat;
    protected Categorie cat;
    private int task_type;


    public Tache(String nom, Duration duree) {
        super();
        this.nom = nom;
        this.duree = duree;
    }


    public Tache(String nom, Duration duree, Priorite prio, LocalDate deadline, Categorie cat) {
        this.nom = nom;
        this.duree = duree;
        this.prio = prio;
        this.deadline = deadline;
        this.cat = cat;
        this.etat = Etat.notRealized;
    }

    public Tache(String nom, Duration duree, Priorite prio, LocalDate deadline, Categorie cat, Etat etat) {
        this.nom = nom;
        this.duree = duree;
        this.prio = prio;
        this.deadline = deadline;
        this.etat = etat;
        this.cat = cat;
    }


    public void afficher_tache() {
        System.out.println("Task name: " + nom);
        if (prio != null) {
            System.out.println("Task priority: " + prio);
        }
        if (deadline != null) {
            System.out.println("Task deadline: " + deadline);
        }
        System.out.println("Task state: " + etat);
        System.out.println("Task type" + task_type);
        System.out.println();
    }

    // Getters and setters for the fields
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Duration getDuree() {
        return duree;
    }

    public void setDuree(Duration duree) {
        this.duree = duree;
    }

    public Priorite getPrio() {
        return prio;
    }

    public void setPrio(Priorite prio) {
        this.prio = prio;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public Etat getEtat() {
        return etat;
    }

    public void setEtat(Etat etat) {
        this.etat = etat;
    }

    public Categorie getCategorie() {
        return cat;
    }

    public Integer getTask_type() {
        return task_type;
    }
}
