package tp.javafx.models;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;

public class Tcomposee extends Tache implements Decomposable {

    private Duration sousTacheDuree;

    private ArrayList<Tache> sousTaches;

    public Tcomposee(String nom, Duration duree, Duration sousTacheDuree, Priorite prio, LocalDate deadline, Categorie cat) {
        super(nom, duree, prio, deadline, cat);
        this.sousTacheDuree = sousTacheDuree;
        sousTaches = new ArrayList<>();
    }

    public Duration getSousTacheDuree() {
        return sousTacheDuree;
    }

    public void setSousTacheDuree(Duration sousTacheDuree) {
        this.sousTacheDuree = sousTacheDuree;
    }

    public void ajouterSousTache(Tache sousTache) {
        sousTaches.add(sousTache);
    }

    @Override
    public boolean estDecomposable(Duration dureeCreneau, Duration dureeTache) {
        return dureeCreneau.compareTo(dureeTache) >= 0;
    }

    public ArrayList<Tache> decomposer(Duration dureeMaxCreneau) {
        ArrayList<Tache> sousTaches = new ArrayList<>();
        Duration dureeComposite = getDuree();

        if (dureeComposite.compareTo(dureeMaxCreneau) <= 0) {
            // La tâche composée peut être planifiée dans un seul créneau
            sousTaches.add(this);
        } else {
            // Diviser la tâche composée en sous-tâches
            long dureeSousTacheMinutes = dureeMaxCreneau.toMinutes();
            int numSousTaches = (int) Math.ceil(dureeComposite.toMinutes() / dureeSousTacheMinutes);

            for (int i = 1; i <= numSousTaches; i++) {
                String nomSousTache = getNom() + " " + i;
                Duration dureeSousTache = Duration.ofMinutes(Math.min(dureeSousTacheMinutes, dureeComposite.toMinutes()));
                Tsimple tacheSimple = new Tsimple(nomSousTache, dureeSousTache, getPrio(), getDeadline(), getCategorie());
                sousTaches.add(tacheSimple);
                dureeComposite = dureeComposite.minus(dureeSousTache);
            }
        }

        return sousTaches;
    }
}