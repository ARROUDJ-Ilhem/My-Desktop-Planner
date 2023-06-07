package tp.javafx.models;

import java.sql.SQLException;
import java.util.*;
import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class Utilisateur {
    private String pseudo;
    private String password;
    private Jour Jour;

    private LocalDate dateDebut;
    private LocalDate dateFin;
    private ArrayList<Tache> taches;
    private ArrayList<Projet> projets;
    private ArrayList<Categorie> categories = new ArrayList<>();
    private ArrayList<Jour> jours;

    private int nbTaches;
    private int nbTachesRealisees;
    private ArrayList<Badge> badges;

    // constructeur 1
    public Utilisateur(String pseudo, Jour Jour, ArrayList<Tache> taches, ArrayList<Projet> projets) {
        super();
        this.pseudo = pseudo;
        this.Jour = Jour;
        this.taches = taches;
        this.projets = projets;
    }


    // constructeur 2
    public Utilisateur(String pseudo) {
        this.pseudo = pseudo;
        this.jours = new ArrayList<>();
        this.taches = new ArrayList<>();
        this.projets = new ArrayList<>();
    }


    public Utilisateur(String pseudo, ArrayList<Categorie> categories) {
        super();
        this.pseudo = pseudo;
        this.categories = categories;
    }


    public Utilisateur(String pseudo, String password) {
        super();
        this.pseudo = pseudo;
        this.password = password;
        this.taches = new ArrayList<>();
        this.dateDebut = LocalDate.now();
        this.dateFin = LocalDate.now().plusDays(1);
        this.projets = new ArrayList<>();
        this.categories = new ArrayList<>();
        this.categories.add(new Categorie("Défaut", "#2596be"));
        this.jours = new ArrayList<>();
        this.jours.add(new Jour(LocalDate.now()));
        this.jours.add(new Jour(LocalDate.now().plusDays(1)));
        this.nbTaches = 5;
        this.badges = new ArrayList<>();
    }

    public Utilisateur(String pseudo, ArrayList<Projet> projets, ArrayList<Categorie> categories, ArrayList<Jour> jours, LocalDate dateDebut, LocalDate dateFin, ArrayList<Badge> badges, Integer nbTaches) {
        super();
        this.pseudo = pseudo;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.projets = projets;
        this.categories = categories;
        this.jours = jours;
        this.nbTachesRealisees = 0;
        this.nbTaches = nbTaches;
        this.badges = badges;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public ArrayList<Jour> getJours() {
        return jours;
    }

    public void setJours(ArrayList<Jour> jours) {
        this.jours = jours;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public ArrayList<Badge> getBadges() {
        return badges;
    }

    public void addBadge(Badge badge) {
        this.badges.add(badge);
    }

    public void setNbTaches(int nbTaches) {
        this.nbTaches = nbTaches;
    }

    public int getNbTaches() {
        return nbTaches;
    }

    public void setNbTachesRealisees() {
        this.nbTachesRealisees = nbTachesRealisees + 1;
    }

    public void setNbTachesRealisees(int nbTachesRealisees) {
        this.nbTachesRealisees = nbTachesRealisees;
    }

    public int getNbTachesRealisees() {
        return nbTachesRealisees;
    }


    public Utilisateur(ArrayList<Jour> jours) {
        super();
        this.jours = jours;
    }

    public void setCategories(ArrayList<Categorie> categories) {
        this.categories = categories;
    }


    public Jour getJour() {
        return Jour;
    }


    // l'utilisateur ajoute une tache selon son nom, dure......
    /*public ArrayList<Tache> ajouter_tache(String nom, Duration duree, Priorite prio, LocalDate deadline, int type, int periodicite) {
        ArrayList<Tache> tachesAjoutees = new ArrayList<>();

        if (type == 1) {
            Tache tache;
            if (periodicite > 0) {
                tache = new Tsimple(nom, duree, prio, deadline, periodicite);
            } else {
                tache = new Tsimple(nom, duree, prio, deadline);
            }
            tachesAjoutees.add(tache);
        } else if (type == 2) {
            Tache tache = new Tcomposee(nom, duree, prio, deadline);
            tachesAjoutees.add(tache);
        } else {
            System.out.println("Erreur veuillez choisir '1' ou '2'");
            return null;
        }

        if (this.taches == null) {
            this.taches = new ArrayList<>(); // Initialize taches ArrayList if null
        }

        this.taches.addAll(tachesAjoutees);

        for (Tache tache : tachesAjoutees) {
            tache.afficher_tache();
        }

        return tachesAjoutees;
    } */


    //recupere tableau des taches from array list (geeter)
    public ArrayList<Tache> getTaches() {
        return taches;
    }

    public ArrayList<Projet> getProjets() {
        return projets;
    }


    // recuperer les proprietes d'une tache a partir de son nom
    public Tache get_tache(String nom_tache) {
        for (Tache tache : taches) {
            if (tache.getNom().equals(nom_tache)) {
                return tache;
            }
        }
        return null;
    }

    //classer une tache dans une categorie
    public void classer_tache(String nom_tache, String nom_categorie) {
        Tache tache = get_tache(nom_tache);
        if (tache != null) {
            Categorie cat = new Categorie(nom_categorie);
            tache.cat = cat;
            System.out.println("la tache a ete ajoutee");
        }
    }

    // ajouter categorie

    public void ajouter_categorie(Categorie categorie) {
        categories.add(categorie);
    }


    //The method getCategories() returns the ArrayList of categories stored in the current instance of the Utilisateur class.
    public ArrayList<Categorie> getCategories() {
        return categories;
    }

    public void assign_couleur_to_category(String nom_categorie, String couleur) {
        // Find the category in the list
        Categorie category = null;
        for (Categorie c : this.categories) {
            if (c.getNom_cat().equals(nom_categorie)) {
                category = c;
                break;
            }
        }

        // If the category doesn't exist, create a new one and add it to the list
        if (category == null) {
            category = new Categorie(nom_categorie, couleur);
            this.categories.add(category);
        } else {
            // Otherwise, update the existing category with the new color
            category.setCouleur(couleur);
        }
    }

    public void creer_projet(String nom, String description, ArrayList<Tache> taches) {
        Projet project = new Projet(nom, description, taches);
        this.projets.add(project);
        project.afficher_projet();

    }

    public Boolean planifierAuto(Tache tache, Boolean libre, Projet projet) throws SQLException {
        for (Jour jour : this.jours) {
            if (jour.getDate().isBefore(tache.getDeadline()) || jour.getDate().isEqual(tache.getDeadline())) {
                for (Creneau creneau : jour.getCreneaux()) {
                    if (creneau.isLibre()) {
                        if (Duration.between(creneau.getDebut(), creneau.getFin()).compareTo(tache.getDuree()) >= 0) {
                            creneau.ajouterTache(tache);
                            creneau.setLibre(libre);
                            creneau.setDebut(creneau.getDebut().plus(tache.getDuree()));
                            if (Duration.between(creneau.getDebut(), creneau.getFin()).compareTo(creneau.getDureeMinimale()) < 0) {
                                creneau.setLibre(false);
                            }
                            UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
                            utilisateurDAO.addTacheProjet(tache, this, jour, creneau, projet);
                            return true;
                        }
                    }
                }
            } else {
                return false;
            }
        }
        return false;
    }

    public Boolean planifierAuto(Tache tache, Boolean libre) throws SQLException {

        for (Jour jour : this.jours) {
            if (jour.getDate().isBefore(tache.getDeadline()) || jour.getDate().isEqual(tache.getDeadline())) {
                for (Creneau creneau : jour.getCreneaux()) {
                    if (creneau.isLibre()) {
                        if (Duration.between(creneau.getDebut(), creneau.getFin()).compareTo(tache.getDuree()) >= 0) {
                            creneau.ajouterTache(tache);
                            creneau.setLibre(libre);
                            creneau.setDebut(creneau.getDebut().plus(tache.getDuree()));
                            if (Duration.between(creneau.getDebut(), creneau.getFin()).compareTo(creneau.getDureeMinimale()) < 0) {
                                creneau.setLibre(false);
                            }
                            UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
                            utilisateurDAO.addTache(tache, this, jour, creneau);
                            return true;
                        }
                    }
                }
            } else {
                return false;
            }
        }
        return false;
    }

    public void planifierManuelTache(Jour jour, Creneau creneau, Tache tache) throws SQLException {
        UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
        creneau.setDebut(creneau.getDebut().plus(tache.getDuree()));
        creneau.ajouterTache(tache);
        utilisateurDAO.addTache(tache, this, jour, creneau);
    }

    public void planifierManuelTache(Jour jour, Creneau creneau, Tache tache, Projet projet) throws SQLException {
        UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
        creneau.setDebut(creneau.getDebut().plus(tache.getDuree()));
        creneau.ajouterTache(tache);
        utilisateurDAO.addTacheProjet(tache, this, jour, creneau, projet);
    }


    public boolean planifierAutoTacheSimple(Tsimple tache, Boolean libre) throws SQLException {

        Integer periodicite = tache.getPeriodicite();

        Boolean isPlanned = false;

        if (periodicite > 0) {
            ArrayList<Tache> planifiedTaches = new ArrayList<>();
            ArrayList<Boolean> isPlannedList = new ArrayList<>();
            for (Integer i = 0; i < jours.size(); i += periodicite) {
                Jour jour = jours.get(i);

                if (jour.getDate().isBefore(tache.getDeadline()) || jour.getDate().isEqual(tache.getDeadline())) {
                    Boolean tachePlanned = false;

                    tachePlanned = planifierAuto(tache, libre);

                    isPlannedList.add(tachePlanned);

                    if (tachePlanned) {
                        planifiedTaches.add(tache);
                    }
                }


            }

            // check if all the taches have been planned

            isPlanned = true;

            for (Boolean b : isPlannedList) {
                if (!b) {
                    isPlanned = false;
                    break;
                }
            }

            if (!isPlanned) {
                for (Tache tacheToDelete : planifiedTaches) {
                    for (Jour jour : this.jours) {
                        for (Creneau creneau : jour.getCreneaux()) {
                            for (Tache creneauTache : creneau.getTaches()) {
                                if (tacheToDelete.getNom() == creneauTache.getNom() && tacheToDelete.cat == creneauTache.cat && tacheToDelete.getPrio() == creneauTache.getPrio() && tacheToDelete.getDuree() == creneauTache.getDuree() && tacheToDelete.getDeadline() == creneauTache.getDeadline()) {
                                    UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
                                    utilisateurDAO.deleteTache(tacheToDelete, this, jour, creneau);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (!isPlanned) {
            tache.setEtat(Etat.unscheduled);
        }

        return isPlanned;
    }

    public boolean planifierAutoTacheSimple(Tsimple tache, Boolean libre, Projet projet) throws SQLException {

        Integer periodicite = tache.getPeriodicite();

        Boolean isPlanned = false;

        if (periodicite > 0) {
            ArrayList<Tache> planifiedTaches = new ArrayList<>();
            ArrayList<Boolean> isPlannedList = new ArrayList<>();
            for (Integer i = 0; i < jours.size(); i += periodicite) {
                Jour jour = jours.get(i);

                if (jour.getDate().isBefore(tache.getDeadline()) || jour.getDate().isEqual(tache.getDeadline())) {
                    Boolean tachePlanned = false;

                    tachePlanned = planifierAuto(tache, libre, projet);

                    isPlannedList.add(tachePlanned);

                    if (tachePlanned) {
                        planifiedTaches.add(tache);
                    }
                }
            }

            // check if all the taches have been planned

            isPlanned = true;

            for (Boolean b : isPlannedList) {
                if (!b) {
                    isPlanned = false;
                    break;
                }
            }

            if (!isPlanned) {
                for (Tache tacheToDelete : planifiedTaches) {
                    for (Jour jour : this.jours) {
                        for (Creneau creneau : jour.getCreneaux()) {
                            for (Tache creneauTache : creneau.getTaches()) {
                                if (tacheToDelete.getNom() == creneauTache.getNom() && tacheToDelete.cat == creneauTache.cat && tacheToDelete.getPrio() == creneauTache.getPrio() && tacheToDelete.getDuree() == creneauTache.getDuree() && tacheToDelete.getDeadline() == creneauTache.getDeadline()) {
                                    UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
                                    utilisateurDAO.deleteTache(tacheToDelete, this, jour, creneau);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (!isPlanned) {
            tache.setEtat(Etat.unscheduled);
        }

        return isPlanned;
    }

    public Duration findAvailableCreneauTime(Tache tache) {
        Duration duree = tache.getDuree();
        Duration dureeSousTaches = Duration.ofHours(0).plusMinutes(0);
        Duration dureeTotaleDesCreneaux = Duration.ofHours(0).plusMinutes(0);
        for (Jour jour : jours) {
            for (Creneau creneauJour : jour.getCreneaux()) {
                if (jour.getDate().isBefore(tache.getDeadline())) {
                    for (Creneau c : jour.getCreneaux()) {

                        Duration dureeCreneau = Duration.between(c.getDebut(), c.getFin());
                        if (dureeSousTaches.compareTo(dureeCreneau) > 0)
                            dureeSousTaches = dureeCreneau;
                        dureeTotaleDesCreneaux = dureeTotaleDesCreneaux.plus(dureeCreneau);
                    }
                }
            }
        }
        if (duree.compareTo(dureeTotaleDesCreneaux) <= 0) {
            return dureeSousTaches;
        } else {
            return Duration.ZERO;
        }
    }

    public boolean planifierAutoTacheComposee(Tcomposee tache, Boolean libre) throws SQLException {

        Integer tacheIndex = 1;

        Boolean isPlanned = false;

        Duration dureeSousTaches = tache.getSousTacheDuree();

        ArrayList<Boolean> isPlannedList = new ArrayList<>();

        ArrayList<Tache> planifiedTaches = new ArrayList<>();

        while (tache.getDuree().minus(dureeSousTaches).compareTo(Duration.ofHours(0).plusMinutes(0)) > 0) {
            Tache t = new Tache((tache.getNom() + " " + tacheIndex.toString()), dureeSousTaches, tache.getPrio(), tache.getDeadline(), tache.getCategorie(), Etat.notRealized);

            Boolean planned = planifierAuto(t, libre);
            isPlannedList.add(planned);
            if (planned) {
                planifiedTaches.add(t);
            }
            tacheIndex++;
            tache.setDuree(tache.getDuree().minus(dureeSousTaches));
        }

        Tache t = new Tache((tache.getNom() + " " + tacheIndex.toString()), tache.getDuree(), tache.getPrio(), tache.getDeadline(), tache.getCategorie(), Etat.notRealized);

        Boolean planned = planifierAuto(t, libre);
        isPlannedList.add(planned);
        if (planned) {
            planifiedTaches.add(t);
        }
        tache.setDuree(tache.getDuree().minus(t.getDuree()));

        // check if all the taches have been planned

        isPlanned = true;

        for (Boolean b : isPlannedList) {
            if (!b) {
                isPlanned = false;
                break;
            }
        }

        // if not, delete all the planned taches

        if (!isPlanned) {
            for (Tache tacheToDelete : planifiedTaches) {
                for (Jour jour : this.jours) {
                    for (Creneau creneau : jour.getCreneaux()) {
                        for (Tache creneauTache : creneau.getTaches()) {
                            if (tacheToDelete.getNom() == creneauTache.getNom() && tacheToDelete.cat == creneauTache.cat && tacheToDelete.getPrio() == creneauTache.getPrio() && tacheToDelete.getDuree() == creneauTache.getDuree() && tacheToDelete.getDeadline() == creneauTache.getDeadline()) {
                                UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
                                utilisateurDAO.deleteTache(tacheToDelete, this, jour, creneau);
                            }
                        }
                    }
                }
            }
        }

        return isPlanned;

    }

    public boolean planifierAutoTacheComposee(Tcomposee tache, boolean libre, Projet projet) throws SQLException {
        Integer tacheIndex = 1;

        Boolean isPlanned = false;

        Duration dureeSousTaches = tache.getSousTacheDuree();

        ArrayList<Boolean> isPlannedList = new ArrayList<>();

        ArrayList<Tache> planifiedTaches = new ArrayList<>();

        while (tache.getDuree().minus(dureeSousTaches).compareTo(Duration.ofHours(0).plusMinutes(0)) > 0) {
            Tache t = new Tache((tache.getNom() + " " + tacheIndex.toString()), dureeSousTaches, tache.getPrio(), tache.getDeadline(), tache.getCategorie(), Etat.notRealized);

            Boolean planned = planifierAuto(t, libre, projet);
            isPlannedList.add(planned);
            if (planned) {
                planifiedTaches.add(t);
            }
            tacheIndex++;
            tache.setDuree(tache.getDuree().minus(dureeSousTaches));
        }

        Tache t = new Tache((tache.getNom() + " " + tacheIndex.toString()), tache.getDuree(), tache.getPrio(), tache.getDeadline(), tache.getCategorie(), Etat.notRealized);

        Boolean planned = planifierAuto(t, libre, projet);
        isPlannedList.add(planned);
        if (planned) {
            planifiedTaches.add(t);
        }
        tache.setDuree(tache.getDuree().minus(t.getDuree()));

        // check if all the taches have been planned

        isPlanned = true;

        for (Boolean b : isPlannedList) {
            if (!b) {
                isPlanned = false;
                break;
            }
        }

        // if not, delete all the planned taches

        if (!isPlanned) {
            for (Tache tacheToDelete : planifiedTaches) {
                for (Jour jour : this.jours) {
                    for (Creneau creneau : jour.getCreneaux()) {
                        for (Tache creneauTache : creneau.getTaches()) {
                            if (tacheToDelete.getNom() == creneauTache.getNom() && tacheToDelete.cat == creneauTache.cat && tacheToDelete.getPrio() == creneauTache.getPrio() && tacheToDelete.getDuree() == creneauTache.getDuree() && tacheToDelete.getDeadline() == creneauTache.getDeadline()) {
                                UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
                                utilisateurDAO.deleteTache(tacheToDelete, this, jour, creneau);
                            }
                        }
                    }
                }
            }
        }

        return isPlanned;
    }


    public void addProjet(Projet projet) throws SQLException {
        this.projets.add(projet);

        try {
            UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
            utilisateurDAO.addProject(projet, this);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    /* public ArrayList<Creneau> findPossibleCreneaux(Tache tache, ArrayList<Creneau> creneauxLibres) {
        ArrayList<Creneau> possibleCreneaux = new ArrayList<>();

        for (Creneau creneau : creneauxLibres) {
            LocalDate creneauDate = creneau.getDebut().toLocalDate();
            LocalDate taskDeadline = LocalDate.parse(tache.getDeadline());

            if (taskDeadline.isAfter(creneauDate) && creneau.isLibre() == true) {
                possibleCreneaux.add(creneau);
            }
        }

        return possibleCreneaux;
    }

    // Méthodes de planification de tâches
    public void planifierManuelPlusieurs(ArrayList<Tache> taches, ArrayList<Creneau> creneauxLibres) {
        for (Tache tache : taches) {
            planifierManuel(tache, creneauxLibres);
        }
    }


    public void planifierManuel(Tache tache, ArrayList<Creneau> creneauxLibres) {

        ArrayList<Creneau> possibleCreneauxList = findPossibleCreneaux(tache, creneauxLibres);

        if (possibleCreneauxList.isEmpty()) {
            System.out.println("pas de creneaux disponibles");
        } else {
            System.out.println("Créneaux disponibles : ");
            for (int i = 0; i < possibleCreneauxList.size(); i++) {
                System.out.println((i + 1) + ". " + possibleCreneauxList.get(i));
            }

            Scanner scanner = new Scanner(System.in);
            System.out.println("Choisissez un créneau (1-" + possibleCreneauxList.size() + ") : ");
            int choix = scanner.nextInt();
            scanner.nextLine();

            Creneau creneauChoisi = creneauxLibres.get(choix - 1);

            if (tache instanceof Tsimple) {
                planifierTacheSimple((Tsimple) tache, creneauChoisi, possibleCreneauxList);

            } else if (tache instanceof Tcomposee) {
                System.out.println("hello compose");
                planifierTacheComposee((Tcomposee) tache, creneauChoisi, possibleCreneauxList);
            } else {
                System.out.println("Type de tâche non pris en charge.");
            }
        }
    }

    private void planifierTacheSimple(Tsimple tacheSimple, Creneau creneauChoisi, ArrayList<Creneau> creneauxLibres) {
        int periodicite = tacheSimple.getPeriodicite();
        int compteur = 0;
        boolean planifier;

        if ((tacheSimple.getDuree().compareTo(Duration.between(creneauChoisi.getDebut(), creneauChoisi.getFin())) <= 0) && (creneauChoisi.isLibre() == true)) {
            planifier = true;
        } else {
            planifier = false;
        }

        if (planifier == true) {
            System.out.println("hello1");
            if (creneauChoisi.estDecomposable(creneauChoisi.getDuree(), tacheSimple.getDuree())) {
                System.out.println("hello2");
                creneauChoisi.ajouterTache(tacheSimple);
                taches.add(tacheSimple);
                System.out.println("La tâche " + tacheSimple.getNom() + " a été planifiée dans le créneau : " + creneauChoisi);
                System.out.println("hello3");

                if (periodicite > 0 && creneauChoisi.estPeriodeLibre(periodicite)) {
                    LocalDateTime date = creneauChoisi.getDebut().plusDays(periodicite);

                    System.out.println("Tâches planifiées périodiques :");
                    // Boucle périodicité
                    for (int i = 1; i <= 4; i++) {
                        compteur = compteur + periodicite;
                        LocalDateTime nextStartTime = creneauChoisi.getDebut().plusDays(compteur);
                        LocalDateTime nextEndTime = nextStartTime.plus(Duration.between(creneauChoisi.getDebut(), creneauChoisi.getFin()));

                        Creneau repeatedCreneau = new Creneau(nextStartTime, nextEndTime);
                        if (repeatedCreneau.isLibre()) {
                            repeatedCreneau.ajouterTache(tacheSimple);
                            System.out.println("La tâche " + tacheSimple.getNom() + " a été planifiée dans le créneau répété : " + repeatedCreneau);
                        }
                    }
                }
            } else {
                System.out.println("le creneau ne peut pas etre decompose");
            }
        } else {
            System.out.println("hello4");
            System.out.println("Le créneau choisi n'a pas une durée suffisante pour planifier la tâche : " + tacheSimple.getNom());

        }
        creneauChoisi.setLibre(false);
    } */


    /* public void planifierTacheComposee(Tcomposee tacheComposee, Creneau creneauChoisi, ArrayList<Creneau> creneauxLibres) {
        ArrayList<Tache> sousTaches = new ArrayList<>();
        ArrayList<Creneau> creneauxAssignes = new ArrayList<>();
        Creneau creneauDispo = null;
        boolean decomposition = false;

        if (creneauChoisi.isLibre() == true) {
            if ((tacheComposee.getDuree().compareTo(Duration.between(creneauChoisi.getDebut(), creneauChoisi.getFin())) <= 0)) {

                if (creneauChoisi.estDecomposable(creneauChoisi.getDuree(), tacheComposee.getDuree()) == false) { // le creneau ne peut pas etre decomposee ==> utiliser tout le creneau
                    System.out.println("xxxx");
                    this.manipuler_creneau_decomposable(tacheComposee, creneauChoisi);


                } else { // le creneau peut etre decompose donc utiliser le nouveau creneau decomposer
                    System.out.println("zzzz");
                    this.manipuler_creneau_decomposable(tacheComposee, creneauChoisi);

                }
            } else {
                decomposition = true;
            }


        } else {
            System.out.println("le creneau choisi est occupe veuillez choisir un autre creneau");
        }


        if (decomposition == true) { //decompsition occured
            Duration dureeRestante = tacheComposee.getDuree();

            for (Creneau creneau : creneauxLibres) {
                if (creneau.getDuree().compareTo(dureeRestante) >= 0) {
                    Tsimple tacheSimple = new Tsimple(tacheComposee.getNom(), dureeRestante, tacheComposee.getPrio(), tacheComposee.getDeadline());
                    this.manipuler2(tacheSimple, creneau);
                    creneau.ajouterTache(tacheSimple);
                    sousTaches.add(tacheSimple);
                    creneauxAssignes.add(creneau);
                    dureeRestante = Duration.ZERO;
                    break;
                } else {
                    System.out.println("here");
                    Tsimple tacheSimple = new Tsimple(tacheComposee.getNom(), creneau.getDuree(), tacheComposee.getPrio(), tacheComposee.getDeadline());
                    this.manipuler2(tacheSimple, creneau);
                    creneau.ajouterTache(tacheSimple);
                    sousTaches.add(tacheSimple);
                    creneauxAssignes.add(creneau);
                    dureeRestante = dureeRestante.minus(creneau.getDuree());
                }
            }
            System.out.println("La tâche " + tacheComposee.getNom() + " a été décomposée en " + sousTaches.size() + " sous-tâches :");
            for (int i = 0; i < sousTaches.size(); i++) {
                Tache sousTache = sousTaches.get(i);
                Creneau creneauAssigne = creneauxAssignes.get(i);
                System.out.println(sousTache.getNom() + i + " - " + creneauAssigne);
            }

        }
        creneauChoisi.setLibre(false);
    } */


    // traiter les creneaux decomposables dans une tache composee qui ne va pas etre decomposee (creneau disponible)
    private void manipuler_creneau_decomposable(Tcomposee tacheComposee, Creneau creneauChoisi) {

        System.out.println("hello2");
        creneauChoisi.ajouterTache(tacheComposee);
        taches.add(tacheComposee);
        System.out.println("La tâche " + tacheComposee.getNom() + " a été planifiée dans le créneau : " + creneauChoisi);

    }

    // // traiter les creneaux decomposables dans une tache composee qui sera decomposee (creneau non disponible)
    private void manipuler2(Tsimple tacheSimple, Creneau creneau) {
        if (creneau.estDecomposable(creneau.getDuree(), tacheSimple.getDuree()) == false) {
            System.out.println("uuuuu");

            creneau.ajouterTache(tacheSimple);
            taches.add(tacheSimple);

        } else {
            System.out.println("vvvvvv");
            creneau.ajouterTache(tacheSimple);
            taches.add(tacheSimple);
        }

    }


    /* public void planifierAutomatiquePlusieurs(ArrayList<Tache> taches, ArrayList<Creneau> creneauxLibres) {
        for (Tache tache : taches) {
            planifierAutomatique(tache, creneauxLibres);
        }
    }

    public void planifierAutomatique(Tache tache, ArrayList<Creneau> creneauxLibres) {
        ArrayList<Creneau> possibleCreneauxList = findPossibleCreneaux(tache, creneauxLibres);

        if (possibleCreneauxList.isEmpty()) {
            System.out.println("pas de creneaux disponibles");
        } else {

            if (tache instanceof Tsimple) {
                Tsimple tacheSimple = (Tsimple) tache;
                planifierTacheSimpleAutomatique(tacheSimple, creneauxLibres);
            } else if (tache instanceof Tcomposee) {
                Tcomposee tacheComposee = (Tcomposee) tache;
                planifierTacheComposeeAutomatique(tacheComposee, creneauxLibres);
            } else {
                System.out.println("La tâche n'est ni une tâche simple ni une tâche composée.");
            }
        }
    } */

    /*private void planifierTacheSimpleAutomatique(Tsimple tacheSimple, ArrayList<Creneau> creneauxLibres) {
        int periodicite = tacheSimple.getPeriodicite();
        int compteur = 0;
        boolean planifier;
        boolean isPlanned = false;

        for (Creneau creneau : creneauxLibres) {
            if ((tacheSimple.getDuree().compareTo(Duration.between(creneau.getDebut(), creneau.getFin())) <= 0) && (creneau.isLibre() == true)) {
                planifier = true;
            } else {
                planifier = false;
            }


            if (planifier == true) {
                System.out.println("hello0");
                if (creneau.estDecomposable(creneau.getDuree(), tacheSimple.getDuree())) {
                    System.out.println("hello2");
                    creneau.ajouterTache(tacheSimple);
                    taches.add(tacheSimple);
                    System.out.println("La tâche " + tacheSimple.getNom() + " a été planifiée dans le créneau : " + creneau);
                    creneau.setLibre(false);
                    System.out.println("hello3");

                    if (periodicite > 0 && creneau.estPeriodeLibre(periodicite)) {
                        LocalDateTime date = creneau.getDebut().plusDays(periodicite);

                        System.out.println("Tâches planifiées périodiques :");
                        // Boucle périodicité
                        for (int i = 1; i <= 4; i++) {
                            compteur = compteur + periodicite;
                            LocalDateTime nextStartTime = creneau.getDebut().plusDays(compteur);
                            LocalDateTime nextEndTime = nextStartTime.plus(Duration.between(creneau.getDebut(), creneau.getFin()));

                            Creneau repeatedCreneau = new Creneau(nextStartTime, nextEndTime);
                            if (repeatedCreneau.isLibre()) {
                                repeatedCreneau.ajouterTache(tacheSimple);
                                System.out.println("La tâche " + tacheSimple.getNom() + " a été planifiée dans le créneau répété : " + repeatedCreneau);
                            }

                        }
                    }

                    isPlanned = true;
                    break;
                }


            }
        }


        if (!isPlanned) {
            System.out.println("Aucun créneau disponible ne peut accueillir la tâche : " + tacheSimple.getNom());
        }


    } */

    /* public void planifierTacheComposeeAutomatique(Tcomposee tacheComposee, ArrayList<Creneau> creneauxLibres) {

        ArrayList<Tache> sousTaches = new ArrayList<>();
        ArrayList<Creneau> creneauxAssignes = new ArrayList<>();
        Creneau creneauDispo = null;
        boolean decomposition = false;
        for (Creneau creneau : creneauxLibres) {
            if (creneau.isLibre() == true) {
                if ((tacheComposee.getDuree().compareTo(Duration.between(creneau.getDebut(), creneau.getFin())) <= 0)) {

                    if (creneau.estDecomposable(creneau.getDuree(), tacheComposee.getDuree()) == false) { // le creneau ne peut pas etre decomposee ==> utiliser tout le creneau
                        System.out.println("xxxx");
                        this.manipuler_creneau_decomposable(tacheComposee, creneau);
                        creneau.setLibre(false);
                        decomposition = false;
                        break;
                    } else { // le creneau peut etre decompose donc utiliser le nouveau creneau decomposer
                        System.out.println("zzzz");
                        this.manipuler_creneau_decomposable(tacheComposee, creneau);
                        creneau.setLibre(false);
                        decomposition = false;
                        break;
                    }
                } else {
                    decomposition = true;
                }


            } else {
                System.out.println("le creneau choisi est occupe veuillez choisir un autre creneau");
            }

        }

        if (decomposition == true) { //decompsition occured
            Duration dureeRestante = tacheComposee.getDuree();

            for (Creneau creneau : creneauxLibres) {
                if (creneau.getDuree().compareTo(dureeRestante) >= 0) {
                    Tsimple tacheSimple = new Tsimple(tacheComposee.getNom(), dureeRestante, tacheComposee.getPrio(), tacheComposee.getDeadline());
                    this.manipuler2(tacheSimple, creneau);
                    creneau.ajouterTache(tacheSimple);
                    sousTaches.add(tacheSimple);
                    creneauxAssignes.add(creneau);
                    dureeRestante = Duration.ZERO;
                    break;
                } else {
                    System.out.println("here");
                    Tsimple tacheSimple = new Tsimple(tacheComposee.getNom(), creneau.getDuree(), tacheComposee.getPrio(), tacheComposee.getDeadline());
                    this.manipuler2(tacheSimple, creneau);
                    creneau.ajouterTache(tacheSimple);
                    sousTaches.add(tacheSimple);
                    creneauxAssignes.add(creneau);
                    dureeRestante = dureeRestante.minus(creneau.getDuree());
                }
            }
            System.out.println("La tâche " + tacheComposee.getNom() + " a été décomposée en " + sousTaches.size() + " sous-tâches :");
            for (int i = 0; i < sousTaches.size(); i++) {
                Tache sousTache = sousTaches.get(i);
                Creneau creneauAssigne = creneauxAssignes.get(i);
                System.out.println(sousTache.getNom() + i + " - " + creneauAssigne);
            }

        }
    } */


    /*public void planifierManuelPeriode(ArrayList<Tache> taches, ArrayList<Creneau> creneauxLibres, LocalDate debut, LocalDate fin) {
        ArrayList<Creneau> creneauxValides = new ArrayList<>();

        for (Creneau creneau : creneauxLibres) {
            LocalDate creneauDebutDate = creneau.getDebut().toLocalDate();
            LocalDate creneauFinDate = creneau.getFin().toLocalDate();

            if (creneauDebutDate.isAfter(debut) && creneauFinDate.isBefore(fin)) {
                creneauxValides.add(creneau);
            }
        }

        planifierManuelPlusieurs(taches, creneauxValides);
    }

    public void planifierAutomatiquePeriode(ArrayList<Tache> taches, ArrayList<Creneau> creneauxLibres, LocalDate debut, LocalDate fin) {
        ArrayList<Creneau> creneauxValides = new ArrayList<>();

        for (Creneau creneau : creneauxLibres) {
            LocalDate creneauDebutDate = creneau.getDebut().toLocalDate();
            LocalDate creneauFinDate = creneau.getFin().toLocalDate();

            if (creneauDebutDate.isAfter(debut) && creneauFinDate.isBefore(fin)) {
                creneauxValides.add(creneau);
            }
        }

        planifierAutomatiquePlusieurs(taches, creneauxValides);
    }


    public void ajouterCreneauLibre(Scanner sc, LocalDate StartDate, LocalDate EndDate) {
        System.out.println("Entrez la date et l'heure de début du créneau libre (format: yyyy-MM-dd HH:mm:ss): ");
        String debutStr = sc.nextLine();
        LocalDateTime debut = LocalDateTime.parse(debutStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        System.out.println("Entrez la date et l'heure de fin du créneau libre (format: yyyy-MM-dd HH:mm:ss): ");
        String finStr = sc.nextLine();
        LocalDateTime fin = LocalDateTime.parse(finStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // Check if the start and end dates of the creneau libre are within the specified range
        if (debut.toLocalDate().isBefore(StartDate) || fin.toLocalDate().isAfter(EndDate)) {
            System.out.println("Les dates de début et de fin du créneau libre doivent être comprises entre "
                    + StartDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + " et "
                    + EndDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + ".");
        } else {
            Creneau creneauLibre = new Creneau(debut, fin, true);
            Jour.addCreneauLibre(creneauLibre);
        }
    } */
  
 
 /*    public void ajouterCreneauLibre(Scanner sc,LocalDate StartDate, LocalDate EndDate) {
    	    System.out.println("Entrez la date et l'heure de début du créneau libre (format: yyyy-MM-dd HH:mm:ss): ");
    	    String debutStr = sc.nextLine();
    	    LocalDateTime debut = LocalDateTime.parse(debutStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    	    System.out.println("Entrez la date et l'heure de fin du créneau libre (format: yyyy-MM-dd HH:mm:ss): ");
    	    String finStr = sc.nextLine();
    	    LocalDateTime fin = LocalDateTime.parse(finStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    	    Creneau creneauLibre = new Creneau(debut, fin, true);
    	    Jour.addCreneauLibre(creneauLibre);
    	}
   */

    // set duree minimale of creneau
    public void setDureeMinimaleCreneau() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("donnez la duree minimale");
        int min = scanner.nextInt();
        Duration duration = Duration.ofMinutes(min);
        //Creneau.setDureeMinimale(duration);
    }

    // parcourir le tableau des creneaux pour trouver si une tache se trouve dedans
    public Creneau getCreneauForTache(Tache tache, ArrayList<Creneau> creneauxLibres) {
        for (Creneau creneau : creneauxLibres) {
            if (creneau.getTaches().contains(tache)) {
                return creneau;
            }
        }
        return null; // Return null if the creneau is not found
    }


    public void nombre_tache_a_realiser_par_jour() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the day (dd): ");
        int day = scanner.nextInt();

        System.out.println("Enter the month (MM): ");
        int month = scanner.nextInt();

        System.out.println("Enter the year (yyyy): ");
        int year = scanner.nextInt();

        System.out.println("Enter the number of minimum tasks: ");
        int nombreMinimumTaches = scanner.nextInt();

        LocalDate date = LocalDate.of(year, month, day);
        Jour jour = new Jour(date);
        jour.setNombreMinimumTaches(nombreMinimumTaches);

        jours.add(jour);

        System.out.println("Jour: " + jour.getDate());
        System.out.println("Nombre Minimum Taches: " + jour.getNombreMinimumTaches());
    }

    public void encouragement(Jour jour) {
        int nombreTachesRealisees = 0;

        // Iterate over the creneaux of the jour and count the tasks done
        for (Creneau creneau : jour.getCreneaux()) {
            for (Tache tache : creneau.getTaches()) {
                if (tache.getEtat().equals("completed")) {
                    nombreTachesRealisees++;
                }
            }
        }
        System.out.println(nombreTachesRealisees);
        // Check if the tasks done meet the minimum requirement
        if (nombreTachesRealisees >= jour.getNombreMinimumTaches()) {
            System.out.println("FÉLICITATIONS ! Vous avez réalisé le nombre minimum de tâches par jour.");
        }
    }


    public void updateEtatTache(String nomTache, Etat etat) {
        for (Tache tache : taches) {
            if (tache.getNom().equals(nomTache)) {
                tache.setEtat(etat);
                System.out.println("L'état de la tâche " + nomTache + " a été mis à jour : " + etat);
                return;
            }
        }
        System.out.println("Aucune tâche trouvée avec le nom : " + nomTache);
    }


}
