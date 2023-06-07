package tp.javafx.models;

import tp.javafx.data.DatabaseManager;

import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;

public class UtilisateurDAO {

    private Connection connection;

    public UtilisateurDAO() {
        DatabaseManager databaseManager = new DatabaseManager();
        this.connection = databaseManager.getConnection();
    }

    public Boolean addUser(Utilisateur user) throws SQLException {
        if (isPseudoUnique(user.getPseudo())) {
            // Insert into table Categorie a new Categorie with the name of the category and the color
            String sql = "INSERT INTO Utilisateur (pseudo, password, dateDebut, dateFin, nbTaches,  badgeGood,  badgeVeryGood,  badgeExcellent) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            int nbGoodBadges = 0;
            for (Badge badge : user.getBadges()) {
                if (badge.equals(Badge.Good)) {
                    nbGoodBadges++;
                }
            }
            int nbVeryGoodBadges = 0;
            for (Badge badge : user.getBadges()) {
                if (badge.equals(Badge.VeryGood)) {
                    nbVeryGoodBadges++;
                }
            }
            int nbExcellentBadges = 0;
            for (Badge badge : user.getBadges()) {
                if (badge.equals(Badge.Excellent)) {
                    nbExcellentBadges++;
                }
            }

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, user.getPseudo());
                statement.setString(2, user.getPassword());
                statement.setString(3, user.getDateDebut().toString());
                statement.setString(4, user.getDateFin().toString());
                statement.setInt(5, user.getNbTaches());
                statement.setInt(6, nbGoodBadges);
                statement.setInt(7, nbVeryGoodBadges);
                statement.setInt(8, nbExcellentBadges);
                statement.executeUpdate();
            }
            System.out.println("User added successfully!");

