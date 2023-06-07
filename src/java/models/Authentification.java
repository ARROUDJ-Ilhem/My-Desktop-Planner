package tp.javafx.models;

import java.util.ArrayList;
import java.util.Scanner;


public class Authentification {
    private ArrayList<Utilisateur> utilisateurs;

    public Authentification(ArrayList<Utilisateur> utilisateurs) {
        this.utilisateurs = utilisateurs;
    }

    public Authentification() {
        utilisateurs = new ArrayList<>();
    }

    private boolean isPseudoAvailable(String pseudo, ArrayList<Utilisateur> users) {
        for (Utilisateur utilisateur : users) {
            if (utilisateur.getPseudo().equals(pseudo)) {
                return false;
            }
        }
        return true;
    }

    public void inscription(ArrayList<Utilisateur> users) {

        boolean stop = false;
        String pseudo;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Donnez le nom de l'utilisateur");

        do {

            pseudo = scanner.nextLine();

            if (isPseudoAvailable(pseudo, users) == true) {
                Utilisateur user = new Utilisateur(pseudo);
                utilisateurs.add(user);

                stop = true;
            } else {
                System.out.println("Pseudo existe déjà, veuillez entrez un autre pseudo");
            }
        } while (!stop);

        System.out.println("donnez le mot de passe");
        String password = scanner.nextLine();
        Utilisateur user = new Utilisateur(pseudo, password);
        users.add(user);
        System.out.println("Inscription successful");
    }


    public void Se_connecter(String pseudo, String password, ArrayList<Utilisateur> users) {
        boolean trouve = false;
        for (Utilisateur utilisateur : users) {
            if (utilisateur.getPseudo().equals(pseudo) && utilisateur.getPassword().equals(password)) {
                System.out.println("Authentification succeful");
                trouve = true;
                break;
            }
        }
        if (trouve == false) {
            System.out.println("authentification failed");
        }

    }


}

