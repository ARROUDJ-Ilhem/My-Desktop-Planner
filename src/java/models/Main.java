package tp.javafx.models;

import tp.javafx.models.Authentification;
import tp.javafx.models.Utilisateur;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Utilisateur> users = new ArrayList();

        Utilisateur user1 = new Utilisateur("user1", "password1");
        Utilisateur user2 = new Utilisateur("user2", "password2");
        Utilisateur user3 = new Utilisateur("user3", "password3");
        users.add(user1);
        users.add(user2);
        users.add(user3);


        // authentiifcation ou inscription
        Authentification auth = new Authentification();

        System.out.println("veuillez choisir l'une de ses deux options : 1- se connecter , 2- s'inscrire");
        int choix1 = scanner.nextInt();
        scanner.nextLine();
        switch (choix1) {
            case 1:
                System.out.println("veuillez saisir votre nom d'utilisateur");
                String pseudo = scanner.nextLine();
                System.out.println("veuillez saisir votre mot de passe");
                String password = scanner.nextLine();
                auth.Se_connecter(pseudo, password, users);

                break;

            case 2:
                auth.inscription(users);
                break;

            default:
                System.out.println("Choix invalide. Veuillez selectionner une option valide.");


        }
        Utilisateur user = new Utilisateur("Personne");

        System.out.println();

        // afficher le calendrier  du mois courant

        LocalDate currentDate = LocalDate.now();
        LocalDate lastDayOfMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth());
        String monthYear = currentDate.getMonth().toString() + " " + currentDate.getYear();
        // Calculate the weekday (1-7) of the current day
        int currentDayOfWeek = currentDate.getDayOfWeek().getValue();

        // Create a formatter for formatting the date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd");


        // print the month and year
        System.out.println("           " + monthYear);

        // Print the header
        System.out.println("Mon  Tue  Wed  Thu  Fri  Sat  Sun");

        // Print space for days
        for (int i = 1; i < currentDayOfWeek; i++) {
            System.out.print("     ");
        }

        // afficher le calendrier sous forme d'une grille
        for (int day = currentDate.getDayOfMonth(); day <= lastDayOfMonth.getDayOfMonth(); day++) {
            LocalDate currentDay = currentDate.withDayOfMonth(day);
            System.out.printf("%-5s", currentDay.format(formatter));

            // Start a new line for the next week
            if (currentDay.getDayOfWeek().getValue() == 7) {
                System.out.println();
            }
        }

        boolean validStartDate = false;
        LocalDate startDate = null;
        LocalDate endDate = null;
        boolean fixer = false;


        System.out.println("\nVoulez-vous fixer une periode pour votre planning ? (Oui/Non)");
        String response = scanner.nextLine();

        if (response.equalsIgnoreCase("OUI")) {

            while (!validStartDate) {

                System.out.println("Veuillez saisir la date de debut de la periode (format : dd-mm-yyyy):");
                String startDateStr = scanner.nextLine();
                startDate = LocalDate.parse(startDateStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));


                System.out.println("Veuillez entrer la date de fin de la periode (format : dd-mm-yyyy) ne depassant pas le mois courant:");
                String endDateStr = scanner.nextLine();
                endDate = LocalDate.parse(endDateStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));


                // Check if the start date is before the current date
                if (startDate.isBefore(currentDate)) {
                    System.out.println("La date de debut ne peut pas etre anterieure a la date d'aujourd'hui.");
                } else {
                    if (endDate.isBefore(startDate)) {
                        System.out.println("La date de fin ne peut pas être antrieure à la date de debut.");
                    } else {
                        if (endDate.isAfter(lastDayOfMonth)) {
                            System.out.println("La date de fin ne peut pas être après le dernier jour du mois courant.");
                        } else {
                            fixer = true;
                            System.out.println("Période fixée : " + startDate.format(formatter) + " - " + endDate.format(formatter));
                            break;
                        }
                    }

                }

            }
            if (fixer == false) {
                startDate = LocalDate.now();

                endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());
            }


            System.out.println("\n Veuillez fixer les creneaux libres");
            System.out.println("Quel est le nombre de creneaux libres que vous voulez ajouter ?");
            int n = scanner.nextInt();
            scanner.nextLine();

            for (int i = 0; i < n; i++) {
                System.out.println("Information sur le creneau" + (i + 1));
                //user.ajouterCreneauLibre(scanner,  startDate, endDate);
            }
            System.out.println(user);

        }

    }
}
    	
    