            String query = "SELECT id FROM Utilisateur WHERE pseudo = ?";
            int id = 0;

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, user.getPseudo());

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        id = resultSet.getInt(1);
                    }
                }
            }

            Categorie categorie = new Categorie(user.getCategories().get(0).getNom_cat(), user.getCategories().get(0).getCouleur());
            String sqlCategorie = "INSERT INTO Categorie (utilisateur_id, nom_cat, couleur) VALUES (?, ?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(sqlCategorie)) {
                statement.setInt(1, id);
                statement.setString(2, categorie.getNom_cat());
                statement.setString(3, categorie.getCouleur());
                statement.executeUpdate();
            }
            System.out.println("Categorie added successfully!");

            Jour jour = new Jour(user.getJours().get(0).getDate());

            String sqlJour = "INSERT INTO Jour (utilisateur_id, date) VALUES (?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(sqlJour)) {
                statement.setInt(1, id);
                statement.setString(2, jour.getDate().toString());
                statement.executeUpdate();
            }

            return Boolean.TRUE;
        } else {
            System.out.println("User with the same pseudo already exists!");
            return Boolean.FALSE;
        }
    }

    public boolean logUser(Utilisateur user) throws SQLException {
        String query = "SELECT COUNT(*) FROM Utilisateur WHERE pseudo = ? AND password = ?";
        int count = 0;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getPseudo());
            statement.setString(2, user.getPassword());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    count = resultSet.getInt(1);
                }
            }
        }

        return count == 1;
    }

    public Utilisateur getUser(String pseudo) throws SQLException {
        String query = "SELECT * FROM Utilisateur WHERE pseudo = ?";

        Utilisateur user = null;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, pseudo);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // get the Projects of the user
                    String queryProjects = "SELECT * FROM Projet WHERE utilisateur_id = ?";
                    int id = resultSet.getInt("id");

                    ArrayList<Projet> projets = new ArrayList<>();

                    try (PreparedStatement statementProjects = connection.prepareStatement(queryProjects)) {
                        statementProjects.setInt(1, id);

                        try (ResultSet resultSetProjects = statementProjects.executeQuery()) {
                            while (resultSetProjects.next()) {
                                // get taches of the project

                                String queryTaches = "SELECT * FROM Tache WHERE projet_id = ?";
                                int idProjet = resultSetProjects.getInt("id");

                                ArrayList<Tache> taches = new ArrayList<>();

                                try (PreparedStatement statementTaches = connection.prepareStatement(queryTaches)) {
                                    statementTaches.setInt(1, idProjet);

                                    try (ResultSet resultSetTaches = statementTaches.executeQuery()) {
                                        while (resultSetTaches.next()) {

                                            // get categorie of the tache

                                            String queryCategorie = "SELECT * FROM Categorie WHERE id = ?";

                                            Categorie categorie = null;

                                            try (PreparedStatement statementCategorie = connection.prepareStatement(queryCategorie)) {
                                                statementCategorie.setInt(1, resultSetTaches.getInt("categorie_id"));

                                                try (ResultSet resultSetCategorie = statementCategorie.executeQuery()) {
                                                    if (resultSetCategorie.next()) {
                                                        categorie = new Categorie(resultSetCategorie.getString("nom_cat"), resultSetCategorie.getString("couleur"));
                                                    }
                                                }
                                            }

                                            Tache tache = new Tache(resultSetTaches.getString("nom"), Duration.parse(resultSetTaches.getString("duree")), Priorite.valueOf(resultSetTaches.getString("prio")), LocalDate.parse(resultSetTaches.getString("deadline")), categorie, Etat.valueOf(resultSetTaches.getString("etat")));
                                            taches.add(tache);
                                        }
                                    }
                                }

                                Projet projet = new Projet(resultSetProjects.getString("nom"), resultSetProjects.getString("description"), taches);
                                projets.add(projet);
                            }
                        }
                    }

                    // get the Categories of the user

                    String queryCategories = "SELECT * FROM Categorie WHERE utilisateur_id = ?";

                    ArrayList<Categorie> categories = new ArrayList<>();

                    try (PreparedStatement statementCategories = connection.prepareStatement(queryCategories)) {
                        statementCategories.setInt(1, id);

                        try (ResultSet resultSetCategories = statementCategories.executeQuery()) {
                            while (resultSetCategories.next()) {
                                Categorie categorie = new Categorie(resultSetCategories.getString("nom_cat"), resultSetCategories.getString("couleur"));
                                categories.add(categorie);
                            }
                        }
                    }

                    // get the Jours of the user

                    String queryJours = "SELECT * FROM Jour WHERE utilisateur_id = ?";

                    ArrayList<Jour> jours = new ArrayList<>();

                    try (PreparedStatement statementJours = connection.prepareStatement(queryJours)) {
                        statementJours.setInt(1, id);
                        try (ResultSet resultSetJours = statementJours.executeQuery()) {
                            while (resultSetJours.next()) {
                                Jour jour = new Jour(LocalDate.parse(resultSetJours.getString("date")));
                                // get creneaux of the jour

                                String queryCreneaux = "SELECT * FROM Creneau WHERE jour_id = ?";

                                ArrayList<Creneau> creneaux = new ArrayList<>();

                                try (PreparedStatement statementCreneaux = connection.prepareStatement(queryCreneaux)) {

                                    statementCreneaux.setInt(1, resultSetJours.getInt("id"));

                                    try (ResultSet resultSetCreneaux = statementCreneaux.executeQuery()) {
                                        while (resultSetCreneaux.next()) {

                                            Creneau creneau = new Creneau(LocalTime.parse(resultSetCreneaux.getString("debut")), LocalTime.parse(resultSetCreneaux.getString("fin")), resultSetCreneaux.getBoolean("libre"), Duration.parse(resultSetCreneaux.getString("dureeMinimale")));

                                            // get taches of the creneau

                                            String queryTaches = "SELECT * FROM Tache WHERE creneau_id = ?";

                                            ArrayList<Tache> taches = new ArrayList<>();

                                            try (PreparedStatement statementTaches = connection.prepareStatement(queryTaches)) {
                                                statementTaches.setInt(1, resultSetCreneaux.getInt("id"));

                                                try (ResultSet resultSetTaches = statementTaches.executeQuery()) {
                                                    while (resultSetTaches.next()) {

                                                        // get categorie of the tache

                                                        String queryCategorie = "SELECT * FROM Categorie WHERE id = ?";

                                                        Categorie categorie = null;

                                                        try (PreparedStatement statementCategorie = connection.prepareStatement(queryCategorie)) {
                                                            statementCategorie.setInt(1, resultSetTaches.getInt("categorie_id"));

                                                            try (ResultSet resultSetCategorie = statementCategorie.executeQuery()) {
                                                                if (resultSetCategorie.next()) {
                                                                    categorie = new Categorie(resultSetCategorie.getString("nom_cat"), resultSetCategorie.getString("couleur"));
                                                                }
                                                            }
                                                        }

                                                        Tache tache = new Tache(resultSetTaches.getString("nom"), Duration.parse(resultSetTaches.getString("duree")), Priorite.valueOf(resultSetTaches.getString("prio")), LocalDate.parse(resultSetTaches.getString("deadline")), categorie, Etat.valueOf(resultSetTaches.getString("etat")));
                                                        taches.add(tache);
                                                    }
                                                }
                                            }

                                            creneau.setTaches(taches);
                                            creneaux.add(creneau);
                                        }
                                    }
                                }

                                jour.setCreneaux(creneaux);
                                jours.add(jour);
                            }
                        }
                    }

                    ArrayList<Badge> badges = new ArrayList<>();

                    Integer badgeGood = resultSet.getInt("badgeGood");
                    Integer badgeVeryGood = resultSet.getInt("badgeVeryGood");
                    Integer badgeExcellent = resultSet.getInt("badgeExcellent");

                    for (int i = 0; i < badgeGood; i++) {
                        badges.add(Badge.Good);
                    }

                    for (int i = 0; i < badgeVeryGood; i++) {
                        badges.add(Badge.VeryGood);
                    }

                    for (int i = 0; i < badgeExcellent; i++) {
                        badges.add(Badge.Excellent);
                    }

                    jours.sort(Comparator.comparing(Jour::getDate));

                    user = new Utilisateur(resultSet.getString("pseudo"), projets, categories, jours, LocalDate.parse(resultSet.getString("dateDebut")), LocalDate.parse(resultSet.getString("dateFin")), badges, resultSet.getInt("nbTaches"));
                }
            }
        }

        return user;
    }

    public void setConfiguration(Utilisateur user) throws SQLException {

        // get id of the user

        String queryId = "SELECT * FROM Utilisateur WHERE pseudo = ?";

        int id = 0;

        try (PreparedStatement statementId = connection.prepareStatement(queryId)) {
            statementId.setString(1, user.getPseudo());

            try (ResultSet resultSetId = statementId.executeQuery()) {
                if (resultSetId.next()) {
                    id = resultSetId.getInt("id");
                }
            }
        }

        // get the entire old user

        Utilisateur oldUser = getUser(user.getPseudo());

        // Delete all past data

        String queryDeleteTaches = "DELETE FROM Tache WHERE creneau_id IN (SELECT id FROM Creneau WHERE jour_id IN (SELECT id FROM Jour WHERE utilisateur_id = ?))";

        try (PreparedStatement statementDeleteTaches = connection.prepareStatement(queryDeleteTaches)) {
            statementDeleteTaches.setInt(1, id);

            statementDeleteTaches.executeUpdate();
        }

        String queryDeleteCreneaux = "DELETE FROM Creneau WHERE jour_id IN (SELECT id FROM Jour WHERE utilisateur_id = ?)";

        try (PreparedStatement statementDeleteCreneaux = connection.prepareStatement(queryDeleteCreneaux)) {
            statementDeleteCreneaux.setInt(1, id);

            statementDeleteCreneaux.executeUpdate();
        }

        String queryDeleteJours = "DELETE FROM Jour WHERE utilisateur_id = ?";

        try (PreparedStatement statementDeleteJours = connection.prepareStatement(queryDeleteJours)) {
            statementDeleteJours.setInt(1, id);

            statementDeleteJours.executeUpdate();
        }

        // insert new data

        // insert new jours

        String queryInsertJours = "INSERT INTO Jour (date, utilisateur_id) VALUES (?, ?)";

        try (PreparedStatement statementInsertJours = connection.prepareStatement(queryInsertJours)) {
            for (Jour jour : user.getJours()) {
                statementInsertJours.setString(1, jour.getDate().toString());
                statementInsertJours.setInt(2, id);
                statementInsertJours.executeUpdate();

                // insert new creneaux

                String queryInsertCreneaux = "INSERT INTO Creneau (debut, fin, libre, dureeMinimale, jour_id) VALUES (?, ?, ?, ?, (SELECT id FROM Jour WHERE date = ? AND utilisateur_id = ?))";

                try (PreparedStatement statementInsertCreneaux = connection.prepareStatement(queryInsertCreneaux)) {
                    for (Creneau creneau : jour.getCreneaux()) {
                        statementInsertCreneaux.setString(1, creneau.getDebut().toString());
                        statementInsertCreneaux.setString(2, creneau.getFin().toString());
                        statementInsertCreneaux.setBoolean(3, creneau.isLibre());
                        statementInsertCreneaux.setString(4, creneau.getDureeMinimale().toString());
                        statementInsertCreneaux.setString(5, jour.getDate().toString());
                        statementInsertCreneaux.setInt(6, id);
                        statementInsertCreneaux.executeUpdate();

                        // insert new taches

                        String queryInsertTaches = "INSERT INTO Tache (nom, duree, prio, deadline, categorie_id, etat, creneau_id) VALUES (?, ?, ?, ?, (SELECT id FROM Categorie WHERE nom_cat = ?), ?, (SELECT id FROM Creneau WHERE debut = ? AND fin = ? AND jour_id = (SELECT id FROM Jour WHERE date = ? AND utilisateur_id = ?)))";

                        try (PreparedStatement statementInsertTaches = connection.prepareStatement(queryInsertTaches)) {
                            for (Tache tache : creneau.getTaches()) {
                                statementInsertTaches.setString(1, tache.getNom());
                                statementInsertTaches.setString(2, tache.getDuree().toString());
                                statementInsertTaches.setString(3, tache.getPrio().toString());
                                statementInsertTaches.setString(4, tache.getDeadline().toString());
                                statementInsertTaches.setString(5, tache.getCategorie().getNom_cat());
                                statementInsertTaches.setString(6, tache.getEtat().toString());
                                statementInsertTaches.setString(7, creneau.getDebut().toString());
                                statementInsertTaches.setString(8, creneau.getFin().toString());
                                statementInsertTaches.setString(9, jour.getDate().toString());
                                statementInsertTaches.setInt(10, id);
                                statementInsertTaches.executeUpdate();
                            }
                        }
                    }
                }
            }
        }

        // update the user with the new dates

        // update the user

        String userQuery = "UPDATE Utilisateur SET dateDebut = ?, dateFin = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(userQuery)) {
            statement.setString(1, user.getDateDebut().toString());
            statement.setString(2, user.getDateFin().toString());
            statement.setInt(3, id);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void addTache(Tache tache, Utilisateur user, Jour jour, Creneau creneau) throws SQLException {

        // get id of the user

        String queryId = "SELECT * FROM Utilisateur WHERE pseudo = ?";

        int id = 0;

        try (PreparedStatement statementId = connection.prepareStatement(queryId)) {
            statementId.setString(1, user.getPseudo());

            try (ResultSet resultSetId = statementId.executeQuery()) {
                if (resultSetId.next()) {
                    id = resultSetId.getInt("id");
                }
            }
        }

        // add tache that refrences the creneau that refrences the jour that refrences the user

        String query = "INSERT INTO Tache (nom, duree, prio, deadline, categorie_id, etat, creneau_id) VALUES (?, ?, ?, ?, (SELECT id FROM Categorie WHERE nom_cat = ?), ?, (SELECT id FROM Creneau WHERE debut = ? AND fin = ? AND jour_id = (SELECT id FROM Jour WHERE date = ? AND utilisateur_id = ?)))";

        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, tache.getNom());
            statement.setString(2, tache.getDuree().toString());
            statement.setString(3, tache.getPrio().toString());
            statement.setString(4, tache.getDeadline().toString());
            statement.setString(5, tache.getCategorie().getNom_cat());
            statement.setString(6, tache.getEtat().toString());
            statement.setString(7, creneau.getDebut().minus(tache.getDuree()).toString());
            statement.setString(8, creneau.getFin().toString());
            statement.setString(9, jour.getDate().toString());
            statement.setInt(10, id);

            statement.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }

        // update creneau libre and debut

        String queryCreneau = "UPDATE Creneau SET libre = ?, debut = ? WHERE debut = ? AND fin = ? AND jour_id = (SELECT id FROM Jour WHERE date = ? AND utilisateur_id = ?)";

        try (PreparedStatement statementCreneau = connection.prepareStatement(queryCreneau)) {
            statementCreneau.setBoolean(1, false);
            statementCreneau.setString(2, creneau.getDebut().toString());
            statementCreneau.setString(3, creneau.getDebut().minus(tache.getDuree()).toString());
            statementCreneau.setString(4, creneau.getFin().toString());
            statementCreneau.setString(5, jour.getDate().toString());
            statementCreneau.setInt(6, id);

            statementCreneau.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void addTacheProjet(Tache tache, Utilisateur user, Jour jour, Creneau creneau, Projet projet) throws SQLException {
        // get id of the user

        String queryId = "SELECT * FROM Utilisateur WHERE pseudo = ?";

        int id = 0;

        try (PreparedStatement statementId = connection.prepareStatement(queryId)) {
            statementId.setString(1, user.getPseudo());

            try (ResultSet resultSetId = statementId.executeQuery()) {
                if (resultSetId.next()) {
                    id = resultSetId.getInt("id");
                }
            }
        }

        // add tache that refrences the creneau && the project id that refrences the jour that refrences the user

        String query = "INSERT INTO Tache (nom, duree, prio, deadline, categorie_id, etat, creneau_id, projet_id) VALUES (?, ?, ?, ?, (SELECT id FROM Categorie WHERE nom_cat = ?), ?, (SELECT id FROM Creneau WHERE debut = ? AND fin = ? AND jour_id = (SELECT id FROM Jour WHERE date = ? AND utilisateur_id = ?)), (SELECT id FROM Projet WHERE nom = ? AND utilisateur_id = ?))";

        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, tache.getNom());
            statement.setString(2, tache.getDuree().toString());
            statement.setString(3, tache.getPrio().toString());
            statement.setString(4, tache.getDeadline().toString());
            statement.setString(5, tache.getCategorie().getNom_cat());
            statement.setString(6, tache.getEtat().toString());
            statement.setString(7, creneau.getDebut().minus(tache.getDuree()).toString());
            statement.setString(8, creneau.getFin().toString());
            statement.setString(9, jour.getDate().toString());
            statement.setInt(10, id);
            statement.setString(11, projet.getNom());
            statement.setInt(12, id);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // update creneau libre and debut

        String queryCreneau = "UPDATE Creneau SET libre = ?, debut = ? WHERE debut = ? AND fin = ? AND jour_id = (SELECT id FROM Jour WHERE date = ? AND utilisateur_id = ?)";

        try (PreparedStatement statementCreneau = connection.prepareStatement(queryCreneau)) {
            statementCreneau.setBoolean(1, false);
            statementCreneau.setString(2, creneau.getDebut().toString());
            statementCreneau.setString(3, creneau.getDebut().minus(tache.getDuree()).toString());
            statementCreneau.setString(4, creneau.getFin().toString());
            statementCreneau.setString(5, jour.getDate().toString());
            statementCreneau.setInt(6, id);

            statementCreneau.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // functions to update tache with new etat

    // add categorie

    public void addCategorie(Categorie categorie, Utilisateur user) throws SQLException {

        // get id of the user

        String queryId = "SELECT * FROM Utilisateur WHERE pseudo = ?";

        int id = 0;

        try (PreparedStatement statementId = connection.prepareStatement(queryId)) {
            statementId.setString(1, user.getPseudo());

            try (ResultSet resultSetId = statementId.executeQuery()) {
                if (resultSetId.next()) {
                    id = resultSetId.getInt("id");
                }
            }
        }

        // add categorie

        String query = "INSERT INTO Categorie (nom_cat, couleur, utilisateur_id) VALUES (?, ? ,?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, categorie.getNom_cat());
            statement.setString(2, categorie.getCouleur());
            statement.setInt(3, id);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTache(Tache tache, Utilisateur user, Creneau creneau, Jour jour) throws SQLException {

        // find id of the user

        String queryId = "SELECT * FROM Utilisateur WHERE pseudo = ?";

        int id = 0;

        try (PreparedStatement statementId = connection.prepareStatement(queryId)) {
            statementId.setString(1, user.getPseudo());

            try (ResultSet resultSetId = statementId.executeQuery()) {
                if (resultSetId.next()) {
                    id = resultSetId.getInt("id");
                }
            }
        }

        // update tache

        String query = "UPDATE Tache SET etat = ? WHERE nom = ? AND duree = ? AND prio = ? AND deadline = ? AND categorie_id = (SELECT id FROM Categorie WHERE nom_cat = ?) AND creneau_id = (SELECT id FROM Creneau WHERE debut = ? AND fin = ? AND jour_id = (SELECT id FROM Jour WHERE date = ? AND utilisateur_id = ?))";

        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, tache.getEtat().toString());
            statement.setString(2, tache.getNom());
            statement.setString(3, tache.getDuree().toString());
            statement.setString(4, tache.getPrio().toString());
            statement.setString(5, tache.getDeadline().toString());
            statement.setString(6, tache.getCategorie().getNom_cat());
            statement.setString(7, creneau.getDebut().toString());
            statement.setString(8, creneau.getFin().toString());
            statement.setString(9, jour.getDate().toString());
            statement.setInt(10, id);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        Integer badgeGood = 0;
        for (Badge badge : user.getBadges()) {
            // user has badge
            System.out.println(badge);
            if (badge.equals(Badge.Good)) {
                //System.out.println("badgeGood");
                System.out.println(badge);
                badgeGood++;
            }
        }

        Integer badgeVeryGood = 0;
        for (Badge badge : user.getBadges()) {
            if (badge.equals(Badge.VeryGood)) {
                badgeVeryGood++;
            }
        }

        Integer badgeExcellent = 0;
        for (Badge badge : user.getBadges()) {
            if (badge.equals(Badge.Excellent)) {
                badgeExcellent++;
            }
        }

        // update badgeGood and badgeVeryGood and badgeExcellent in Utilisateur

        String queryBadge = "UPDATE Utilisateur SET badgeGood = ?, badgeVeryGood = ?, badgeExcellent = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(queryBadge)) {
            statement.setInt(1, badgeGood);
            statement.setInt(2, badgeVeryGood);
            statement.setInt(3, badgeExcellent);
            statement.setInt(4, id);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void addProject(Projet projet, Utilisateur user) throws SQLException {

        // get id of user

        String queryId = "SELECT * FROM Utilisateur WHERE pseudo = ?";

        int id = 0;

        try (PreparedStatement statementId = connection.prepareStatement(queryId)) {
            statementId.setString(1, user.getPseudo());

            try (ResultSet resultSetId = statementId.executeQuery()) {
                if (resultSetId.next()) {
                    id = resultSetId.getInt("id");
                }
            }
        }

        // add project that refrences the user

        String query = "INSERT INTO Projet (nom, description, utilisateur_id) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, projet.getNom());
            statement.setString(2, projet.getDescription());
            statement.setInt(3, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void deleteTache(Tache tache, Utilisateur user, Jour jour, Creneau creneau) throws SQLException {
        // get id of the user

        String queryId = "SELECT * FROM Utilisateur WHERE pseudo = ?";

        int id = 0;

        try (PreparedStatement statementId = connection.prepareStatement(queryId)) {
            statementId.setString(1, user.getPseudo());

            try (ResultSet resultSetId = statementId.executeQuery()) {
                if (resultSetId.next()) {
                    id = resultSetId.getInt("id");
                }
            }
        }

        // delete tache that refrences the creneau that refrences the jour that refrences the user

        String query = "DELETE FROM Tache WHERE nom = ? AND duree = ? AND prio = ? AND deadline = ? AND categorie_id = (SELECT id FROM Categorie WHERE nom_cat = ?) AND etat = ? AND creneau_id = (SELECT id FROM Creneau WHERE debut = ? AND fin = ? AND jour_id = (SELECT id FROM Jour WHERE date = ? AND utilisateur_id = ?))";

        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, tache.getNom());
            statement.setString(2, tache.getDuree().toString());
            statement.setString(3, tache.getPrio().toString());
            statement.setString(4, tache.getDeadline().toString());
            statement.setString(5, tache.getCategorie().getNom_cat());
            statement.setString(6, tache.getEtat().toString());
            statement.setString(7, creneau.getDebut().minus(tache.getDuree()).toString());
            statement.setString(8, creneau.getFin().toString());
            statement.setString(9, jour.getDate().toString());
            statement.setInt(10, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // update creneau debut and libre

        String queryCreneau = "UPDATE Creneau SET debut = ?, libre = ? WHERE debut = ? AND fin = ? AND jour_id = (SELECT id FROM Jour WHERE date = ? AND utilisateur_id = ?)";

        try (PreparedStatement statement = connection.prepareStatement(queryCreneau)) {

            statement.setString(1, creneau.getDebut().toString());
            statement.setBoolean(2, true);
            statement.setString(3, creneau.getDebut().toString());
            statement.setString(4, creneau.getFin().toString());
            statement.setString(5, jour.getDate().toString());
            statement.setInt(6, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public boolean isPseudoUnique(String pseudo) throws SQLException {
        String query = "SELECT COUNT(*) FROM Utilisateur WHERE pseudo = ?";
        int count = 0;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, pseudo);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    count = resultSet.getInt(1);
                }
            }
        }

        return count == 0;
    }

}
